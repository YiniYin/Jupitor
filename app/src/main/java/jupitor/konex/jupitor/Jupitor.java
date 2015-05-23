package jupitor.konex.jupitor;


import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.orm.SugarApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Jupitor extends SugarApp {

    @Override
    public final void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {
        initDB();
    }

    private void initDB() {
        try {
            if (!doesDatabaseExist(this, consts.dbPath)) {
                Context context = getApplicationContext();
                SQLiteDatabase db = context.openOrCreateDatabase(consts.dbName, context.MODE_PRIVATE, null);
                db.close();
                InputStream dbInput = getApplicationContext().getAssets().open(consts.dbName);
                String outFileName = consts.dbPath;
                OutputStream dbOutput = new FileOutputStream(outFileName);
                try {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = dbInput.read(buffer)) > 0) {
                        dbOutput.write(buffer, 0, length);
                    }
                } finally {
                    dbOutput.flush();
                    dbOutput.close();
                    dbInput.close();
                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    private boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
