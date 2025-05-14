# Scalable Network Asset Discovery System

> Designed and Developed by Kartikey Chaudhary

---

## 📘 Overview

This project is part of the Motadata Software Engineer assignment. It’s built to **discover 50,000+ network assets** across cloud/on-prem environments using:

- 🔁 Multithreading (1000+ devices/sec)
- 📡 Active (ICMP) & Passive (NetFlow) scanning
- 🗃️ Spring Data JPA for inventory management
- 📨 Kafka for event-driven compliance/CMDB processing

---

## 🚀 Features

- **High throughput** with `ExecutorService` (20+ threads)
- **Real-time inventory updates** with optimistic locking
- **Kafka integration** for scalable downstream events
- **Batch-safe concurrent writes**
- **Simulation tools** for mock device generation and testing
- **AI-assisted tuning** using ChatGPT for batching/backoff logic

---

## 🧱 Tech Stack

| Component      | Tech Used             |
|----------------|------------------------|
| Language       | Java 17                |
| Framework      | Spring Boot            |
| Persistence    | Spring Data JPA        |
| Messaging      | Apache Kafka           |
| Concurrency    | ExecutorService        |
| Testing        | JUnit, Mockito         |

---


---

## 🔍 Component Highlights

- **ExecutorService** for parallel scans with exponential backoff.
- **`@Version`** in JPA entity to support optimistic locking.
- **Kafka** used for decoupling discovery from post-processing.
- **Simulation**: Generates randomized IP/MACs, simulates latency.

---

## ⚠️ Key Challenges & Fixes

| Challenge               | Fix                                      |
|------------------------|-------------------------------------------|
| Thread starvation       | Exponential backoff + bounded queue       |
| Legacy fingerprinting   | Regex-based simulation logic              |
| Query performance       | DB indexing + projection optimizations    |
| Event duplication       | Deduplication via `(ip + MAC)` combo      |

---

## ✅ Conclusion

This system is built for **scalability**, **extensibility**, and **real-world readiness** using modern Java/Spring practices and a modular event-driven approach.

---





