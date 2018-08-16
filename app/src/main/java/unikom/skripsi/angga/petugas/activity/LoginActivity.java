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
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.model.UserModel;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;
import unikom.skripsi.angga.petugas.util.SessionUtils;

public class LoginActivity extends AppCompatActivity  {

    ProgressDialog pDialog;
    Button btn_login;
    EditText txt_username, txt_password;
    TextView txt_lupa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btn_login = findViewById(R.id.btn_login);
        txt_lupa  = findViewById(R.id.txt_lupa);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);


        if (SessionUtils.isLoggedIn(this)){
            Intent intent = new Intent(this, UtamaActivity.class);
            startActivity(intent);
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                        checkLogin(username, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Ada field yang kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void lupa(View view) {
        Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
        startActivity(intent);
    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        Call<UserModel.UserDataModel> api = ApiClient.getClient().create(ApiInterface.class).postLoginPetugas(username, password);
        api.enqueue(new Callback<UserModel.UserDataModel>() {
            @Override
            public void onResponse(Call<UserModel.UserDataModel> call, retrofit2.Response<UserModel.UserDataModel> response) {
                hideDialog();
                if (response.isSuccessful()){
                    if (response.body().getMessage().equalsIgnoreCase("Berhasil")){
                        if (SessionUtils.login(LoginActivity.this, response.body().getResults().get(0))){
                            Intent intent = new Intent(LoginActivity.this, UtamaActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel.UserDataModel> call, Throwable t) {
                hideDialog();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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