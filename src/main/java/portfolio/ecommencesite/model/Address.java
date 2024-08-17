package portfolio.ecommencesite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min=5,message="Street name must be at least 5 char")
    private String street;

    @NotBlank
    @Size(min=1,message="Building name must be at least 1 char")
    private String buildingName;

    @NotBlank
    @Size(min=2,message="City name must be at least 2 char")
    private String city;

    @NotBlank
    @Size(min=2,message="State name must be at least 2 char")
    private String state;

    @NotBlank
    @Size(min=2,message="Country name must be at least 2 char")
    private String country;

    @NotBlank
    @Size(min=6,message="Postal Code must be at least 2 char")
    private String postalCode;

   @ManyToMany(mappedBy = "addresses")
   @ToString.Exclude
    private List<User> users = new ArrayList<>();

}
