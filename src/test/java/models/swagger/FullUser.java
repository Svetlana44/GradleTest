package models.swagger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/*  для игнорирования незаполненных полей, вставит в json всё, что не NULL */
@JsonInclude(JsonInclude.Include.NON_NULL)
/* для игнорирования id, т.к. id может прийти в ответе, но не создаётся тут */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullUser {

    @JsonProperty("pass")
    private String pass;

    @JsonProperty("games")
    private List<GamesItem> games;

    @JsonProperty("login")
    private String login;

}