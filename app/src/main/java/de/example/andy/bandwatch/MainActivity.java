package de.example.andy.bandwatch;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int MY_PERMISSIONS_REQUEST_READ_MEDIA = 123;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();


//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                viewPager.setCurrentItem(1);
//            }
//        });

        //        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
//        } else {
//            readDataExternal();
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    readDataExternal();
//                }
//                break;
//
//            default:
//                break;
//        }
//    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFragment(), "1");
        adapter.addFragment(new BandsWrapperFragment(), "2");
        adapter.addFragment(new NearbyFragment(), "3");

        // workaround if problems with setting start tab @ viewPager.setCurrentItem(1);
        //if (viewPager.getAdapter() != null) viewPager.setAdapter(null);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_whatshot_black_24px);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group_black_24px);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_location_city_black_24dp);

        // custom Tab Views um das Icon links vom Text anzuzeigen
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("  News");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_black_24px, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("  Bands");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_black_24px, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("  Nearby");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_city_black_24dp, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

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
}
