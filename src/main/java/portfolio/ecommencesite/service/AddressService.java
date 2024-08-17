package portfolio.ecommencesite.service;

import portfolio.ecommencesite.dto.AddressDto;
import portfolio.ecommencesite.response.AddressResponse;

import java.util.List;

public interface AddressService{
    AddressDto addAddress(AddressDto address);

    AddressResponse getAllAddress(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    AddressDto findAddressById(Long addressId);

    List<AddressDto> findUserAddress();

    AddressDto updateAddress(AddressDto addressDto, Long addressId);

    AddressDto deleteAddress(Long addressId);

    AddressDto deleteCurrentUserAddress(Long addressId);
}
