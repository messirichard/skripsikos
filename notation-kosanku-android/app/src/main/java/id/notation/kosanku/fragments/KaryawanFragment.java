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
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import id.notation.kosanku.MainActivity;
import id.notation.kosanku.R;
import id.notation.kosanku.adapters.EmployeeAdapter;
import id.notation.kosanku.models.employee.Employee;
import id.notation.kosanku.models.employee.EmployeeCollection;
import id.notation.kosanku.models.kamarkos.Kamarkos;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class KaryawanFragment extends Fragment implements View.OnClickListener{


    View view;
    SharedPreferenceManager sharedPreferenceManager;
    FloatingActionButton fab;
    Intent intent;
    IntentIntegrator qrScan;
    ApiService apiService;
    EmployeeAdapter employeeAdapter;
    RecyclerView karyawanRV;
    List<Employee> employeeList = new ArrayList<>();


    public KaryawanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_karyawan, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Karyawan");

        apiService = UtilsApi.getAPIService();

        qrScan = IntentIntegrator.forSupportFragment(getActivity().getSupportFragmentManager().findFragmentById(R.id.main_container));
        qrScan.setOrientationLocked(true);

        sharedPreferenceManager = new SharedPreferenceManager(getActivity());

        fab = view.findViewById(R.id.fab);

        if (!sharedPreferenceManager.getAppUserRole().equals("Admin"))
        {
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(this);

        karyawanRV = view.findViewById(R.id.karyawanRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        karyawanRV.setLayoutManager(layoutManager);
        karyawanRV.setItemAnimator(new DefaultItemAnimator());

        populateEmployee();

        return view;
    }

    public void populateEmployee()
    {
        apiService.getEmployees("Bearer " + sharedPreferenceManager.getAppAccessToken())
                .enqueue(new Callback<EmployeeCollection>() {
                    @Override
                    public void onResponse(Call<EmployeeCollection> call, Response<EmployeeCollection> response) {
                        Log.d(TAG, "onResponse: " + response.body().getData());


                        employeeList = response.body().getData();
                        employeeAdapter = new EmployeeAdapter(getContext(), employeeList);
                        karyawanRV.setAdapter(employeeAdapter);
                        employeeAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<EmployeeCollection> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                Log.d(TAG, "onClick:  tes ");
                qrScan.initiateScan();
                //intent = new Intent(getActivity(), ScannerActivity.class);
                //intent.putExtra(ScannerActivity.TYPE, 1);
                //startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "onActivityResult: " + result.getContents());
                Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                addEmployee(Integer.parseInt(result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addEmployee(int userId)
    {
        apiService.setEmployee("Bearer " + sharedPreferenceManager.getAppAccessToken(), userId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Karyawan Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Karyawan Gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
}
