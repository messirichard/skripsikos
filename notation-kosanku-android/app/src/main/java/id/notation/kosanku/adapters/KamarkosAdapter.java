package id.notation.kosanku.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.notation.kosanku.R;
import id.notation.kosanku.models.indekos.Indekos;
import id.notation.kosanku.models.kamarkos.Kamarkos;

public class KamarkosAdapter extends RecyclerView.Adapter<KamarkosAdapter.ViewHolder> {

    Context mContext;
    List<Kamarkos> kamarkosList;

    public KamarkosAdapter(Context mContext, List<Kamarkos> kamarkosList) {
        this.mContext = mContext;
        this.kamarkosList = kamarkosList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView noKamar;
        TextView hargaKamar;
        TextView statusKamar;

        public ViewHolder(View itemView) {
            super(itemView);

            noKamar = itemView.findViewById(R.id.noKamar);
            hargaKamar = itemView.findViewById(R.id.hargaKamar);
            statusKamar = itemView.findViewById(R.id.statusKamar);
        }
    }

    @Override
    public KamarkosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kamarkos_adapter_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Kamarkos item = kamarkosList.get(position);

        holder.noKamar.setText("Kamar " + String.valueOf(item.getNo_kamar()));
        holder.hargaKamar.setText("Rp "+ String.valueOf(item.getHarga()));

        switch (item.getStatus()) {
            case 0:
                holder.statusKamar.setText("Tersedia");
                break;
            case 1:
                holder.statusKamar.setText("Tidak Tersedia");
                break;
            case 2:
                holder.statusKamar.setText("Menunggu Pembayaran");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return kamarkosList.size();
    }
}
