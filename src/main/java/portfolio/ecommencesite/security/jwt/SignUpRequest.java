package portfolio.ecommencesite.security.jwt;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String username;


    private String email;

    @NotBlank
    @Size(min = 2)
    private String password;

    private Set<String> roles = new HashSet<>();
}
