package unikom.skripsi.angga.petugas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private List<Notification> notifications;
    private Context context;
    private Listener listener;

    public NotificationAdapter(List<Notification> notifications, Context context, Listener listener) {
        this.notifications = notifications;
        this.context = context;
        this.listener = listener;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout listNotification;
        TextView title;
        TextView message;
        TextView timestamp;
        NotificationViewHolder(View v) {
            super(v);
            listNotification = v.findViewById(R.id.notification_layout);
            title = v.findViewById(R.id.viewTitle);
            message = v.findViewById(R.id.viewMessage);
            timestamp = v.findViewById(R.id.viewTimestamp);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.clickNotif(notifications.get(getAdapterPosition()).getMessage_id());
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.title.setText(notifications.get(position).getTitle());
        holder.timestamp.setText(notifications.get(position).getTimestamp());
        holder.message.setText(notifications.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public interface Listener{
        void clickNotif(String id);
    }

    public void replaceData(List<Notification> list){
        notifications = list;
        notifyDataSetChanged();
    }
}
