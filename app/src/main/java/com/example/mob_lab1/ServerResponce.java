package com.example.mob_lab1;

import java.util.ArrayList;

public class ServerResponce {
    ArrayList<Trip> planes;

    public ServerResponce(ArrayList<Trip> trips) {
        this.planes = trips;
    }

    public ArrayList<Trip> getTrips() {
        return planes;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.planes = trips;
    }
}
