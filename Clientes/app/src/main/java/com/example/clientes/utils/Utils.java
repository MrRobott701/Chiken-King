package com.example.clientes.utils;

import android.content.Context;

import androidx.room.Room;

import com.example.clientes.database.AppDatabase;

public class Utils {
    final private String DB_NAME = "dbAgendaApp";

    AppDatabase db;

    public AppDatabase getAppDatabase(Context context){
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).build();
        return db;
    }
}
