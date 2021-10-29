package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class Plants extends PlantsAndHerbs{

    public Plants(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void queryData() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("CREATE TABLE IF NOT EXISTS PLANTS(id INTEGER PRIMARY KEY AUTOINCREMENT, image BLOB, name VARCHAR, definition VARCHAR, vernacular VARCHAR, color VARCHAR, odour VARCHAR, taste VARCHAR, morphology VARCHAR, medicinal VARCHAR, safety VARCHAR, storage VARCHAR)");
    }


}
