package unikom.skripsi.angga.petugas.model;

import java.util.List;

public class TmaModel {
    private String tanggal;
    private String jam;
    private String tma;


    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getTma() {
        return tma;
    }

    public void setTma(String tma) {
        this.tma = tma;
    }

    public class TmaDataModel{
        private List<TmaModel> results;
        private String message;


        public List<TmaModel> getResults() {
            return results;
        }

        public void setResults(List<TmaModel> results) {
            this.results = results;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
