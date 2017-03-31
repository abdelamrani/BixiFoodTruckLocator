package ca.uqam.projet.resources.foodtruck;

import ca.uqam.projet.resources.Point;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the effective food truck class we use internally.
 */
@JsonPropertyOrder({"id", "truckId", "name", "location", "coordinates", "startTime", "endTime"})
public class FoodTruck {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    private int id;

    private String truckId;

    private String name;

    private String location;

    private Point coordinates;

    private Date startTime;

    private Date endTime;

    public FoodTruck(int id, String truckId, String name, String location, double longitude, double latitude, Date startTime, Date endTime) {
        this.id = id;
        this.truckId = truckId;
        this.name = name;
        this.location = location;
        this.coordinates = new Point(longitude, latitude);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public FoodTruck(String truckId, String name) {
        this.truckId = truckId;
        this.name = name;
    }

    @JsonProperty("id")
    public int getId() {
        return this.id;
    }

    @JsonProperty("truckId")
    public String getTruckId() {
        return this.truckId;
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("location")
    public String getLocation() {
        return this.location;
    }

    @JsonProperty("coordinates")
    public Point getCoordinates() {
        return this.coordinates;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    @JsonProperty("startTime")
    public String getStartTimeAsString() {
        return DATE_FORMAT.format(this.startTime);
    }

    public Date getEndTime() {
        return this.endTime;
    }

    @JsonProperty("endTime")
    public String getEndTimeAsString() {
        return DATE_FORMAT.format(this.endTime);
    }
}
