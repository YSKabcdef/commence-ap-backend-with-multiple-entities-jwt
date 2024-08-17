package portfolio.ecommencesite.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import portfolio.ecommencesite.model.AppRole;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDto {

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private AppRole roleName;
}
