package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.UserEmp;

public interface UserEmpRepository extends JpaRepository<UserEmp, Integer>{
	// pagination
	
	@Query("from UserEmp as c where c.user.id =:userId")
	public List<UserEmp> findUserEmpByUser(@Param("userId")int userId);
}
