package com.example.twoperfect.MODEL;

public class Technician extends Employee{

    public static Employee technician = null;

    public Technician(){};

    public static Employee getTechnician() {
        return technician;
    }

    public static void setTechnician(Employee technician) {
        Technician.technician = technician;
    }

}
