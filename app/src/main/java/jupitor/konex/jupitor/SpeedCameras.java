package jupitor.konex.jupitor;


import com.orm.SugarRecord;

public class SpeedCameras extends SugarRecord<SpeedCameras> {
    int _id;
    String description;
    String street;
    String suburb;
    String state;
    int speed_limit;
    String type;
    float latitude;
    float longitude;
    String reference;
    long last_update_time;
    String country;

    public SpeedCameras() {
    }

    public SpeedCameras(int _id, String description, String street, String suburb, String state,
                        int speed_limit, String type, float latitude, float longitude, String reference,
                        long last_update_time, String country) {
        this._id = _id;
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
