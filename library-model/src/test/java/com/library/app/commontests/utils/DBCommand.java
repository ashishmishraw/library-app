package com.library.app.commontests.utils;

import org.junit.Ignore;

@Ignore
@FunctionalInterface
public interface DBCommand<T> {

	T execute();
}
