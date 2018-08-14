package unikom.skripsi.angga.petugas.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.helper.Config;
import unikom.skripsi.angga.petugas.model.Notification;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;

public class ShowNotificationActivity extends AppCompatActivity {
    ProgressBar progress;
    TextView txtProgress, title, time, message;
    ImageView img;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);
        toolbar = findViewById(R.id.notif_toolbar);
        img = findViewById(R.id.showNotificationImage);
        title = findViewById(R.id.showNotificationTitle);
        time = findViewById(R.id.showNotificationTime);
        message = findViewById(R.id.showNotificationMessage);
        progress = findViewById(R.id.loadingProgress);
        txtProgress = findViewById(R.id.loadingText);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        img.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String id = getIntent().getStringExtra("id");
        Call<Notification> call = apiService.getNotification(id);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, final Response<Notification> response) {
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);

                title.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                message.setVisibility(View.VISIBLE);

                title.setText(response.body().getTitle());
                time.setText(response.body().getTimestamp());
                message.setText(response.body().getMessage());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReferenceFromUrl(Config.FIREBASE_URL);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    storageRef.child("foto-notifikasi/" + response.body().getImage_name()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            img.setVisibility(View.VISIBLE);
                            Picasso.get().load(uri).into(img);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(ShowNotificationActivity.this, "Gagal mendapatkan foto.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    mAuth.signInAnonymously().addOnSuccessListener(ShowNotificationActivity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            storageRef.child("foto-notifikasi/" + response.body().getImage_name()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    img.setVisibility(View.VISIBLE);
                                    Picasso.get().load(uri).into(img);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(ShowNotificationActivity.this, "Gagal mendapatkan foto.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(ShowNotificationActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(ShowNotificationActivity.this, "Gagal melakukan autentikasi firebase.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
                Toast.makeText(ShowNotificationActivity.this, "Koneksi terputus.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
