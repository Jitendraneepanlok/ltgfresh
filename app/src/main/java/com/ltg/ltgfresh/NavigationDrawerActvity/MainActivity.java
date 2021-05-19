package com.ltg.ltgfresh.NavigationDrawerActvity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Activity.CategoryActivity;
import com.ltg.ltgfresh.Activity.LoginActivity;
import com.ltg.ltgfresh.Activity.SplashScreenActivity;
import com.ltg.ltgfresh.Adapter.ProductAdapter;
import com.ltg.ltgfresh.Helper.UpdateInterface;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.LogoutResponse;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.UserProfileResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private AppBarConfiguration mAppBarConfiguration;
    private SessionManager sessionManager;
    AppCompatTextView tvname, tv_address;
    static String result = "";
    AppCompatImageView img_profile, img_cart;
    String name;
    NavController navController;
    private ProgressDialog pDialog;
    View headerView;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    String Id;
    CircleImageView img_user_profile;
    TextView tv_cart_count;
    public static String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value = "1";

        img_profile = (AppCompatImageView) findViewById(R.id.img_profile);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });


        img_cart = (AppCompatImageView) findViewById(R.id.img_cart);
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_cartViewFragment);
            }
        });


        tv_cart_count = (TextView) findViewById(R.id.tv_cart_count);

        sessionManager = new SessionManager(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_address = (AppCompatTextView) findViewById(R.id.tv_address);

        getCurrentLocation();
        getUserProfileDetails();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvname = (AppCompatTextView) headerView.findViewById(R.id.tvname);
        img_user_profile = (CircleImageView) headerView.findViewById(R.id.img_user_profile);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
                        break;

                    case R.id.nav_organic:
                        value = "2";
//                        Bundle bundle = new Bundle();
//                        bundle.putString("Category_No",value);
                        navController.navigate(R.id.nav_slideshow/*,bundle*/);
                        break;

                    case R.id.nav_logout:
                        OpenLoggedOutDailog();
                        break;

                    case R.id.nav_contact_us:
                        String url = "https://ltgfresh.com/welcome/contact";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;


                    case R.id.nav_privacy_policy:
                        String privacy_url = "https://ltgfresh.com/welcome/privacy_policy";
                        Intent in = new Intent(Intent.ACTION_VIEW);
                        in.setData(Uri.parse(privacy_url));
                        startActivity(in);
                        break;

                    case R.id.nav_terms:
                        String terms_url = "https://ltgfresh.com/welcome/terms_condition";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(terms_url));
                        startActivity(intent);
                        break;

                    case R.id.nav_share:
                        String share_url = "https://play.google.com/store/apps/details?id=co.wl.freshapure";
                        Intent share = new Intent(Intent.ACTION_VIEW);
                        share.setData(Uri.parse(share_url));
                        startActivity(share);
                        break;


                }

                NavigationUI.onNavDestinationSelected(item, navController);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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
                                tv_address.setText(address.getLocality() + " " + address.getCountryName());
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

    private void getUserProfileDetails() {
        Id = sessionManager.getUserData(SessionManager.ID);
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserProfileResponse> call = apiService.getUserDetails(Id);
        try {
            call.enqueue(new Callback<UserProfileResponse>() {
                @Override
                public void onResponse(Call<UserProfileResponse> call, retrofit2.Response<UserProfileResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        tvname.setText(response.body().getData().getName());
                        Glide.with(MainActivity.this)
                                .load(response.body().getData().getProfilePic())
                                .placeholder(R.drawable.ic_user)
                                .into(img_user_profile);
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<UserProfileResponse>() {
                            }.getType();
                            UserProfileResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(MainActivity.this, String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Exception", "" + e);

                        }
                    }
                }

                @Override
                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("", "Failer" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(MainActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

    }

    private void OpenLoggedOutDailog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCancelable(false);

        AppCompatTextView tvcancel = (AppCompatTextView) dialog.findViewById(R.id.tvcancel);
        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        AppCompatTextView tvyes = (AppCompatTextView) dialog.findViewById(R.id.tvyes);
        tvyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                CallLogoutApi();
            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    private void CallLogoutApi() {
        Id = sessionManager.getUserData(SessionManager.ID);
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LogoutResponse> call = apiService.getlogout(Id);
        try {
            call.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, retrofit2.Response<LogoutResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        sessionManager.logout();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<LogoutResponse>() {
                            }.getType();
                            LogoutResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(MainActivity.this, String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Exception", "" + e);

                        }
                    }
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("", "Failer" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(MainActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

    }

}