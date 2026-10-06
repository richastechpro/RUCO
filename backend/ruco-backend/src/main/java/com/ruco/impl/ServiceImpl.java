package com.ruco.impl;

import org.springframework.data.domain.Page;

public interface ServiceImpl<T, ID> {

	void create(T data);

	Page<?> get(int limit, int offset);

	T get(ID id);

	void update(T data);

	void delete(ID id);

}
