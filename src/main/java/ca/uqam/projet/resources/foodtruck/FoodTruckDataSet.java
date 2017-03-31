package ca.uqam.projet.resources.foodtruck;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Stream;

public class FoodTruckDataSet {

    private List<FoodTruckData> foodTruckDataList;

    public FoodTruckDataSet() {
    }

    public List<FoodTruckData> getFoodTruckDataList() {
        return foodTruckDataList;
    }

    @JsonProperty("features")
    public void setFoodTruckDataList(List<FoodTruckData> foodTruckDataList) {
        this.foodTruckDataList = foodTruckDataList;
    }

    Stream<FoodTruckData> stream() {
        return foodTruckDataList.stream();
    }
}
