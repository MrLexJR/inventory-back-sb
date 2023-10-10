package com.company.inventory.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

public interface IProductServices {

	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId);
	
	public ResponseEntity<ProductResponseRest> searchById(Long productId);
	
	public ResponseEntity<ProductResponseRest> searchByName(String name);
	
	public ResponseEntity<ProductResponseRest> deleteById(Long productId);
	
	public ResponseEntity<ProductResponseRest> searchProducts();
	
	public ResponseEntity<ProductResponseRest> update(Product product, Long productId, Long categoryId);
}
