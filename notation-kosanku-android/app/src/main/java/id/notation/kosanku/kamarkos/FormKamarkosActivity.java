package id.notation.kosanku.kamarkos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.notation.kosanku.MainActivity;
import id.notation.kosanku.R;
import id.notation.kosanku.indekos.IndekosDetailActivity;
import id.notation.kosanku.models.kamarkos.Kamarkos;
import id.notation.kosanku.models.kamarkos.KamarkosItem;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormKamarkosActivity extends AppCompatActivity {

    ApiService apiService;
    EditText nomorET, lantaiET, ukuranET, kwhET, fasilitasET, hargaET;
    Button submitButton;
    String mode;
    SharedPreferenceManager sharedPreferenceManager;
    int indekosId, kamarKosId;
    Kamarkos kamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_kamarkos);

        nomorET = findViewById(R.id.nomorKamarET);
        lantaiET = findViewById(R.id.lantaiKeET);
        ukuranET = findViewById(R.id.ukuranET);
        kwhET = findViewById(R.id.kwhET);
        fasilitasET = findViewById(R.id.fasilitasET);
        hargaET = findViewById(R.id.hargaET);
        submitButton = findViewById(R.id.submitButton);

        apiService = UtilsApi.getAPIService();

        Intent intent = getIntent();
        mode = intent.getStringExtra("kamarkos.mode");
        indekosId = intent.getIntExtra("indekos.id", 0);
        kamarKosId = intent.getIntExtra("kamarkos.id", 0);

        switch (mode) {
            case "add":
                getSupportActionBar().setTitle("Add Kamar Kos");
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(nomorET.getText()) ||
                                TextUtils.isEmpty(lantaiET.getText()) ||
                                TextUtils.isEmpty(ukuranET.getText()) ||
                                TextUtils.isEmpty(kwhET.getText()) ||
                                TextUtils.isEmpty(fasilitasET.getText()) ||
                                TextUtils.isEmpty(hargaET.getText())) {
                            Toast.makeText(getApplicationContext(), "Isi semua data", Toast.LENGTH_SHORT).show();
                        } else {
                            storeKamarKos();
                        }
                    }
                });
                break;
            case "update":
                getSupportActionBar().setTitle("Edit Kamar Kos");

                getKamarDetail(kamarKosId);

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateKamarKos();
                    }
                });

                break;
        }
    }

    public void storeKamarKos()
    {
        apiService.createKamar(indekosId,
                Integer.parseInt(nomorET.getText().toString()),
                Integer.parseInt(lantaiET.getText().toString()),
                Integer.parseInt(ukuranET.getText().toString()),
                Integer.parseInt(hargaET.getText().toString()),
                fasilitasET.getText().toString(),
                Integer.parseInt(kwhET.getText().toString()))
        .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Kamar berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), IndekosDetailActivity.class);
                    intent.putExtra("indekos.id", indekosId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void updateKamarKos()
    {
        apiService.updateKamar(kamar.getId_kamar(),
                Integer.parseInt(nomorET.getText().toString()),
                Integer.parseInt(lantaiET.getText().toString()),
                Integer.parseInt(ukuranET.getText().toString()),
                Integer.parseInt(hargaET.getText().toString()),
                fasilitasET.getText().toString(),
                Integer.parseInt(kwhET.getText().toString()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getApplicationContext(), "Kamar berhasil diubah", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), KamarKosDetailActivity.class);
                            intent.putExtra("kamarkos.id_kamar", kamar.getId_kamar());
                            intent.putExtra("kamarkos.id_indekos", kamar.getId_indekos());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }


    public void getKamarDetail(int id)
    {
        apiService.showKamar(id).enqueue(new Callback<KamarkosItem>() {
            @Override
            public void onResponse(Call<KamarkosItem> call, Response<KamarkosItem> response) {
                kamar = response.body().getData();
                nomorET.setText(String.valueOf(kamar.getNo_kamar()));
                hargaET.setText(String.valueOf(kamar.getHarga()));
                lantaiET.setText(String.valueOf(kamar.getLantai_ke()));
                ukuranET.setText(String.valueOf(kamar.getUkuran()));
                kwhET.setText(String.valueOf(kamar.getKwh()));
                fasilitasET.setText(kamar.getFasilitas());
            }

            @Override
            public void onFailure(Call<KamarkosItem> call, Throwable t) {

            }
        });
    }
}
