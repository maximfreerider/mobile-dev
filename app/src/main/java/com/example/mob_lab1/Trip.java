package com.example.mob_lab1;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {
    public Trip(String name, String come_from, String come_to, String avia_company, String speed, String registration_info, String flight_distance,String img) {
        this.name = name;
        this.come_from = come_from;
        this.come_to = come_to;
        this.avia_company = avia_company;
        this.speed = speed;
        this.registration_info = registration_info;
        this.flight_distance = flight_distance;
        this.img = img;
    }

    protected Trip(Parcel in) {
        name = in.readString();
        come_from = in.readString();
        come_to = in.readString();
        avia_company = in.readString();
        speed = in.readString();
        registration_info = in.readString();
        flight_distance = in.readString();
        img = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public String getImg() {
        return img;
    }

    private String name, come_from, come_to, avia_company, speed, registration_info, flight_distance,img;
    public Trip() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCome_from() {
        return come_from;
    }

    public void setCome_from(String come_from) {
        this.come_from = come_from;
    }

    public String getCome_to() {
        return come_to;
    }

    public void setCome_to(String come_to) {
        this.come_to = come_to;
    }

    public String getAvia_company() {
        return avia_company;
    }

    public void setAvia_company(String avia_company) {
        this.avia_company = avia_company;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getRegistration_info() {
        return registration_info;
    }

    public void setRegistration_info(String registration_info) {
        this.registration_info = registration_info;
    }

    public String getFlight_distance() {
        return flight_distance;
    }

    public void setFlight_distance(String flight_distance) {
        this.flight_distance = flight_distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(come_from);
        dest.writeString(come_to);
        dest.writeString(avia_company);
        dest.writeString(speed);
        dest.writeString(registration_info);
        dest.writeString(flight_distance);
        dest.writeString(img);
    }
}
