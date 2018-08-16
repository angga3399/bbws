package unikom.skripsi.angga.petugas.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.model.MessageModel;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;

public class LupaPasswordActivity extends AppCompatActivity {
    Button btn_kirim;
    EditText txt_username;
    TextView txt_login;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        btn_kirim = findViewById(R.id.btn_kirim);
        txt_username = findViewById(R.id.txt_username);
        txt_login = findViewById(R.id.txt_login);

        btn_kirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 ) {
                    checkLupaPassword(username);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkLupaPassword(String username) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mengirim ke Email Anda");
        showDialog();
        Call<MessageModel> api = ApiClient.getClient().create(ApiInterface.class).postLupa(username);
        api.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response){
                if (response.isSuccessful()){
                    hideDialog();
                    Toast.makeText(LupaPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                hideDialog();
                Toast.makeText(LupaPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(View view) {
        Intent intent = new Intent(LupaPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
