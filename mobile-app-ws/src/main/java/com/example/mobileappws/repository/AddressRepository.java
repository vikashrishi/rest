package com.example.mobileappws.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.mobileappws.entity.AddressEntity;
import com.example.mobileappws.entity.UserEntity;

@Repository
public interface AddressRepository  extends CrudRepository<AddressEntity, Long>{
	
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

}
