package models.fakeapiuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Geolocation {

    @JsonProperty("lat")
    private String lat;

    @JsonProperty("long")
    private String jsonMemberLong;

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setJsonMemberLong(String jsonMemberLong) {
        this.jsonMemberLong = jsonMemberLong;
    }

    public String getJsonMemberLong() {
        return jsonMemberLong;
    }

    @Override
    public String toString() {
        return
                "Geolocation{" +
                        "lat = '" + lat + '\'' +
                        ",long = '" + jsonMemberLong + '\'' +
                        "}";
    }
}