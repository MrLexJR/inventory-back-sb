package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.company.inventory.model.Category;

public interface ICategoryDao extends CrudRepository<Category, Long> {
	
	@Query("SELECT c FROM Category c WHERE c.name LIKE %:query% OR c.description LIKE %:query%")
    List<Category> searchByQuery(@Param("query") String query);

}
