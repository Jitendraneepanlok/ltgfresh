package com.ltg.ltgfresh.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ltg.ltgfresh.Activity.LoginActivity;
import com.ltg.ltgfresh.Activity.RegisterActivity;
import com.ltg.ltgfresh.Helper.Utility;
import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.RegistrationResponse;
import com.ltg.ltgfresh.Pojo.UpdateProfileResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileFragment extends Fragment {
    View view;
    private NavController navController;
    AppCompatTextView tv_text, tv_edit_image;
    AppCompatImageView img_back,img_cart,img_profile;
    CircleImageView profile_img;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    Bitmap getImage;
    Uri filePath, CameraFilePath;
    File destination;
    AppCompatEditText edit_first_name, edit_last_name, edit_phone, edit_email, edit_address;
    AppCompatButton btn_update;
    String First_Name, Last_Name, Phone, Email, Address,User_ID,GalleryImagetemp,CameraImagetemp;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    Toolbar toolbar;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        sessionManager = new SessionManager(getActivity());
        User_ID = sessionManager.getUserData(SessionManager.ID);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        img_cart = (AppCompatImageView)toolbar.findViewById(R.id.img_cart);
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_cartViewFragment);
            }
        });

        img_profile = (AppCompatImageView)toolbar.findViewById(R.id.img_profile);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });
        initView();
        return view;
    }

    private void initView() {
        edit_first_name = (AppCompatEditText) view.findViewById(R.id.edit_first_name);
        edit_last_name = (AppCompatEditText) view.findViewById(R.id.edit_last_name);
        edit_phone = (AppCompatEditText) view.findViewById(R.id.edit_phone);
        edit_email = (AppCompatEditText) view.findViewById(R.id.edit_email);
        edit_address = (AppCompatEditText) view.findViewById(R.id.edit_address);

        btn_update = (AppCompatButton) view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        tv_text = (AppCompatTextView) view.findViewById(R.id.tv_text);
        tv_text.setText("Edit Profile");

        img_back = (AppCompatImageView) view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        profile_img = (CircleImageView) view.findViewById(R.id.profile_img);
        tv_edit_image = (AppCompatTextView) view.findViewById(R.id.tv_edit_image);
        tv_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
    }

    private void SelectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        checkPermission();
                    // cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 5);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                filePath = data.getData();
                Log.e("Gallerypath", "" + filePath);

                onSelectFromGalleryResult(data);

            } else if (requestCode == REQUEST_CAMERA) {
                CameraFilePath = data.getData();
                Log.e("Camerapath", "" + CameraFilePath);
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        getImage = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        getImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");
        destination.getPath();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("patheee", destination.getAbsolutePath() + " check" + destination.getPath());
        CameraFilePath = Uri.parse(destination.getAbsolutePath() + " check" + destination.getPath());

        profile_img.setImageBitmap(getImage);

    }

    private void onSelectFromGalleryResult(Intent data) {
        getImage = null;
        if (data != null) {
            try {
                getImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profile_img.setImageBitmap(getImage);


    }

    private void checkValidation() {
        First_Name = edit_first_name.getText().toString().trim();
        Last_Name = edit_last_name.getText().toString().trim();
        Phone = edit_phone.getText().toString().trim();
        Email = edit_email.getText().toString().trim();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Address = edit_address.getText().toString().trim();

        if (!First_Name.equals("")) {
            if (!Last_Name.equals("")) {
                if (isValidPhone(Phone)) {
                    if (Email.matches(emailpattern)) {
                        if (!Address.equals("")) {
                            CallUpdateProfileApi();
                        } else {
                            edit_address.setError("Enter your Address");
                        }
                    } else {
                        edit_email.setError("Enter your Valid Email-Id");
                    }
                } else {
                    edit_phone.setError("Enter your Valid Phone No.");
                }
            } else {
                edit_last_name.setError("Enter Your Last Name");

            }
        } else {
            edit_first_name.setError("Enter Your First Name");
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

    private void CallUpdateProfileApi() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateProfileResponse> call = apiService.postUpdateProfile(First_Name, Last_Name, Phone, Email, Address, filePath+","+CameraFilePath,User_ID);

        try {
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, retrofit2.Response<UpdateProfileResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true){
                            Log.e("Login_Response", "" + response.body().toString());
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            pDialog.cancel();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    // Log error here since request failed
                    Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("Failer", "" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
