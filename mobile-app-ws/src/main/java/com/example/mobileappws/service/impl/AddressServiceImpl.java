package com.example.mobileappws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mobileappws.entity.AddressEntity;
import com.example.mobileappws.entity.UserEntity;
import com.example.mobileappws.repository.AddressRepository;
import com.example.mobileappws.repository.UserRepository;
import com.example.mobileappws.service.AddressService;
import com.example.mobileappws.shared.dto.AddressDTO;

@Service
public class AddressServiceImpl implements AddressService  {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDTO> getAddresses(String userId) {

		List<AddressDTO> returnValue = new ArrayList<AddressDTO>();
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = userRepository.findUserByUserId(userId);
		if(userEntity == null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for(AddressEntity addressEntity: addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDTO.class));
		}
		
		return returnValue;
	}

}
