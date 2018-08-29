package id.notation.kosanku.kamarkos;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.notation.kosanku.R;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.models.kamarkos.Kamarkos;
import id.notation.kosanku.models.kamarkos.KamarkosItem;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KamarKosDetailActivity extends AppCompatActivity {

    FloatingActionButton qrFab, editFab;
    Context context;
    ApiService apiService;

    int id_kamar;
    int id_indekos;
    TextView noKamar, hargaKamar, fasilitasKamar, statusKamar, lantaiKe, kwhKamar, ukuran;
    Kamarkos kamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_kos_detail);

        context = this;
        Intent intent = getIntent();
        apiService = UtilsApi.getAPIService();

        noKamar = findViewById(R.id.noKamar);
        hargaKamar = findViewById(R.id.hargaKamar);
        fasilitasKamar = findViewById(R.id.fasilitasKamar);
        lantaiKe = findViewById(R.id.lantaiKe);
        kwhKamar = findViewById(R.id.kwhKamar);
        statusKamar = findViewById(R.id.statusKamar);
        ukuran = findViewById(R.id.ukuran);
        editFab = findViewById(R.id.fabEdit);

        id_kamar = intent.getIntExtra("kamarkos.id_kamar", 0);
        id_indekos = intent.getIntExtra("kamarkos.id_indekos", 0);

        getKamarDetail(id_kamar);

        qrFab = findViewById(R.id.showQR);

        qrFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KamarKosQRActivity.class);
                intent.putExtra("kamarkos.id_kamar", id_kamar);
                intent.putExtra("kamarkos.id_indekos", id_indekos);

                startActivity(intent);
            }
        });

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createKamarIntent = new Intent(getApplicationContext(), FormKamarkosActivity.class);
                createKamarIntent.putExtra("kamarkos.mode", "update");
                createKamarIntent.putExtra("indekos.id", kamar.getId_indekos());
                createKamarIntent.putExtra("kamarkos.id", kamar.getId_kamar());
                startActivity(createKamarIntent);
            }
        });

    }

    public void getKamarDetail(int id)
    {
        apiService.showKamar(id).enqueue(new Callback<KamarkosItem>() {
            @Override
            public void onResponse(Call<KamarkosItem> call, Response<KamarkosItem> response) {
                kamar = response.body().getData();
                noKamar.setText("Kamar " + kamar.getNo_kamar());
                hargaKamar.setText("Rp " + kamar.getHarga());
                lantaiKe.setText("Lantai ke " + kamar.getLantai_ke());
                kwhKamar.setText("Daya: " + kamar.getKwh() + "kwh");
                ukuran.setText(kamar.getUkuran() + " m2");
                fasilitasKamar.setText(kamar.getFasilitas());

                switch (kamar.getStatus()) {
                    case 0:
                        statusKamar.setText("Tersedia");
                        break;
                    case 1:
                        statusKamar.setText("Tidak Tersedia");
                        break;
                    case 2:
                        statusKamar.setText("Menunggu Pembayaran");
                        break;
                }

                if (kamar.getStatus() != 0) {
                    qrFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<KamarkosItem> call, Throwable t) {

            }
        });
    }
}
