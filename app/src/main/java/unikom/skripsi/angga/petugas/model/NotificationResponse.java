package unikom.skripsi.angga.petugas.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {
    @SerializedName("results")
    private List<Notification> results;

    public List<Notification> getResults() {
        return results;
    }

    public void setResults(List<Notification> results) {
        this.results = results;
    }
}
