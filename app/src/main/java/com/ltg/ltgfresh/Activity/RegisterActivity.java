package com.ltg.ltgfresh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.LoginResponse;
import com.ltg.ltgfresh.Pojo.RegistrationResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.Service.MyReceiver;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {
    RelativeLayout rl_sign_in;
    AppCompatImageView img_back;
    AppCompatEditText et_name, et_email, et_phone_no, et_password;
    AppCompatButton btn_register;
    private BroadcastReceiver MyReceiver = null;

    String Name, Email, Phone, Password;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        View toolbar = (View) findViewById(R.id.toolbar);
        img_back = (AppCompatImageView) toolbar.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        rl_sign_in = (RelativeLayout) findViewById(R.id.rl_sign_in);
        rl_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        btn_register = (AppCompatButton) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        et_name = (AppCompatEditText) findViewById(R.id.et_name);
        et_email = (AppCompatEditText) findViewById(R.id.et_email);
        et_phone_no = (AppCompatEditText) findViewById(R.id.et_phone_no);
        et_password = (AppCompatEditText) findViewById(R.id.et_password);

        Name = et_name.getText().toString().trim();
        Email = et_email.getText().toString().trim();
        Phone = et_phone_no.getText().toString().trim();
        Password = et_password.getText().toString().trim();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!Name.equals("")) {
            if (Email.matches(emailpattern)) {
                if (isValidPhone(Phone)) {
                    if (!Password.equals("")) {
                        CallRegisterApi();
                    } else {
                        et_password.setError("Enter your Password");
                    }
                } else {
                    et_phone_no.setError("Enter your Phone No.");
                }
            } else {
                et_email.setError("Enter your Valid Email");
            }
        } else {
            et_name.setError("Enter your Name");
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

    private void CallRegisterApi() {

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RegistrationResponse> call = apiService.postSignUp(Name, Email, Phone, Password);
        try {
            call.enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(Call<RegistrationResponse> call, retrofit2.Response<RegistrationResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("Login_Response", "" + response.body().toString());
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                       /* SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.setValue(SessionManager.USER_NAME, response.body().getData().getEmail());*/

                       /* finish();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);*/
                        pDialog.dismiss();
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    // Log error here since request failed
                    Toast.makeText(RegisterActivity.this, "" + t, Toast.LENGTH_SHORT).show();
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