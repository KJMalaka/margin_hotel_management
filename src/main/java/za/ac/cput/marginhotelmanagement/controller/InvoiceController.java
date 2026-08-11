package za.ac.cput.marginhotelmanagement.controller;
/*
    Author: MS Malapile (222904267)
    Date: 19 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.service.InvoiceService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/create")
    public Invoice create(@RequestBody Invoice invoice) { return invoiceService.create(invoice); }

    @GetMapping("/read/{id}")
    public Invoice read(@PathVariable Long id) { return invoiceService.read(id); }

    @PutMapping("/update")
    public Invoice update(@RequestBody Invoice invoice) { return invoiceService.update(invoice); }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) { return invoiceService.delete(invoiceService.read(id)); }

    @GetMapping("/getall")
    public List<Invoice> getAll() { return invoiceService.findAll(); }

    @GetMapping("/findByStatus/{status}")
    public List<Invoice> findByStatus(@PathVariable String status) {
        return invoiceService.findByStatus(InvoiceStatus.valueOf(status));
    }

    @GetMapping("/findByIssueDate/{issueDate}")
    public List<Invoice> findByIssueDate(@PathVariable LocalDate issueDate) {
        return invoiceService.findByIssueDate(issueDate);
    }

    @GetMapping("/findByBookingId/{bookingId}")
    public List<Invoice> findByBookingId(@PathVariable Long bookingId) {
        return invoiceService.findByBookingId(bookingId);
    }
}