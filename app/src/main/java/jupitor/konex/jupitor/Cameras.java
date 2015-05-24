package jupitor.konex.jupitor;


import com.orm.SugarRecord;

public class Cameras extends SugarRecord {
    long ID;
    String description;
    String street;
    String suburb;
    String state;
    long speed_limit;
    String type;
    double latitude;
    double longitude;
    String reference;
    long last_update_time;
    String country;

    public Cameras() {
    }

    public Cameras(int ID, String description, String street, String suburb, String state,
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
