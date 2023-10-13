package com.company.inventory.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductServices;
import com.company.inventory.util.ProductExcelExporter;
import com.company.inventory.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

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
	public ResponseEntity<ProductResponseRest> searchProductById(@PathVariable Long id) {
		return productServices.searchById(id);
	}

	/**
	 * Get Product by Names
	 * 
	 * @param names
	 * @return
	 */
	@GetMapping("/products/filter/{name}")
	public ResponseEntity<ProductResponseRest> searchProductById(@PathVariable String name) {
		return productServices.searchByName(name);
	}

	/**
	 * Delete Product
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> delet(@PathVariable Long id) {
		return productServices.deleteById(id);
	}

	/**
	 * Get Products
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/products")
	public ResponseEntity<ProductResponseRest> searchsProduct() {
		return productServices.searchProducts();
	}

	/**
	 * Update Product
	 * 
	 * @param picture
	 * @param data
	 * @param categoryId
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> update(@RequestParam("picture") MultipartFile picture,
			@RequestParam("data") String data, @RequestParam("categoryId") Long categoryId, @PathVariable Long id)
			throws IOException {

		Product product = this.objectMapper.readValue(data, Product.class);
		product.setPicture(Util.compressZLib(picture.getBytes()));

		return productServices.update(product, id, categoryId);
	}
	
	/**
	 * Export Excel
	 *  
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/products/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=result_product_"+new Date().toString()+".xlsx";
		response.setHeader(headerKey, headerValue);

		ResponseEntity<ProductResponseRest> productResponse = productServices.searchProducts();
		List<Product> productList = productResponse.getBody().getProduct().getProducts();

		ProductExcelExporter excelExporter = new ProductExcelExporter(productList);

		excelExporter.export(response);
	}
}
