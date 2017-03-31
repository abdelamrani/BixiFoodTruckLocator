package ca.uqam.projet.tasks;


import ca.uqam.projet.resources.foodtruck.FoodTruckDataService;
import ca.uqam.projet.resources.foodtruck.FoodTruckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
* This class represents a task run at 12:00 and 24:00 every day
* that fetches the food trucks schedule at the specified URL.
*/
@Component
public class UpdateFoodTruckDataTask implements ApplicationListener {

    public static final String FOODTRUCK_URL = "http://camionderue.com/donneesouvertes/geojson";
    public static final Logger log = LoggerFactory.getLogger(UpdateFoodTruckDataTask.class);

    @Autowired
    private FoodTruckRepository repository;


    @Scheduled(cron = "0 0 12/12 * * ?")
    public void updateFoodTruckData() {
        repository.clearFoodTrucks();
        FoodTruckDataService
                .fetchDataSet(FOODTRUCK_URL)
                .forEach(repository::insertFoodTruck);
        log.info("Successfully loaded food truck data.");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            updateFoodTruckData();
        }
    }
}
