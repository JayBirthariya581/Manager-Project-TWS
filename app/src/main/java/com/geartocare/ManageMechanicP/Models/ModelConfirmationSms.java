package com.geartocare.ManageMechanicP.Models;

public class ModelConfirmationSms {
    String fullName,phone;
    ModelVehicle vehicle;
    ModelService service;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ModelVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(ModelVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ModelService getService() {
        return service;
    }

    public void setService(ModelService service) {
        this.service = service;
    }
}
