package id.notation.kosanku.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import id.notation.kosanku.MainActivity;
import id.notation.kosanku.R;
import id.notation.kosanku.adapters.IndekosAdapter;
import id.notation.kosanku.adapters.KamarkosAdapter;
import id.notation.kosanku.indekos.IndekosDetailActivity;
import id.notation.kosanku.kamarkos.FormKamarkosActivity;
import id.notation.kosanku.kamarkos.KamarKosDetailActivity;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.models.indekos.IndekosCollection;
import id.notation.kosanku.models.kamarkos.Kamarkos;
import id.notation.kosanku.models.kamarkos.KamarkosCollection;
import id.notation.kosanku.utils.RecyclerItemClickListener;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class KamarKosFragment extends Fragment {

    private static final String TAG = "###";

    List<Indekos> listOfIndekos;
    List<Kamarkos> listOfKamarkos;

    SharedPreferenceManager sharedPreferenceManager;
    ApiService apiService;
    String indekosUserId;
    Spinner indekosSpinner;
    KamarkosAdapter kamarKosAdapter;
    RecyclerView rvKamarkos;

    public KamarKosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kamar_kos, container, false);

        // Ubah title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Kamar Kos");

        indekosSpinner = view.findViewById(R.id.indekosSpinner);

        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        apiService = UtilsApi.getAPIService();

        Log.d(TAG, "onCreateView: " + sharedPreferenceManager.getAppUserRole());
        Log.d(TAG, "onCreateView: ID Admin " + sharedPreferenceManager.getAppAdminId());

        if (sharedPreferenceManager.getAppUserRole().equals("Admin")) {
            indekosUserId = sharedPreferenceManager.getAppUserId();
        } else {
            indekosUserId = sharedPreferenceManager.getAppAdminId();
        }

        setSpinner();

        kamarKosAdapter = new KamarkosAdapter(getContext(), listOfKamarkos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvKamarkos = view.findViewById(R.id.kamarKosRV);
        rvKamarkos.setLayoutManager(mLayoutManager);
        rvKamarkos.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    public void setSpinner()
    {
        apiService.getIndekos(Integer.parseInt(indekosUserId)).enqueue(new Callback<IndekosCollection>() {
            @Override
            public void onResponse(Call<IndekosCollection> call, Response<IndekosCollection> response) {
                Log.d(TAG, "onResponse: " + response);

                listOfIndekos = response.body().getData();
                ArrayAdapter<Indekos> dataAdapter = new ArrayAdapter<Indekos>(getContext(), android.R.layout.simple_spinner_item, listOfIndekos);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                indekosSpinner.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<IndekosCollection> call, Throwable t) {

            }
        });

        indekosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + listOfIndekos.get(position).getId());
                populateRv(listOfIndekos.get(position).getId());
                Log.d(TAG, "onItemSelected: " + listOfIndekos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void populateRv(int id)
    {
        apiService.getKamarByIndekos(id).enqueue(new Callback<KamarkosCollection>() {
            @Override
            public void onResponse(Call<KamarkosCollection> call, Response<KamarkosCollection> response) {
                Log.d(TAG, "onResponse: " + response);

                if (response.code() == 200) {
                    listOfKamarkos = response.body().getData();
                    rvKamarkos.setAdapter(new KamarkosAdapter(getContext(), listOfKamarkos));
                    rvKamarkos.addOnItemTouchListener(
                            new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
                                    Kamarkos item = listOfKamarkos.get(position);


                                    Intent detailKamarKos = new Intent(getContext(), KamarKosDetailActivity.class);
                                    detailKamarKos.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    detailKamarKos.putExtra("kamarkos.id_kamar", item.getId_kamar());
                                    detailKamarKos.putExtra("kamarkos.id_indekos", item.getId_indekos());

                                    startActivity(detailKamarKos);
                                }
                            }));
                }
            }

            @Override
            public void onFailure(Call<KamarkosCollection> call, Throwable t) {

            }
        });
    }

}
