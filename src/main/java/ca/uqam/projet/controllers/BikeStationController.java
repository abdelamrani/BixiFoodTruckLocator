package ca.uqam.projet.controllers;

import ca.uqam.projet.exceptions.BadRequestParamException;
import ca.uqam.projet.resources.bikestation.BikeStation;
import ca.uqam.projet.resources.bikestation.BikeStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class BikeStationController {

    public static final Logger log = LoggerFactory.getLogger(BikeStationController.class);

    @Autowired
    BikeStationRepository repository;

    @RequestMapping("/bikestations/{id}")
    public BikeStation findById(@PathVariable("id") int id) {
        return repository.findById(id);
    }

    @RequestMapping(value = "/bikestations", method = RequestMethod.GET)
    public List<BikeStation> findRange(
            @RequestParam(value = "long", required = false) String longitudeCenter
            , @RequestParam(value = "lat", required = false) String latitudeCenter
            , @RequestParam(value = "rad", required = false) String radiusLimit
    ) throws BadRequestParamException {

        log.info("GET /bikestations?long=" + longitudeCenter + "&lat=" + latitudeCenter + "&rad=" + radiusLimit);

        if (longitudeCenter == null &&
                latitudeCenter == null &&
                radiusLimit == null) {
            return repository.findAll();
        } else if (longitudeCenter != null &&
                latitudeCenter != null &&
                radiusLimit != null) {
            try {
                return repository.findInRadius(Double.parseDouble(longitudeCenter),
                        Double.parseDouble(latitudeCenter),
                        Double.parseDouble(radiusLimit)
                );
            } catch (NumberFormatException ex) {
                throw new BadRequestParamException(ex.getMessage());
            } catch (NullPointerException ex) {
                throw new BadRequestParamException(ex.getMessage());
            }

        } else if (longitudeCenter == null) {
            throw new BadRequestParamException("Missing parameter 'long'.");
        } else if (latitudeCenter == null) {
            throw new BadRequestParamException("Missing parameter 'lat'.");
        } else if (radiusLimit == null) {
            throw new BadRequestParamException("Missing parameter 'rad'.");
        }
        return null;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    void handleNotFoundRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(BadRequestParamException.class)
    void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
