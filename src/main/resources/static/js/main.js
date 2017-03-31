//selectpicker : https://silviomoreto.github.io/bootstrap-select/examples/
$(document).ready(function () {
    mymap.panTo(new L.LatLng(45.5126, -73.5606));
    layerControl.addTo(mymap);

    $('#pickerFavoritesFoodTrucks').selectpicker({
        style: 'btn-info',
        size: 4,
        title: 'Food trucks préférés',
        selectAllText: 'Sélectionner tout',
        deselectAllText: 'Désélectionner tout'
    });

    $('#pickerAllFoodTrucks').selectpicker({
        style: 'btn-info',
        size: 4,
        title: 'Tous les food trucks ',
        selectAllText: 'Sélectionner tout',
        deselectAllText: 'Désélectionner tout'
    });

});

document.getElementById('btn_deleteFavoritesFoodTrucks').addEventListener("click", function (event) {
    postUserData("delete", updateFavoritesFoodTrucks("pickerFavoritesFoodTrucks"));
    gotoDisplayBoard();
});

document.getElementById('btn_insertFavoritesFoodTrucks').addEventListener("click", function (event) {
    postUserData("insert", updateFavoritesFoodTrucks("pickerAllFoodTrucks"));
    gotoDisplayBoard();
});

document.getElementById("btn_search_dates").addEventListener("click", function (event) {
    clearInformationsBoard();
    event.preventDefault();
    var startDate = startDateElement.value;
    var endDate = endDateElement.value;
    var checkDatesMessage = checkDates(startDate, endDate);

    if (checkDatesMessage.length == 0) {
        var favoriteFoodTrucks = updateFavoritesFoodTrucks("pickerFavoritesFoodTrucks");
        if (favoriteFoodTrucks.length > 0) {
            showFavoriteFoodTrucks = confirm("Obtenir la liste ne contenant que ses camions préférés ?");
        }
        queryFoodTrucksByDate(startDate, endDate, updateFoodTruckMarkers);
        errorsToShow = "";

    } else {
        errorManager(400, checkDatesMessage);
    }
    gotoDisplayBoard();
});


/** Queries */
function generateLoginPost() {
    postUserData("login", []);
    gotoDisplayBoard();
}

function createUserJsonPost(requestType, logintype, currentUsername, currentPassword, selectNames) {
    var postInformations =
    {
        request: requestType,
        login: logintype,
        username: currentUsername,
        password: currentPassword,
        names: selectNames
    };
    return postInformations;
}

function onFoodTruckMarkerClicked() {
    if (bikeStationLayerCheck.checked === true) {
        queryBikeStationInRadius(this.getLatLng().lng, this.getLatLng().lat, radiusSearch, updateBikeStationMarkers);
    } else {
        clearBikeStationsLayer();
        gotoDisplayBoard();
    }

    if (bikeRackLayerCheck.checked === true) {
        queryBikeRackInRadius(this.getLatLng().lng, this.getLatLng().lat, radiusSearch, updateBikeRackMarkers);

    } else {
        clearBikeRacksLayer();
        gotoDisplayBoard();
    }
}

function updateFavoritesFoodTrucks(pickerType) {
    var getSelect = [];
    $(document).ready(function () {
        $.each($("#" + pickerType + " option:selected"), function () {
            getSelect.push($(this).val());
        });
    });
    return getSelect;
}

function queryOnloadFavoritesFoodTrucks() {
    accountTypes.type = "default";
    accountTypes.usr = "username";
    accountTypes.pwd = "password";
    generateLoginPost();
}

function clearInformationsBoard() {
    errorsToShow = "";
    foodTruckQtyToShow = "";
    bikeStationQtyToShow = "";
    bikeRackQtyToShow = "";
    favoritesFoodTrucksQtyToShow = "";
    allFoodTrucksQtyToShow = "";
}

function postUserData(requestType, informations) {
    if (requestType === "login") {
        queryAllFoodTrucks(updateFavoriteFoodTruckChoices);
    }

    var xhttp = new XMLHttpRequest();
    var jsonString = JSON.stringify(
        createUserJsonPost(
            requestType,
            accountTypes.type,
            accountTypes.usr,
            accountTypes.pwd,
            informations
        )
    );

    xhttp.onreadystatechange = function () {
        if (xhttp.readyState == 4 && xhttp.status == 200) {

            jsonLogin = JSON.parse(xhttp.responseText);
            var rcv_SelectFavoritesFoodTrucks = {};

            $(document).ready(function () {
                $('#pickerFavoritesFoodTrucks').empty().selectpicker('refresh');
                $.each(jsonLogin, function (index, item) {
                    if (rcv_SelectFavoritesFoodTrucks[item["truckId"]] === undefined) {
                        rcv_SelectFavoritesFoodTrucks[item["truckId"]] = item["name"];

                        $('#pickerFavoritesFoodTrucks').append($('<option>', {
                            text: item["name"]
                        })).selectpicker('refresh');
                    }
                });
            });
            favoritesFoodTrucksQtyToShow = "<font size=" + fontSizeInformationBoard + ">Nombre de camions préférés : <b>" + Object.keys(rcv_SelectFavoritesFoodTrucks).length + "</b></font>";
            gotoDisplayBoard();
        }
    };
    xhttp.open("POST"
        , "http://localhost:8080/foodtrucks/favorites"
        , true);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(jsonString);
}

/** Clear the food truck map */
function clearFoodTruckLayer() {
    mymap.removeLayer(featureGroupFoodTrucks);
}

/** Clear the bike station map*/
function clearBikeStationsLayer() {
    bikeStationQtyToShow = "";
    mymap.removeLayer(featureGroupBikeStations);
}

/** Clear the bike rack map*/
function clearBikeRacksLayer() {
    bikeRackQtyToShow = "";
    mymap.removeLayer(featureGroupBikeRacks);
}

/** Function that queries all the food trucks and calls the function passed inthe parameter when the result returns*/
function queryAllFoodTrucks(processFoodTrucks) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function (evt) {
        evt.preventDefault();
        evt.stopPropagation();
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                processFoodTrucks(JSON.parse(xhttp.responseText));
                errorsToShow = "";
                gotoDisplayBoard();
            }
            else {
                errorManager(xhttp.status, []);
                xhttp.abort();
            }
        }
    }
    xhttp.open("GET", "http://localhost:8080/foodtrucks", true);
    xhttp.send();
}

/** Function that queries the food trucks by date and calls the function passed inthe parameter when the result returns*/
function queryFoodTrucksByDate(startDate, endDate, processFoodTrucks) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function (evt) {
        evt.preventDefault();
        evt.stopPropagation();
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                processFoodTrucks(JSON.parse(xhttp.responseText));
                errorsToShow = "";
                gotoDisplayBoard();
            }
            else {
                errorManager(xhttp.status, []);
                xhttp.abort();
            }
        }
    }
    xhttp.open("GET", "http://localhost:8080/foodtrucks?du=" + startDate + "&au=" + endDate, true);
    xhttp.send();
}

/** Function called when the food truck data has been received */
function updateFoodTruckMarkers(jsonFoodTrucks) {
    // empty the markers
    clearFoodTruckLayer();
    clearBikeStationsLayer();
    clearBikeRacksLayer();

    featureGroupFoodTrucks = new L.FeatureGroup();
    var favoriteFoodTrucks = null;

    if (showFavoriteFoodTrucks) favoriteFoodTrucks = updateFavoritesFoodTrucks("pickerFavoritesFoodTrucks");

    var truckMarkers = {};
    var foodTruckCount = 0;
    jsonFoodTrucks.forEach(function (foodTruck) {
        if (!showFavoriteFoodTrucks || (showFavoriteFoodTrucks && favoriteFoodTrucks.indexOf(foodTruck.name) >= 0)) {
            var markerKey = foodTruck.coordinates.longitude + " " + foodTruck.coordinates.latitude;
            if (truckMarkers[markerKey] === undefined) {
                var foodTruckMarkerInfo = createFoodTruckMarkerInfo(foodTruck);
                truckMarkers[markerKey] = createEventMarker(foodTruckMarkerInfo, onFoodTruckMarkerClicked);
                featureGroupFoodTrucks.addLayer(truckMarkers[markerKey]);
            }
            else {
                var popup = truckMarkers[markerKey].getPopup();
                popup.setContent(popup.getContent() + getFoodTruckDescription(foodTruck));
                popup.update();
            }
            foodTruckCount += 1;
        }
    });

    mymap.addLayer(featureGroupFoodTrucks);

    if (foodTruckCount > 0) {
        mymap.fitBounds(featureGroupFoodTrucks, {padding: [25, 25]});
    } else {
        mymap.setView([45.5126, -73.5606], 13/*zoom*/);
    }

    foodTruckQtyToShow = "<font size=" + fontSizeInformationBoard + ">Nombre d'horaires de camions : <b>" + foodTruckCount + "</b></font>";
}

/** Create a food Truck marker*/
function createFoodTruckMarkerInfo(foodTruck) {
    var truckMarkerInfo = {
        latitude: foodTruck.coordinates.latitude,
        longitude: foodTruck.coordinates.longitude,
        desc: getFoodTruckDescription(foodTruck),
        type: allMarkerTypes.foodTruck
    };
    return truckMarkerInfo;
}


/** Create an event marker from a marker. onClick is a callback for when the marker is clicked. If it's null then the marker is not clickable.*/
function createEventMarker(marker, onClick) {
    var eventMarker = null;
    var currentCoordinates = [parseFloat(marker.latitude), parseFloat(marker.longitude)];

    switch (marker.type) {
        case allMarkerTypes.foodTruck:
            eventMarker = L.marker(currentCoordinates, {icon: foodTruckIcon});
            break;
        case allMarkerTypes.bikeStation:
            eventMarker = L.marker(currentCoordinates, {icon: bikeStationIcon});
            break;
        case allMarkerTypes.bikeRack:
            eventMarker = L.marker(currentCoordinates, {icon: bikeRackIcon});
            break;
        case allMarkerTypes.bikeStationRack:
            eventMarker = L.marker(currentCoordinates, {icon: bikeStationAndRack});
            break;
    }

    if (onClick !== null) {
        eventMarker.on('click', onClick);
    }

    eventMarker.bindPopup(marker.desc, {
        minWidth: 200,
        maxHeight: 175,
        showOnMouseOver: true
    }).openPopup();

    return eventMarker;
}

/** This function updates the favorite food truck choices in the drop down menu.*/
function updateFavoriteFoodTruckChoices(foodTrucks) {
    var favoriteFoodTrucksChoices = {};
    var foodTruckChoiceCount = 0;
    foodTrucks.forEach(function (foodTruck) {
        if (favoriteFoodTrucksChoices[foodTruck.truckId] === undefined) {
            favoriteFoodTrucksChoices[foodTruck.truckId] = foodTruck.name;
            $('#pickerAllFoodTrucks').append($('<option>', {
                text: foodTruck.name
            })).selectpicker('refresh');
            foodTruckChoiceCount += 1;
        }
    });
    allFoodTrucksQtyToShow = "<font size=" + fontSizeInformationBoard + ">Nombre de food truck possible : <b>" + foodTruckChoiceCount + "</b></font>";

}

/** This function gets all the bike stations in the specified radius and calls processBikeStations with the returned result*/
function queryBikeStationInRadius(longitudeCenter, latitudeCenter, radiusLimit, processBikeStations) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function (evt) {
        evt.preventDefault();
        evt.stopPropagation();
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                processBikeStations(longitudeCenter, latitudeCenter, radiusLimit, JSON.parse(xhttp.responseText));
                errorsToShow = "";
                gotoDisplayBoard();
            }
            else {
                errorManager(xhttp.status, []);
                xhttp.abort();
            }
        }
    };
    xhttp.open("GET",
        "http://localhost:8080/bikestations?long=" + longitudeCenter
        + "&lat=" + latitudeCenter
        + "&rad=" + radiusLimit,
        true);
    xhttp.send();
}

function updateBikeStationMarkers(longitudeCenter, latitudeCenter, radiusLimit, bikeStations) {
    clearBikeStationsLayer();
    featureGroupBikeStations = new L.FeatureGroup();

    if (bikeStationLayerCheck.checked === true) {
        var bikeStationCount = updateFeatureGroupBikeStations(bikeStations);
        mymap.addLayer(featureGroupBikeStations);

        if (bikeStationCount > 0) {
            var selectBound = createBound(
                featureGroupBikeStations,
                latitudeCenter,
                longitudeCenter
            );
            mymap.fitBounds(selectBound, {padding: [100, 100]});
        }
    }
    bikeStationQtyToShow = "<font size=" + fontSizeInformationBoard + ">Nombre de station de bicycle : <b>" + bikeStationCount + "</b></font>"
}

function updateFeatureGroupBikeStations(bikeStations) {
    var bikeStationCount = 0;
    var bikeStationMarkers = {};
    bikeStations.forEach(function (bikeStation) {
        var markerKey = bikeStation.coordinates.longitude + " " + bikeStation.coordinates.latitude;
        if (bikeStationMarkers[markerKey] === undefined) {
            var bikeStationMarkerInfo = createBikeStationMarkerInfo(bikeStation);
            bikeStationMarkers[markerKey] = createEventMarker(bikeStationMarkerInfo, null);
            featureGroupBikeStations.addLayer(bikeStationMarkers[markerKey]);
        } else {
            var popup = bikeStationMarkers[markerKey].getPopup();
            popup.setContent(popup.getContent() + getBikeStationDescription(bikeStation));
            popup.update();
        }
        bikeStationCount += 1;
    });
    return bikeStationCount;
}

function getFoodTruckDescription(foodTruck) {
    return "<b> Food truck </b><br/>" +
        "<b>Nom : " + foodTruck.name + "</b><br/>" +
        "Emplacement : " + foodTruck.location + "<br/>" +
        // "Longitude : " + foodTruck.coordinates.longitude + "<br/>" +
        // "Latitude : " + foodTruck.coordinates.latitude + "<br/>" +
        "Date : " + getDateFromTimeStamp(foodTruck.startTime) + "<br/>" +
        "Arrivée : " + getTimeFromTimeStamp(foodTruck.startTime) + "<br/>" +
        "Départ : " + getTimeFromTimeStamp(foodTruck.endTime) + "<br/><br/>"
}

function getBikeStationDescription(bikeStation) {
    return "<b> Bike Station </b><br/>" +
        "<b>Nom : " + bikeStation.name + "</b><br/>" +
        // "Id : " + bikeStation.id + "<br/>" +
        // "Longitude : " + bikeStation.coordinates.longitude + "<br/>" +
        // "Latitude : " + bikeStation.coordinates.latitude + "<br/>" +
        "Nb bicyclettes : " + bikeStation.bikeCount + "<br/>" +
        "Espaces vides : " + bikeStation.emptySpacesCount + "<br/>";
}


function getBikeRackDescription(bikeRack) {
    return "<b> Bike Rack </b><br/>" +
        "<b>Marque : " + bikeRack.brand + "</b><br/>";
    // "Id : " + bikeRack.id + "<br/>" +
    // "Longitude : " + bikeRack.coordinates.longitude + "<br/>" +
    // "Latitude : " + bikeRack.coordinates.latitude + "<br/>";
}

function createBikeStationMarkerInfo(bikeStation) {
    var bikeStationMarkerInfo =
    {
        latitude: bikeStation.coordinates.latitude,
        longitude: bikeStation.coordinates.longitude,
        desc: getBikeStationDescription(bikeStation),
        type: allMarkerTypes.bikeStation
    };
    return bikeStationMarkerInfo;
}

/** This function gets all the bike racks in the specified radius and calls processBikeRacks with the returned result*/
function queryBikeRackInRadius(longitudeCenter, latitudeCenter, radiusLimit, processBikeRacks) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function (evt) {
        evt.preventDefault();
        evt.stopPropagation();
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                processBikeRacks(longitudeCenter, latitudeCenter, radiusLimit, JSON.parse(xhttp.responseText));
                errorsToShow = "";
                gotoDisplayBoard();

            }
            else {
                errorManager(xhttp.status, []);
                xhttp.abort();
            }
        }
    };
    xhttp.open("GET",
        "http://localhost:8080/bikeracks?long=" + longitudeCenter
        + "&lat=" + latitudeCenter
        + "&rad=" + radiusLimit,
        true);
    xhttp.send();
}

function updateBikeRackMarkers(longitudeCenter, latitudeCenter, radiusLimit, bikeRacks) {
    clearBikeRacksLayer();
    featureGroupBikeRacks = new L.FeatureGroup();
    if (bikeRackLayerCheck.checked === true) {
        var bikeRackCount = updateFeatureGroupBikeRacks(bikeRacks);
        mymap.addLayer(featureGroupBikeRacks);
        if (bikeRackCount > 0) {
            var selectBound = createBound(
                featureGroupBikeRacks,
                latitudeCenter,
                longitudeCenter
            );

            mymap.fitBounds(selectBound, {padding: [100, 100]});
        }
    }
    bikeRackQtyToShow = "<font size=" + fontSizeInformationBoard + ">Nombre de rack de bicycle : <b>" + bikeRackCount + "</b></font>"
}

function updateFeatureGroupBikeRacks(bikeRacks) {
    var bikeRackCount = 0;
    var bikeRackMarkers = {};
    bikeRacks.forEach(function (bikeRack) {
        var markerKey = bikeRack.coordinates.longitude + " " + bikeRack.coordinates.latitude;
        if (bikeRackMarkers[markerKey] === undefined) {
            var bikeRackMarkerInfo = createBikeRackMarkerInfo(bikeRack);
            bikeRackMarkers[markerKey] = createEventMarker(bikeRackMarkerInfo, null);
            featureGroupBikeRacks.addLayer(bikeRackMarkers[markerKey]);
        } else {
            var popup = bikeRackMarkers[markerKey].getPopup();
            popup.setContent(popup.getContent() + getBikeRackDescription(bikeRack));
            popup.update();
        }
        bikeRackCount += 1;
    });
    return bikeRackCount;
}


function createBikeRackMarkerInfo(bikeRack) {
    var bikeRackMarkerInfo =
    {
        latitude: bikeRack.coordinates.latitude,
        longitude: bikeRack.coordinates.longitude,
        desc: getBikeRackDescription(bikeRack),
        type: allMarkerTypes.bikeRack
    };
    return bikeRackMarkerInfo;
}

function createBound(currentfeatureGroup, latitudeCenter, longitudeCenter) {
    var allBounds = currentfeatureGroup.getBounds();
    var group = new L.FeatureGroup();
    var eventMarker = L.marker(
        [allBounds.getSouthWest().lat, allBounds.getSouthWest().lng]
    );
    group.addLayer(eventMarker);

    eventMarker = L.marker(
        [allBounds.getNorthEast().lat, allBounds.getNorthEast().lng]
    );
    group.addLayer(eventMarker);

    eventMarker = L.marker(
        [latitudeCenter, longitudeCenter]
    );
    group.addLayer(eventMarker);
    return group;
}

function getTimeFromTimeStamp(time) {
    return time.substring(11, 16);
}

function getDateFromTimeStamp(date) {
    return date.substring(0, 10);
}

function errorManager(status, messages) {
    errorsToShow = "";
    if (messages.length > 1) {
        errorsToShow = "<p><font size=" + fontSizeInformationBoard + "><b>ERREURS</b></font></p>";
    }
    else {
        errorsToShow = "<p><font size=" + fontSizeInformationBoard + "><b>ERREUR</b></font></p>";
    }

    for (var i = 0; i < messages.length; i++) {
        errorsToShow += generateHtmlMessage(status, messages[i]);
    }
}

function generateHtmlMessage(status, message) {
    var htmlMessage = "<p><font size=" + fontSizeInformationBoard + ">" + message + "<br/>" + status + ": ";
    switch (status) {
        case 0:
            htmlMessage += "Unknown error";
            break;
        case 400:
            htmlMessage += "Bad Request";
            break;
        case 404:
            htmlMessage += "Page not found";
            break;
    }
    return htmlMessage += "</font></p>";
}

function gotoDisplayBoard() {
    pInfo1.innerHTML = "";
    pInfo1.innerHTML += errorsToShow;
    pInfo1.innerHTML += "<p><font size=" + fontSizeInformationBoard + "> <b>INFORMATIONS</b></font></p>";
    pInfo1.innerHTML += "<p>" + foodTruckQtyToShow + "</p>";
    pInfo1.innerHTML += "<p>" + bikeStationQtyToShow + "</p>";
    pInfo1.innerHTML += "<p>" + bikeRackQtyToShow + "</p>";
    pInfo1.innerHTML += "<p>" + favoritesFoodTrucksQtyToShow + "</p>";
    pInfo1.innerHTML += "<p>" + allFoodTrucksQtyToShow + "</p>";
}