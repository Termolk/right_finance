package com.Termolk.rightfinance;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private Context context;
    private String DB_NAME = "game.db";
    private final String TABLE_NAME = "OPERATIONS";
    private final String COLUMN_NAME_OPERATION = "NAME";
    private final String COLUMN_COUNT_MONEY = "COUNT";

    private SQLiteDatabase db;

    private static DBManager dbManager;

    public static DBManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    private DBManager(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        createTablesIfNeedBe();
    }

    void addOperation(String operation, int count) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME_OPERATION, operation);
        contentValues.put(COLUMN_COUNT_MONEY, count);

        db.insert(TABLE_NAME, null, contentValues);
    }

    void clearTable() {
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }


    ArrayList<Operation> getAllOperations() {
        ArrayList<Operation> data = new ArrayList<Operation>();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_OPERATION, COLUMN_COUNT_MONEY}, null, null, null, null, COLUMN_COUNT_MONEY + " DESC");
        boolean hasMoreData = cursor.moveToFirst();

        while (hasMoreData) {
            String operationName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_OPERATION));
            int countMoney = Integer.parseInt(cursor.getString(cursor
                    .getColumnIndex(COLUMN_COUNT_MONEY)));
            data.add(new Operation(operationName, countMoney));
            hasMoreData = cursor.moveToNext();
        }

        return data;
    }

    private void createTablesIfNeedBe() {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + " '" + COLUMN_NAME_OPERATION + "' " + "TEXT, " + COLUMN_COUNT_MONEY + " INTEGER);");
    }

}
