package unikom.skripsi.angga.petugas.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.helper.Config;
import unikom.skripsi.angga.petugas.helper.FilePath;
import unikom.skripsi.angga.petugas.util.SessionUtils;

public class PushNotificationActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(Config.FIREBASE_URL);
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;

    private RadioGroup radioGroup;
    private RadioButton radioButtonSiaga;
    private RadioButton radioButtonSiap;
    private RadioButton radioButtonAncur;

    String status;

    private Toolbar toolbar;

    private float latitude;
    private float longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        toolbar = findViewById(R.id.pushnotif_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kirim Notif Banjir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView img = findViewById(R.id.notificationImage);
        radioGroup = findViewById(R.id.pushnotif_radiogroup);
        radioButtonSiaga = findViewById(R.id.pushnotif_radiobutton_siaga);
        radioButtonSiap = findViewById(R.id.pushnotif_radiobutton_siap);
        radioButtonAncur = findViewById(R.id.pushnotif_radiobutton_awas);
        final EditText message = findViewById(R.id.notificationMessage);

        status = radioButtonSiaga.getText().toString();


        setLocation();

        Button btnSend = findViewById(R.id.notificationSend);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButtonSiaga.isChecked()) {
                    status = radioButtonSiaga.getText().toString();
                } else if (radioButtonSiap.isChecked()) {
                    status = radioButtonSiap.getText().toString();
                } else {
                    status = radioButtonAncur.getText().toString();
                }
            }
        });


        final Uri file = Uri.fromFile(new File(FilePath.getPathFile()));
        Picasso.get().load(file).into(img);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(status, message.getText().toString());
            }
        });

    }

    private void setLocation() {
        try {
            ExifInterface exifInterface = new ExifInterface(FilePath.getPathFile());
            float[] latLong = new float[2];
            if (exifInterface.getLatLong(latLong)) {
                latitude = latLong[0];
                longitude = latLong[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(final String title, final String message) {
        if (mAuth.getCurrentUser() != null) {
            uploadToFirebase(title, message);
        } else {
            mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    uploadToFirebase(title, message);
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(PushNotificationActivity.this, "Gagal melakukan autentikasi firebase.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void uploadToFirebase(final String title, final String message) {
        final Uri file = Uri.fromFile(new File(FilePath.getPathFile()));
        final StorageReference riversRef = storageRef.child("foto-notifikasi/" + file.getLastPathSegment());

        UploadTask uploadTask = riversRef.putFile(file);
        progressDialog = new ProgressDialog(PushNotificationActivity.this);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressLint("DefaultLocale") String progress = String.format("%.2f", (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                progressDialog.setTitle("Mengirim Notifikasi");
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Mengunggah foto: " + progress + " %");
                progressDialog.show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(PushNotificationActivity.this, "Berhenti mengunggah foto.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(PushNotificationActivity.this, "Gagal mengunggah foto.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setMessage("Mengirim notifikasi...");
                progressDialog.setCancelable(false);
                RequestQueue queue = Volley.newRequestQueue(PushNotificationActivity.this);
                StringRequest strreq = new StringRequest(Request.Method.POST,
                        Config.API_URL + "pushNotification.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String Response) {
                                progressDialog.dismiss();
                                Toast.makeText(PushNotificationActivity.this, "Notifikasi telah dikirim.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(PushNotificationActivity.this, UtamaActivity.class);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }) {
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("idUser", SessionUtils.getLoggedUser(PushNotificationActivity.this).getUser_id());
                        params.put("title", title);
                        params.put("message", message);
                        params.put("image", file.getLastPathSegment());
                        params.put("lat", String.valueOf(latitude));
                        params.put("lng", String.valueOf(longitude));
                        return params;
                    }
                };
                queue.add(strreq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        File file = new File(FilePath.getPathFile());
        file.delete();
        FilePath.setPathFile(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}