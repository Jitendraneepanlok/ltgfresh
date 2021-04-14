package com.ltg.ltgfresh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.ForgetPasswordResponse;
import com.ltg.ltgfresh.Pojo.LoginResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordActiviy extends AppCompatActivity {

    AppCompatTextView tv_text;
    AppCompatImageView img_back;
    AppCompatEditText et_phone_no;
    AppCompatButton btn_forget_password;
    private ProgressDialog pDialog;
    String Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_activiy);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        }

        initView();
    }

    private void initView() {

        tv_text = (AppCompatTextView) findViewById(R.id.tv_text);
        tv_text.setText("Forgot Password");

        img_back = (AppCompatImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        et_phone_no = (AppCompatEditText) findViewById(R.id.et_phone_no);
        btn_forget_password = (AppCompatButton) findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
            }
        });

    }

    private void CheckValidation() {
        Phone = et_phone_no.getText().toString().trim();
        if (isValidPhone(Phone)) {
            CallForgotApi();
        } else {
            et_phone_no.setError("Please Enter Registred Mobile No.");
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

    private void CallForgotApi() {
        pDialog = new ProgressDialog(ForgotPasswordActiviy.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ForgetPasswordResponse> call = apiService.PostForgotPassword(Phone);
        try {
            call.enqueue(new Callback<ForgetPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgetPasswordResponse> call, retrofit2.Response<ForgetPasswordResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(ForgotPasswordActiviy.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        } else {
                            Toast.makeText(ForgotPasswordActiviy.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ForgotPasswordActiviy.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                    // Log error here since request failed
                    Toast.makeText(ForgotPasswordActiviy.this, "" + t, Toast.LENGTH_SHORT).show();
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