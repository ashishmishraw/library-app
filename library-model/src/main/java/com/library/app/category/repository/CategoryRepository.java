package com.library.app.category.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.library.app.category.model.Category;

public class CategoryRepository {

	EntityManager em;

	public Category add(final Category category) {
		em.persist(category);
		return category;
	}

	public Category findById(final Long id) {

		if (id == null) {
			return null;
		}
		return em.find(Category.class, id);
	}

	public void update(final Category category) {
		em.merge(category);
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAll(final String orderField) {
		return em.createQuery("Select e from Category e Order by e." + orderField).getResultList();
	}

	public boolean alreadyExists(final Category category) {

		final StringBuilder jpql = new StringBuilder();
		jpql.append("Select 1 from Category e where e.name = :name");

		final Query query = em.createQuery(jpql.toString());
		query.setParameter("name", category.getName());

		return query.setMaxResults(1).getResultList().size() > 0;
	}

}
