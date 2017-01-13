package com.library.app.category.repository;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;

public class CategoryRepositoryUTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
	}

	@Test
	public void addCategoryAndFindIt() {
		/*
		 * boilerPlateTxnWrapper(new Txn() {
		 * private void doIt() {
		 * System.out.println("hi");
		 * }
		 * });
		 */

		boilerPlateTxnWrapper(() -> {
			final Long categoryAddedId = categoryRepository.add(java()).getId();
			assertThat(categoryAddedId, is(notNullValue()));
			final Category cat = categoryRepository.findById(categoryAddedId);
			assertThat(cat, is(notNullValue()));
			assertThat(cat.getName(), is(equalTo(java().getName())));
		});
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	private void boilerPlateTxnWrapper(final TxnPredicate work) {
		try {
			em.getTransaction().begin();
			System.out.println("beginning txn ");
			work.doIt();
			em.getTransaction().commit();
			System.out.println("committed txn");
			em.clear();
		} catch (final Exception ex) {
			fail("should not have come here ");
			ex.printStackTrace();
			em.getTransaction().rollback();
			System.out.println("rolled back txn ");
		}
	}

	@FunctionalInterface
	public interface TxnPredicate {
		void doIt();
	}

}
