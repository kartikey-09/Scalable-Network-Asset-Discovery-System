package com.motadata.discovery.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AssetEventConsumer {

    @KafkaListener(topics = "asset-topic", groupId = "discovery-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(AssetDiscoveredEvent event) {
        System.out.println("Processing CMDB mapping and compliance check for IP: " + event.getIp());
        // TODO: Add CMDB relationship and compliance logic
    }
}
