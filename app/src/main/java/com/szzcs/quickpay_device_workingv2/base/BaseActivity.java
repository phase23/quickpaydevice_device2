package com.szzcs.quickpay_device_workingv2.base;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by yyzz on 19.7.11.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
