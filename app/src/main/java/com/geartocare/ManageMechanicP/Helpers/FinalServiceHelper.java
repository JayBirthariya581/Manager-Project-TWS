package com.geartocare.ManageMechanicP.Helpers;

public class FinalServiceHelper {

    String serviceID,date,time,phone,status;

    LocationHelper location;
    PaymentHelper payment;


    public FinalServiceHelper(String serviceID, String phone, String date, String time, LocationHelper location, PaymentHelper payment, String status) {
        this.serviceID = serviceID;
        this.date = date;
        this.time = time;

        this.location = location;
        this.payment = payment;
        this.phone = phone;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocationHelper getLocation() {
        return location;
    }

    public void setLocation(LocationHelper location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public LocationHelper getlocation() {
        return location;
    }

    public void setlocation(LocationHelper location) {
        this.location = location;
    }

    public PaymentHelper getPayment() {
        return payment;
    }

    public void setPayment(PaymentHelper payment) {
        this.payment = payment;
    }
}
