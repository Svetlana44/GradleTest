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
public class Address {

    @JsonProperty("zipcode")
    private String zipcode;

    @JsonProperty("number")
    private int number;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("geolocation")
    private Geolocation geolocation;

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    @Override
    public String toString() {
        return
                "Address{" +
                        "zipcode = '" + zipcode + '\'' +
                        ",number = '" + number + '\'' +
                        ",city = '" + city + '\'' +
                        ",street = '" + street + '\'' +
                        ",geolocation = '" + geolocation + '\'' +
                        "}";
    }
}