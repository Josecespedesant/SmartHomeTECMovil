package com.example.smarthometec.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "smartHomeTEC";
    private static final String TABLE_USERS = "users";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASS = "password";
    private static final String KEY_CONTINENT = "continent";
    private static final String KEY_COUNTRY = "country";

    private static final String TABLE_APOSENTO = "aposentos";
    private static final String KEY_NAME_APOSENTO = "nombre_aposento";
    private static final String KEY_APOSENTO_EMAIL = "user_email";

    private static final String TABLE_DISPOSITIVO = "dispositivo";
    private static final String KEY_DESCRIPTION = "descripcion";
    private static final String KEY_MARCA = "marca";
    private static final String KEY_USER_CORREO = "user_email";
    private static final String KEY_APOSENTO = "aposento";
    private static final String KEY_SERIE = "numSerie";
    private static final String KEY_CONSUMO = "consumo";
    private static final String KEY_ISON = "isOn";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_PASS + " TEXT,"
                + KEY_CONTINENT + " TEXT,"
                + KEY_COUNTRY + " TEXT"
                + ")";

        String CREATE_APOSENTOS_TABLE = "CREATE TABLE " + TABLE_APOSENTO + "("
                + KEY_APOSENTO_EMAIL + " TEXT ,"
                + KEY_NAME_APOSENTO + " TEXT , PRIMARY KEY (" + KEY_APOSENTO_EMAIL + "," + KEY_NAME_APOSENTO
                + "))";

        String CREATE_DISPOSITIVOS_TABLE = "CREATE TABLE " + TABLE_DISPOSITIVO + "("
                + KEY_SERIE + " TEXT PRIMARY KEY,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_MARCA + " TEXT,"
                + KEY_USER_CORREO + " TEXT,"
                + KEY_APOSENTO + " TEXT,"
                + KEY_CONSUMO + " TEXT,"
                + KEY_ISON + " INTEGER"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_APOSENTOS_TABLE);
        db.execSQL(CREATE_DISPOSITIVOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APOSENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISPOSITIVO);
        // Create tables again
        onCreate(db);
    }

    // APOSENTOS

    public void addAposento(Aposento aposento){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_APOSENTO_EMAIL, aposento.getUserCorreo());
        values.put(KEY_NAME_APOSENTO, aposento.getName());
        // Inserting Row
        db.insert(TABLE_APOSENTO, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public Aposento getAposento(String email, String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APOSENTO, new String[] { KEY_APOSENTO_EMAIL, KEY_NAME_APOSENTO }, KEY_APOSENTO_EMAIL + "=?",
                new String[] { email , nombre}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Aposento aposento = new Aposento(cursor.getString(0), cursor.getString(1));
        return aposento;
    }

    public int updateAposento(Aposento aposento) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_APOSENTO_EMAIL, aposento.getUserCorreo());
        values.put(KEY_NAME_APOSENTO, aposento.getName());

        // updating row
        return db.update(TABLE_APOSENTO, values, KEY_APOSENTO_EMAIL + " = ?",
                new String[] { aposento.getUserCorreo() });
    }

    public void deleteAposento(Aposento aposento) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APOSENTO, KEY_APOSENTO_EMAIL + " = ?",
                new String[] { aposento.getUserCorreo() });
        db.close();
    }

    // USERS

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_PASS, user.getPass());
        values.put(KEY_CONTINENT, user.getContinent());
        values.put(KEY_COUNTRY, user.getCountry());
        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_EMAIL,
                        KEY_NAME, KEY_LAST_NAME, KEY_ADDRESS, KEY_PASS, KEY_CONTINENT, KEY_COUNTRY }, KEY_EMAIL + "=?",
                new String[] { email }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)
                ,cursor.getString(4),cursor.getString(5),cursor.getString(6));
        return user;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_PASS, user.getPass());
        values.put(KEY_CONTINENT, user.getContinent());
        values.put(KEY_COUNTRY, user.getCountry());

        // updating row
        return db.update(TABLE_USERS, values, KEY_EMAIL + " = ?",
                new String[] { user.getEmail() });
    }

    public boolean checkUser(String email){
        String[] columns = {
                KEY_NAME
        };
        String selection = KEY_EMAIL + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null, null, null
                );

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if(cursorCount>0){
            return true;
        }
        return false;
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_EMAIL + " = ?",
                new String[] { user.getEmail() });
        db.close();
    }

    public Cursor viewAposentos(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_APOSENTO + " WHERE TRIM(user_email) ='"+ email.trim()+"'";
       // String query = "SELECT " + KEY_NAME_APOSENTO +" FROM " + TABLE_APOSENTO + " WHERE TRIM(user_email) = '"+ email.trim()+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }


    //DISPOSITIVOS

    public void addDispositivo(Dispositivo dispositivo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SERIE, dispositivo.getNumSerie());
        values.put(KEY_DESCRIPTION, dispositivo.getDescription());
        values.put(KEY_MARCA, dispositivo.getMarca());
        values.put(KEY_USER_CORREO, dispositivo.getUserCorreo());
        values.put(KEY_APOSENTO, dispositivo.getAposento());
        values.put(KEY_CONSUMO, dispositivo.getConsumoElectrico());
        values.put(KEY_ISON, dispositivo.isOn());
        // Inserting Row
        db.insert(TABLE_DISPOSITIVO, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void deleteDispositivo(Dispositivo dispositivo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DISPOSITIVO, KEY_SERIE + " = ?",
                new String[] { dispositivo.getNumSerie() });
        db.close();
    }

    public int updateDispositivo(Dispositivo dispositivo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERIE, dispositivo.getNumSerie());
        values.put(KEY_DESCRIPTION, dispositivo.getDescription());
        values.put(KEY_MARCA, dispositivo.getMarca());
        values.put(KEY_USER_CORREO, dispositivo.getUserCorreo());
        values.put(KEY_APOSENTO, dispositivo.getAposento());
        values.put(KEY_CONSUMO, dispositivo.getConsumoElectrico());
        values.put(KEY_ISON, dispositivo.isOn());
        // updating row
        return db.update(TABLE_DISPOSITIVO, values, KEY_SERIE + " = ?",
                new String[] { dispositivo.getNumSerie() });
    }

    public boolean checkDispositivo(String numserie){
        String[] columns = {
                KEY_SERIE
        };
        String selection = KEY_EMAIL + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {numserie};

        Cursor cursor = db.query(TABLE_DISPOSITIVO,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if(cursorCount>0){
            return true;
        }
        return false;
    }

    public Cursor viewDispositivos(String email, String aposento){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DISPOSITIVO + " WHERE TRIM(user_email) ='"+ email.trim()+"'" + " AND " + " TRIM(aposento) ='"+ aposento.trim()+"'";
        // String query = "SELECT " + KEY_NAME_APOSENTO +" FROM " + TABLE_APOSENTO + " WHERE TRIM(user_email) = '"+ email.trim()+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor viewDispositivoInfo(String email, String aposento, String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DISPOSITIVO + " WHERE TRIM(user_email) ='"+ email.trim()+"'" + " AND " + " TRIM(aposento) ='"+ aposento.trim()+"'" + " AND " + " TRIM(numSerie) ='"+ id.trim()+"'";
        // String query = "SELECT " + KEY_NAME_APOSENTO +" FROM " + TABLE_APOSENTO + " WHERE TRIM(user_email) = '"+ email.trim()+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


}
