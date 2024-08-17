package portfolio.ecommencesite.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private Long addressId;


    private String street;


    private String buildingName;


    private String city;


    private String state;


    private String country;


    private String postalCode;


}
