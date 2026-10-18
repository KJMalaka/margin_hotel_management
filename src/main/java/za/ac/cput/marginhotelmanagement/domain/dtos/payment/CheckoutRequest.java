package za.ac.cput.marginhotelmanagement.domain.dtos.payment;

public class CheckoutRequest {
    private Long invoiceId;

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}
