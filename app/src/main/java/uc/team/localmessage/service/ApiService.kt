package uc.team.localmessage.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import uc.team.localmessage.model.DeviceModel
import uc.team.localmessage.model.RegisterDevice
import uc.team.localmessage.model.TokenModel

interface ApiService {

    @POST("/login/token/")
    @Headers("No-Authentication: true")
    @FormUrlEncoded
    fun getToken(
        @Field("code") code: String,
        @Field("scope") scope: String,
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String
    ): Call<TokenModel>


    @POST("/api/v1/devices/")
    fun registerDevice(
        @Body request: RegisterDevice
    ): Call<DeviceModel>

    @GET("/api/v1/devices/{deviceName}/")
    fun getMe(@Path("deviceName") deviceName: String): Call<DeviceModel>
}