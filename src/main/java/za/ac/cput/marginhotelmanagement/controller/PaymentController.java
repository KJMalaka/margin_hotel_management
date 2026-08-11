package za.ac.cput.marginhotelmanagement.controller;
/*
   Author: DM Madondo (230949703)
   Date: 17 July 2026
   */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public Payment create(@RequestBody Payment payment) {
        return paymentService.create(payment);
    }

    @GetMapping("/read/{id}")
    public Payment read(@PathVariable Long id) {
        return paymentService.read(id);
    }

    @PutMapping("/update")
    public Payment update(@RequestBody Payment payment) {
        return paymentService.update(payment);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return paymentService.delete(paymentService.read(id));
    }

    @GetMapping("/getall")
    public List<Payment> getAll() {
        return paymentService.findAll();
    }

    @GetMapping("/findByAmount/{amount}")
    public List<Payment> findByAmount(@PathVariable double amount) {
        return paymentService.findPaymentByAmount(amount);
    }

    @GetMapping("/findPaymentByPaymentStatus/{paymentStatus}")
    public List<Payment> findPaymentByPaymentStatus(@PathVariable String paymentStatus) {
        return paymentService.findPaymentByPaymentStatus(PaymentStatus.valueOf(paymentStatus));
    }

    @GetMapping("/findPaymentByPaymentDateBetween/{startDate}/{endDate}")
    public List<Payment> findPaymentByPaymentDateBetween(@PathVariable LocalDateTime startDate, @PathVariable LocalDateTime endDate) {
        return paymentService.findPaymentByPaymentDateBetween(startDate, endDate);
    }
}
