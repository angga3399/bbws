package unikom.skripsi.angga.petugas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.model.TmaModel;

public class TmaAdapter extends RecyclerView.Adapter<TmaAdapter.TmaViewHolder>{
    private Context context;
    private List<TmaModel> list;

    public TmaAdapter(Context context, List<TmaModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_tma, null);
        return new TmaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TmaViewHolder holder, int position) {
        TmaModel model = list.get(position);
        holder.textViewTangal.setText(model.getTanggal());
        holder.textViewJam.setText(model.getJam());
        holder.textViewTma.setText(model.getTma());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TmaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTangal;
        private TextView textViewJam;
        private TextView textViewTma;
        public TmaViewHolder(View itemView) {
            super(itemView);
            textViewTangal = itemView.findViewById(R.id.row_tma_tanggal);
            textViewJam = itemView.findViewById(R.id.row_tma_jam);
            textViewTma= itemView.findViewById(R.id.row_tma_tma);
        }
    }

    public void replaceData(List<TmaModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
