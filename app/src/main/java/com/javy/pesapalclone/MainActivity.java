package com.javy.pesapalclone;


import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.javy.pesapalclone.utils.EncryptedPrefsUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private String mAuthToken;
    private String mEmail;
    private String mPassword;
    private String mUserId;
    private String mTokenExpiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setTitle("Dashboard");

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        Navigation.setNavigation(this,bottomNavigationView);

        EncryptedPrefsUtil.init(this);
        mUserId = EncryptedPrefsUtil.getString("userId", "0");
        mEmail = EncryptedPrefsUtil.getString("email", "0");
        mPassword = EncryptedPrefsUtil.getString("password", "0");
        mTokenExpiry = EncryptedPrefsUtil.getString("tokenExpiry", "0");
        mAuthToken = EncryptedPrefsUtil.getString("authToken", "");

        if (mUserId.equals("0")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {


//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            // Set default fragment
            if (savedInstanceState == null) {
                String fragmentName = getIntent().getStringExtra("fragmentName");
                Fragment fragment = new HomeFragment();
                if (fragmentName != null) {
                    if (fragmentName.equals("deals")) {
                        actionBar.setTitle("Deals");
                        fragment = new DealsFragment();
                        bottomNavigationView.setSelectedItemId(R.id.navigation_deals);
                    }
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }


            // Handle navigation item selection
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
//            Toast.makeText(context, "Item ID: " + itemId, Toast.LENGTH_SHORT).show();
                if (itemId == R.id.navigation_home) {
                    actionBar.setTitle("Dashboard");
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.navigation_deals) {
                    actionBar.setTitle("Payments");
                    selectedFragment = new DealsFragment();
                }
//                else if (itemId == R.id.navigation_accounts) {
//                    actionBar.setTitle("Accounts");
//                    selectedFragment = new AccountsFragment();
//
//                }
//                else if (itemId == R.id.navigation_more) {
////                    actionBar.setTitle("More");
////                    selectedFragment = new MoreFragment();
////                    selectedFragment = new HomeFragment();
//                    Intent intent = new Intent(MainActivity.this, SelectLeadAccountActivity.class);
//                    startActivity(intent);
//
//
//                }


//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//
//                case R.id.navigation_accounts:
//                    selectedFragment = new DealsFragment();
//                    break;
//                case R.id.navigation_deals:
//                    selectedFragment = new DealsFragment();
//                    break;
//                case R.id.navigation_more:
//                    selectedFragment = new DealsFragment();
//                    break;
//            }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }


                return true;
            });

        }



    }


}