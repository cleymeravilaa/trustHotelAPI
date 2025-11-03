package edu.unicolombo.trustHotelAPI.domain.model;

import edu.unicolombo.trustHotelAPI.dto.invoice.RegisterNewInvoiceDTO;
import edu.unicolombo.trustHotelAPI.dto.invoice.UpdateInvoiceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Invoice")
@Table(name = "invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "invoiceId")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoiceId;
    private LocalDateTime issueDate;
    private int totalRooms;
    private Double finalTotal;
    @OneToOne
    @JoinColumn(name = "staying")
    private Staying staying;

    public Invoice(Staying staying, LocalDateTime issueDate, int totalOfRooms, Double finalTotal) {
        this.staying = staying;
        this.issueDate = issueDate;
        this.totalRooms = totalOfRooms;
        this.finalTotal = finalTotal;
    }

    public Invoice(RegisterNewInvoiceDTO data) {
        this.issueDate = data.issueDate();
        this.totalRooms = data.totalOfRooms();
        this.finalTotal = data.finalTotal();
    }

    public void updateData(UpdateInvoiceDTO data) {

        if(data.issueDate() != null) {
            this.issueDate = data.issueDate();
        }

        if(data.totalOfRooms() != 0) {
            this.totalRooms = data.totalOfRooms();
        }

        if (data.finalTotal() != 0) {
            this.finalTotal = data.finalTotal();
        }
    }
}
