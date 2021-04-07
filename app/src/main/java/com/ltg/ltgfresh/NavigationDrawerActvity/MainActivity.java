package com.ltg.ltgfresh.NavigationDrawerActvity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private AppBarConfiguration mAppBarConfiguration;
    private SessionManager sessionManager;
    AppCompatTextView tvname, tv_address;
    static String result = "";

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        }
*/

        sessionManager = new SessionManager(this);
        name = sessionManager.getUserData(SessionManager.NAME);
        Log.e("user_id", name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_address = (AppCompatTextView) findViewById(R.id.tv_address);

        getCurrentLocation();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        /*tvname = (AppCompatTextView) drawer.findViewById(R.id.tvname);
        tvname.setText(name);*/

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.main, menu);
         return true;
     }
 */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getAddressFromLocation(final double latitude, final double longitude, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addressList != null && addressList.size() > 0) {
                                Address address = addressList.get(0);
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                    sb.append(address.getAddressLine(i)); //.append("\n");
                                }
                                sb.append(address.getLocality()).append("\n");
                                sb.append(address.getPostalCode()).append("\n");
                                sb.append(address.getCountryName());
                                result = sb.toString();
                                Log.e("Address", "" + result);
                                tv_address.setText(address.getLocality()+" "+address.getCountryName());
                            }
                        } catch (IOException e) {
                            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
                        } finally {
                            Message message = Message.obtain();
                            message.setTarget(handler);
                            if (result != null) {
                                message.what = 1;
                                Bundle bundle = new Bundle();
                                bundle.putString("address", result);
                                message.setData(bundle);
                            } else {
                                message.what = 1;
                                Bundle bundle = new Bundle();
                                result = " Unable to get address for this location.";
                                bundle.putString("address", result);
                                message.setData(bundle);
                            }
                            message.sendToTarget();
                        }
                    }
                });
            }
        };
        thread.start();
        return result;
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, MainActivity.this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Log.e("Lati", "" + location.getLatitude());
        Log.e("Longi", "" + location.getLongitude());

        getAddressFromLocation(location.getLatitude(), location.getLongitude(), this, new Handler());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}