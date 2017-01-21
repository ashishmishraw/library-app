package com.library.app.category.services.impl;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices {

	Validator validator;
	CategoryRepository categoryRepository;

	@Override
	public Category add(final Category category) {

		final Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		final Iterator<ConstraintViolation<Category>> errors = constraintViolations.iterator();

		if (errors.hasNext()) {
			final ConstraintViolation<Category> violation = errors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}

		if (categoryRepository.alreadyExists(category)) {
			throw new CategoryExistentException();
		}

		return categoryRepository.add(category);
	}

}
