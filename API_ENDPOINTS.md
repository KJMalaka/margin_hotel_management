# API Endpoints Summary

This project exposes several REST endpoints through Spring MVC controllers. The routes below are the ones currently implemented in the controller package. All paths are relative to the context path `/marginhotel` (e.g. `/booking/create` is actually `http://localhost:8080/marginhotel/booking/create`).

## Base routing

The application uses these controller base paths:

- `/auth`
- `/booking`
- `/invoice`
- `/payment`
- `/guest`
- `/room`
- `/staff`

## Authentication

Most endpoints require a valid JWT. Log in via `/auth/login` and send the returned token as `Authorization: Bearer <token>` on every request after that.

| Base path | Access |
|---|---|
| `/auth/**` | Public — no token needed |
| `/room/**` (GET only) | Public |
| `/room/**` (POST/PUT/DELETE) | `ADMIN` |
| Guest lookup/create and booking create | `USER`, `RECEPTIONIST`, or `ADMIN`; `USER` is limited to their own guest email/record |
| Invoice create/read | `USER`, `RECEPTIONIST`, or `ADMIN`; `USER` is limited to their own booking/invoice |
| Other booking, guest, and invoice operations | `RECEPTIONIST` or `ADMIN` |
| `/staff/**` | `ADMIN` |
| `/payment/create` and other standard payment operations | `RECEPTIONIST` or `ADMIN` |
| `/payment/checkout` | `USER` only and only when `APP_DEMO_CHECKOUT_ENABLED=true`; creates a simulated successful payment for the user's own invoice |

## Auth API

Base path: `/auth`

- `POST /auth/register`
  - Registers a new user account (role defaults to `USER`).
  - Request body: `RegisterUserRequest`
  - Response: `AuthUserResponse` (includes JWT) or 400 if invalid/email already taken

- `POST /auth/login`
  - Logs in with email and password.
  - Request body: `LoginUserRequest`
  - Response: `AuthUserResponse` (includes JWT) or 401 if credentials are wrong

- `POST /auth/check-email`
  - Checks whether an email is already registered, so the frontend can prefill a returning guest's details.
  - Request body: `CheckUserEmailRequest`
  - Response: `CheckUserEmailResponse`

## Booking API

Base path: `/booking`

- `POST /booking/create`
  - Creates a booking. Rejects overlapping bookings for the same room.
  - Requires a JWT with role `USER`, `RECEPTIONIST`, or `ADMIN`. A `USER` can book only for a guest record whose email matches the authenticated account.
  - Request body: `CreateBookingRequest` with `checkInDate`, `checkOutDate` (`yyyy-MM-dd'T'HH:mm:ss`), `bookingChannel`, `guestId`, and `roomId` (the numeric `roomId` returned by a room GET endpoint).
  - Example: `{"checkInDate":"2026-10-20T14:00:00","checkOutDate":"2026-10-22T10:00:00","bookingChannel":"ONLINE","guestId":1,"roomId":1}`
  - Response: created `BookingDto`, 409 if the room is already booked for those dates, or 400 if the request is invalid

- `GET /booking/read/{id}`
  - Reads one booking by ID.
  - Response: `BookingDto` or 404 if not found

- `PUT /booking/update`
  - Updates a booking. The booking ID is part of the request body, not the URL.
  - Request body: `UpdateBookingRequest`
  - Response: updated `BookingDto` or 404 if not found

- `DELETE /booking/delete/{id}`
  - Deletes a booking by ID.
  - Response: 204 No Content or 404 if not found

- `GET /booking/getall`
  - Returns all bookings.
  - Response: list of `BookingDto`

## Guest API

Base path: `/guest`

- `POST /guest/create`
  - Creates a guest.
  - Request body: `CreateGuestRequest`
  - Response: created `GuestDto` or 400 if invalid
  - Requires role `USER`, `RECEPTIONIST`, or `ADMIN`. A `USER` may create only a guest with the authenticated account's email and only when one does not already exist.

- `GET /guest/read/{id}`
  - Reads one guest by ID.
  - Response: `GuestDto` or 404 if not found

- `PUT /guest/update`
  - Updates a guest. The guest ID is part of the request body, not the URL.
  - Request body: `UpdateGuestRequest`
  - Response: updated `GuestDto` or 404 if not found

- `DELETE /guest/delete/{id}`
  - Deletes a guest by ID.
  - Response: 204 No Content or 404 if not found

- `GET /guest/getall`
  - Returns all guests.
  - Response: list of `GuestDto`

- `GET /guest/findByFirstName/{firstName}`
  - Finds guests by first name.

- `GET /guest/findByLastName/{lastName}`
  - Finds guests by last name.

- `GET /guest/findByEmail/{email}`
  - Finds a guest by email — used to detect a returning guest.
  - Response: `GuestDto` or 404 if not found
  - Requires role `USER`, `RECEPTIONIST`, or `ADMIN`. A `USER` can look up only their authenticated email.

## Invoice API

Base path: `/invoice`

- `POST /invoice/create`
  - Creates an invoice for a booking.
  - Request body: `CreateInvoiceRequest`
  - Response: created `InvoiceDto` or 400 if invalid
  - Requires role `USER`, `RECEPTIONIST`, or `ADMIN`. A `USER` may create an invoice only for their own booking; the backend forces its initial status to `PENDING`.

- `GET /invoice/read/{id}`
  - Reads one invoice by ID.
  - Response: `InvoiceDto` or 404 if not found
  - Requires role `USER`, `RECEPTIONIST`, or `ADMIN`. A `USER` can read only their own invoice.

- `PUT /invoice/update/{id}`
  - Updates an invoice's status/amount.
  - Request body: `UpdateInvoiceRequest`
  - Response: updated `InvoiceDto` or 404 if not found

- `DELETE /invoice/delete/{id}`
  - Deletes an invoice by ID.
  - Response: 204 No Content or 404 if not found

- `GET /invoice/getall`
  - Returns all invoices.
  - Response: list of `InvoiceDto`

- `GET /invoice/findByStatus/{status}`
  - Finds invoices by status (`PENDING` or `PAID`).

- `GET /invoice/findByIssueDate/{issueDate}`
  - Finds invoices by issue date (`yyyy-MM-dd`).

- `GET /invoice/findByBookingId/{bookingId}`
  - Finds invoices for a given booking.

## Payment API

Base path: `/payment`

- `POST /payment/create`
  - Records a payment against an invoice.
  - Request body: `CreatePaymentRequest`
  - Response: created `PaymentDto` or 400 if invalid
  - Staff operation for `RECEPTIONIST` or `ADMIN`; guests cannot set payment outcomes through this endpoint.

- `POST /payment/checkout`
  - Simulates successful checkout for the authenticated user's own invoice. The backend sets the payment amount to the invoice total, sets status to `SUCCESS`, sets the payment date, and marks the invoice `PAID`.
  - Requires role `USER` and `APP_DEMO_CHECKOUT_ENABLED=true` on the backend. The body contains only `invoiceId`, e.g. `{"invoiceId":12}`. Duplicate payments for an invoice return 409.

- `GET /payment/read/{id}`
  - Reads one payment by ID.
  - Response: `PaymentDto` or 404 if not found

- `PUT /payment/update/{id}`
  - Updates a payment (e.g. status).
  - Request body: `UpdatePaymentRequest`
  - Response: updated `PaymentDto` or 404 if not found

- `DELETE /payment/delete/{id}`
  - Deletes a payment by ID.
  - Response: 204 No Content or 404 if not found

- `GET /payment/getall`
  - Returns all payments.
  - Response: list of `PaymentDto`

- `GET /payment/findByAmount/{amount}`
  - Finds payments by exact amount.

- `GET /payment/findPaymentByPaymentStatus/{paymentStatus}`
  - Finds payments by status (`SUCCESS` or `FAILED`).

- `GET /payment/findPaymentByPaymentDateBetween/{startDate}/{endDate}`
  - Finds payments made between two `LocalDateTime` values.

## Room API

Base path: `/room`

- `POST /room/create`
  - Creates a room. **Admin only.**
  - Request body: `Room`
  - Response: created `Room`

- `GET /room/read/{id}`
  - Reads one room by ID.
  - Response: `Room` or 404 if not found

- `PUT /room/update`
  - Updates a room. The room ID is part of the request body, not the URL. **Admin only.**
  - Request body: `Room`
  - Response: updated `Room` or 404 if not found

- `DELETE /room/delete/{id}`
  - Deletes a room by ID. **Admin only.**
  - Response: 204 No Content or 404 if not found

- `GET /room/all`, `/room/getall`, or `/room/getAll`
  - Returns all rooms.
  - Response: list of rooms, each with a generated numeric `roomId` (a JSON number), `roomNumber`, `roomType`, `pricePerNight`, and `roomStatus`.
  - The GET routes are public. For example, `GET http://localhost:8080/marginhotel/room/getall` can be used by the frontend to populate its room list and obtain valid `roomId` values.

- `GET /room/status/{status}`
  - Finds rooms by status (e.g. `AVAILABLE`, `OCCUPIED`).

- `GET /room/available?checkIn=yyyy-MM-dd&checkOut=yyyy-MM-dd`
  - Finds rooms with no overlapping booking in the given date range.
  - Response: list of `Room`, or 400 if the dates are missing/invalid

## Staff API

Base path: `/staff`. **Admin only.**

- `POST /staff/manager/create`
  - Creates a manager.
  - Request body: `CreateManagerRequest`
  - Response: created `ManagerDto`

- `GET /staff/manager/read/{id}`
  - Reads one manager by ID.

- `PUT /staff/manager/update`
  - Updates a manager.
  - Request body: `UpdateManagerRequest`

- `DELETE /staff/manager/delete/{id}`
  - Deletes a manager by ID.

- `GET /staff/manager/getall`
  - Returns all managers.

- `POST /staff/receptionist/create`
  - Creates a receptionist.
  - Request body: `CreateReceptionistRequest`
  - Response: created `ReceptionistDto`

- `GET /staff/receptionist/read/{id}`
  - Reads one receptionist by ID.

- `PUT /staff/receptionist/update`
  - Updates a receptionist.
  - Request body: `UpdateReceptionistRequest`

- `DELETE /staff/receptionist/delete/{id}`
  - Deletes a receptionist by ID.

- `GET /staff/receptionist/getall`
  - Returns all receptionists.
