package com.library.app.category.services.impl;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
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

	private void addCategoryWithInvalidName(final String name) {
		try {
			categoryService.add(new Category());
			fail("Some error must have been thrown");
		} catch (final FieldNotValidException fnve) {
			assertThat(fnve.getFieldName(), is(equalTo("name")));
		}
	}

}
