package com.mobile.mobileordering.util;

public final class Constants {

    public static final String ORDER_STATUS_PENDING = "0";
    public static final String ORDER_STATUS_PAID = "1";
    public static final String ORDER_STATUS_CANCELLED = "2";
    public static final String ORDER_STATUS_VOID = "3";

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_CASHIER = 2;
    public static final int ROLE_COOK = 3;

    public static volatile int ROLE = -1;

}
