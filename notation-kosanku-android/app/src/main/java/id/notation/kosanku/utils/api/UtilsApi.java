package id.notation.kosanku.utils.api;

import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.RetrofitClient;

/**
 * Created by alfredo on 4/8/2018.
 */

public class UtilsApi {

    // Pastikan IP Komputer dan letak direktori projek sama
//    public static final String BASE_URL_API = "http://192.168.43.171/notation-kosanku/";
    public static final String BASE_URL_API = "http://kosan.skripsii.net/";

    // Mendeklarasikan Interface BaseApiService
    public static ApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(ApiService.class);
    }
}
