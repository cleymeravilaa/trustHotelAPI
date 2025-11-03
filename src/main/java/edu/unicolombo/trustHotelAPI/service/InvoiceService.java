package edu.unicolombo.trustHotelAPI.service;

import edu.unicolombo.trustHotelAPI.domain.model.Invoice;
import edu.unicolombo.trustHotelAPI.domain.model.Staying;
import edu.unicolombo.trustHotelAPI.domain.model.StayingRoom;
import edu.unicolombo.trustHotelAPI.domain.repository.InvoiceRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.StayingRepository;
import edu.unicolombo.trustHotelAPI.dto.invoice.InvoiceDTO;
import edu.unicolombo.trustHotelAPI.dto.invoice.RegisterNewInvoiceDTO;
import edu.unicolombo.trustHotelAPI.dto.invoice.UpdateInvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    public InvoiceRepository invoiceRepository;

    @Autowired
    public StayingRepository stayingRepository;

    public Invoice registerInvoice(RegisterNewInvoiceDTO data) {
        var invoice = new Invoice(data);
        return invoiceRepository.save(invoice);
    }

    public Invoice findById(long id) {return invoiceRepository.getReferenceById(id);}

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream().map(InvoiceDTO::new).collect(Collectors.toList());
    }

    public InvoiceDTO getInvoicesById(long invoiceId) {
        Invoice invoice = invoiceRepository.getReferenceById(invoiceId);

        return new InvoiceDTO(invoiceRepository.save(invoice));
    }

    public void deleteById(long invoiceId) {
        var invoice = invoiceRepository.getReferenceById(invoiceId);

        invoiceRepository.delete(invoice);
    }

    public InvoiceDTO updateInvoice(long invoiceId, UpdateInvoiceDTO data) {
        Invoice invoice = invoiceRepository.getReferenceById(invoiceId);
        invoice.updateData(data);

        return new InvoiceDTO(invoiceRepository.save(invoice));
    }

    public Invoice generateInvoice(Long stayingId){

        Staying staying = stayingRepository.getReferenceById(stayingId);

        Invoice invoice = new Invoice();
        invoice.setStaying(staying);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setFinalTotal(calculateFinalTotal(staying.getStayingRoom()));
        invoice.setTotalRooms(staying.getStayingRoom().size());

        return invoiceRepository.save(invoice);

    }

    public Double calculateFinalTotal(List<StayingRoom> stayingRooms){
        Double finalTotal = 0.0;
        for(StayingRoom stayingRoom: stayingRooms){
            var nights = ChronoUnit.DAYS.between(stayingRoom.getCheckInDate(), stayingRoom.getCheckOutDate());
            finalTotal += stayingRoom.getRoom().getPricePerNight() * nights;
        }
        return finalTotal;
    }
}
