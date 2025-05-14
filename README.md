Scalable Network Asset Discovery System
🛠️ Designed and Developed by Kartikey Chaudhary
📘 Overview
This project is a Scalable Network Asset Discovery System, developed as part of the Motadata Software Engineer assignment. It is engineered to discover over 50,000+ network assets across cloud and on-premises environments using a multithreaded, event-driven architecture.

The system supports both active (ICMP) and passive (NetFlow) scanning techniques and ensures real-time inventory updates via Spring Data JPA and asynchronous event processing using Apache Kafka.

🚀 Key Features
High Throughput: Handles 1000+ devices/second using Java concurrency.

Discovery Methods: Supports both active (ICMP) and passive (mocked NetFlow) scanning.

Persistent Inventory: Stores and updates assets using Spring Data JPA with optimistic locking.

Event-Driven Architecture: Emits discovery events to Apache Kafka for downstream processing like CMDB updates and compliance checks.

Concurrent Batch Processing: Uses ExecutorService with a thread pool (20+) and exponential backoff for scalability and reliability.

Simulation & Testing Utilities: Includes tools for IP/MAC generation and mock latency testing.

AI-Aided Optimization: Integrated ChatGPT prompts for JPA tuning and backoff logic.

🧱 Technology Stack
Layer	Technology	Rationale
Language	Java 17	Modern concurrency support, robust ecosystem
Framework	Spring Boot	Rapid development, seamless JPA and Kafka integration
Persistence	Spring Data JPA	Simplified ORM with transaction handling and batching
Messaging	Apache Kafka	Scalable and decoupled event processing
Concurrency	ExecutorService	Fine-grained control over threading and task management
Testing	JUnit, Mockito	Standard frameworks for unit and integration testing

🧩 Architecture Diagram
lua
Copy
Edit
+-------------------------+
|   Discovery Controller  |
+-------------------------+
            |
            v
+-------------------------+
|    Discovery Service    |<------------------+
+-------------------------+                   |
       |                          +------------------------+
       v                          |                        |
+-------------+     +-------------------------+   +----------------+
| ActiveScan  |<--> |    ThreadPool (20+)     |<--|  PassiveScan   |
|   (ICMP)    |     +-------------------------+   | (Mock NetFlow) |
+-------------+                                +----------------+
       |
       v
+------------------------------------+
|     Asset Inventory (JPA)          |
+------------------------------------+
            |
            v
     KafkaPublisher → KafkaTopic
            |
            v
    KafkaConsumer (Async) → CMDB / Compliance
🔍 Detailed Component Breakdown
✅ Multithreaded Discovery Engine
Utilizes ThreadPoolExecutor with 20+ threads.

Submits batches of scan jobs using Callable tasks.

Implements exponential backoff to mitigate thread starvation.

✅ NetworkAsset Entity (JPA)
java
Copy
Edit
@Entity
public class NetworkAsset {
    @Id
    private String ip;
    private String mac;
    private String vendor;
    private LocalDateTime lastSeen;
    private String complianceStatus;

    @Version
    private Integer version; // For optimistic locking
}
Uses @Version for optimistic locking.

Bulk writes using JpaRepository.saveAll() with batch configuration.

✅ Kafka Integration
Custom event class: AssetDiscoveredEvent.

Publishes new discoveries asynchronously via KafkaPublisher.

Consumes events using @KafkaListener for compliance checks and subnet mapping.

✅ Simulation & Testing Utilities
Utility script generates random IP/MAC/vendor data.

Integration tests launch concurrent scan batches.

Simulates network latency and logs performance metrics.

🧠 Prompt Engineering
🤖 Why Use AI (ChatGPT)?
AI assistance was leveraged to:

Configure optimal JPA batching.

Implement exponential backoff on failed ICMP scans.

📌 Example Prompt:
“What’s the optimal Spring Data JPA configuration for 1000+ batch inserts with optimistic locking?”

✅ Key Suggestions from AI:
spring.jpa.properties.hibernate.jdbc.batch_size=1000

Flush and clear after each batch

hibernate.order_inserts=true

📈 Result: ~30% performance improvement in batch insert operations.

⚠️ Challenges & Mitigations
Challenge	Solution
Thread starvation in /16 scans	Bounded queue + exponential backoff
Legacy device fingerprinting	Regex-based vendor/OS pattern simulation
Sub-500ms asset query time	Indexed fields on ip, lastSeen; limited projections
Duplicate device events	Deduplication using (ip + MAC) combo in DB

✅ Conclusion
This project demonstrates how to build a scalable, concurrent, and event-driven network discovery system using modern Java and Spring technologies. Every design choice—from Kafka integration to JPA batching—was made to ensure:

⚙️ Performance

🔁 Reliability

🔌 Extensibility

It lays the foundation for an enterprise-ready solution capable of handling production-scale network inventory and compliance workflows.
