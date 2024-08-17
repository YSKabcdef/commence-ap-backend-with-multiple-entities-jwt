package portfolio.ecommencesite.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {



    @JsonIgnore
    private ResponseCookie responseCookie;

    private String username;

    private List<String> roles;

    private Long id;


}
