package id.notation.kosanku.indekos;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.notation.kosanku.R;
import id.notation.kosanku.kamarkos.FormKamarkosActivity;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.models.indekos.IndekosItem;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndekosDetailActivity extends AppCompatActivity {

    ApiService apiService;
    TextView indekosNama, indekosGender, indekosKota, indekosAddress, fasilitasUmum;
    ImageView indekosThumbnail;
    FloatingActionButton fabEdit;
    int indekosId;
    Indekos indekos;
    Button buttonCreateKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indekos_detail);

        apiService = UtilsApi.getAPIService();

        Intent intent = getIntent();
        indekosId = intent.getIntExtra("indekos.id", 0);

        indekosNama = findViewById(R.id.indekosNama);
        indekosGender = findViewById(R.id.indekosGender);
        indekosKota = findViewById(R.id.indekosKota);
        indekosAddress = findViewById(R.id.indekosAddress);
        fasilitasUmum = findViewById(R.id.fasilitasUmum);
        fabEdit = findViewById(R.id.fabEdit);
        buttonCreateKamar = findViewById(R.id.createKamar);

        indekosThumbnail = findViewById(R.id.indekosImageThumb);

        getIndekosData(indekosId);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), FormIndekosActivity.class);
                intent1.putExtra("indekos.mode", "update");
                intent1.putExtra("indekos.id", indekosId);
                startActivity(intent1);
            }
        });

        buttonCreateKamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createKamarIntent = new Intent(getApplicationContext(), FormKamarkosActivity.class);
                createKamarIntent.putExtra("kamarkos.mode", "add");
                createKamarIntent.putExtra("indekos.id", indekosId);
                startActivity(createKamarIntent);
            }
        });
    }

    public void getIndekosData(int id)
    {
        apiService.showIndekos(id).enqueue(new Callback<IndekosItem>() {
            @Override
            public void onResponse(Call<IndekosItem> call, Response<IndekosItem> response) {
                indekos = response.body().getData();
                setIndekosDetail();

            }

            @Override
            public void onFailure(Call<IndekosItem> call, Throwable t) {

            }
        });
    }

    public void setIndekosDetail()
    {
        indekosNama.setText(indekos.getNama());
        indekosGender.setText(indekos.getGender());
        indekosKota.setText(indekos.getKota());
        indekosAddress.setText(indekos.getAlamat());
        fasilitasUmum.setText(indekos.getFasilitas_umum());

        String imageUrl = UtilsApi.BASE_URL_API + indekos.getFoto();
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .fitCenter()
                .into(indekosThumbnail);
    }
}
