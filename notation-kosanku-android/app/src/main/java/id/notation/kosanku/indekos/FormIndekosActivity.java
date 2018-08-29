package id.notation.kosanku.indekos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import gun0912.tedbottompicker.TedBottomPicker;
import id.notation.kosanku.MainActivity;
import id.notation.kosanku.R;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.models.indekos.IndekosItem;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormIndekosActivity extends AppCompatActivity {

    static String TAG = "FormIndekosActivity";

    String mode;
    Intent intent;
    Context context;
    Spinner genderSpinner;
    Uri imageUri;
    TedBottomPicker tedBottomPicker;
    ImageView indekosImageView;
    File indekosImage = null;
    ApiService apiService;
    SharedPreferenceManager sharedPreferenceManager;
    EditText indekosName, indekosAlamat, indekosKota, indekosFasilitasUmum;
    Button submitButton;
    Indekos indekos;
    int indekosId;
    ArrayAdapter<CharSequence> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_indekos);

        indekosId = getIntent().getIntExtra("indekos.id", 0);

        sharedPreferenceManager = new SharedPreferenceManager(this);

        indekosImageView = findViewById(R.id.indekosImageView);
        indekosName = findViewById(R.id.indekosNama);
        indekosAlamat = findViewById(R.id.indekosAlamat);
        indekosFasilitasUmum = findViewById(R.id.indekosFasilitas);
        indekosKota = findViewById(R.id.indekosKota);
        submitButton = findViewById(R.id.submitButton);
        tedBottomPicker = new TedBottomPicker.Builder(FormIndekosActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        indekosImage = new File(uri.getPath());
                        indekosImageView.setImageURI(uri);
                        indekosImageView.setVisibility(View.VISIBLE);
                        findViewById(R.id.imagePlaceholder).setVisibility(View.GONE);
                    }
                })
                .create();

        context = this;
        intent = getIntent();
        mode = intent.getStringExtra("indekos.mode");


        genderSpinner = findViewById(R.id.gender);
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);

        findViewById(R.id.imagePlaceholder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tedBottomPicker.show(getSupportFragmentManager());
            }
        });

        indekosImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tedBottomPicker.show(getSupportFragmentManager());
            }
        });

        apiService = UtilsApi.getAPIService();

        Log.d(TAG, "onCreate: " + mode.equals("update"));
        if (mode.equals("update")) {
            getSupportActionBar().setTitle("Edit Indekos");

            indekosImageView.setVisibility(View.VISIBLE);
            findViewById(R.id.imagePlaceholder).setVisibility(View.GONE);
            getIndekosData(indekosId);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndekos(indekosId);
                }
            });

        } else {
            getSupportActionBar().setTitle("Add Indekos");

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeIndekos();
                }
            });
        }

        Log.d(TAG, "onCreate: " + mode);
    }

    public void storeIndekos() {
        String accessToken = "Bearer " + sharedPreferenceManager.getAppAccessToken();

        if (indekosImage != null) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), indekosImage);
            MultipartBody.Part indekosImagePart = MultipartBody.Part.createFormData("foto", indekosImage.getName(), reqFile);
            apiService.createIndekos(accessToken,
                    indekosName.getText().toString(),
                    indekosAlamat.getText().toString(),
                    genderSpinner.getSelectedItem().toString(),
                    indekosKota.getText().toString(),
                    indekosFasilitasUmum.getText().toString(),
                    indekosImagePart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: " + response);
                    try {
                        JSONObject result = new JSONObject(response.body().string());

                        if (result.getInt("status") == 200) {
                            Toast.makeText(getApplicationContext(), "Berhasil ditambahkan", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("mainactivity.page", "indekos");
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Foto harus diisi", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateIndekos(int id) {
        String accessToken = "Bearer " + sharedPreferenceManager.getAppAccessToken();

        if (indekosImage != null) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), indekosImage);
            MultipartBody.Part indekosImagePart = MultipartBody.Part.createFormData("foto", indekosImage.getName(), reqFile);
            apiService.updateIndekos(accessToken,
                    id,
                    indekosName.getText().toString(),
                    indekosAlamat.getText().toString(),
                    genderSpinner.getSelectedItem().toString(),
                    indekosKota.getText().toString(),
                    indekosFasilitasUmum.getText().toString(),
                    indekosImagePart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), "Berhasil diubah", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("mainactivity.page", "indekos");
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            apiService.updateIndekos(accessToken,
                    id,
                    indekosName.getText().toString(),
                    indekosAlamat.getText().toString(),
                    genderSpinner.getSelectedItem().toString(),
                    indekosKota.getText().toString(),
                    indekosFasilitasUmum.getText().toString(),
                    null).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: " + response);

                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), "Berhasil diubah", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("mainactivity.page", "indekos");
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void getIndekosData(int id) {
        apiService.showIndekos(id).enqueue(new Callback<IndekosItem>() {
            @Override
            public void onResponse(Call<IndekosItem> call, Response<IndekosItem> response) {
                indekos = response.body().getData();
                Log.d(TAG, "onResponse: " + response);

                indekosName.setText(indekos.getNama());
                indekosAlamat.setText(indekos.getAlamat());
                indekosFasilitasUmum.setText(indekos.getFasilitas_umum());
                indekosKota.setText(indekos.getKota());
                genderSpinner.setSelection(arrayAdapter.getPosition(indekos.getGender()));

                String imageUrl = UtilsApi.BASE_URL_API + indekos.getFoto();
                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .fitCenter()
                        .into(indekosImageView);
            }

            @Override
            public void onFailure(Call<IndekosItem> call, Throwable t) {

            }
        });
    }
}
