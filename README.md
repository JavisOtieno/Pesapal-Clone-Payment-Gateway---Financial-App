# Pesapal Clone – Payment Gateway / Financial App

An Android application designed as a clone/inspiration of the Pesapal payment gateway experience. It focuses on financial transactions, deals, and related CRM-style operations for merchants or financial service users.

---

## Overview

This project is a mobile financial application that allows users to:

- Authenticate and manage sessions
- View and manage deals / transactions
- Handle products and related financial records
- Interact with a backend API for data persistence

Currently configured to connect to a demo CRM-style backend (`https://crmdemo.av.ke/api/`), but structured to support payment gateway style flows.

---

## Key Features

- **User Authentication** – Login with secure token storage (Android Security Crypto)
- **Deals Management** – Add, view, and manage deals/transactions
- **Product Handling** – View individual products
- **Home Dashboard** – Overview of financial activities
- **Network Layer** – Retrofit + OkHttp with logging interceptor
- **Material Design UI** – Modern Android UI components

---

## Tech Stack

| Component          | Technology                          |
|--------------------|-------------------------------------|
| Language           | Java                                |
| Min SDK            | 27                                  |
| Target / Compile   | 35                                  |
| Networking         | Retrofit 2.11 + Gson + OkHttp       |
| Security           | AndroidX Security Crypto            |
| UI                 | Material Design · AppCompat         |
| Package            | `com.javy.pesapalclone`             |

---

## Project Structure
