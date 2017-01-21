package com.library.app.category.services.impl;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesUTest {

	private CategoryServices categoryService;
	private CategoryRepository categoryRepository;
	private Validator validator;

	@Before
	public void initTest() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		categoryService = new CategoryServicesImpl();

		categoryRepository = mock(CategoryRepository.class);

		((CategoryServicesImpl) categoryService).validator = validator;
		((CategoryServicesImpl) categoryService).categoryRepository = categoryRepository;
	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}

	@Test
	public void addCategoryWithShortName() {
		addCategoryWithInvalidName("A");
	}

	@Test
	public void addCategoryWithLongName() {
		addCategoryWithInvalidName("A bogus Looooooong name is freakin not allowed ");
	}

	@Test(expected = CategoryExistentException.class)
	public void addCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(java())).thenReturn(true); // expectation

		categoryService.add(java());
	}

	@Test
	public void addValidCategory() {
		when(categoryRepository.alreadyExists(java())).thenReturn(false);
		when(categoryService.add(java())).thenReturn(categoryWithId(java(), 1L));

		final Category categoryAdded = categoryService.add(java());
		assertThat(categoryAdded.getId(), is(equalTo(1L)));

	}

	@Test
	public void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);
	}

	@Test
	public void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("A");
	}

	@Test
	public void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("A bogus Looooooong name is freakin not allowed ");
	}

	@Test(expected = CategoryExistentException.class)
	public void updateCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(true); // expectation

		categoryService.update(categoryWithId(java(), 1L));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void updateCategoryNotFound() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false); // expectation
		when(categoryRepository.existsById(1L)).thenReturn(false);

		categoryService.update(categoryWithId(java(), 1L));
	}

	@Test
	public void updateValidCategory() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false); // expectation
		when(categoryRepository.existsById(1L)).thenReturn(true);

		categoryService.update(categoryWithId(java(), 1L));
		verify(categoryRepository).update(categoryWithId(java(), 1L));
	}

	@Test
	public void testFindCategoryById() {
		when(categoryRepository.findById(1L)).thenReturn(categoryWithId(java(), 1L));
		final Category cat = categoryService.findById(1L);
		assertThat(cat, is(notNullValue()));
		assertThat(cat.getId(), is(equalTo(1L)));
		assertThat(cat.getName(), is(equalTo(java().getName())));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void findNonExistentCategoryById() {
		when(categoryRepository.findById(1L)).thenReturn(null);
		categoryService.findById(1L);

	}

	@Test
	public void findAllNoCategory() {
		when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());

		final List<Category> categories = categoryService.findAll();
		assertThat(categories.isEmpty(), is(equalTo(true)));
	}

	@Test
	public void findAllCategories() {
		when(categoryRepository.findAll("name")).thenReturn(
				Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));

		final List<Category> categories = categoryService.findAll();
		assertThat(categories.isEmpty(), is(equalTo(false)));
		assertThat(categories.size(), is(equalTo(2)));
	}

	private void addCategoryWithInvalidName(final String name) {
		try {
			categoryService.add(new Category());
			fail("Some error must have been thrown");
		} catch (final FieldNotValidException fnve) {
			assertThat(fnve.getFieldName(), is(equalTo("name")));
		}
	}

	private void updateCategoryWithInvalidName(final String name) {
		try {
			categoryService.update(new Category());
			fail("Some error must have been thrown");
		} catch (final FieldNotValidException fnve) {
			assertThat(fnve.getFieldName(), is(equalTo("name")));
		}
	}

}
