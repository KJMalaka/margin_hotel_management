package za.ac.cput.marginhotelmanagement.controller;
/*
    Author: MS Malapile (222904267)
    Date: 19 July 2026
    Updated: Matuma Malapile (222904267), 08 September 2026 -
    rewritten against the DTO-based contract
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.dtos.CreateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.InvoiceDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.UpdateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.service.InvoiceService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateInvoiceRequest request) {
        try {
            InvoiceDto created = invoiceService.createInvoice(request);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<InvoiceDto> read(@PathVariable Long id) {
        InvoiceDto invoiceDto = invoiceService.readInvoice(id);
        if (invoiceDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoiceDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateInvoiceRequest request) {
        try {
            InvoiceDto updated = invoiceService.updateInvoice(id, request);
            if (updated == null) {
                return new ResponseEntity<>("No invoice found with ID #" + id, HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = invoiceService.deleteInvoice(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getall")
    public ResponseEntity<List<InvoiceDto>> getAll() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/findByStatus/{status}")
    public ResponseEntity<?> findByStatus(@PathVariable String status) {
        InvoiceStatus invoiceStatus;
        try {
            invoiceStatus = InvoiceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid invoice status '" + status + "', expected PENDING or PAID", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(invoiceService.getInvoicesByStatus(invoiceStatus));
    }

    @GetMapping("/findByIssueDate/{issueDate}")
    public ResponseEntity<List<InvoiceDto>> findByIssueDate(@PathVariable LocalDate issueDate) {
        return ResponseEntity.ok(invoiceService.getInvoicesByIssueDate(issueDate));
    }

    @GetMapping("/findByBookingId/{bookingId}")
    public ResponseEntity<List<InvoiceDto>> findByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByBookingId(bookingId));
    }
}