package com.motadata.discovery;

import com.motadata.discovery.model.NetworkAsset;
import com.motadata.discovery.repository.NetworkAssetRepository;
import com.motadata.discovery.service.DiscoveryService;
import com.motadata.discovery.events.AssetDiscoveredEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DiscoveryServiceTest {

    @Mock
    private NetworkAssetRepository assetRepository;

    @Mock
    private KafkaTemplate<String, AssetDiscoveredEvent> kafkaTemplate;

    @InjectMocks
    private DiscoveryService discoveryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDiscoverDevices_withValidIps_shouldSaveAssetsAndPublishEvents() {
        // Arrange
        List<String> ips = Arrays.asList("192.168.1.1", "192.168.1.2");

        // Act
        discoveryService.discoverDevices(ips);

        // Assert
        verify(assetRepository, times(1)).saveAll(anyList());
        verify(kafkaTemplate, times(2)).send(eq("asset-topic"), any(AssetDiscoveredEvent.class));
    }

    @Test
    void testSimulateDiscovery_setsCorrectFields() {
        // Reflection test to directly call private method via public behavior
        List<String> ips = Collections.singletonList("10.0.0.1");

        discoveryService.discoverDevices(ips);

        ArgumentCaptor<List<NetworkAsset>> captor = ArgumentCaptor.forClass(List.class);
        verify(assetRepository).saveAll(captor.capture());

        NetworkAsset asset = captor.getValue().get(0);
        assertEquals("10.0.0.1", asset.getIp());
        assertNotNull(asset.getMac());
        assertEquals("Cisco", asset.getVendor());
        assertEquals("COMPLIANT", asset.getComplianceStatus());
        assertNotNull(asset.getLastSeen());
    }
}
