package com.company.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.services.ICategoryServices;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController()
@RequestMapping("/api/v1")
public class CategoryRestController {

	@Autowired
	private ICategoryServices categoryServices;

	/**
	 * Get Categories
	 * 
	 * @return
	 */
	@GetMapping("/categories")
	public ResponseEntity<CategoryResponseRest> searchCategories() {
		return categoryServices.search();
	}

	/**
	 * Get Category by Id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> searchCategoryById(@PathVariable Long id) {
		return categoryServices.searchById(id);
	}

	/**
	 * Save Categories
	 * 
	 * @param category
	 * @return
	 */
	@PostMapping("/categories")
	public ResponseEntity<CategoryResponseRest> save(@RequestBody Category category) {
		return categoryServices.save(category);
	}

	/**
	 * Update Categories
	 * 
	 * @param category
	 * @param id
	 * @return
	 */
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> update(@PathVariable Long id, @RequestBody Category category) {
		return categoryServices.update(category, id);
	}

	/**
	 * Delete Category
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> delet(@PathVariable Long id) {
		return categoryServices.deleteById(id);
	}

	/**
	 * Search Query
	 * 
	 * @param query
	 * @return
	 */
	@GetMapping("/categories/search")
	public ResponseEntity<CategoryResponseRest> searchItems(@RequestParam String query) {
		return categoryServices.searchData(query);
	}
}
