package com.example.twoperfect.MODEL;

public class Communication {

    public int id, technicianId, clientId, interventionId;
    public String dateTime,type;

    public Communication() { }

    public Communication(int id, int technicianId, int interventionId, int clientId, String dateTime, String type) {
        super();
        this.id = id;
        this.technicianId = technicianId;
        this.interventionId = interventionId;
        this.clientId = clientId;
        this.dateTime = dateTime;
        this.type = type;
    }

    public Communication(int technicianId, int interventionId, int clientId, String dateTime, String type) {
        super();
        this.technicianId = technicianId;
        this.interventionId = interventionId;
        this.clientId = clientId;
        this.dateTime = dateTime;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public int getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(int interventionId) {
        this.interventionId = interventionId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Communication [id=" + id + ", technicianId=" + technicianId + ", interventionId=" + interventionId + ", clientId=" + clientId +  ", dateTime="
                + dateTime + ", type=" + type + "]";
    }
}


