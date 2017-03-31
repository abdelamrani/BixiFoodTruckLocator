package ca.uqam.projet.resources.bikerack;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class BikeRackRepository {

    private static final String TRUNCATE_BIKERACK_STMT = "TRUNCATE bikerack RESTART IDENTITY";

    private static final String INSERT_BIKERACK_STMT =
            " INSERT INTO bikerack(rack_id, brand, coordinates)"
                    + " VALUES ("
                    + " ?,"
                    + " ?,"
                    + " ST_GeographyFromText('POINT(' || ? || ' ' ||? || ')')"
                    + ")"
                    + " ON CONFLICT DO NOTHING";


    private static final String FIND_ALL_STMT =
            "SELECT "
                    + "rack_id"
                    + ", brand"
                    + ", ST_X(coordinates::geometry) AS longitude"
                    + ", ST_Y(coordinates::geometry) AS latitude"
                    + " FROM bikerack";


    private static final String FIND_ALL_BY_ID_STMT =
            "SELECT "
                    + "rack_id"
                    + ", brand"
                    + ", ST_X(coordinates::geometry) AS longitude"
                    + ", ST_Y(coordinates::geometry) AS latitude"
                    + " FROM bikerack"
                    + " WHERE rack_id = ?";


    private static final String FIND_ALL_BY_RADIUS_STMT =
            "SELECT * " +
                    "FROM getBikeRacksInRadius( ?, ?, ? )";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int clearBikeRackData() {
        return jdbcTemplate.update(TRUNCATE_BIKERACK_STMT);
    }

    public void insertBikeRack(BikeRack bikeRack) {
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_BIKERACK_STMT);
            ps.setInt(1, bikeRack.getId());
            ps.setString(2, bikeRack.getBrand());
            ps.setDouble(3, bikeRack.getCoordinates().getLongitude());
            ps.setDouble(4, bikeRack.getCoordinates().getLatitude());
            return ps;
        });
    }

    public List<BikeRack> findAll() {
        return jdbcTemplate.query(FIND_ALL_STMT, new BikeRackRowMapper());
    }

    public BikeRack findById(int id) {
        return jdbcTemplate.queryForObject(FIND_ALL_BY_ID_STMT, new Object[]{id}, new BikeRackRowMapper());
    }

    public List<BikeRack> findInRadius(double longitudeCenter, double latitudeCenter, double radiusLimit) {
        return jdbcTemplate.query(FIND_ALL_BY_RADIUS_STMT,
                new Object[]{longitudeCenter, latitudeCenter, radiusLimit},
                new BikeRackRowMapper()
        );
    }
}

class BikeRackRowMapper implements RowMapper<BikeRack> {

    @Override
    public BikeRack mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BikeRack(
                rs.getInt("rack_id")
                , rs.getString("brand")
                , rs.getDouble("longitude")
                , rs.getDouble("latitude")
        );
    }
}

