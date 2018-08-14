package unikom.skripsi.angga.petugas.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        btn_kirim = findViewById(R.id.btn_kirim);
        txt_username = findViewById(R.id.txt_username);

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
        Call<MessageModel> api = ApiClient.getClient().create(ApiInterface.class).postLupa(username);
        api.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response){
                if (response.isSuccessful()){
                    Toast.makeText(LupaPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(LupaPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
