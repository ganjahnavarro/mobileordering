package com.mobile.mobileordering;

import android.content.Context;

import com.mobile.mobileordering.util.Constants;

public class PaidOrdersActivity extends  AbstractOrdersActivity{

    @Override
    protected Context getContext() {
        return PaidOrdersActivity.this;
    }

    @Override
    protected String getStatus() {
        return Constants.ORDER_STATUS_PAID;
    }

}
