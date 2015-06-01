package jupitor.konex.jupitor.dataAccess;


import com.orm.SugarRecord;

public class UserPreference extends SugarRecord {
    public long ID;
    public String name;
    public String value;

    public UserPreference() {
    }

    public UserPreference(int ID, String name, String value) {
        this.ID = ID;
        this.name = name;
        this.value = value;
    }
}
