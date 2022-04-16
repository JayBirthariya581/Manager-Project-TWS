package com.geartocare.ManageMechanicP.Helpers;

public class UserHelperClass {
    String lastName;
    String firstName;
    String phone;
    String email;
    String password;
    String keyStatus="false";
    KeyHelper keyHelper;




    public UserHelperClass(String lastName, String firstName, String phone, String email, String password, String keyStatus , KeyHelper keyHelper) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.keyStatus = keyStatus;
        this.keyHelper=keyHelper;
    }

    public KeyHelper getKeyHelper() {
        return keyHelper;
    }

    public void setKeyHelper(KeyHelper keyHelper) {
        this.keyHelper = keyHelper;
    }

    public String getKeyStatus() {
        return keyStatus;
    }

    public void setKeyStatus(String keyStatus) {
        this.keyStatus = keyStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
