package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServices implements ICategoryServices {

	@Autowired
	private ICategoryDao categoryDao;

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> search() {

		CategoryResponseRest response = new CategoryResponseRest();

		try {
			List<Category> category = (List<Category>) categoryDao.findAll();
			response.getCategoryResponse().setCategory(category);
			response.setMetadata("Respuesta Ok", "200", "Respuesta exitosa");
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al Consultar ");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();

		try {
			Optional<Category> category = categoryDao.findById(id);
			if (category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta Ok", "200", "Respuesta exitosa. Categoria encontradad");
			} else {
				response.setMetadata("Respuesta Fail", "-1", "Error al Consultar Categorias por Id");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al Consultar por ID");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();

		try {
			Category categorySaved = categoryDao.save(category);
			if (categorySaved != null && categorySaved.getId() != null) {
				list.add(categorySaved);
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta Ok", "200", "La categoria se ha guardado exitosamente");
			} else {
				response.setMetadata("Respuesta Fail", "-1", "Error al guardar la categoria.");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al guardar la categoria ");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();

		try {
			Optional<Category> categorySearch = categoryDao.findById(id);

			if (categorySearch.isPresent()) {

				categorySearch.get().setName(category.getName());
				categorySearch.get().setDescription(category.getDescription());

				Category categoryUpdate = categoryDao.save(categorySearch.get());

				if (categoryUpdate != null) {
					list.add(categoryUpdate);
					response.getCategoryResponse().setCategory(list);
					response.setMetadata("Respuesta Ok", "200", "La categoria se ha actualizado exitosamente");
				} else {
					response.setMetadata("Respuesta Fail", "-1", "Error al actualizar la categoria");
					return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
				}
			} else {
				response.setMetadata("Respuesta Fail", "-1", "Error al actualizar la categoria.");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al Consultar ");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

}
