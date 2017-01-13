package com.library.app.category.repository;

import javax.persistence.EntityManager;

import com.library.app.category.model.Category;

public class CategoryRepository {

	EntityManager em;

	public Category add(final Category category) {
		em.persist(category);
		return category;
	}

	public Category findById(final Long id) {
		return em.find(Category.class, id);
	}
}
