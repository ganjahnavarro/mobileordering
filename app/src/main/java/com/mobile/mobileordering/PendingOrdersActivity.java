package com.mobile.mobileordering;

import android.content.Context;

import com.mobile.mobileordering.util.Constants;

public class PendingOrdersActivity extends AbstractOrdersActivity {

    @Override
    protected Context getContext() {
        return PendingOrdersActivity.this;
    }

    @Override
    protected String getStatus() {
        return Constants.ORDER_STATUS_PENDING;
    }

}
