package za.ac.cput.marginhotelmanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import za.ac.cput.marginhotelmanagement.domain.Payment;
import za.ac.cput.marginhotelmanagement.domain.dtos.booking.BookingDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.GuestDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.invoice.CreateInvoiceRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.invoice.InvoiceDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.payment.PaymentDto;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;
import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;
import za.ac.cput.marginhotelmanagement.service.BookingService;
import za.ac.cput.marginhotelmanagement.service.GuestFlowAccess;
import za.ac.cput.marginhotelmanagement.service.GuestService;
import za.ac.cput.marginhotelmanagement.service.InvoiceService;
import za.ac.cput.marginhotelmanagement.service.JwtService;
import za.ac.cput.marginhotelmanagement.service.PaymentService;
import za.ac.cput.marginhotelmanagement.service.IRoomService;
import za.ac.cput.marginhotelmanagement.mappers.PaymentMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "app.checkout.demo-enabled=true",
        "app.demo-data.enabled=false"
})
@AutoConfigureMockMvc
class GuestBookingSecurityTest {
    private static final String GUEST_EMAIL = "guest@example.com";
    private static final String TEST_TOKEN = "guest-test-token";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GuestService guestService;
    @MockitoBean
    private BookingService bookingService;
    @MockitoBean
    private InvoiceService invoiceService;
    @MockitoBean
    private PaymentService paymentService;
    @MockitoBean
    private PaymentMapper paymentMapper;
    @MockitoBean
    private GuestFlowAccess guestFlowAccess;
    @MockitoBean
    private IRoomService roomService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @org.junit.jupiter.api.BeforeEach
    void setUpGuestToken() {
        when(jwtService.extractEmail(TEST_TOKEN)).thenReturn(GUEST_EMAIL);
        when(userDetailsService.loadUserByUsername(GUEST_EMAIL)).thenReturn(
                org.springframework.security.core.userdetails.User
                        .withUsername(GUEST_EMAIL)
                        .password("")
                        .roles("USER")
                        .build());
        when(jwtService.isTokenValid(eq(TEST_TOKEN), any())).thenReturn(true);
    }

    @Test
    void authenticatedGuestCanRunOwnedBookingAndCheckoutFlow() throws Exception {
        when(guestFlowAccess.isGuest(any())).thenReturn(true);
        when(guestFlowAccess.ownsEmail(eq(GUEST_EMAIL), any())).thenReturn(true);
        when(guestFlowAccess.ownsGuest(eq(8L), any())).thenReturn(true);
        when(guestFlowAccess.ownsBooking(eq(14L), any())).thenReturn(true);
        when(guestFlowAccess.ownsInvoice(eq(21L), any())).thenReturn(true);
        when(guestService.getGuestByEmail(GUEST_EMAIL)).thenReturn(null);
        when(guestService.createGuest(any())).thenReturn(
                new GuestDto(8L, "Guest", null, "Customer", GUEST_EMAIL, "0000000000"));
        when(bookingService.createBooking(any())).thenReturn(new BookingDto(
                14L, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                BookingChannel.ONLINE, 8L, 4L, 101));
        when(invoiceService.createInvoice(any())).thenReturn(new InvoiceDto(
                21L, "DEMO-INV-21", 100.0, InvoiceStatus.PENDING, LocalDate.now(), 14L));
        when(invoiceService.readInvoice(21L)).thenReturn(new InvoiceDto(
                21L, "DEMO-INV-21", 100.0, InvoiceStatus.PAID, LocalDate.now(), 14L));
        Payment payment = new Payment.Builder().setAmount(100.0).setPaymentStatus(PaymentStatus.SUCCESS).build();
        when(paymentService.createCheckout(21L)).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(
                new PaymentDto(31L, 100.0, PaymentStatus.SUCCESS, LocalDateTime.now(), 21L, "DEMO-INV-21"));

        mockMvc.perform(get("/guest/findByEmail/" + GUEST_EMAIL).with(guestUser()))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/guest/create")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Guest","lastName":"Customer","email":"guest@example.com","mobile":"0000000000"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guestId").value(8));

        mockMvc.perform(post("/booking/create")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"checkInDate":"2026-10-20T14:00:00","checkOutDate":"2026-10-22T10:00:00","bookingChannel":"ONLINE","guestId":8,"roomId":4}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId").value(14))
                .andExpect(jsonPath("$.guestId").value(8))
                .andExpect(jsonPath("$.roomId").value(4));

        mockMvc.perform(post("/invoice/create")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"reference":"DEMO-INV-21","totalAmount":100.0,"status":"PAID","bookingId":14}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceId").value(21))
                .andExpect(jsonPath("$.bookingId").value(14))
                .andExpect(jsonPath("$.status").value("PENDING"));
        org.mockito.ArgumentCaptor<CreateInvoiceRequest> invoiceRequest =
                org.mockito.ArgumentCaptor.forClass(CreateInvoiceRequest.class);
        verify(invoiceService).createInvoice(invoiceRequest.capture());
        org.junit.jupiter.api.Assertions.assertEquals(InvoiceStatus.PENDING, invoiceRequest.getValue().getStatus());

        mockMvc.perform(post("/payment/checkout")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"invoiceId":21}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(31))
                .andExpect(jsonPath("$.invoiceId").value(21))
                .andExpect(jsonPath("$.paymentStatus").value("SUCCESS"));
        verify(paymentService).createCheckout(21L);

        mockMvc.perform(get("/invoice/read/21").with(guestUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").value(21))
                .andExpect(jsonPath("$.bookingId").value(14));
    }

    @Test
    void guestCannotReadOrCreateRecordsForAnotherAccount() throws Exception {
        when(guestFlowAccess.isGuest(any())).thenReturn(true);
        when(guestFlowAccess.ownsEmail(anyString(), any())).thenReturn(false);
        when(guestFlowAccess.ownsGuest(anyLong(), any())).thenReturn(false);
        when(guestFlowAccess.ownsBooking(anyLong(), any())).thenReturn(false);
        when(guestFlowAccess.ownsInvoice(anyLong(), any())).thenReturn(false);

        mockMvc.perform(get("/guest/findByEmail/other@example.com").with(guestUser()))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/guest/getall").with(guestUser()))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/booking/getall").with(guestUser()))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/invoice/getall").with(guestUser()))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/payment/getall").with(guestUser()))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/guest/create")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Other","lastName":"Person","email":"other@example.com","mobile":"0000000000"}
                                """))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/booking/create")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"bookingChannel":"ONLINE","guestId":9,"roomId":4}
                                """))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/invoice/create")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"reference":"OTHER","totalAmount":100.0,"bookingId":15}
                                """))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/invoice/read/22").with(guestUser()))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/payment/checkout")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"invoiceId":22}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void anonymousCannotBookAndGuestCannotUseStaffPaymentCreateButRoomListStaysPublic() throws Exception {
        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"bookingChannel":"ONLINE","guestId":8,"roomId":4}
                                """))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/payment/create")
                        .with(guestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":100.0,"paymentStatus":"SUCCESS","invoiceId":21}
                                """))
                .andExpect(status().isForbidden());
        when(roomService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/room/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    private RequestPostProcessor guestUser() {
        return request -> {
            request.addHeader("Authorization", "Bearer " + TEST_TOKEN);
            return request;
        };
    }
}
