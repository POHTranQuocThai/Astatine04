/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class Transport {
    private int transportId;
    private String transportName;
    private String description;

    public Transport() {
    }

    public Transport(int transportId, String transportName, String description) {
        this.transportId = transportId;
        this.transportName = transportName;
        this.description = description;
    }
    
    public Transport(String transportName, String description, int transportId) {
        this.transportId = transportId;
        this.transportName = transportName;
        this.description = description;
    }

    public int getTransportId() {
        return transportId;
    }

    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transport{" + "transportId=" + transportId + ", transportName=" + transportName + ", description=" + description + '}';
    }
    
}
