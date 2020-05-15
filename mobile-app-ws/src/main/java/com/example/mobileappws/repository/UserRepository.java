package com.example.mobileappws.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mobileappws.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

	UserEntity findUserByEmail(String email);
	UserEntity findUserByUserId(String userId);
	
	/*
	//native SQL query with positional parameter
	@Query(value="select * from  test_table t where t.EMAIL_VERIFICATION_STATUS = 'true",
			countQuery="select count(*) from test_table t where t.EMAIL_VERIFICATION_STATUS ='true'",
			nativeQuery = true)
	Page<UserEntity> findAllUserWithConfirmedEmailAddress(Pageable pageableRequest);
	
	//native SQL query with positional parameter
	@Query(value="select * from test_table t where t.first_name =?1",nativeQuery=true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	//native SQL query with named parameter
	@Query(value="select * from test_table t where t.last_name = :lastName",nativeQuery=true)
	List<UserEntity> findUserByLastName(@Param("lastName")String lastName);
	
	//it will select if firstName and lastName contain keyword
	@Query(value="select * from test_table t  where first_name LIKE %:keyword% or last_name LIKE %:keyword%"
			,nativeQuery=true)
	List<UserEntity> findUsersByKeyword(@Param("keyword")String keyword);
	
	//updating a user
	@Transactional
	@Modifying
	@Query(value="update test_table t set t.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus where t.user_id =:userId",nativeQuery=true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus")boolean emailVerificationStatus, 
			@Param("userId")String userId);
	
	//JPQL select query
	@Query("select user from userEntity user where user.userId =:userId")
	UserEntity findUserEntityByUserId(@Param("userId")String userId);
	
	//JPQL to select specific fields only
	@Query("select user.firstName, user.lastName from UserEntity user where user.userId =:userId")
	List<Object[]> getUserEntityFullNameById(@Param("userId")String userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE UserEntity u set u.emailVerificationStatus = :emailVerificationStatus where u.userId =:userId")
	void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus")boolean emailVerificationStatus, 
			@Param("userId")String userId);
			*/
	
}
