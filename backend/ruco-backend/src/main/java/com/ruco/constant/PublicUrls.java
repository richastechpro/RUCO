package com.ruco.constant;

import java.util.Arrays;
import java.util.List;

public class PublicUrls {

	public static final String LOGIN = "/auth/login";

	public static final String VENDOR_LOGIN = "/vendor/login";

	public static final String REGISTER = "/auth/register";

	public static final String VALIDATE = "/auth/validate";

	public static final String FORGOT_PASSWORD = "/auth/forgot-password";

	public static final String PUBLIC_TEST_GET = "/test/public/get";

	public static final String ITEMS_GET = "/customer-item/valid";

	public static final String PRODUCTS_GET = "/customer-item/products";

	public static final String ORDERS_GET = "/order/**";

	public static final String FILE_GET = "/file/**";

	public static final List<String> PUBLIC_PATHS = Arrays.asList(LOGIN, VENDOR_LOGIN, REGISTER, VALIDATE,
			FORGOT_PASSWORD, PUBLIC_TEST_GET, ITEMS_GET, ORDERS_GET, PRODUCTS_GET, FILE_GET);

}
