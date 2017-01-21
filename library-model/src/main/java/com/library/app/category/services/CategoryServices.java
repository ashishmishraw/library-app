package com.library.app.category.services;

import java.util.List;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.common.exception.FieldNotValidException;

public interface CategoryServices {

	Category add(Category category) throws FieldNotValidException, CategoryExistentException;

	void update(Category category) throws FieldNotValidException, CategoryNotFoundException;

	Category findById(Long id) throws CategoryNotFoundException;

	List<Category> findAll();

}
