package com.motadata.discovery.controller;

import com.motadata.discovery.service.DiscoveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discovery")
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @PostMapping
    public ResponseEntity<String> startDiscovery(@RequestBody List<String> ipList) {
        discoveryService.discoverDevices(ipList);
        return ResponseEntity.ok("Discovery started.");
    }
}
