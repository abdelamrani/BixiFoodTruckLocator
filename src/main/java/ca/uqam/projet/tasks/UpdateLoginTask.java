package ca.uqam.projet.tasks;

import ca.uqam.projet.resources.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class UpdateLoginTask implements ApplicationListener {

    public static final Logger log = LoggerFactory.getLogger(UpdateLoginTask.class);

    @Autowired
    private UserRepository repository;

    public void updateLoginAccount() {
        repository.clearLoginAccounts();
        repository.insertOnloadFavoritesFoodTrucks();
        log.info("Successfully clear all accounts and add one login");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            updateLoginAccount();
        }
    }
}
