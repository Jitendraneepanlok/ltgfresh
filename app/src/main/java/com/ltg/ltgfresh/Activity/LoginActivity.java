package com.ltg.ltgfresh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.LoginResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.Service.MyReceiver;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout rl_register;
    AppCompatEditText et_phone, et_password;
    AppCompatButton btn_login;
    String contact_no, passwprd;
    private ProgressDialog pDialog;
    private BroadcastReceiver MyReceiver = null;
    private SessionManager sessionManager;
    Boolean ischecked = false;
    AppCompatCheckBox cb_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        }

        // for check internet
        MyReceiver = new MyReceiver();
        broadcastIntent();
        initView();
    }

    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    private void initView() {
        rl_register = (RelativeLayout) findViewById(R.id.rl_register);
        rl_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        cb_remember = (AppCompatCheckBox)findViewById(R.id.cb_remember);
        cb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ischecked = isChecked;

            }
        });

    }

    private void checkValidation() {
        et_phone = (AppCompatEditText) findViewById(R.id.et_phone);
        et_password = (AppCompatEditText) findViewById(R.id.et_password);
        passwprd = et_password.getText().toString();
        if (isValidPhone(contact_no = et_phone.getText().toString())) {
            if (!passwprd.equals("")) {
                CallApi();
            } else {
                et_password.setError("Enter your Password");
            }

        } else {
            et_phone.setError("Enter valid Number");
        }
    }

    private boolean isValidPhone(String phone) {
        boolean check = false;
        if (!Pattern.matches("[0-10]+", phone)) {
            if (phone.length() < 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private void CallApi() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.postSignIn(contact_no, passwprd);
        try {
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("Login_Response", "" + response.body().toString());
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        //=============== Store Data in Session using SessionManager Class ===========================================
                        sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.setValue(SessionManager.ID, response.body().getData().getId());
                        sessionManager.setValue(SessionManager.NAME, response.body().getData().getName());
                        sessionManager.setValue(SessionManager.EMAIL, response.body().getData().getEmail());
                        sessionManager.setValue(SessionManager.PHONE, response.body().getData().getContact());
                        sessionManager.setValue(SessionManager.ROLE, response.body().getData().getRole());
                        sessionManager.setValueBoolean(SessionManager.VALUE, ischecked);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        pDialog.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // Log error here since request failed
                    Toast.makeText(LoginActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("Failer", "" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }


    }
}