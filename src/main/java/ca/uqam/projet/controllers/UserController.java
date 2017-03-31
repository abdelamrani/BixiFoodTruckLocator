package ca.uqam.projet.controllers;

import ca.uqam.projet.exceptions.BadRequestParamException;
import ca.uqam.projet.resources.foodtruck.FoodTruck;
import ca.uqam.projet.resources.user.RequestFavoritesFoodTrucksWrapper;
import ca.uqam.projet.resources.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    UserRepository repository;

    @RequestMapping(value = "/foodtrucks/favorites", method = RequestMethod.POST)
    public List<FoodTruck> login(@RequestBody RequestFavoritesFoodTrucksWrapper requestWrapper
    ) throws BadRequestParamException {

        if (requestWrapper.getRequest().equals("login")) {
            return repository.queryUserInformations(
                    requestWrapper.getLogin(),
                    requestWrapper.getUsername(),
                    requestWrapper.getPassword()
                    );

        } else if (requestWrapper.getRequest().equals("insert")) {
            return repository.insertUserInformations(
                    requestWrapper.getLogin(),
                    requestWrapper.getUsername(),
                    requestWrapper.getPassword(),
                    requestWrapper.getNames()
            );
        } else if (requestWrapper.getRequest().equals("delete")) {
            return repository.deleteUserInformations(
                    requestWrapper.getLogin(),
                    requestWrapper.getUsername(),
                    requestWrapper.getPassword(),
                    requestWrapper.getNames()
            );
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