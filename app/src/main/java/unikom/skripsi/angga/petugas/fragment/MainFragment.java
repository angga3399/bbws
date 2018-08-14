package unikom.skripsi.angga.petugas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.activity.ShowNotificationActivity;
import unikom.skripsi.angga.petugas.adapter.NotificationAdapter;
import unikom.skripsi.angga.petugas.model.Notification;
import unikom.skripsi.angga.petugas.model.NotificationResponse;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;

public class MainFragment extends Fragment implements NotificationAdapter.Listener {

    ProgressBar progress;
    TextView txtProgress;
    NotificationAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        progress = view.findViewById(R.id.loadingProgress);
        txtProgress = view.findViewById(R.id.loadingText);
        recyclerView = view.findViewById(R.id.notification_recycler_view);
        setupRecyclerView();
        loadNotification();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NotificationAdapter(new ArrayList<Notification>(), getActivity(), this);
        recyclerView.setAdapter(adapter);
    }

    private void loadNotification() {
        progress.setVisibility(View.VISIBLE);
        txtProgress.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NotificationResponse> call = apiService.getAllNotifications();
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if(response.body().getResults() != null){
                      adapter.replaceData(response.body().getResults());
                    }
                }
            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal mengambil data dari server.", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void clickNotif(String id) {
        Intent intent = new Intent(getActivity(), ShowNotificationActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
