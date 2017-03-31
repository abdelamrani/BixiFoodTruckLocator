/* global L*/

////////////////////////////////// MAP
// reference: http://maptimeboston.github.io/leaflet-intro/
// reference: http://leafletjs.com/examples/quick-start.html
// reference for themes : https://leaflet-extras.github.io/leaflet-providers/preview/

var OpenStreetMap_HOT = L.tileLayer('http://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, Tiles courtesy of <a href="http://hot.openstreetmap.org/" target="_blank">Humanitarian OpenStreetMap Team</a>'
});

var OpenStreetMap_BlackAndWhite = L.tileLayer('http://{s}.tiles.wmflabs.org/bw-mapnik/{z}/{x}/{y}.png', {
    maxZoom: 18,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
});

var Thunderforest_TransportDark = L.tileLayer('http://{s}.tile.thunderforest.com/transport-dark/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://www.thunderforest.com/">Thunderforest</a>, &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    maxZoom: 19
});

var OpenTopoMap = L.tileLayer('http://{s}.tile.opentopomap.org/{z}/{x}/{y}.png', {
    maxZoom: 17,
    attribution: 'Map data: &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://viewfinderpanoramas.org">SRTM</a> | Map style: &copy; <a href="https://opentopomap.org">OpenTopoMap</a> (<a href="https://creativecommons.org/licenses/by-sa/3.0/">CC-BY-SA</a>)'
});

var mymap = L.map('map', {
    layers: [OpenStreetMap_HOT]
}).setView([45.5126, -73.5606], 13/*zoom*/);

var baseLayers = {
    "Hot": OpenStreetMap_HOT,
    "Black and white": OpenStreetMap_BlackAndWhite,
    "Forest": Thunderforest_TransportDark,
    "Topo": OpenTopoMap
};

var layerControl = L.control.layers(baseLayers, null);


////////////////////////////////// AJAX
var radiusSearch = 200;
var fontSizeInformationBoard = 2;

var iconSizes = [[38, 95], [50, 64], [22, 94], [4, 62], [-3, -76]];
var foodTruckIcon = L.icon({
    iconUrl: 'img/food_truck_blue.png',
    shadowUrl: 'img/custom_shadow.png',
    iconSize: iconSizes[0],
    shadowSize: iconSizes[1],
    iconAnchor: iconSizes[2],
    shadowAnchor: iconSizes[3],
    popupAnchor: iconSizes[4]
});

var bikeStationIcon = L.icon({
    iconUrl: 'img/bike_station_red.png',
    shadowUrl: 'img/custom_shadow.png',
    iconSize: iconSizes[0],
    shadowSize: iconSizes[1],
    iconAnchor: iconSizes[2],
    shadowAnchor: iconSizes[3],
    popupAnchor: iconSizes[4]
});

var bikeRackIcon = L.icon({
    iconUrl: 'img/bike_rack_green.png',
    shadowUrl: 'img/custom_shadow.png',
    iconSize: iconSizes[0],
    shadowSize: iconSizes[1],
    iconAnchor: iconSizes[2],
    shadowAnchor: iconSizes[3],
    popupAnchor: iconSizes[4]
});

var bikeStationAndRack = L.icon({
    iconUrl: 'img/bike_station_red_rack_green.png',
    shadowUrl: 'img/two_shadows.png',
    iconSize: iconSizes[0],
    shadowSize: iconSizes[1],
    iconAnchor: iconSizes[2],
    shadowAnchor: iconSizes[3],
    popupAnchor: iconSizes[4]
});

var featureGroupFoodTrucks = new L.FeatureGroup();
var featureGroupBikeStations = new L.FeatureGroup();
var featureGroupBikeRacks = new L.FeatureGroup();

var errorsToShow = "";
var foodTruckQtyToShow = "";
var bikeStationQtyToShow = "";
var bikeRackQtyToShow = "";
var favoritesFoodTrucksQtyToShow = "";
var allFoodTrucksQtyToShow = "";

var pInfo1 = document.getElementById("p_info_1");

var bikeStationLayerCheck = document.getElementById("bike_station_layer_check");
var bikeRackLayerCheck = document.getElementById("bike_rack_layer_check");

var allMarkerTypes = {foodTruck: "fd", bikeStation: "bs", bikeRack: "br", bikeStationRack: "bsr"};

var showFavoriteFoodTrucks = false;
var jsonLogin = null;
var accountTypes = {type: "default", usr: "username", pwd: "password"};

/** Date elements */
var startDateElement = document.getElementById("start_date");
var endDateElement = document.getElementById("end_date");