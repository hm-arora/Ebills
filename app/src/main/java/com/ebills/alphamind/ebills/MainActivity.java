package com.ebills.alphamind.ebills;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.widget.Toast;

import com.ebills.alphamind.ebills.Adapters.ViewPagerAdapter;
import com.ebills.alphamind.ebills.Fragments.MainActivityFragments.Recent;
import com.ebills.alphamind.ebills.Fragments.MainActivityFragments.AllBills;
import com.ebills.alphamind.ebills.Notifications.FirebaseTokenService;
import com.ebills.alphamind.ebills.Storage.OTPToken.Otptoken;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private DrawerLayout sideBar;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST = 1;

    private ActionBarDrawerToggle sideBarToggle;
    String[] PERMISSIONS = {Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences(FirebaseTokenService.PREFS, Context.MODE_PRIVATE);
        String token = preferences.getString(FirebaseTokenService.TOKEN, "null");
        Log.e(TAG, "Token: " + token);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        sideBar = findViewById(R.id.drawer_layout);
//        sideBarToggle = new ActionBarDrawerToggle(this, sideBar, toolbar, R.string.sideBarOpen, R.string.sideBarClose);
//        sideBar.addDrawerListener(sideBarToggle);
//        sideBarToggle.syncState();

        if (hasPermissions(this, PERMISSIONS)) {

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            showLayouts();

            // Check Login
            checkLogin();

            // FLoating Action Button Click Listener
            clickFab();

        } else
            checkPermission();

    }

    //Check Login
    private void checkLogin() {

        Otptoken otptoken = new Otptoken(MainActivity.this);
        String otp = otptoken.getOTP();
        if (otp.equals(" ")) {
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    //clickFab
    private void clickFab() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LoginActivityClass
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void showLayouts() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Fab login
        floatingActionButton = findViewById(R.id.fab);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Recent(MainActivity.this), "Recent");
        adapter.addFragment(new AllBills(MainActivity.this), "All Bills");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search the Animations");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent i = new Intent(MainActivity.this, SearchingActivity.class);
                i.putExtra("query", query);
                startActivity(i);
                MainActivity.this.overridePendingTransition(R.anim.anim_slide_left, R.anim.anim_slide_left_two);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS) +
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) +
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECEIVE_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions to read OTP from messages",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
                            }
                        }).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        PERMISSIONS, MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            // Should we show an explanation?
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean read_sms = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean recieve_sms = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    // permission is DENIED !!
                    if (read_sms && recieve_sms && storage) {
                        showLayouts();
                        // Check Login
                        checkLogin();

                        // FLoating Action Button Click Listener
                        clickFab();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content),
                                "Please Grant Permissions to read OTP from messages",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                MY_PERMISSIONS_REQUEST);
                                    }
                                }).show();
                        boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS);
                        boolean showRationale1 = shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS);
                        boolean showRationale2 = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (!showRationale && !showRationale1 && !showRationale2) {
                            Toast.makeText(this, "Now you need to give permission manually", Toast.LENGTH_LONG);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, MY_PERMISSIONS_REQUEST);
                        }
                    }
                }
                break;
            }
        }
    }


}

