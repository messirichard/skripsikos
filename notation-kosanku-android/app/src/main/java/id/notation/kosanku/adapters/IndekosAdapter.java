package id.notation.kosanku.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.notation.kosanku.R;
import id.notation.kosanku.models.indekos.Indekos;

public class IndekosAdapter extends RecyclerView.Adapter<IndekosAdapter.ViewHolder>{

    List<Indekos> indekosList;
    Context mContext;

    public IndekosAdapter(Context context, List<Indekos> indekosList) {
        this.mContext = context;
        this.indekosList = indekosList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView indekosName;
        TextView indekosAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            indekosName = itemView.findViewById(R.id.indekosName);
            indekosAddress = itemView.findViewById(R.id.indekosAddress);
        }
    }

    @Override
    public IndekosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.indekos_adapter_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IndekosAdapter.ViewHolder holder, int position) {
        final Indekos item = indekosList.get(position);

        holder.indekosName.setText(item.getNama());
        holder.indekosAddress.setText(item.getAlamat());
    }

    @Override
    public int getItemCount() {
        return indekosList.size();
    }
}
