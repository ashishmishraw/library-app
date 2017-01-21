package com.library.app.category.services.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices {

	Validator validator;
	CategoryRepository categoryRepository;

	@Override
	public Category add(final Category category) {

		validateCategory(category);

		return categoryRepository.add(category);
	}

	private void validateCategory(final Category category) {
		validateCategoryFields(category);

		if (categoryRepository.alreadyExists(category)) {
			throw new CategoryExistentException();
		}
	}

	private void validateCategoryFields(final Category category) {
		final Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		final Iterator<ConstraintViolation<Category>> errors = constraintViolations.iterator();

		if (errors.hasNext()) {
			final ConstraintViolation<Category> violation = errors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}

	@Override
	public void update(final Category category) {

		validateCategory(category);

		if (!categoryRepository.existsById(category.getId())) {
			throw new CategoryNotFoundException();
		}

		categoryRepository.update(category);

	}

	@Override
	public Category findById(final Long id) throws CategoryNotFoundException {

		final Category cat = categoryRepository.findById(id);
		if (null == cat) {
			throw new CategoryNotFoundException();
		}
		return cat;
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll("name");
	}

}
