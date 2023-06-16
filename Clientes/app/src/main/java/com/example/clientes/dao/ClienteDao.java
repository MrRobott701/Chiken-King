package com.example.clientes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.clientes.models.Cliente;

import java.util.List;

@Dao
public interface ClienteDao {
    @Query("SELECT * FROM clientes")
    List<Cliente> obtenerCitas();
    @Update
    void actualizarCliente(Cliente... cliente);
    @Insert
    void agregarClientes(Cliente... cliente);
    @Delete
    void eliminarClientes(Cliente cliente);


}
