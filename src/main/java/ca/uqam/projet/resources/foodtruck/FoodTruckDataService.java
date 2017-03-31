package ca.uqam.projet.resources.foodtruck;

import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FoodTruckDataService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static List<FoodTruck> fetchDataSet(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, FoodTruckDataSet.class)
                .stream()
                .map(FoodTruckDataService::createFoodTruck)
                .collect(Collectors.toList());
    }

    private static FoodTruck createFoodTruck(FoodTruckData data) {
        FoodTruckProperties properties = data.getProperties();
        FoodTruckGeoLocation geoLocation = data.getGeoLocation();

        return new FoodTruck(
                0,
                properties.getTruckId(),
                properties.getCamion(),
                properties.getLieu(),
                geoLocation.getLongitude(),
                geoLocation.getLatitude(),
                parseDate(String.format("%s %s", properties.getDate(), properties.getHeureDebut())),
                parseDate(String.format("%s %s", properties.getDate(), properties.getHeureFin())));
    }

    private static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            System.err.println("Date parsing failed");
        }
        return null;
    }
}
