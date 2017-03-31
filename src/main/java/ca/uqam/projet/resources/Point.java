package ca.uqam.projet.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {

    private double longitude;

    private double latitude;

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @JsonProperty
    public double getLongitude() {
        return this.longitude;
    }

    @JsonProperty
    public double getLatitude() {
        return this.latitude;
    }
}
