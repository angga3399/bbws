package unikom.skripsi.angga.petugas.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("message_id")
    private String message_id;
    @SerializedName("pos")
    private String pos;
    @SerializedName("title")
    private String title;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("message")
    private String message;
    @SerializedName("image_name")
    private String image_name;

    public Notification(String message_id, String pos, String title, String timestamp, String message, String image_name) {
        this.message_id = message_id;
        this.pos = pos;
        this.title = title;
        this.timestamp = timestamp;
        this.message = message;
        this.image_name = image_name;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getPos() { return pos; }

    public void setPos(String pos) { this.pos = pos; }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
