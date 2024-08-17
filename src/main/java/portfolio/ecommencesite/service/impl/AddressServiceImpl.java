package portfolio.ecommencesite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.ecommencesite.dto.AddressDto;
import portfolio.ecommencesite.exception.ResourceNotFoundException;
import portfolio.ecommencesite.model.Address;
import portfolio.ecommencesite.model.User;
import portfolio.ecommencesite.repo.AddressRepo;
import portfolio.ecommencesite.repo.UserRepo;
import portfolio.ecommencesite.response.AddressResponse;
import portfolio.ecommencesite.service.AddressService;
import portfolio.ecommencesite.util.AuthUtils;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepo addressRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    UserRepo userRepo;

    @Override
    public AddressDto addAddress(AddressDto address) {
        Address preSavedAddress = modelMapper.map(address,Address.class);
        User user = authUtils.loggedInUser();
        List<User> users = preSavedAddress.getUsers();
        users.add(user);
        List<Address> addresses = user.getAddresses();
        addresses.add(preSavedAddress);
        user.setAddresses(addresses);
        preSavedAddress.setUsers(users);
        return modelMapper.map(addressRepo.save(preSavedAddress),AddressDto.class);
    }

    @Override
    public AddressResponse getAllAddress(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Address> pages = addressRepo.findAll(pageDetails);
        List<AddressDto> addresses = pages.getContent().stream().map(
                item -> modelMapper.map(item,AddressDto.class)
        ).toList();
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setContent(addresses);
        addressResponse.setLastPage(pages.isLast());
        addressResponse.setPageNumber(pages.getNumber());
        addressResponse.setPageSize(pages.getSize());
        addressResponse.setTotalElement(pages.getTotalElements());
        addressResponse.setTotalPage(pages.getTotalPages());
        return addressResponse;
    }

    @Override
    public AddressDto findAddressById(Long addressId) {
        return modelMapper.map(
                addressRepo.findById(addressId).orElseThrow(
                        () -> new ResourceNotFoundException("Address","address id",addressId)
                ),AddressDto.class
        );
    }

    @Override
    public List<AddressDto> findUserAddress() {
        return authUtils.loggedInUser().getAddresses().stream().map(
                item -> modelMapper.map(item,AddressDto.class)
        ).toList();
    }

    @Override
    public AddressDto updateAddress(AddressDto addressDto, Long addressId) {
        Address address = addressRepo.findById(addressId).orElseThrow(
                ()->new ResourceNotFoundException("Address","addressId", addressId)
        );
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setState(addressDto.getState());
        address.setBuildingName(addressDto.getBuildingName());
        address.setPostalCode(addressDto.getPostalCode());
        address.setStreet(addressDto.getStreet());

        return modelMapper.map(addressRepo.save(address),AddressDto.class);
    }

    @Override
    public AddressDto deleteAddress(Long addressId) {
        Address address = addressRepo.findById(addressId).orElseThrow(
                ()->new ResourceNotFoundException("Address", "addressId",addressId)
        );
        address.getUsers().forEach(user -> {
            List<Address> addresses = user.getAddresses();
            addresses.remove(address);
            user.setAddresses(addresses);
            userRepo.save(user);
        });
        addressRepo.delete(address);

        return modelMapper.map(address,AddressDto.class);
    }

    @Override
    public AddressDto deleteCurrentUserAddress(Long addressId) {
        Address address = addressRepo.findById(addressId).orElseThrow(
                ()->new ResourceNotFoundException("Address", "addressId",addressId)
        );
        User user = authUtils.loggedInUser();
        List<Address> addresses = user.getAddresses();
        if(!addresses.contains(address)){
         throw new ResourceNotFoundException("Address", "addressId",addressId);

        }
        addresses.remove(address);
        List<User> users = address.getUsers();
        users.remove(user);
        if (users.isEmpty()){
            deleteAddress(addressId);
        }
        user.setAddresses(addresses);
        userRepo.save(user);
        return  modelMapper.map(address,AddressDto.class);
    }
}
