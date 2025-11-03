package edu.unicolombo.trustHotelAPI.dto.invoice;

import edu.unicolombo.trustHotelAPI.domain.model.Invoice;

import java.time.LocalDateTime;

public record UpdateInvoiceDTO(LocalDateTime issueDate, int totalOfRooms, Double finalTotal) {

    public UpdateInvoiceDTO(Invoice invoice) {
        this(invoice.getIssueDate(), invoice.getTotalRooms(), invoice.getFinalTotal());
    }
}
