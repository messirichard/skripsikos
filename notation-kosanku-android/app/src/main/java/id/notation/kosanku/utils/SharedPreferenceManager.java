package id.notation.kosanku.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

/**
 * Created by alfredo on 4/8/2018.
 */

public class SharedPreferenceManager {

    public static final String APP_NAME = "Kosan";
    public static final String APP_USER_ID = "AppUserID";
    public static final String APP_ADMIN_ID = "AppAdminID";
    public static final String APP_INDEKOS_ID = "AppIndekosID";

    public static final String APP_USERNAME = "AppUsername";
    public static final String APP_FIRST_NAME = "AppFirstName";
    public static final String APP_LAST_NAME = "AppLastName";
    public static final String APP_NIK = "AppNIK";
    public static final String APP_PEKERJAAN = "AppPekerjaan";
    public static final String APP_EMAIL = "AppEmail";
    public static final String APP_ACCESS_TOKEN = "AppAccessToken";
    public static final String APP_USER_ROLE = "AppUserRole";

    public static final String APP_LOGIN_STATE = "AppLoginState";

    SharedPreferences sharedPreference;
    SharedPreferences.Editor sharedPreferenceEditor;

    public SharedPreferenceManager(Context context){
        sharedPreference = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreference.edit();
    }

    public void saveString(String key, String value){
        sharedPreferenceEditor.putString(key, value);
        sharedPreferenceEditor.commit();
    }

    public void saveInt(String key, int value){
        sharedPreferenceEditor.putInt(key, value);
        sharedPreferenceEditor.commit();
    }

    public void saveBoolean(String key, boolean value){
        sharedPreferenceEditor.putBoolean(key, value);
        sharedPreferenceEditor.commit();
    }

    public void saveFloat(String key, float value){
        sharedPreferenceEditor.putFloat(key, value);
        sharedPreferenceEditor.commit();
    }

    public String getAppUsername(){
        return sharedPreference.getString(APP_USERNAME, "");
    }

    public String getAppEmail(){
        return sharedPreference.getString(APP_EMAIL, "");
    }

    public Boolean getLoginState(){
        return sharedPreference.getBoolean(APP_LOGIN_STATE, false);
    }

    public String getAppAccessToken() {
        return sharedPreference.getString(APP_ACCESS_TOKEN, "");
    }

    public String getAppFirstName() {
        return sharedPreference.getString(APP_FIRST_NAME, "");
    }

    public String getAppLastName() {
        return sharedPreference.getString(APP_LAST_NAME, "");
    }

    public String getAppNik() {
        return sharedPreference.getString(APP_NIK, "");
    }

    public String getAppPekerjaan() {
        return sharedPreference.getString(APP_PEKERJAAN, "");
    }

    public String getAppUserRole() {
        String role = "";
        switch( sharedPreference.getString(APP_USER_ROLE, "")) {
            case "1":
                role = "Admin";
                break;
            case "2":
                role = "Karyawan";
                break;
            case "3":
                role = "User";
                break;
            default:
                role = "";
                break;
        }
        return role;
    }

    public String getAppUserId() {
        return sharedPreference.getString(APP_USER_ID, "");
    }

    public String getAppAdminId() {
        return sharedPreference.getString(APP_ADMIN_ID, "");
    }

    public String getAppIndekosId() {
        return sharedPreference.getString(APP_INDEKOS_ID, "");
    }


    public void clear() {
        sharedPreference.edit().clear().commit();
    }
}