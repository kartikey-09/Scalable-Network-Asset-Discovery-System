package com.motadata.discovery.repository;

import com.motadata.discovery.model.NetworkAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NetworkAssetRepository extends JpaRepository<NetworkAsset, String> {
    List<NetworkAsset> findByComplianceStatus(String status);
}
