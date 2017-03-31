package ca.uqam.projet.tasks;

import ca.uqam.projet.resources.bikerack.BikeRackDataService;
import ca.uqam.projet.resources.bikerack.BikeRackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
* This class represents a task run every first day of the month
* that fetches the bike stations schedule at the specified URL.
*/
@Component
public class UpdateBikeRackDataTask implements ApplicationListener {

    public static final String BIKERACK_URL =
            "http://donnees.ville.montreal.qc.ca/dataset/c4dfdeb1-cdb7-44f4-8068-247755a56cc6/resource/78dd2f91-2e68-4b8b-bb4a-44c1ab5b79b6/download/supportvelosigs.csv";
    public static final Logger log = LoggerFactory.getLogger(UpdateBikeRackDataTask.class);

    @Autowired
    private BikeRackRepository repository;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateBikeRacksData() {
        repository.clearBikeRackData();
        BikeRackDataService.fetchDataSet(BIKERACK_URL)
                .forEach(repository::insertBikeRack);
        log.info("Successfully loaded bike rack data.");

    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            updateBikeRacksData();
        }
    }
}