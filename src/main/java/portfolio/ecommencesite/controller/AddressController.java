package portfolio.ecommencesite.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommencesite.config.APIConstants;
import portfolio.ecommencesite.dto.AddressDto;
import portfolio.ecommencesite.response.AddressResponse;
import portfolio.ecommencesite.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController  {

    @Autowired
    private AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> createAddress(@RequestBody @Valid AddressDto address){
        return new ResponseEntity<>(addressService.addAddress(address), HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<AddressResponse> getAllAddress(
            @RequestParam(name="pageNumber" , defaultValue = APIConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = APIConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = APIConstants.SORT_ADDRESS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = APIConstants.SORT_ADDRESS_DIR) String sortOrder
    ){
        return ResponseEntity.ok(addressService.getAllAddress(pageNumber,pageSize,sortBy,sortOrder));
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> findAddress(@PathVariable Long addressId){
        return ResponseEntity.ok(addressService.findAddressById(addressId));
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDto>> findUserAddress(){
        return ResponseEntity.ok(addressService.findUserAddress());
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@RequestBody AddressDto addressDto,@PathVariable Long addressId){
        return ResponseEntity.ok(addressService.updateAddress(addressDto,addressId));
    }
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> deleteAddress(@PathVariable Long addressId){
        return ResponseEntity.ok(addressService.deleteAddress(addressId));
    }
    @DeleteMapping("/users/addresses/{addressId}")
    public ResponseEntity<AddressDto> deleteCurrentUserAddress(@PathVariable Long addressId){
        return ResponseEntity.ok(addressService.deleteCurrentUserAddress(addressId));
    }
}
