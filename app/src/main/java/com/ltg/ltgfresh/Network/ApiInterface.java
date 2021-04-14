package com.ltg.ltgfresh.Network;

import com.ltg.ltgfresh.Pojo.ForgetPasswordResponse;
import com.ltg.ltgfresh.Pojo.LoginResponse;
import com.ltg.ltgfresh.Pojo.LogoutResponse;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.RegistrationResponse;
import com.ltg.ltgfresh.Pojo.SingleProductResponse;
import com.ltg.ltgfresh.Pojo.UpdateProfileResponse;
import com.ltg.ltgfresh.Pojo.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    //=====================  User Registration Api===============================
    @FormUrlEncoded
    @POST("Registration")
    Call<RegistrationResponse> postSignUp(@Field("name") String name,
                                          @Field("email") String email,
                                          @Field("mobile") String mobile,
                                          @Field("password") String password);

    //=====================  User Login Api===============================

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> postSignIn(@Field("mobile") String mobile, @Field("password") String password);

//=====================  Get All Products Api===============================

    @GET("products")
    Call<ProductResponse> getProductList();

    //=====================  Get Single Product Api===============================

    @FormUrlEncoded
    @POST("singleproduct")
    Call<SingleProductResponse> getSingleProduct(@Field("id") String name);

//=====================  Forgot Password Api===============================

    @FormUrlEncoded
    @POST("forgot")
    Call<ForgetPasswordResponse> PostForgotPassword(@Field("mobile") String mobile);


    //=====================  User Update Profile Api===============================

    @FormUrlEncoded
    @POST("update")
    Call<UpdateProfileResponse> postUpdateProfile(@Field("first_name") String FirstName,
                                                  @Field("last_name") String LastName,
                                                  @Field("email") String Email,
                                                  @Field("phone") String Phone,
                                                  @Field("file_name") String ImageName,
                                                  @Field("address") String Address,
                                                  @Field("id") String Id);


    //=====================  Get User Details Api===============================

    @GET("user?")
    Call<UserProfileResponse> getUserDetails(@Query("id") String Id);

    //=====================  Get Logout Api===============================

    @GET("logout?")
    Call<LogoutResponse> getlogout(@Query("id") String Id);



}
