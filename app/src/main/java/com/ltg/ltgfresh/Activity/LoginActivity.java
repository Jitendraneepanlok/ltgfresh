package com.ltg.ltgfresh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ltg.ltgfresh.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout rl_register;
    AppCompatEditText et_phone, et_password;
    AppCompatButton btn_login;
    String contact_no, passwprd;

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

        initView();
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


    }
}