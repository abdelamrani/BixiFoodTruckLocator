package ca.uqam.projet.resources.foodtruck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
public class FoodTruckRepository {

    private static final String TRUNCATE_STMT = "TRUNCATE foodtruck  RESTART IDENTITY CASCADE ";

    private static final String INSERT_STMT =
            " INSERT INTO foodtruck(truck_id, name, location, coordinates, start_time, end_time)"
                    + " VALUES ("
                    + " ?,"
                    + " ?,"
                    + " ?,"
                    + " ST_GeographyFromText('POINT(' || ? || ' ' ||? || ')'),"
                    + " ?,"
                    + " ?)"
                    + " ON CONFLICT DO NOTHING";

    private static final String FIND_ALL_STMT =
            "SELECT "
                    + "id"
                    + ", truck_id"
                    + ", name"
                    + ", location"
                    + ", ST_X(coordinates::geometry) AS longitude"
                    + ", ST_Y(coordinates::geometry) AS latitude"
                    + ", start_time"
                    + ", end_time"
                    + " FROM foodtruck";


    private static final String FIND_ALL_BY_ID_STMT =
            "SELECT "
                    + "id"
                    + ", truck_id"
                    + ", name"
                    + ", location"
                    + ", ST_X(coordinates::geometry) AS longitude"
                    + ", ST_Y(coordinates::geometry) AS latitude"
                    + ", start_time"
                    + ", end_time"
                    + " FROM foodtruck"
                    + " WHERE id = ?";


    private static final String FIND_ALL_BY_DATE_STMT =
            "SELECT "
                    + "id"
                    + ", truck_id"
                    + ", name"
                    + ", location"
                    + ", ST_X(coordinates::geometry) AS longitude"
                    + ", ST_Y(coordinates::geometry) AS latitude"
                    + ", start_time"
                    + ", end_time"
                    + " FROM foodtruck"
                    + " WHERE"
                    + " start_time BETWEEN ? AND ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int clearFoodTrucks() {
        return jdbcTemplate.update(TRUNCATE_STMT);
    }

    public void insertFoodTruck(FoodTruck foodTruck) {
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            ps.setString(1, foodTruck.getTruckId());
            ps.setString(2, foodTruck.getName());
            ps.setString(3, foodTruck.getLocation());
            ps.setDouble(4, foodTruck.getCoordinates().getLongitude());
            ps.setDouble(5, foodTruck.getCoordinates().getLatitude());
            ps.setTimestamp(6, new Timestamp(foodTruck.getStartTime().getTime()));
            ps.setTimestamp(7, new Timestamp(foodTruck.getEndTime().getTime()));
            return ps;
        });
    }

    public List<FoodTruck> findAll() {
        return jdbcTemplate.query(FIND_ALL_STMT, new FoodTruckRowMapper());
    }

    public FoodTruck findById(int id) {
        return jdbcTemplate.queryForObject(FIND_ALL_BY_ID_STMT, new Object[]{id}, new FoodTruckRowMapper());
    }

    public List<FoodTruck> findByDate(Date startDate, Date endDate) {
        return jdbcTemplate.query(FIND_ALL_BY_DATE_STMT
                , new Object[]{new Timestamp(startDate.getTime())
                        , new Timestamp(endDate.getTime())}, new FoodTruckRowMapper());
    }
}


