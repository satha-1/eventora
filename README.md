# Eventora

React + Spring Boot MVP for a Sri Lankan **events marketplace** (weddings, birthdays, corporate) that also scales to dayouts/nightouts. Includes Auth (JWT), Events, RFQ/Proposals, Bookings, and a mocked PayHere payment flow.

> Status: MVP running locally (frontend + backend). DB: H2 in-memory for dev.

---

## ✨ Features (MVP)

- **Auth (JWT)** — register/login, role-aware (ATTENDEE, ORGANIZER, VENDOR, ADMIN)
- **Events** — create/browse/search events (title, location, price, category/subcategory)
- **RFQ / Proposals** — attendees post RFQs; vendors send proposals; organizer/attendee accepts
- **Bookings** — created upon proposal acceptance; status updates
- **Payments (mocked)** — PayHere sandbox “init” + webhook stub for success
- **Reviews (basic)** — leave a rating/comment (API wired)

---

## 🧱 Tech Stack

- **Frontend:** React + Vite, TypeScript, Axios
- **Backend:** Spring Boot 3.3 (Java 21), Spring Security (JWT), Spring Data JPA, H2
- **Build/Dev:** Maven, npm
- **DB (dev):** H2 in-memory (Postgres planned via Flyway migrations)

---

## 🚀 Quick Start (Local)

### Prereqs
- **Java 21**, **Maven 3.9+**
- **Node 18+**, **npm 8+**

### 1) Backend

```bash
cd backend
mvn spring-boot:run
