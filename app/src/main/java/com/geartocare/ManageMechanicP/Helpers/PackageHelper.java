package com.geartocare.ManageMechanicP.Helpers;

public class PackageHelper {
    String id,cost,description,name,serviceCost,validity,vehicleCount;

    public PackageHelper(String id, String name, String cost, String description, String serviceCost, String validity, String vehicleCount) {
        this.id = id;
        this.cost = cost;
        this.description = description;
        this.name = name;
        this.serviceCost = serviceCost;
        this.validity = validity;
        this.vehicleCount = vehicleCount;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(String vehicleCount) {
        this.vehicleCount = vehicleCount;
    }
}
