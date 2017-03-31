package ca.uqam.projet.resources.foodtruck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodTruckGeoLocation {
    private double latitude;
    private double longitude;

    public FoodTruckGeoLocation() {
    }

    public FoodTruckGeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(double[] coordinates) {
        this.longitude = coordinates[0];
        this.latitude = coordinates[1];
    }

    @Override
    public String toString() {
        return String.format("FoodTruckGeoLocation{latitude=%f, longitude=%f}", latitude, longitude);
    }
}
