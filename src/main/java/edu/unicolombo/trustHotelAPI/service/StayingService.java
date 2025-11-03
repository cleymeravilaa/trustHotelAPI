package edu.unicolombo.trustHotelAPI.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import edu.unicolombo.trustHotelAPI.dto.invoice.UpdateInvoiceDTO;
import edu.unicolombo.trustHotelAPI.infrastructure.errors.exception.BusinessLogicValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.unicolombo.trustHotelAPI.domain.model.Invoice;
import edu.unicolombo.trustHotelAPI.domain.model.Room;
import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomStatus;
import edu.unicolombo.trustHotelAPI.domain.model.Staying;
import edu.unicolombo.trustHotelAPI.domain.model.StayingRoom;
import edu.unicolombo.trustHotelAPI.domain.model.StayingRoomId;
import edu.unicolombo.trustHotelAPI.domain.repository.BookingRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.StayingRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.StayingRoomRepository;
import edu.unicolombo.trustHotelAPI.dto.staying.StayingDTO;
import edu.unicolombo.trustHotelAPI.dto.staying.UpdateStayingDTO;
import edu.unicolombo.trustHotelAPI.dto.staying.CheckOutRoomDTO;
import jakarta.transaction.Transactional;

@Slf4j
@Service
public class StayingService {

    @Autowired
    public BookingRepository bookingRepository;

    @Autowired
    public StayingRepository stayingRepository;

    @Autowired
    public StayingRoomRepository stayingRoomRepository;

    @Autowired
    public InvoiceService invoiceService;

    // Cola para optimización de CheckOut masivos y su procesamiento
    private final Queue<CheckOutTask> checkOutQueue = new ConcurrentLinkedQueue<>(); // FIFO

    // Pila para deshacer checkouts realizando instantaneas
    private final Stack<StayingRoomSnapshot> undoCheckOutStack = new Stack<>();// LIFO


    // Metodo para encolar checkOuts
    @Transactional
    public void enqueueCheckOut(Long stayingId, UpdateStayingDTO data){
        checkOutQueue.add(new CheckOutTask(stayingId, data));
        log.info("Check-Out encolado para stayingId: {}", stayingId);
    }

    // Procesar la cola ejecutar con Schedule o manualmente
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void processCheckOutQueue(){
        log.info("Procesando cola de check-outs ...");
        while (!checkOutQueue.isEmpty()){
            //1. Desencolar primer elemento de la cola
            CheckOutTask task = checkOutQueue.poll();
            try{
                // 2. Guardar estado anterior del checkout
                pushSnapCheckOutToStack(task.stayingId(), task.data);
                processSingleCheckOut(task.stayingId, task.data);
                log.info("check-out procesado para stayingId {}", task.stayingId);
            } catch (Exception e){
                log.info("Error procesando check-out: {}", e.getMessage(), e);
            }
        }
    }

    private void pushSnapCheckOutToStack(Long stayingId, UpdateStayingDTO data){
        // 2. Antes de aplicar cambios, guardar estado previo en la Pila
        for(CheckOutRoomDTO dto: data.checkOutRoomDTOs()){
            StayingRoomId id = new StayingRoomId(stayingId, dto.roomId());
            StayingRoom room = stayingRoomRepository.findById(id).orElseThrow();
            undoCheckOutStack.push(new StayingRoomSnapshot(
                    id,
                    room.getCheckOutDate(),
                    room.getNotes()
            ));
        }
    }

    public void undoLastCheckOut(){
        if (!undoCheckOutStack.isEmpty()){
            StayingRoomSnapshot snapshot = undoCheckOutStack.pop();
            StayingRoom  room = stayingRoomRepository.findById(snapshot.id).orElseThrow();
            // Restaurar valores anteriores
            room.setCheckOutDate(snapshot.checkOutDate);
            room.setNotes(snapshot.notes);
            stayingRoomRepository.save(room);
        } else {
            throw new BusinessLogicValidationException("No hay operaciones a deshacer");
        }
    }
    @Transactional
    public StayingDTO toCheckIn(Long bookingId) {
        var booking = bookingRepository.getReferenceById(bookingId);

        Staying staying = new Staying();
        staying.setBooking(booking);
        staying.setStartDate(booking.getStartDate());
        staying.setEndDate(booking.getEndDate());
        
        Staying savedStaying = stayingRepository.save(staying);

        List<StayingRoom> stayingRooms = new ArrayList<>();
        for(Room room: booking.getRooms()){
            room.setCurrentState(RoomStatus.OCCUPIED);

            StayingRoomId id = new StayingRoomId(savedStaying.getStayingId(), room.getRoomId());

            StayingRoom stayingRoom = new StayingRoom();
            stayingRoom.setId(id);
            stayingRoom.setCheckInDate(staying.getStartDate());
            stayingRoom.setRoom(room);
            stayingRoom.setStaying(staying);

            stayingRooms.add(stayingRoom);
        }
        savedStaying.setStayingRoom(stayingRooms);
        
        stayingRoomRepository.saveAll(stayingRooms);
        savedStaying = stayingRepository.save(savedStaying);

        return new StayingDTO(savedStaying);

    }

    public List<StayingDTO> getAllStayings(){
        return stayingRepository.findAll().stream().map(StayingDTO::new).toList();
    }

    public StayingDTO getStayingById(Long stayingId) {
        var staying = stayingRepository.getReferenceById(stayingId);

        return new StayingDTO(staying);
    }

    private void processSingleCheckOut(Long stayingId, UpdateStayingDTO data) {
        Staying staying = stayingRepository.getReferenceById(stayingId);

        for(CheckOutRoomDTO checkOutRoom: data.checkOutRoomDTOs()){
            if (!stayingRoomRepository.existsByStayingIdAndRoomId(stayingId, checkOutRoom.roomId())){
                throw new BusinessLogicValidationException("La habitación "+ checkOutRoom.roomId()+ " no pertenece a la estancia "+ stayingId);
            }
        }
        Map<Long, StayingRoom> stayingRoomMap = staying.getStayingRoom().stream()
                .collect(Collectors.toMap(
                        sr -> sr.getRoom().getRoomId(), // Clave: ID de la habitacion
                        sr -> sr                        // Valor: Objeto del stayingRoom
                ));

        // Procesar checkouts con acceso rapido
        for(CheckOutRoomDTO checkOutRoom: data.checkOutRoomDTOs()){
            StayingRoom stayingRoom = stayingRoomMap.get(checkOutRoom.roomId());
            if(stayingRoom != null){
                stayingRoom.setCheckOutDate(LocalDate.now());
                stayingRoom.setNotes(checkOutRoom.notes());
                stayingRoom.getRoom().setCurrentState(RoomStatus.FREE);
            }
        }
        stayingRoomRepository.saveAll(staying.getStayingRoom());

        // Manejo de factura
        if (checkOutRoomsComplete(staying.getStayingRoom())) {
            if (stayingHaveInvoice(staying)) {
                // Actualizar factura existente
                Invoice invoice = staying.getInvoice();
                invoice.setIssueDate(LocalDateTime.now());
                invoice.setTotalRooms(staying.getStayingRoom().size());
                invoice.setFinalTotal(invoiceService.calculateFinalTotal(staying.getStayingRoom()));
                invoiceService.updateInvoice(staying.getInvoice().getInvoiceId(),
                        new UpdateInvoiceDTO(
                            LocalDateTime.now(),
                            staying.getStayingRoom().size(),
                            invoiceService.calculateFinalTotal(staying.getStayingRoom())
                        ));
            } else {
                // Crear nueva factura
                Invoice invoice = invoiceService.generateInvoice(stayingId);
                staying.setInvoice(invoice);
            }
        }
        stayingRepository.save(staying);
    }

    public StayingDTO updateStaying(Long stayingId, UpdateStayingDTO data) {
        var staying = stayingRepository.getReferenceById(stayingId);
        return new StayingDTO(stayingRepository.save(staying));

    }

    public boolean checkOutRoomsComplete(List<StayingRoom> stayingRooms){
        for(StayingRoom stayingRoom: stayingRooms){
            if(stayingRoom.getCheckOutDate()==null){
                return false;
            }
        }
        return true;
    }

    public boolean stayingHaveInvoice(Staying staying){
        return staying.getInvoice()!=null;
    }
    public void deleteStaying(Long stayingId){

    }

    private record CheckOutTask(Long stayingId, UpdateStayingDTO data){}

    private record StayingRoomSnapshot(StayingRoomId id, LocalDate checkOutDate, String notes){}
}
