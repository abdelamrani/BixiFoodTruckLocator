package ca.uqam.projet.resources.foodtruck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
* This class is only used as an intermediate object to get
* the data contained within the fetched JSON file.
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodTruckData {

    private FoodTruckGeoLocation foodTruckGeoLocation;
    private FoodTruckProperties properties;

    public FoodTruckData() {
    }

    public FoodTruckData(FoodTruckGeoLocation foodTruckGeoLocation, FoodTruckProperties properties) {
        this.foodTruckGeoLocation = foodTruckGeoLocation;
        this.properties = properties;
    }

    @JsonProperty("geometry")
    public FoodTruckGeoLocation getGeoLocation() {
        return foodTruckGeoLocation;
    }

    @JsonProperty("geometry")
    public void setGeoLocation(FoodTruckGeoLocation foodTruckGeoLocation) {
        this.foodTruckGeoLocation = foodTruckGeoLocation;
    }

    @JsonProperty("properties")
    public FoodTruckProperties getProperties() {
        return properties;
    }


    @JsonProperty("properties")
    public void setProperties(FoodTruckProperties properties) {
        this.properties = properties;
    }
}
