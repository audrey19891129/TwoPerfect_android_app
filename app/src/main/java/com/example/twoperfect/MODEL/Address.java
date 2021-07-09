package com.example.twoperfect.MODEL;

public class Address {

    public int id;
    public int clientId;
    public int civicnumber;
    public String appartment;
    public String street;
    public String city;
    public String province;
    public String country;
    public String zipcode;

    public Address() {};

    public Address(int id, int clientId, int civicnumber, String appartment, String street, String city,
                   String province, String country, String zipcode) {
        super();
        this.id = id;
        this.clientId = clientId;
        this.civicnumber = civicnumber;
        this.appartment = appartment;
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.zipcode = zipcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCivicnumber() {
        return civicnumber;
    }

    public void setCivicnumber(int civicnumber) {
        this.civicnumber = civicnumber;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        /*
        String address = civicnumber + " ";
        if(!appartment.isEmpty()) {
            address += appartment + " ";
        }
        address += street + ", " + city + ", " + province + ", "
                + country + ", " + zipcode;
        return address;*/

        String address = String.valueOf(civicnumber) + " ";
        if(!appartment.contentEquals("0") || appartment != null || appartment.contentEquals(""))
            address += appartment + " ";
        address += street + ", " + city + ", " + province + ", "
                + country + ", " + zipcode;
        return address;
    }



}
