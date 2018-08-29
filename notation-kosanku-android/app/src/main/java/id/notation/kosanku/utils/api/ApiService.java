package id.notation.kosanku.utils.api;

import org.json.JSONObject;

import id.notation.kosanku.models.KosanTransaction;
import id.notation.kosanku.models.employee.EmployeeCollection;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.models.indekos.IndekosCollection;
import id.notation.kosanku.models.indekos.IndekosItem;
import id.notation.kosanku.models.kamarkos.KamarkosCollection;
import id.notation.kosanku.models.kamarkos.KamarkosItem;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by alfredo on 4/8/2018.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("kosan_api/transfer")
    Call<ResponseBody> transfer(@Field("transaction_id") String transactionId);

    @POST("charge/")
    Call<ResponseBody> charge(@Body JSONObject transactionDetails);

    @FormUrlEncoded
    @POST("users_api/login")
    Call<ResponseBody> loginRequest(@Field("username") String username,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("users_api/set_device_token")
    Call<ResponseBody> setDeviceToken(@Header("Authorization") String accessToken,
                                      @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST("indekos_api/")
    Call<IndekosCollection> getIndekos(@Field("id_user") int id_user);

    @Multipart
    @POST("indekos_api/store")
    Call<ResponseBody> createIndekos(@Header("Authorization") String accessToken,
                                     @Part("nama") String nama,
                                     @Part("alamat") String alamat,
                                     @Part("gender") String gender,
                                     @Part("kota") String kota,
                                     @Part("fasilitas_umum") String fasilitas_umum,
                                     @Part MultipartBody.Part image);

    @Multipart
    @POST("indekos_api/update/{id}")
    Call<ResponseBody> updateIndekos(@Header("Authorization") String accessToken,
                                     @Path("id") int id,
                                     @Part("nama") String nama,
                                     @Part("alamat") String alamat,
                                     @Part("gender") String gender,
                                     @Part("kota") String kota,
                                     @Part("fasilitas_umum") String fasilitas_umum,
                                     @Part MultipartBody.Part image);

    @GET("indekos_api/show/{id}")
    Call<IndekosItem> showIndekos(@Path("id") int id);

    @DELETE("indekos_api/destroy/{id}")
    Call<ResponseBody> deleteIndekos(@Path("id") int id);

    @GET("kos_api/show_by_indekos_id/{id_indekos}")
    Call<KamarkosCollection> getKamarByIndekos(@Path("id_indekos") int id);

    @GET("kos_api/show/{id}")
    Call<KamarkosItem> showKamar(@Path("id") int id);

    @FormUrlEncoded
    @POST("kos_api/create_order")
    Call<KosanTransaction> createOrder(@Field("id_indekos") int id_indekos, @Field("id_kamar") int id_kamar);

    @FormUrlEncoded
    @POST("kos_api/get_order")
    Call<ResponseBody> getOrder(@Field("id_order") int id_order);

    @FormUrlEncoded
    @POST("kos_api/after_transaction")
    Call<ResponseBody> afterTransaction(@Field("id_order") int id_order,
                                        @Field("transaction_id") String transaction_id,
                                        @Field("id_user") int id_user,
                                        @Field("status") String status,
                                        @Field("payment_type") String payment_type);

    @GET("employee_api/")
    Call<EmployeeCollection> getEmployees(@Header("Authorization") String accessToken);

    @FormUrlEncoded
    @POST("employee_api/set_to_employee")
    Call<ResponseBody> setEmployee(@Header("Authorization") String accessToken,
                                   @Field("id_user") int id);
    @FormUrlEncoded
    @POST("employee_api/set_to_user")
    Call<ResponseBody> setToUser(@Header("Authorization") String accessToken,
                                 @Field("id_user") int id);

    @FormUrlEncoded
    @POST("kos_api/store")
    Call<ResponseBody> createKamar(@Field("id_indekos") int idIndekos,
                                       @Field("no_kamar") int noKamar,
                                       @Field("lantai_ke") int lantaiKe,
                                       @Field("ukuran") int ukuran,
                                       @Field("harga") int harga,
                                       @Field("fasilitas") String fasilitas,
                                       @Field("kwh") int kwh);

    @FormUrlEncoded
    @POST("kos_api/update/{id}")
    Call<ResponseBody> updateKamar(@Path("id") int idKamar,
                                   @Field("no_kamar") int noKamar,
                                   @Field("lantai_ke") int lantaiKe,
                                   @Field("ukuran") int ukuran,
                                   @Field("harga") int harga,
                                   @Field("fasilitas") String fasilitas,
                                   @Field("kwh") int kwh);

    @FormUrlEncoded
    @POST("users_api/register")
    Call<ResponseBody> register(@Field("username") String username,
                                @Field("password") String password,
                                @Field("email") String email,
                                @Field("first_name") String firstName,
                                @Field("last_name") String lastName);
}
