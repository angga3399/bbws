package unikom.skripsi.angga.petugas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.activity.PushNotificationActivity;
import unikom.skripsi.angga.petugas.activity.TmaActivity;
import unikom.skripsi.angga.petugas.adapter.TmaAdapter;
import unikom.skripsi.angga.petugas.model.TmaModel;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;
import unikom.skripsi.angga.petugas.util.SessionUtils;

public class TMAFragment extends Fragment {
    private RecyclerView recyclerView;
    private TmaAdapter tmaAdapter;


    public TMAFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tma,
                container, false);
        FloatingActionButton button = view.findViewById(R.id.btn_tambah_tma);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), TmaActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_tma);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        tmaAdapter = new TmaAdapter(getActivity(),new ArrayList<TmaModel>());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tmaAdapter);

        loadTma();
    }

    private void loadTma() {
        Call<TmaModel.TmaDataModel> api = ApiClient.getClient().create(ApiInterface.class).getTMA(SessionUtils.getLoggedUser(getActivity()).getUser_id());
        api.enqueue(new Callback<TmaModel.TmaDataModel>() {
            @Override
            public void onResponse(Call<TmaModel.TmaDataModel> call, Response<TmaModel.TmaDataModel> response) {
                if (response.isSuccessful()){
                    if(response.body().getMessage().equalsIgnoreCase("Berhasil")) {
                        tmaAdapter.replaceData(response.body().getResults());
                    }else{
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TmaModel.TmaDataModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
