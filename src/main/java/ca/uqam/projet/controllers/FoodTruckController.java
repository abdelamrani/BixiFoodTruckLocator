package ca.uqam.projet.controllers;

import ca.uqam.projet.exceptions.BadRequestParamException;
import ca.uqam.projet.resources.foodtruck.FoodTruck;
import ca.uqam.projet.resources.foodtruck.FoodTruckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class FoodTruckController {

    public static final Logger log = LoggerFactory.getLogger(FoodTruckController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String REG_EXP_DATE_FORMAT = "^[0-9]{4}-((1[0-2])|(0[1-9]))-((0[1-9])|([1-2][0-9])|(3[0-1]))$";

    @Autowired
    FoodTruckRepository repository;

    private static Date parseDate(String date) throws ParseException {
        if (!date.matches(REG_EXP_DATE_FORMAT)) {
            throw new ParseException(String.format("Unable to parse %s.", date), 0);
        }
        dateFormat.setLenient(false);
        return dateFormat.parse(date);
    }

    @RequestMapping("/foodtrucks/{id}")
    public FoodTruck findById(@PathVariable("id") int id) {
        return repository.findById(id);
    }

    @RequestMapping(value = "/foodtrucks", method = RequestMethod.GET)
    public List<FoodTruck> findRange(
            @RequestParam(value = "du", required = false) String fromDate
            , @RequestParam(value = "au", required = false) String toDate) throws BadRequestParamException {

        if (fromDate == null & toDate == null) {
            log.info("GET /foodtrucks");
            return repository.findAll();
        } else if (fromDate != null && toDate != null) {
            try {
                Date from = parseDate(fromDate);
                Date to = new Date(parseDate(toDate).getTime()
                        + TimeUnit.DAYS.toMillis(1) - 1);

                if (from.after(to)) {
                    throw new BadRequestParamException("Date range error.");
                }

                log.info("GET /foodtrucks?du=" + fromDate + "&au=" + toDate);

                return repository.findByDate(from, to);

            } catch (ParseException ex) {
                throw new BadRequestParamException(ex.getMessage());
            }
        } else if (fromDate == null) {
            throw new BadRequestParamException("Missing parameter 'du'.");
        } else if (toDate == null) {
            throw new BadRequestParamException("Missing parameter 'au'.");
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
