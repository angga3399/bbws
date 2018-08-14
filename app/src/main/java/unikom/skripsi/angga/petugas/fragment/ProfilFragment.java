package unikom.skripsi.angga.petugas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.activity.LoginActivity;
import unikom.skripsi.angga.petugas.model.MessageModel;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;
import unikom.skripsi.angga.petugas.util.SessionUtils;


public class ProfilFragment extends Fragment {

    private TextView txt_profil_nama, txt_profil_pos, txt_profil_email,
                     txt_profil_password_lama,txt_profil_password_baru,txt_profil_password_ver;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil,
                container, false);

        txt_profil_nama = view.findViewById(R.id.txt_profil_nama);
        txt_profil_email = view.findViewById(R.id.txt_profil_email);
        txt_profil_pos = view.findViewById(R.id.txt_profil_pos);

        txt_profil_password_lama = view.findViewById(R.id.txt_profil_password_lama);
        txt_profil_password_baru = view.findViewById(R.id.txt_profil_password_baru);
        txt_profil_password_ver = view.findViewById(R.id.txt_profil_password_ver);

        txt_profil_nama.setText(String.valueOf(SessionUtils.getLoggedUser(getActivity()).getNama()));
        txt_profil_email.setText(String.valueOf(SessionUtils.getLoggedUser(getActivity()).getEmail()));
        txt_profil_pos.setText(String.valueOf(SessionUtils.getLoggedUser(getActivity()).getPos()));

        Button button = view.findViewById(R.id.btn_edit_profil);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email    = txt_profil_email.getText().toString();
                String passLama = txt_profil_password_lama.getText().toString();
                String passBaru = txt_profil_password_baru.getText().toString();
                String passVer  = txt_profil_password_ver.getText().toString();

                if (passLama.trim().length() > 0 && passBaru.trim().length() > 0) {
                    ubah(email,passBaru);
                } else if (passLama.trim().length() == 0 && passBaru.trim().length() == 0){
                    Toast.makeText(getActivity(),"Ada field yang kosong", Toast.LENGTH_SHORT).show();
                }else if (!md5(passLama).equals(SessionUtils.getLoggedUser(getActivity()).getPassword()) ) {
                    Toast.makeText(getActivity(),"Password Lama Salah", Toast.LENGTH_SHORT).show();
                } else if (!passBaru.equals(passVer)){
                    Toast.makeText(getActivity(),"Password Baru Berbeda", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void ubah(String email, String password) {
        Call<MessageModel> api = ApiClient.getClient().create(ApiInterface.class).postProfil(SessionUtils.getLoggedUser(getActivity()).getUser_id(), email , password);
        api.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    SessionUtils.logout(getActivity());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
