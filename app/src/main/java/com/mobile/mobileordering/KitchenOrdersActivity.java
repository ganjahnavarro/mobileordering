package com.mobile.mobileordering;

import android.content.Context;

import com.mobile.mobileordering.util.Constants;

public class KitchenOrdersActivity extends AbstractOrdersActivity {

    @Override
    protected Context getContext() {
        return KitchenOrdersActivity.this;
    }

    @Override
    protected String getStatus() {
        return Constants.ORDER_STATUS_PAID;
    }

    @Override
    protected boolean isKitchenOrders() {
        return true;
    }
}