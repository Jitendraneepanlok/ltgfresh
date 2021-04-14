package com.ltg.ltgfresh.Network;

import com.ltg.ltgfresh.Pojo.ForgetPasswordResponse;
import com.ltg.ltgfresh.Pojo.LoginResponse;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.RegistrationResponse;
import com.ltg.ltgfresh.Pojo.SingleProductResponse;
import com.ltg.ltgfresh.Pojo.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("Registration")
    Call<RegistrationResponse> postSignUp(@Field("name") String name,
                                          @Field("email") String email,
                                          @Field("mobile") String mobile,
                                          @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> postSignIn(@Field("mobile") String mobile, @Field("password") String password);


    @GET("products")
    Call<ProductResponse> getProductList();

    @FormUrlEncoded
    @POST("singleproduct")
    Call<SingleProductResponse> getSingleProduct(@Field("id") String name);


    @FormUrlEncoded
    @POST("forgot")
    Call<ForgetPasswordResponse> PostForgotPassword(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("update")
    Call<UpdateProfileResponse> postUpdateProfile(@Field("first_name") String FirstName,
                                                  @Field("last_name") String LastName,
                                                  @Field("email") String Email,
                                                  @Field("phone") String Phone,
                                                  @Field("file_name") String ImageName,
                                                  @Field("address") String Address,
                                                  @Field("id") String Id);


}
