package ca.uqam.projet.resources.user;

import java.util.List;

/**
 * Created by celstn on 2016-07-21.
 */
public class RequestFavoritesFoodTrucksWrapper {
    private String request;
    private String login;
    private String username;
    private String password;
    private List<String> names;

    public RequestFavoritesFoodTrucksWrapper() {
    }

    public RequestFavoritesFoodTrucksWrapper(String request, String login, String username, String password, List<String> names) {
        this.request = request;
        this.login = login;
        this.username = username;
        this.password = password;
        this.names = names;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}