package unikom.skripsi.angga.petugas.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.model.MessageModel;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;
import unikom.skripsi.angga.petugas.util.SessionUtils;

public class TmaActivity extends AppCompatActivity  implements View.OnClickListener{

    ProgressBar progress;
    TextView txtProgress;
    Toolbar toolbar;
    private Button btntanggal, btnwaktu, btn_tma;
    private LinearLayout parent1;
    private EditText txttanggal, txtwaktu, txttma;
    ProgressDialog progressDialog;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tma);
        toolbar = findViewById(R.id.notif_toolbar);
        progress = findViewById(R.id.loadingProgress);
        txtProgress = findViewById(R.id.loadingText);
        btntanggal =  findViewById(R.id.btntanggal);
        btnwaktu =  findViewById(R.id.btnwaktu);
        btn_tma =  findViewById(R.id.btn_tma);

        txttanggal = findViewById(R.id.txttanggal);
        txtwaktu =  findViewById(R.id.txtwaktu);
        txttma = findViewById(R.id.txttma);
        parent1 = findViewById(R.id.parent1);

        btntanggal.setOnClickListener(this);
        btnwaktu.setOnClickListener( this);
        btn_tma.setOnClickListener( this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pencatatan TMA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btntanggal:

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txttanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.btnwaktu:

                c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                txtwaktu.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.btn_tma:

                String tanggal = txttanggal.getText().toString();
                String jam = txtwaktu.getText().toString();
                String tma = txttma.getText().toString();

                // mengecek kolom yang kosong
                if (tanggal.length() > 0 && jam.trim().length() > 0 && tma.length() > 0) {
                    postTMA(tanggal, jam, Double.parseDouble(tma));
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void postTMA(final String tanggal, final String jam, final Double tma) {
        progressDialog = ProgressDialog.show(this, "", "Menyimpan...", true, false);
        Call<MessageModel> api = ApiClient.getClient().create(ApiInterface.class).postTMA(
                SessionUtils.getLoggedUser(TmaActivity.this).getUser_id(), tanggal,jam,tma);
        api.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    Toast.makeText(TmaActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TmaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
