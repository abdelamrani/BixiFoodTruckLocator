DROP DATABASE projet;
CREATE DATABASE projet;
\c projet
CREATE EXTENSION postgis;
CREATE EXTENSION pgcrypto;

--     {
--       "type": "Feature",
--       "geometry": {
--         "type": "Point",
--         "coordinates": [
--           -73.569415,
--           45.499123
--         ]
--       },
--       "properties": {
--         "name": "",
--         "description": "",
--         "Date": "2016-07-02",
--         "Heure_debut": "12:00",
--         "Heure_fin": "17:00",
--         "Lieu": "Place du Canada",
--         "Camion": "Grumman &#039;78",
--         "Truckid": "T00280008"
--       }
--     }

CREATE TABLE foodtruck (
  id          SERIAL PRIMARY KEY,
  truck_id    VARCHAR(10)  NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  location    VARCHAR(100) NOT NULL,
  coordinates GEOGRAPHY( 'POINT'
) NOT NULL,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
end_time TIMESTAMP WITH TIME ZONE NOT NULL
);

-- <station>
-- <id>2</id>
-- <name>Dézery/Ste-Catherine</name>
-- <terminalName>6002</terminalName>
-- <lastCommWithServer>1467472623890</lastCommWithServer>
-- <lat>45.539185</lat>
-- <long>-73.541019</long>
-- <installed>true</installed>
-- <locked>false</locked>
-- <installDate/>
-- <removalDate/>
-- <temporary>false</temporary>
-- <public>true</public>
-- <nbBikes>9</nbBikes>
-- <nbEmptyDocks>14</nbEmptyDocks>
-- <lastUpdateTime>1467469676418</lastUpdateTime>
-- </station>

CREATE TABLE bikestation (
  id          SERIAL PRIMARY KEY,
  station_id  INTEGER      NOT NULL,
  name        VARCHAR(100) NOT NULL,
  coordinates GEOGRAPHY( 'POINT'
) NOT NULL,
  bike_count INTEGER NOT NULL CHECK (bike_count >= 0
),
  empty_spaces INTEGER NOT NULL CHECK (empty_spaces >= 0
)
);


CREATE TABLE bikerack (
  id          SERIAL PRIMARY KEY,
  rack_id     INTEGER      NOT NULL,
  brand       VARCHAR(100) NOT NULL,
  coordinates GEOGRAPHY( 'POINT'
) NOT NULL
);

CREATE TABLE useraccount (
  id             SERIAL PRIMARY KEY,
  foodtruck_name VARCHAR(50)  NOT NULL,
  username       VARCHAR(100) NOT NULL,
  password       TEXT         NOT NULL
);

-- truck_id   INTEGER NOT NULL REFERENCES foodtruck(id) ON DELETE CASCADE,

-- psql -U postgres -f sql/create.sql
-- psql -U postgres projet


-- select * from useraccount limit 2;
-- select * from foodtruck WHERE name LIKE '%mtl%';

-- ------------------------------------------------------------------------------
-- ref: http://www.postgresonline.com/journal/archives/165-Encrypting-data-with-pgcrypto.html

-- SELECT * FROM  postUserData;



-- INSERT INTO useraccount (foodtruck_name, username, password)
-- VALUES (
--   'Dim Sum Mtl',
--   'username',
--   crypt('password', gen_salt('md5'))
-- );



-- INSERT INTO useraccount (foodtruck_name, username, password)
--   SELECT
--     'BURGER Truck',
--     'username',
--     crypt('password', gen_salt('md5'))
--   WHERE
--     NOT EXISTS(
--         SELECT foodtruck_name
--         FROM useraccount
--         WHERE foodtruck_name = 'BURGER Truck'
--               AND username = 'username'
--               AND password = crypt('password', password)
--     );

-- INSERT INTO useraccount (foodtruck_name, username, password)
--   SELECT
--     names,
--     'username',
--     crypt('password', gen_salt('md5'))
--   FROM unnest(ARRAY ['max', 'ime', 'roy']) names
--   WHERE
--     NOT EXISTS(
--         SELECT foodtruck_name
--         FROM useraccount
--         WHERE foodtruck_name = ANY(ARRAY ['max', 'ime', 'roy'])
--               AND username = 'username'
--               AND password = crypt('password', password)
--     );

-- DELETE FROM useraccount
-- WHERE username = 'username'
--       AND password = crypt('password', password)
--       AND foodtruck_name = 'Dim Sum Mtl';

--   WHERE   foodtruck_name NOT IN
--           (
--             SELECT  foodtruck_name
--             FROM    useraccount;
--           );

-- DELETE FROM useraccount USING foodtruck -- check condition that relates to other columns in another table



-- SELECT
--   fd.truck_id,
--   fd.name
-- FROM foodtruck fd, useraccount usr
-- WHERE usr.username = 'username'
--       AND password = crypt('password', password)
--       AND fd.name = usr.foodtruck_name
-- GROUP BY fd.truck_id, fd.name;

-- SELECT
--   fd.id,
--   fd.truck_id,
--   fd.name,
--   fd.location,
--   ST_X(fd.coordinates :: GEOMETRY) AS longitude,
--   ST_Y(fd.coordinates :: GEOMETRY) AS latitude,
--   fd.start_time,
--   fd.end_time
-- FROM foodtruck fd, useraccount usr
-- WHERE usr.username = 'username'
--       AND password = crypt('password', password)
--       AND fd.name = usr.foodtruck_name;





-- SELECT
--   fd.id,
--   fd.truck_id,
--   fd.name,
--   fd.location,
--   ST_X(fd.coordinates :: GEOMETRY) AS longitude,
--   ST_Y(fd.coordinates :: GEOMETRY) AS latitude,
--   fd.start_time,
--   fd.end_time
-- FROM foodtruck fd, useraccount usr
-- WHERE usr.username = 'username'
--       AND password = crypt('password', password)
--       AND fd.name = usr.foodtruck_name
-- GROUP BY fd.id,
--   fd.truck_id,
--   fd.name,
--   fd.location,
--   longitude,
--   latitude,
--   fd.start_time,
--   fd.end_time;

-- SELECT fd.id ,
--   fd.truck_id,
--   fd.location,
--   ST_X(fd.coordinates::geometry) AS longitude,
--   ST_Y(fd.coordinates::geometry) AS latitude,
--   fd.start_time,
--   fd.end_time


-- INSERT INTO useraccount (truck_name, username, password)
-- VALUES (
--   (SELECT truck_id
--    FROM foodtruck
--    WHERE name = 'Dim Sum Mtl'
--    GROUP BY truck_id),
--   'username', crypt('password', gen_salt('md5'))
-- );
-- ------------------------------------------------------------------------------



--   truck_id INTEGER NOT NULL REFERENCES foodtruck ON DELETE CASCADE,

-- CREATE TABLE testusers(username varchar(100) PRIMARY KEY,
-- cryptpwd text, md5pwd text);

-- or encrypt with md5('test') or crypt('password', gen_salt('md5'))


-- INSERT INTO postUserData( truck_id, username, password )
-- VALUES (1, 'username'  ,crypt('password', gen_salt('md5')));


-- SELECT  name, id FROM foodtruck WHERE name='Dim Sum Mtl' AND truck_id='T00130010';
-- WHERE username = 'artoo' AND cryptpwd = crypt('test', cryptpwd);





CREATE OR REPLACE FUNCTION getBikeStationsInRadius(longi DOUBLE PRECISION, lati DOUBLE PRECISION, rad DOUBLE PRECISION)
  RETURNS
    TABLE(station_id   INTEGER,
          name         VARCHAR(100),
          bike_count   INTEGER,
          empty_spaces INTEGER,
          longitude    DOUBLE PRECISION,
          latitude     DOUBLE PRECISION
    ) AS $$
DECLARE
  distanceLimit DOUBLE PRECISION;
BEGIN
  distanceLimit := rad;
  RETURN QUERY SELECT
                 bs.station_id,
                 bs.name,
                 bs.bike_count,
                 bs.empty_spaces,
                 ST_X(bs.coordinates :: GEOMETRY) AS longitude,
                 ST_Y(bs.coordinates :: GEOMETRY) AS latitude
               FROM bikestation bs
               WHERE st_distance_sphere(
                         st_point(longi, lati),
                         st_point(ST_X(coordinates :: GEOMETRY), ST_Y(coordinates :: GEOMETRY))
                     ) <= distanceLimit;
END
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION getBikeRacksInRadius(longi DOUBLE PRECISION, lati DOUBLE PRECISION, rad DOUBLE PRECISION)
  RETURNS
    TABLE(rack_id   INTEGER,
          brand     VARCHAR(100),
          longitude DOUBLE PRECISION,
          latitude  DOUBLE PRECISION
    ) AS $$
DECLARE
  distanceLimit DOUBLE PRECISION;
BEGIN
  distanceLimit := rad;
  RETURN QUERY SELECT
                 br.rack_id,
                 br.brand,
                 ST_X(br.coordinates :: GEOMETRY) AS longitude,
                 ST_Y(br.coordinates :: GEOMETRY) AS latitude
               FROM bikerack br
               WHERE st_distance_sphere(
                         st_point(longi, lati),
                         st_point(ST_X(coordinates :: GEOMETRY), ST_Y(coordinates :: GEOMETRY))
                     ) <= distanceLimit;
END
$$ LANGUAGE plpgsql;
