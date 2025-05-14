package com.motadata.discovery.events;

public class AssetDiscoveredEvent {
    private String ip;

    public AssetDiscoveredEvent() {}

    public AssetDiscoveredEvent(String ip) {
        this.ip = ip;
    }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}
