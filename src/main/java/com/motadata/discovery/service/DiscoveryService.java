package com.motadata.discovery.service;

import com.motadata.discovery.model.NetworkAsset;
import com.motadata.discovery.repository.NetworkAssetRepository;
import com.motadata.discovery.events.AssetDiscoveredEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
public class DiscoveryService {

    private final NetworkAssetRepository repository;
    private final KafkaTemplate<String, AssetDiscoveredEvent> kafkaTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Value("${discovery.batchSize:100}")
    private int batchSize;

    public DiscoveryService(NetworkAssetRepository repository, KafkaTemplate<String, AssetDiscoveredEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void discoverDevices(List<String> ipList) {
        List<Callable<NetworkAsset>> tasks = new ArrayList<>();
        for (String ip : ipList) {
            tasks.add(() -> simulateDiscovery(ip));
        }

        try {
            List<Future<NetworkAsset>> futures = executorService.invokeAll(tasks);
            List<NetworkAsset> assets = new ArrayList<>();
            for (Future<NetworkAsset> future : futures) {
                NetworkAsset asset = future.get();
                assets.add(asset);
                kafkaTemplate.send("asset-topic", new AssetDiscoveredEvent(asset.getIp()));
            }
            repository.saveAll(assets);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private NetworkAsset simulateDiscovery(String ip) {
        NetworkAsset asset = new NetworkAsset();
        asset.setIp(ip);
        asset.setMac(UUID.randomUUID().toString().substring(0, 17));
        asset.setVendor("Cisco");
        asset.setLastSeen(LocalDateTime.now());
        asset.setComplianceStatus("COMPLIANT");
        return asset;
    }
}
