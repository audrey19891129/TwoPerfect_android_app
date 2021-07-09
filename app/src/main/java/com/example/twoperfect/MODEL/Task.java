package com.example.twoperfect.MODEL;

public class Task {

    public static Intervention intervention;
    public static Ticket ticket;

    public static Intervention getIntervention() {
        return intervention;
    }

    public static void setIntervention(Intervention intervention) {
        Task.intervention = intervention;
    }

    public static Ticket getTicket() {
        return ticket;
    }

    public static void setTicket(Ticket ticket) {
        Task.ticket = ticket;
    }
}
