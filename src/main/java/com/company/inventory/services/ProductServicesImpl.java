package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

@Service
public class ProductServicesImpl implements IProductServices {

	private ICategoryDao categoryDao;
	private IProductDao productDao;

	public ProductServicesImpl(ICategoryDao categoryDao, IProductDao productDao) {
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();

		try {
			// search category to set in the product object
			Optional<Category> category = categoryDao.findById(categoryId);
			if (category.isPresent()) {
				product.setCategory(category.get());
				response.setMetadata("Respuesta Ok", "200", "Respuesta exitosa. Categoria encontradad");
			} else {
				response.setMetadata("Respuesta Ok", "202", "No existe la categoria");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

			Product productSave = productDao.save(product);

			if (productSave != null && productSave.getId() != null) {
				list.add(productSave);
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta Ok", "200", "El producto se ha guardado exitosamente");
			} else {
				response.setMetadata("Respuesta Fail", "-1", "Error al guardar el producto.");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al guardar el producto ");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long productId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();

		try {
			// search product by id
			Optional<Product> product = productDao.findById(productId);
			if (product.isPresent()) {
				byte[] imgDesc = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imgDesc);
				list.add(product.get());
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta Ok", "200", "Respuesta exitosa. Producto encontrado");
			} else {
				response.setMetadata("Respuesta Ok", "202", "No existe la categoria");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al consultar el producto por Id");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();

		try {
			// search product by id
			List<Product> products = productDao.findByNameContainingIgnoreCase(name);
			if (products.size() > 0) {
				products.stream().forEach( (prod) -> {
					byte[] imgDesc = Util.decompressZLib(prod.getPicture());
					prod.setPicture(imgDesc);
					list.add(prod);
				});
				
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta Ok", "200", "Respuesta exitosa. Producto encontrado");
			} else {
				response.setMetadata("Respuesta Ok", "202", "No existe el producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al consultar el producto por nombre");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

}
