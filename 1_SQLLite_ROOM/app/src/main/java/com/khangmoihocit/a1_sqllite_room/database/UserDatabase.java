package com.khangmoihocit.a1_sqllite_room.database;

import android.content.Context;
import android.service.autofill.UserData;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.khangmoihocit.a1_sqllite_room.MainActivity;
import com.khangmoihocit.a1_sqllite_room.User;

@Database(entities = User.class, version = 2)
public abstract class UserDatabase extends RoomDatabase {
    static Migration migration_form_1_to_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("alter table users add column year integer");
        }
    };
    private static final String DATABASE_NAME = "user.db";
    private static UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(migration_form_1_to_2)
                    .build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();

}
