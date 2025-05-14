# Scalable-Network-Asset-Discovery-System
Scalable Network Asset Discovery System – Technical Design &amp; Rationale


Scalable Network Asset Discovery System – Technical Design & Rationale
By Kartikey Chaudhary 

1. Overview
This document describes the architecture, design decisions, and implementation details of the Scalable Network Asset Discovery System built as part of the Motadata Software Engineer assignment. The goal was to build a system capable of discovering over 50,000 network assets across cloud/on-prem environments using multithreaded scanning, real-time inventory updates, and an event-driven model.

2. Problem Understanding & Interpretation
Key requirements:
Handle 1000+ devices/sec using multithreading.


Support active (ICMP) and passive (NetFlow) discovery methods.


Efficiently manage and update asset inventory using Spring Data JPA.


Emit events to Kafka for CMDB and compliance checks.


Allow batch updates while maintaining concurrency safety.



3. Technology Stack
Layer
Technology
Rationale
Language
Java 17
Modern concurrency support, robust ecosystem
Framework
Spring Boot
Rapid development, ease of integration with JPA, Kafka
Persistence
Spring Data JPA
Simplifies ORM and transaction handling
Messaging
Apache Kafka
Scalable, decoupled event processing
Concurrency
ExecutorService
Fine-grained control over thread pools
Testing
JUnit, Mockito
Standard testing tools for unit and integration testing


4. Design Architecture
🧩 Component Diagram
lua
CopyEdit
+-------------------------+
| Discovery Controller    |
+-------------------------+
            |
            v
+-------------------------+
| Discovery Service       | <--------------------------+
+-------------------------+                            |
     |            |                                     |
     v            v                                     |
ActiveScan     PassiveScan                             |
 (ICMP)        (Mocked NetFlow)                         |
     \            /                                     |
     +-----> ThreadPool (20+) --------------------------+
                      |
                      v
+-------------------------------------+
| Asset Inventory (Spring Data JPA)  |
+-------------------------------------+
                      |
                      v
            KafkaPublisher --> KafkaTopic
                      |
                      v
        KafkaConsumer (Async) --> Compliance / CMDB



5. Detailed Component Rationale
✅ Discovery Engine (Multithreaded Executor)
Why: To meet the throughput goal of 1000+ devices/sec, single-threaded or sequential scanning is infeasible.
How:
Used ThreadPoolExecutor with 20+ threads.


Scheduled tasks submit scan jobs in configurable batches.


Callable tasks simulate ICMP responses using randomized logic for realism.


Design Decision:
Adopted ExecutorService for control and scaling.


Added backoff logic (discussed later) to avoid thread starvation.



✅ NetworkAsset Entity (JPA)
Why: To persist real-time asset details with integrity and concurrency safety.
How:
java
CopyEdit
@Entity
public class NetworkAsset {
    @Id private String ip;
    private String mac;
    private String vendor;
    private LocalDateTime lastSeen;
    private String complianceStatus;

    @Version
    private Integer version;
}

Design Decision:
Enabled @Version for optimistic locking during concurrent writes.


Used JpaRepository.saveAll() with batch configuration.



✅ Kafka Integration
Why: For loose coupling between discovery and post-processing (CMDB, compliance).
How:
Created a custom event: AssetDiscoveredEvent.


Published from service layer when new devices are found.


Asynchronously consumed for downstream operations.


Design Decision:
Kafka provides scalable event queuing, enabling future plug-ins.


Used Spring’s @KafkaListener to trigger compliance & subnet mapping.



✅ Simulation and Testing Utilities
Why: To validate scalability and concurrency under load.
How:
Utility script generates randomized IP/MAC/vendor data.


Integration tests spawn multiple scan batches concurrently.


Design Decision:
Simulations use mock delay to resemble real network latency.


Provided performance logging per scan cycle to validate 1000+/sec rate.



6. Prompt Engineering (prompts.md)
Why AI Prompts?
 Used ChatGPT for two scenarios:
Configuring JPA batch performance


Implementing exponential backoff on failed scans


Example Prompt (JPA Batching):
“What’s the optimal Spring Data JPA configuration for 1000+ batch inserts with optimistic locking?”
AI’s Response:
Use spring.jpa.properties.hibernate.jdbc.batch_size=1000


Flush and clear after every 1000 records


Enable hibernate.order_inserts=true


My Reflections:
AI suggestions helped avoid common batching pitfalls.


Before/after performance improved ~30%.



7. Challenges & Mitigation
Challenge
Mitigation Strategy
Thread starvation in /16 scans
Bounded queue + exponential backoff on ICMP timeout
Legacy device fingerprinting
Simulated based on OS pattern regex and vendor hints
Ensuring <500ms asset query time
Indexed fields on ip, lastSeen; limited query projections
Duplicate device events
Deduplication based on (ip + MAC) combo in DB layer


8. Conclusion
This system leverages modern Java and Spring capabilities to address a real-world enterprise challenge: scalable and reliable network discovery. By using a concurrent, event-driven architecture backed with strong persistence strategies, we ensure the system is ready for production-grade traffic.
Every decision — from Kafka adoption to JPA optimization — was rooted in a need for scalability, reliability, and extensibility.
This document outlines the design and rationale for a Scalable Network Asset Discovery System. Here's a summary of its key sections:

Purpose: To describe the system's architecture, design choices, and implementation details, which is designed to discover over 50,000 network assets across different environments.

Key Aspects of the System:
Scalability: Handles high device discovery rates (1000+ devices/second) using multithreading.
Discovery Methods: Supports both active (ICMP) and passive (NetFlow) discovery.
Data Management: Uses Spring Data JPA for efficient asset inventory management and updates.
Event-Driven Model: Emits events to Kafka for further processing, like compliance checks.
Concurrency: Designed to manage batch updates while ensuring concurrency safety.
Technology Stack Used:
Language: Java 17
Framework: Spring Boot
Persistence: Spring Data JPA
Messaging: Apache Kafka
Concurrency: ExecutorService
Testing: JUnit, Mockito
Architecture: The system uses a component-based design involving a Discovery Controller, Discovery Service, Active/Passive Scans, ThreadPool, Asset Inventory, Kafka Publisher/Consumer, and subsequent operations for Compliance/CMDB.

Detailed Component Rationale: The document explains the reasoning behind key components like the multithreaded Executor, NetworkAsset entity (JPA), Kafka Integration, and simulation/testing utilities.

Prompt Engineering: AI prompts (specifically ChatGPT) were used for assistance with JPA batch performance configuration and implementing exponential backoff on failed scans.

Challenges and Mitigations: The document lists challenges encountered and strategies used to mitigate them, such as thread starvation, legacy device fingerprinting, query time optimization, and duplicate device events.

Conclusion: The system is built to be scalable, reliable, and extensible, leveraging modern Java and Spring technologies for enterprise-level network discovery.


Design and Develop by - Kartikey Chaudhary, SDE 1 
