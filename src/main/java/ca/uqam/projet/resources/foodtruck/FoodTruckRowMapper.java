package ca.uqam.projet.resources.foodtruck;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by celstn on 2016-07-14.
 */
public class FoodTruckRowMapper implements RowMapper<FoodTruck> {

    @Override
    public FoodTruck mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FoodTruck(
                rs.getInt("id")
                , rs.getString("truck_id")
                , rs.getString("name")
                , rs.getString("location")
                , rs.getDouble("longitude")
                , rs.getDouble("latitude")
                , new Date(rs.getTimestamp("start_time").getTime())
                , new Date(rs.getTimestamp("end_time").getTime())
        );
    }
}

