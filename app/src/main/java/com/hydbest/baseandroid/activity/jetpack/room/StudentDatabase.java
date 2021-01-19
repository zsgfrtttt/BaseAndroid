package com.hydbest.baseandroid.activity.jetpack.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * 数据库升级规则：
 * 例如当前版本是1，最新版本是3,数据库会优先查找1直接升到3的Migration
 * 如果找不到，则1升到2，再从2升到3
 */
@Database(entities = {Student.class}, version = 3)
public abstract class StudentDatabase extends RoomDatabase {

    private volatile static StudentDatabase INSTANCE;

    public static StudentDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (StudentDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StudentDatabase.class, "stu_db")
                            .addMigrations(Migration_1_2,Migration_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract StudentDao getStudentDao();

    public static Migration Migration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.beginTransaction();
            try {
                database.execSQL("ALTER TABLE Student ADD COLUMN sex INTEGER NOT NULL DEFAULT 0");
                database.execSQL("ALTER TABLE Student ADD COLUMN score INTEGER NOT NULL DEFAULT 60");
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        }
    };

    public static Migration Migration_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.beginTransaction();
            try {
                database.execSQL("CREATE TABLE Student_temp (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT,age INTEGER NOT NULL DEFAULT 0,sex INTEGER NOT NULL DEFAULT 0)");
                database.execSQL("INSERT INTO Student_temp (id,name,age,sex) select id,name,age,sex FROM Student");
                database.execSQL("DROP TABLE Student");
                database.execSQL("ALTER TABLE Student_temp RENAME TO Student");
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        }
    };
}
