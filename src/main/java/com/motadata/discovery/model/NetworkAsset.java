package com.motadata.discovery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class NetworkAsset {

    @Id
    private String ip;

    private String mac;
    private String vendor;
    private LocalDateTime lastSeen;
    private String complianceStatus;

    @Version
    private Integer version;

    // Getters and Setters
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getMac() { return mac; }
    public void setMac(String mac) { this.mac = mac; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }

    public String getComplianceStatus() { return complianceStatus; }
    public void setComplianceStatus(String complianceStatus) { this.complianceStatus = complianceStatus; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
}
