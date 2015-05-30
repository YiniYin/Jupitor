package jupitor.konex.jupitor;


import com.orm.SugarRecord;

public class Camera extends SugarRecord {
    public long ID;
    public String description;
    public String street;
    public String suburb;
    public String state;
    public long speed_limit;
    public String type;
    public double latitude;
    public double longitude;
    public String reference;
    public long last_update_time;
    public String country;

    public Camera() {
    }

    public Camera(int ID, String description, String street, String suburb, String state,
                  int speed_limit, String type, float latitude, float longitude, String reference,
                  long last_update_time, String country) {
        this.ID = ID;
        this.description = description;
        this.street = street;
        this.suburb = suburb;
        this.state = state;
        this.speed_limit = speed_limit;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reference = reference;
        this.last_update_time = last_update_time;
        this.country = country;
    }
}
