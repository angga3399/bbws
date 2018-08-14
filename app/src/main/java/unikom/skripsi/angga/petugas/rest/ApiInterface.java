package unikom.skripsi.angga.petugas.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import unikom.skripsi.angga.petugas.model.JarakModel;
import unikom.skripsi.angga.petugas.model.LupaPassword;
import unikom.skripsi.angga.petugas.model.MessageModel;
import unikom.skripsi.angga.petugas.model.Notification;
import unikom.skripsi.angga.petugas.model.NotificationResponse;
import unikom.skripsi.angga.petugas.model.TmaModel;
import unikom.skripsi.angga.petugas.model.UserModel;

public interface ApiInterface {
    @GET("getNotification.php")
    Call<NotificationResponse> getAllNotifications();

    @FormUrlEncoded
    @POST("getNotification.php")
    Call<Notification> getNotification(@Field("message_id") String message_id);

    @FormUrlEncoded
    @POST("getPetugas.php")
    Call<UserModel.UserDataModel> postLoginPetugas(@Field("email") String email,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST("batasGeotagging.php")
    Call<JarakModel> postGeotagging(@Field("lat1") double lat1,
                                    @Field("lng1") double lng1,
                                    @Field("lat2") double lat2,
                                    @Field("lng2") double lng2);

    @FormUrlEncoded
    @POST("postTMA.php")
    Call<MessageModel> postTMA(@Field("id") String id,
                           @Field("tanggal") String tanggal,
                           @Field("jam") String jam,
                           @Field("tma") double tma);

    @FormUrlEncoded
    @POST("LupaPassword.php")
    Call<MessageModel> postLupa(@Field("email") String email);


    @FormUrlEncoded
    @POST("profil.php")
    Call<MessageModel> postProfil(@Field("idUser") String idUser,
                           @Field("email") String email,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("getTMA.php")
    Call<TmaModel.TmaDataModel> getTMA(@Field("user_id") String user_id);

}
