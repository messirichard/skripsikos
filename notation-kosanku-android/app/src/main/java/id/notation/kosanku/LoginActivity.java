package id.notation.kosanku;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.notation.kosanku.R;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiService apiApiService;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Context context;
    SharedPreferenceManager sharedPreferenceManager;
    TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        sharedPreferenceManager = new SharedPreferenceManager(this);

        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.azure_primary_dark));


        apiApiService = UtilsApi.getAPIService();
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*usernameEditText.setText("administrator");
                passwordEditText.setText("password");*/
                doLogin();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });

    }

    private void doLogin() {
        apiApiService.loginRequest(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject results = new JSONObject(response.body().string());
                                Log.d("##!", "onResponse: " + results);

                                if (results.getInt("status") == 200) {
                                    Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    JSONObject user = results.getJSONObject("data");

                                    if (user.has("id_admin")) {
                                        Log.d("ID ADMIN", "onResponse: " + user.getString("id_admin"));
                                        sharedPreferenceManager.saveString(SharedPreferenceManager.APP_ADMIN_ID, user.getString("id_admin"));
                                    }

                                    if (user.has("id_indekos")) {
                                        sharedPreferenceManager.saveString(SharedPreferenceManager.APP_INDEKOS_ID, user.getString("id_indekos"));
                                    }

                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_USER_ID, user.getString("user_id"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_ACCESS_TOKEN, user.getString("access_token"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_EMAIL, user.getString("email"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_USERNAME, user.getString("username"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_FIRST_NAME, user.getString("first_name"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_LAST_NAME, user.getString("last_name"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_NIK, user.getString("nik"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_PEKERJAAN, user.getString("pekerjaan"));
                                    sharedPreferenceManager.saveString(SharedPreferenceManager.APP_USER_ROLE, user.getString("group_id"));
                                    sharedPreferenceManager.saveBoolean(SharedPreferenceManager.APP_LOGIN_STATE, true);

                                    startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();

                                } else {
                                    Toast.makeText(context, "Login Gagal!", Toast.LENGTH_SHORT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
}
