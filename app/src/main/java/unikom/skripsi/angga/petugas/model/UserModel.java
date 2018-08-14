package unikom.skripsi.angga.petugas.model;

import java.util.List;

public class UserModel {
    private String user_id;
    private String nama;
    private String email;
    private String password;
    private String pos;
    private double lat;
    private double lng;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public class UserDataModel{
        private String message;
        private List<UserModel> results;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<UserModel> getResults() {
            return results;
        }

        public void setResults(List<UserModel> results) {
            this.results = results;
        }
    }
}
