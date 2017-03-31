package ca.uqam.projet.resources.user;

import ca.uqam.projet.resources.foodtruck.FoodTruck;
//import ca.uqam.projet.resources.foodtruck.FoodTruckRowMapper;

import java.util.List;

import ca.uqam.projet.resources.foodtruck.FoodTruckRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class UserRepository {
    private static final String TRUNCATE_STMT = "TRUNCATE useraccount  RESTART IDENTITY";

    private static final String INSERT_DEFAULT_FAVORITES_STMT =
            "INSERT INTO useraccount (foodtruck_name, username, password)\n" +
                    "  SELECT\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    crypt(?, gen_salt('md5')) \n" +
                    "  WHERE \n" +
                    "    NOT EXISTS(\n" +
                    "        SELECT foodtruck_name \n" +
                    "        FROM useraccount \n" +
                    "        WHERE foodtruck_name = ? \n" +
                    "              AND username = ? \n" +
                    "              AND  password = crypt(?, password)\n" +
                    "    )";

    private static final String INSERT_FACEBOOK_FAVORITES_STMT =
            "INSERT INTO useraccount (foodtruck_name, username, password)\n" +
                    "  SELECT\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    crypt(?, gen_salt('md5'))\n" +
                    "  WHERE\n" +
                    "    NOT EXISTS(\n" +
                    "        SELECT foodtruck_name\n" +
                    "        FROM useraccount \n" +
                    "        WHERE foodtruck_name = ?\n" +
                    "              AND username = ? \n" +
                    "              AND  password = crypt(?, password)\n" +
                    "    )";

    private static final String DELETE_DEFAULT_FAVORITES_STMT =
            "DELETE FROM useraccount\n" +
                    "WHERE username = ?\n" +
                    "      AND password = crypt(?, password)\n" +
                    "      AND foodtruck_name = ?";

    private static final String DELETE_FACEBOOK_FAVORITES_STMT =
            "DELETE FROM useraccount\n" +
                    "WHERE username = ?\n" +
                    "      AND foodtruck_name = ?";

    private static final String FIND_DEFAULT_FAVORITES_STMT =
            "SELECT\n" +
                    "  fd.id,\n" +
                    "  fd.truck_id,\n" +
                    "  fd.name,\n" +
                    "  fd.location,\n" +
                    "  ST_X(fd.coordinates :: GEOMETRY) AS longitude,\n" +
                    "  ST_Y(fd.coordinates :: GEOMETRY) AS latitude,\n" +
                    "  fd.start_time,\n" +
                    "  fd.end_time\n" +
                    "FROM foodtruck fd, useraccount usr\n" +
                    "WHERE usr.username = ? \n" +
                    "      AND password = crypt( ? , password)\n" +
                    "      AND fd.name = usr.foodtruck_name";

    private static final String FIND_FACEBOOK_FAVORITES_STMT =
            "SELECT\n" +
                    "  fd.id,\n" +
                    "  fd.truck_id,\n" +
                    "  fd.name,\n" +
                    "  fd.location,\n" +
                    "  ST_X(fd.coordinates :: GEOMETRY) AS longitude,\n" +
                    "  ST_Y(fd.coordinates :: GEOMETRY) AS latitude,\n" +
                    "  fd.start_time,\n" +
                    "  fd.end_time\n" +
                    "FROM foodtruck fd, useraccount usr\n" +
                    "WHERE usr.username = ? \n" +
                    "      AND password = crypt( ? , password)\n" +
                    "      AND fd.name = usr.foodtruck_name";

    private static final String INSERT_ON_LOAD_FAVORITES_STMT =
            "INSERT INTO useraccount (foodtruck_name, username, password)\n" +
                    "  SELECT\n" +
                    "    ? ,\n" +
                    "    'username',\n" +
                    "    crypt('password', gen_salt('md5'))\n" +
                    "  WHERE\n" +
                    "    NOT EXISTS(\n" +
                    "        SELECT foodtruck_name\n" +
                    "        FROM useraccount\n" +
                    "        WHERE foodtruck_name = ? \n" +
                    "              AND username = 'username'\n" +
                    "              AND password = crypt('password', password)\n" +
                    "    )";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int clearLoginAccounts() {
        return jdbcTemplate.update(TRUNCATE_STMT);
    }

    public void insertOnloadFavoritesFoodTrucks() {
        String[] favoritesFoodTrucks = {
                "Dim Sum Mtl",
                "Pinokio",
                "Landry & filles",
                "Traiteur Guru",
                "BURGER Truck",
                "Pas d'cochon dans mon salon"
        };

        for (int i = 0; i < favoritesFoodTrucks.length; i++) {
            final int ii = i;
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_ON_LOAD_FAVORITES_STMT);
                ps.setString(1, favoritesFoodTrucks[ii]);
                ps.setString(2, favoritesFoodTrucks[ii]);

                return ps;
            });
        }
    }

    public List<FoodTruck> queryUserInformations(String login, String userNameAccount, String passwordAccount) {
        if(login.equals("facebook")){
            return jdbcTemplate.query( FIND_FACEBOOK_FAVORITES_STMT
                    , new Object[]{userNameAccount, passwordAccount}
                    , new FoodTruckRowMapper());
        }else{
            return jdbcTemplate.query(FIND_DEFAULT_FAVORITES_STMT
                    , new Object[]{userNameAccount, passwordAccount}
                    , new FoodTruckRowMapper());
        }
    }

    public List<FoodTruck> insertUserInformations(String login, String userNameAccount, String passwordAccount, List<String> names) {

        if(login.equals("facebook")){

            for (String name :names) {
                jdbcTemplate.update(INSERT_FACEBOOK_FAVORITES_STMT
                        , new Object[]{name, userNameAccount, passwordAccount,name, userNameAccount, passwordAccount});
            }

        }else {
            for (String name : names) {
                jdbcTemplate.update(INSERT_DEFAULT_FAVORITES_STMT
                        , new Object[]{name, userNameAccount, passwordAccount, name, userNameAccount, passwordAccount});
            }
        }
        return queryUserInformations(login, userNameAccount, passwordAccount);
    }

    public List<FoodTruck> deleteUserInformations(String login, String userNameAccount, String passwordAccount, List<String> names) {
        if(login.equals("facebook")){
            for (String name :names) {
                jdbcTemplate.update(DELETE_FACEBOOK_FAVORITES_STMT
                        , new Object[]{userNameAccount, passwordAccount, name});
            }

        }else {
            for (String name : names) {
                jdbcTemplate.update(DELETE_DEFAULT_FAVORITES_STMT
                        , new Object[]{userNameAccount, passwordAccount, name});
            }
        }
        return queryUserInformations(login, userNameAccount, passwordAccount);
    }
}
