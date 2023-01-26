package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


import java.util.ArrayList;
@Getter

public class Location {
    String postCode;
    String country;
    String countryAbbreviation;
    ArrayList<Place> places;

    @JsonProperty("post code")
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("country abbreviation")
    public void setCountryAbbreviation(String countryAbbreviation) {
        this.countryAbbreviation = countryAbbreviation;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }
}
