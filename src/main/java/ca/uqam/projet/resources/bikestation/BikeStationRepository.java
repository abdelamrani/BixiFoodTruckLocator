package ca.uqam.projet.resources.bikestation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class BikeStationRepository {

    private static final String TRUNCATE_BIKESTATION_STMT = "TRUNCATE bikestation RESTART IDENTITY";

    private static final String INSERT_BIKESTATION_STMT =
            " INSERT INTO bikestation(station_id, name, coordinates, bike_count, empty_spaces)"
                    + " VALUES ("
                    + " ?,"
                    + " ?,"
                    + " ST_GeographyFromText('POINT(' || ? || ' ' ||? || ')'),"
                    + " ?,"
                    + " ?)"
                    + " ON CONFLICT DO NOTHING";


    private static final String FIND_ALL_STMT =
            "SELECT "
                    + "station_id"
                    + ", name"
                    + ", bike_count"
                    + ", empty_spaces"
                    + ", ST_X(coordinates::geometry) AS longitude"
                    + ", ST_Y(coordinates::geometry) AS latitude"
                    + " FROM bikestation";


    private static final String FIND_ALL_BY_ID_STMT =
            "SELECT "
                    + "station_id"
                    + ", name"
                    + ", bike_count"
                    + ", empty_spaces"
                    + ", ST_X(coordinates::geometry) AS longitude"
                    + ", ST_Y(coordinates::geometry) AS latitude"
                    + " FROM bikestation"
                    + " WHERE station_id = ?";


    private static final String FIND_ALL_BY_RADIUS_STMT =
            "SELECT * " +
                    "FROM getBikeStationsInRadius( ?, ?, ? )";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int clearBikeStations() {
        return jdbcTemplate.update(TRUNCATE_BIKESTATION_STMT);
    }

    public void insertBikeStation(BikeStation bikeStation) {
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_BIKESTATION_STMT);
            ps.setInt(1, bikeStation.getId());
            ps.setString(2, bikeStation.getName());
            ps.setDouble(3, bikeStation.getCoordinates().getLongitude());
            ps.setDouble(4, bikeStation.getCoordinates().getLatitude());
            ps.setInt(5, bikeStation.getBikeCount());
            ps.setInt(6, bikeStation.getEmptySpacesCount());
            return ps;
        });
    }

    public List<BikeStation> findAll() {
        return jdbcTemplate.query(FIND_ALL_STMT, new BikeStationRowMapper());
    }

    public BikeStation findById(int id) {
        return jdbcTemplate.queryForObject(FIND_ALL_BY_ID_STMT, new Object[]{id}, new BikeStationRowMapper());
    }

    public List<BikeStation> findInRadius(double longitudeCenter, double latitudeCenter, double radiusLimit) {
        return jdbcTemplate.query(FIND_ALL_BY_RADIUS_STMT,
                new Object[]{longitudeCenter, latitudeCenter, radiusLimit},
                new BikeStationRowMapper()
        );
    }
}

class BikeStationRowMapper implements RowMapper<BikeStation> {

    @Override
    public BikeStation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BikeStation(
                rs.getInt("station_id")
                , rs.getString("name")
                , rs.getInt("bike_count")
                , rs.getInt("empty_spaces")
                , rs.getDouble("longitude")
                , rs.getDouble("latitude")
        );
    }
}

