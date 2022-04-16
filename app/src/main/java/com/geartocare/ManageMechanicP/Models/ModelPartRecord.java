package com.geartocare.ManageMechanicP.Models;

public class ModelPartRecord {
    String partID,available,used,originalCount;


    public String getPartID() {
        return partID;
    }

    public void setPartID(String partID) {
        this.partID = partID;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getOriginalCount() {
        return originalCount;
    }

    public void setOriginalCount(String originalCount) {
        this.originalCount = originalCount;
    }
}
