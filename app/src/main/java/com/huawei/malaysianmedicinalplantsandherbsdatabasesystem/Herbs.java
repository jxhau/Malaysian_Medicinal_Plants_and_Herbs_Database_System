package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class Herbs extends PlantsAndHerbs{
    public Herbs(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void queryData() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("CREATE TABLE IF NOT EXISTS HERBS(id INTEGER PRIMARY KEY AUTOINCREMENT, image BLOB, name VARCHAR, definition VARCHAR, vernacular VARCHAR, color VARCHAR, odour VARCHAR, taste VARCHAR, morphology VARCHAR, medicinal VARCHAR, safety VARCHAR, storage VARCHAR)");
    }

    @Override
    public void insertData(byte[] image, String name, String definition, String vernacular, String color, String odour, String taste, String morphology, String medicinal, String safety, String storage) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO HERBS VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindBlob(1, image);
        statement.bindString(2, name);
        statement.bindString(3, definition);
        statement.bindString(4, vernacular);
        statement.bindString(5, color);
        statement.bindString(6, odour);
        statement.bindString(7, taste);
        statement.bindString(8, morphology);
        statement.bindString(9, medicinal);
        statement.bindString(10, safety);
        statement.bindString(11, storage);

        statement.executeInsert();
        database.close();
    }

    @Override
    public void updateData(byte[] image, String name, String definition, String vernacular, String color, String odour, String taste, String morphology, String medicinal, String safety, String storage, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE HERBS SET image = ?, name = ?, definition = ?, vernacular = ?, color = ?, odour = ?, taste = ?, morphology = ?, medicinal = ?, safety = ?, storage = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindBlob(1, image);
        statement.bindString(2, name);
        statement.bindString(3, definition);
        statement.bindString(4, vernacular);
        statement.bindString(5, color);
        statement.bindString(6, odour);
        statement.bindString(7, taste);
        statement.bindString(8, morphology);
        statement.bindString(9, medicinal);
        statement.bindString(10, safety);
        statement.bindString(11, storage);
        statement.bindDouble(12, (double)id);

        statement.execute();
        database.close();
    }

    @Override
    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM HERBS WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    @Override
    public Cursor getData() {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery("SELECT * FROM HERBS", null);
    }
}