package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_print_pkg;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.utility.CustomViewPager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Invoice_Print_Activity extends AppCompatActivity implements View.OnClickListener {


    public static final int STORAGE_REQUEST = 1000;
    Invoice_Print_Fragment fragment;
    private int currentSelectedPage = 0;
    private CustomViewPager viewPagerInvoice;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private TextView txtTempOne, txtTempTwo;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            Log.d("position-", "" + position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.d("position--", "" + arg0);
            currentSelectedPage = arg0;
            setBackground(currentSelectedPage);

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.d("position---", "" + arg0);

        }
    };
    private Button btnSavePdf, btnCancel;
    private Inv_Res_Model invoice_Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_print);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Bundle bundle = getIntent().getExtras();

        if (getIntent().hasExtra("invoiceDetails")) {
            String convertObject = bundle.getString("invoiceDetails");
            invoice_Details = new Gson().fromJson(convertObject, Inv_Res_Model.class);
        }

        viewPagerInvoice = findViewById(R.id.viewPagerInvoice);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPagerInvoice.setAdapter(mPagerAdapter);
        viewPagerInvoice.addOnPageChangeListener(viewPagerPageChangeListener);

        txtTempOne = findViewById(R.id.txtTempOne);
        txtTempTwo = findViewById(R.id.txtTempTwo);
        btnSavePdf = findViewById(R.id.btnSavePdf);
        btnCancel = findViewById(R.id.btnCancel);

        txtTempOne.setOnClickListener(this);
        txtTempTwo.setOnClickListener(this);
        btnSavePdf.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        //disable viewPager horizontal scrolling.
        viewPagerInvoice.disableScroll(true);
    }

    private void setBackground(int position) {
        if (position == 0) {
            txtTempOne.setBackgroundResource(R.drawable.temp_enable);
            txtTempTwo.setBackgroundResource(R.drawable.temp_disable);
            txtTempTwo.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtTempOne.setTextColor(getResources().getColor(R.color.white));
        } else if (position == 1) {
            txtTempOne.setBackgroundResource(R.drawable.temp_disable);
            txtTempTwo.setBackgroundResource(R.drawable.temp_enable);
            txtTempTwo.setTextColor(getResources().getColor(R.color.white));
            txtTempOne.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtTempOne:
                viewPagerInvoice.setCurrentItem(0, true);
                break;
            case R.id.txtTempTwo:
                viewPagerInvoice.setCurrentItem(1, true);
                break;
            case R.id.btnSavePdf:
                if (ContextCompat.checkSelfPermission(Invoice_Print_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    ScreenSlidePagerAdapter adapter = (ScreenSlidePagerAdapter) viewPagerInvoice.getAdapter();
                    fragment = adapter.getFragment(currentSelectedPage);
                    fragment.convertZoomLayoutToPdf();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_REQUEST);
                }
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ScreenSlidePagerAdapter adapter = (ScreenSlidePagerAdapter) viewPagerInvoice.getAdapter();
                    fragment = adapter.getFragment(currentSelectedPage);
                    fragment.convertZoomLayoutToPdf();
                }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        Map<Integer, Invoice_Print_Fragment> fragmentMap = new HashMap<>();
        String invoiceDetailsObject;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            invoiceDetailsObject = new Gson().toJson(invoice_Details);
        }

        @Override
        public Fragment getItem(int position) {
            Invoice_Print_Fragment fragment = new Invoice_Print_Fragment();
            fragmentMap.put(position, fragment);
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("invoiceDetails", invoiceDetailsObject);
            fragment.setArguments(bundle);
            return fragment;
        }

        public Invoice_Print_Fragment getFragment(int index) {
            return fragmentMap.get(index);
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
}
