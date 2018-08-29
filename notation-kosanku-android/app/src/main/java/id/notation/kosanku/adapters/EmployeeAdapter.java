package id.notation.kosanku.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.notation.kosanku.R;
import id.notation.kosanku.models.employee.Employee;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    Context mContext;
    SharedPreferenceManager sharedPreferenceManager;
    ApiService apiService;
    List<Employee> employeeList;

    public EmployeeAdapter(Context mContext, List<Employee> employeeList) {
        this.apiService = UtilsApi.getAPIService();
        this.sharedPreferenceManager = new SharedPreferenceManager(mContext);
        this.mContext = mContext;
        this.employeeList = employeeList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView employeeName;
        TextView employeeEmail;
        ImageView deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            employeeName = itemView.findViewById(R.id.employeeName);
            employeeEmail = itemView.findViewById(R.id.employeeEmail);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_adapter_item, parent, false);

        return new EmployeeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Employee item = employeeList.get(position);


        Log.d("###", "onBindViewHolder: " + item.getUser().getFirst_name());

        holder.employeeName.setText(item.getUser().getFirst_name() + " " + item.getUser().getLast_name());
        holder.employeeEmail.setText(item.getUser().getEmail());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.setToUser("Bearer "+ sharedPreferenceManager.getAppAccessToken(),
                                    item.getUser().getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            removeItem(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void removeItem(int position) {
        employeeList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

}
