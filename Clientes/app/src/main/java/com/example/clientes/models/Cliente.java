package com.example.clientes.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clientes")
public class Cliente {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id_cliente")
    public String idCliente;
    @ColumnInfo(name = "nombre_cliente")
    public String nomCliente;
    @ColumnInfo(name = "telefono_cliente")
    public String telCliente;
    @ColumnInfo(name = "direccion_cliente")
    public String direccionCliente;
    @ColumnInfo(name = "referencia_cliente")
    public String referenciaCliente;
    @ColumnInfo(name = "compras_cliente")
    public String comprasCliente;

}
