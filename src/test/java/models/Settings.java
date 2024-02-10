package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/* в настройках
        url=https://google.com
        is_production=true   это поле названо не по правилам java
        threads=3 */
public class Settings {
    private String url;
    /* аннотация, как поле будет называться в json  */
    @JsonProperty("is_production")
    private Boolean isProduction;
    private Integer threads;
}
