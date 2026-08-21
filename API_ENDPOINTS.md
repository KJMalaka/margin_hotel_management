# API Endpoints Summary

This project exposes several REST endpoints through Spring MVC controllers. The routes below are the ones currently implemented in the controller package.

## Base routing

The application uses these controller base paths:

- `/booking`
- `/invoice`
- `/payment`
- `/guest`
- `/staff`

## Booking API

Base path: `/booking`

- `POST /booking/create`
  - Creates a booking.
  - Request body: `Booking`
  - Response: created `Booking`

- `GET /booking/read/{id}`
  - Reads one booking by ID.
  - Path variable: `id`
  - Response: `Booking` or 404 if not found

- `PUT /booking/update`
  - Updates a booking.
  - Request body: `Booking`
  - Response: updated `Booking` or 404 if not found

- `DELETE /booking/delete/{id}`
  - Deletes a booking by ID.
  - Path variable: `id`
  - Response: 204 No Content or 404 if not found

- `GET /booking/getall`
  - Returns all bookings.
  - Response: list of `Booking`

## Guest API

Base path: `/guest`

- `POST /guest/create`
  - Creates a guest.
  - Request body: `Guest`
  - Response: created `Guest`

- `GET /guest/read/{id}`
  - Reads one guest by ID.
  - Path variable: `id`
  - Response: `Guest` or 404 if not found

- `PUT /guest/update`
  - Updates a guest.
  - Request body: `Guest`
  - Response: updated `Guest` or 404 if not found

- `DELETE /guest/delete/{id}`
  - Deletes a guest by ID.
  - Path variable: `id`
  - Response: 204 No Content or 404 if not found

- `GET /guest/getall`
  - Returns all guests.
  - Response: list of `Guest`

- `GET /guest/findByFirstName/{firstName}`
  - Finds guests by first name.
  - Path variable: `firstName`

- `GET /guest/findByLastName/{lastName}`
  - Finds guests by last name.
  - Path variable: `lastName`

- `GET /guest/findByEmail/{email}`
  - Finds a guest by email.
  - Path variable: `email`
  - Response: `Guest` or 404 if not found


## Invoice API

Base path: `/invoice`

- `POST /invoice/create`
  - Creates an invoice.
  - Request body: `Invoice`

- `GET /invoice/read/{id}`
  - Reads one invoice by ID.

- `PUT /invoice/update`
  - Updates an invoice.

- `DELETE /invoice/delete/{id}`
  - Deletes an invoice by ID.

- `GET /invoice/getall`
  - Returns all invoices.

- `GET /invoice/findByStatus/{status}`
  - Finds invoices by status.
  - Path variable: `status`

- `GET /invoice/findByIssueDate/{issueDate}`
  - Finds invoices by issue date.
  - Path variable: `issueDate`

- `GET /invoice/findByBookingId/{bookingId}`
  - Finds invoices by booking ID.
  - Path variable: `bookingId`

## Payment API

Base path: `/payment`

- `POST /payment/create`
  - Creates a payment.
  - Request body: `Payment`

- `GET /payment/read/{id}`
  - Reads one payment by ID.

- `PUT /payment/update`
  - Updates a payment.

- `DELETE /payment/delete/{id}`
  - Deletes a payment by ID.

- `GET /payment/getall`
  - Returns all payments.

- `GET /payment/findByAmount/{amount}`
  - Finds payments by amount.

- `GET /payment/findPaymentByPaymentStatus/{paymentStatus}`
  - Finds payments by payment status.

- `GET /payment/findPaymentByPaymentDateBetween/{startDate}/{endDate}`
  - Finds payments between two dates.

## Staff API

Base path: `/staff`

- `POST /staff/manager/create`
  - Creates a manager.
  - Request body: `Manager`

- `GET /staff/manager/read/{id}`
  - Reads one manager by ID.

- `PUT /staff/manager/update`
  - Updates a manager.

- `DELETE /staff/manager/delete/{id}`
  - Deletes a manager by ID.

- `GET /staff/manager/getall`
  - Returns all managers.

- `POST /staff/receptionist/create`
  - Creates a receptionist.
  - Request body: `Receptionist`

- `GET /staff/requeptionist/read/{id}`
  - Reads one receptionist by ID.
  - Note: this path appears to be misspelled as `requeptionist`.

- `PUT /staff/receptionist/update`
  - Updates a receptionist.

- `DELETE /staff/receptionist/delete/{id}`
  - Deletes a receptionist by ID.

- `GET /staff/receptionist/getall`
  - Returns all receptionists.

## Notes for Claude / AI understanding

- These are Spring Boot REST controllers.
- Each controller is mapped with `@RequestMapping` on a resource path.
- Most endpoints use standard CRUD-style actions plus a few query endpoints.
- The main request/response objects are domain classes such as `Booking`, `Invoice`, `Guest` , `Payment`, `Manager`, and `Receptionist`.
- One endpoint path looks inconsistent: `/staff/requeptionist/read/{id}` should likely be `/staff/receptionist/read/{id}`.
