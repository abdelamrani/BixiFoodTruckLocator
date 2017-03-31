package ca.uqam.projet.tasks;

import ca.uqam.projet.resources.bikestation.BikeStationDataParseException;
import ca.uqam.projet.resources.bikestation.BikeStationDataService;
import ca.uqam.projet.resources.bikestation.BikeStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
* This class represents a task run every 10 minutes
* that fetches the bike stations schedule at the specified URL.
*/
@Component
public class UpdateBikeStationDataTask implements ApplicationListener {

    public static final String BIKESTATIONS_URL = "https://montreal.bixi.com/data/bikeStations.xml";
    public static final Logger log = LoggerFactory.getLogger(UpdateBikeStationDataTask.class);

    @Autowired
    private BikeStationRepository repository;


    @Scheduled(cron = "0 */10 * * * ?")
    public void updateBikeStationsData() {
        repository.clearBikeStations();
        try {
            BikeStationDataService
                    .fetchDataSet(BIKESTATIONS_URL)
                    .forEach(repository::insertBikeStation);
            log.info("Successfully loaded bike station data.");
        } catch (BikeStationDataParseException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            updateBikeStationsData();
        }
    }
}
