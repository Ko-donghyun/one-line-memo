package com.mycompany.onelinememo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LG on 2015-07-13.
 */
class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT," + " memo TEXT, date TEXT, time TEXT, color INTEGER);");
        db.execSQL("INSERT INTO person VALUES(null, '사 용 방 법 (길게 눌러 주세요)', '본 어플리케이션은 간단한 메모를 급하게 할 필요가 있을 때 사용할 목적으로 만들어 졌습니다. " +
                "나의 번뜩이는 아이디어를 손쉽게 저장하고, 저장한 것을 길게 눌러 세부메모를 저장할 수 있도록 도와줍니다. " +
                "오른쪽 위의 메뉴를 통해 수정 및 삭제가 가능합니다.', ' - 2015.07.29 10:00', 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS person");
        db.execSQL("insert ");
        onCreate(db);
    }
}