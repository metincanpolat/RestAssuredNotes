package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter

public class Place {
    String placeName;
    String longitude;
    String state;
    String stateAbbreviation;
    String latitude;

    @JsonProperty("place name")
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("state abbreviation")
    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
