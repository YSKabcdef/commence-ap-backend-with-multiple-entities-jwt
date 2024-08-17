package portfolio.ecommencesite.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private Long userId;

    private String userName;

    private String email;

    private Set<RoleDto> roles;
    private Set<ProductDto> products;

    private List<AddressDto> addresses = new ArrayList<>();
}
