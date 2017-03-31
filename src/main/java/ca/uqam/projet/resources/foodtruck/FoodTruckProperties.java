package ca.uqam.projet.resources.foodtruck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringEscapeUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodTruckProperties {

    private String truckId;
    private String camion;
    private String lieu;
    private String date;
    private String heureDebut;
    private String heureFin;

    public FoodTruckProperties() {
    }

    public String getTruckId() {
        return truckId;
    }

    @JsonProperty("Truckid")
    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getCamion() {
        return camion;
    }

    @JsonProperty("Camion")
    public void setCamion(String camion) {
        this.camion = StringEscapeUtils.unescapeHtml4(camion);
    }

    public String getLieu() {
        return lieu;
    }

    @JsonProperty("Lieu")
    public void setLieu(String lieu) {
        this.lieu = StringEscapeUtils.unescapeHtml4(lieu);
    }

    public String getDate() {
        return date;
    }

    @JsonProperty("Date")
    public void setDate(String date) {
        this.date = date;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    @JsonProperty("Heure_debut")
    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    @JsonProperty("Heure_fin")
    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    @Override
    public String toString() {
        return String.format("FoodTruckProperties{TruckId='%s', Camion='%s', Lieu='%s', Date='%s', HeureDebut='%s', HeureFin='%s'}",
                truckId, camion, lieu, date, heureDebut, heureFin);
    }
}
