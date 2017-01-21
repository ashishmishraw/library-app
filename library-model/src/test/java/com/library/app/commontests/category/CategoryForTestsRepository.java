package com.library.app.commontests.category;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;

import com.library.app.category.model.Category;

@Ignore
public class CategoryForTestsRepository {

	public static Category java() {
		return new Category("Java");
	}

	public static Category cleanCode() {
		return new Category("Clean Code");
	}

	public static Category architecture() {
		return new Category("Architecture");
	}

	public static Category networks() {
		return new Category("Networks");
	}

	public static Category categoryWithId(final Category cat, final Long id) {
		cat.setId(id);
		return cat;
	}

	public static List<Category> getAllCategories() {
		return Arrays.asList(java(), cleanCode(), architecture(), networks());
	}
}
