package id.notation.kosanku;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView loginLink;
    EditText usernameET, emailET, passwordET, passwordConfirmationET, firstNameET, lastNameET;
    Button registerButton;
    Context mContext;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        usernameET = findViewById(R.id.usernameEditText);
        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        passwordConfirmationET = findViewById(R.id.passwordConfirmationEditText);
        registerButton = findViewById(R.id.registerButton);

        apiService = UtilsApi.getAPIService();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                if (!TextUtils.equals(passwordET.getText(), passwordConfirmationET.getText())) {
                    error = true;
                    passwordET.setError("Password harus sama");
                    passwordConfirmationET.setError("Password harus sama");
                }

                if (TextUtils.isEmpty(passwordET.getText())) {
                    error = true;
                    passwordET.setError("Email harus diisi");
                }

                if (TextUtils.isEmpty(emailET.getText())) {
                    error = true;
                    emailET.setError("Email harus diisi");
                }

                if (TextUtils.isEmpty(usernameET.getText())) {
                    error = true;
                    usernameET.setError("Username harus diisi");
                }

                if (TextUtils.isEmpty(firstNameET.getText())) {
                    error = true;
                    firstNameET.setError("Nama depan harus diisi");
                }

                if (!error) {
                    apiService.register(
                            usernameET.getText().toString(),
                            passwordET.getText().toString(),
                            emailET.getText().toString(),
                            firstNameET.getText().toString(),
                            lastNameET.getText().toString()
                    ).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(mContext, "Anda berhasil membuat akun, silahkan login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });

        loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
