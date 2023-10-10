package com.company.inventory.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductServices;
import com.company.inventory.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

	@Autowired
	private IProductServices productServices;

	@Autowired
	protected ObjectMapper objectMapper;

	/**
	 * Save Product
	 * 
	 * @param picture
	 * @param data
	 * @param categoryId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(@RequestParam("picture") MultipartFile picture,
			@RequestParam("data") String data, @RequestParam("categoryId") Long categoryId) throws IOException {

		Product product = this.objectMapper.readValue(data, Product.class);

		product.setPicture(Util.compressZLib(picture.getBytes()));
		return productServices.save(product, categoryId);
	}

	/**
	 * Get Product by Id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> searchCategoryById(@PathVariable Long id) {
		return productServices.searchById(id);
	}
}
