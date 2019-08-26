package com.intutrack.intudock.UI;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.intutrack.intudock.R;
import com.intutrack.intudock.UI.Fragments.ViewDocksFragment;
import com.intutrack.intudock.UI.Fragments.ViewTransactionsFragment;
import com.intutrack.intudock.Utilities.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class DockTransactionActivity extends BaseActivity {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dock_transaction);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        setupViewPager(viewPager);

        tabLayout.getSelectedTabPosition();

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(DockTransactionActivity.this.getSupportFragmentManager());
        adapter.addFragment(new ViewDocksFragment(), "View Docks");
        adapter.addFragment(new ViewTransactionsFragment(), "View Transactions");
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        tabLayout.setupWithViewPager(viewPager, true);

//        tabLayout.setupWithViewPager(viewPager);
//
//        setupViewPager(viewPager);
    }
}
