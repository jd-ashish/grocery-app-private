package com.apps.onlinegroceriesapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.databinding.ActivityOrdersBinding;
import com.apps.onlinegroceriesapps.fragment.order.AllOrderFragment;
import com.apps.onlinegroceriesapps.fragment.order.CancelOrderFragment;
import com.apps.onlinegroceriesapps.fragment.order.DeliveredOrderFragment;
import com.apps.onlinegroceriesapps.fragment.order.InProgressOrderFragment;
import com.apps.onlinegroceriesapps.fragment.order.PendingOrderFragment;
import com.apps.onlinegroceriesapps.fragment.order.ShippedOrderFragment;
import com.apps.onlinegroceriesapps.viewPage.ViewPage;
import com.google.android.material.tabs.TabLayout;

public class OrdersActivity extends AppCompatActivity {

    ActivityOrdersBinding binding;
    ViewPage viewPage;
    AllOrderFragment allOrderFragment;
    PendingOrderFragment pendingOrderFragment;
    CancelOrderFragment cancelOrderFragment;
    InProgressOrderFragment inProgressOrderFragment;
    ShippedOrderFragment shippedOrderFragment;
    DeliveredOrderFragment deliveredOrderFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpViewPager();
    }


    private void setUpViewPager() {

        ViewPage viewPage = new ViewPage(getSupportFragmentManager());
        allOrderFragment = new AllOrderFragment();
        pendingOrderFragment = new PendingOrderFragment();
        cancelOrderFragment = new CancelOrderFragment();
        inProgressOrderFragment = new InProgressOrderFragment();
        shippedOrderFragment = new ShippedOrderFragment();
        deliveredOrderFragment = new DeliveredOrderFragment();
        viewPage.addFragment(allOrderFragment, getString(R.string.all_order));
        viewPage.addFragment(pendingOrderFragment, getString(R.string.pending_order));
        viewPage.addFragment(inProgressOrderFragment, getString(R.string.inprogress_order));
        viewPage.addFragment(shippedOrderFragment, getString(R.string.shipped));
        viewPage.addFragment(deliveredOrderFragment, getString(R.string.delivered_order));
        viewPage.addFragment(cancelOrderFragment, getString(R.string.cancel));

        binding.viewpager.setAdapter(viewPage);

//        TODO Add a handler later on
        binding.viewpager.setOffscreenPageLimit(5);

        binding.commonTabsBarLayout.setupWithViewPager(binding.viewpager);

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        System.out.println("allOrderFragment");
                        break;
                    case 1:
                        System.out.println("pendingOrderFragment");
                        break;
                    case 2:
                        System.out.println("cancelOrderFragment");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }
}