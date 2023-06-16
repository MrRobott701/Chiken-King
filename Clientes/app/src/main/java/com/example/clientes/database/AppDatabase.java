package com.example.clientes.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.clientes.dao.ClienteDao;
import com.example.clientes.models.Cliente;

@Database(
        entities = {Cliente.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ClienteDao clienteDao();
}
