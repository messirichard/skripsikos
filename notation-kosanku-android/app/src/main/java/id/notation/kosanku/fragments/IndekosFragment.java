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

import java.util.ArrayList;
import java.util.List;

import id.notation.kosanku.MainActivity;
import id.notation.kosanku.R;
import id.notation.kosanku.adapters.IndekosAdapter;
import id.notation.kosanku.indekos.FormIndekosActivity;
import id.notation.kosanku.indekos.IndekosDetailActivity;
import id.notation.kosanku.kamarkos.FormKamarkosActivity;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.models.indekos.IndekosCollection;
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
public class IndekosFragment extends Fragment {

    private static final String TAG = "Indekos Fragment";
    ApiService apiService;
    SharedPreferenceManager sharedPreferenceManager;
    IndekosAdapter indekosAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rvIndekos;
    List<Indekos> indekosList = new ArrayList<>();



    public IndekosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_indekos, container, false);

        // Ubah title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Indekos");

        FloatingActionButton fab = view.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FormIndekosActivity.class);
                intent.putExtra("indekos.mode", "add");
                startActivity(intent);
            }
        });

        sharedPreferenceManager = new SharedPreferenceManager(getContext());

        indekosAdapter = new IndekosAdapter(getContext(), indekosList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvIndekos = view.findViewById(R.id.indekosRV);
        rvIndekos.setLayoutManager(mLayoutManager);
        rvIndekos.setItemAnimator(new DefaultItemAnimator());

        populateIndekosRV();


        return view;
    }

    public void populateIndekosRV()
    {
        apiService = UtilsApi.getAPIService();

        String indekosUserId;

        if (sharedPreferenceManager.getAppUserRole() == "Admin" ) {
            indekosUserId = sharedPreferenceManager.getAppUserId();
        } else {
            indekosUserId = sharedPreferenceManager.getAppAdminId();
        }

        Log.d(TAG, "populateIndekosRV: " + indekosUserId);

        apiService.getIndekos(Integer.parseInt(indekosUserId))
                .enqueue(new Callback<IndekosCollection>() {
                    @Override
                    public void onResponse(Call<IndekosCollection> call, Response<IndekosCollection> response) {
                        Log.d(TAG, "onResponse: " + response);

                        if (response.code() == 200) {
                            final List<Indekos> indekosList = response.body().getData();
                            rvIndekos.setAdapter(new IndekosAdapter(getContext(), indekosList));
                            indekosAdapter.notifyDataSetChanged();

                            addOnItemTouchListenerSupport(indekosList);
                        }

                    }

                    @Override
                    public void onFailure(Call<IndekosCollection> call, Throwable t) {

                    }
                });

    }

    private void addOnItemTouchListenerSupport(final List<Indekos> indekosList) {
        rvIndekos.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int id = indekosList.get(position).getId_indekos();

                        Intent detailIndekos = new Intent(getContext(), IndekosDetailActivity.class);
                        detailIndekos.putExtra("indekos.id", id);

                        startActivity(detailIndekos);
                    }
                }));
    }

}
