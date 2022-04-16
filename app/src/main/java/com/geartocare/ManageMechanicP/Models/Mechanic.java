package com.geartocare.ManageMechanicP.Models;

public class Mechanic {
    String firstName,lastName,phone,email,address,mechanicID,mechStatus,password,service_counter;


    public Mechanic(String firstName, String lastName, String phone, String email, String address, String mechanicID, String mechStatus, String password, String service_counter) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.mechanicID = mechanicID;
        this.mechStatus = mechStatus;
        this.password = password;
        this.service_counter = service_counter;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMechanicID() {
        return mechanicID;
    }

    public void setMechanicID(String mechanicID) {
        this.mechanicID = mechanicID;
    }

    public String getMechStatus() {
        return mechStatus;
    }

    public void setMechStatus(String mechStatus) {
        this.mechStatus = mechStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService_counter() {
        return service_counter;
    }

    public void setService_counter(String service_counter) {
        this.service_counter = service_counter;
    }
}
