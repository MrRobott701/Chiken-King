package com.example.clientes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientes.models.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {
    List<Cliente> listaCitas;
    public OnItemClicked onclick;

    public ClienteAdapter(List<Cliente> listaCitas, OnItemClicked onclick){

        this.listaCitas = listaCitas;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ClienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_clientes,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ViewHolder holder, int position) {

        Cliente cliente = listaCitas.get(position);

        holder.txNombreCliente.setText(cliente.nomCliente.toUpperCase());
        holder.tvTelCliente.setText(cliente.telCliente.toUpperCase());
        holder.tvDireccionCliente.setText(cliente.direccionCliente.toUpperCase());
        holder.tvRefereciasCliente.setText(cliente.referenciaCliente.toUpperCase());
        holder.tvComprasCliente.setText(cliente.comprasCliente.toUpperCase());

        holder.ibtnEditar.setOnClickListener(view ->{
            onclick.editarCliente(cliente);
        });
        holder.ibtnBorrar.setOnClickListener(view ->{
            onclick.borrarCliente(cliente);
        });

    }

    @Override
    public int getItemCount() {

        return listaCitas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txNombreCliente,tvTelCliente,tvDireccionCliente,tvRefereciasCliente,tvComprasCliente;
        ImageButton ibtnEditar,ibtnBorrar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txNombreCliente = itemView.findViewById(R.id.txNombreCliente);
            tvTelCliente = itemView.findViewById(R.id.tvTelCliente);
            tvDireccionCliente = itemView.findViewById(R.id.tvDireccionCliente);
            tvRefereciasCliente = itemView.findViewById(R.id.tvRefereciasCliente);
            tvComprasCliente = itemView.findViewById(R.id.tvComprasCliente);
            ibtnBorrar = itemView.findViewById(R.id.ibtnBorrar);
            ibtnEditar = itemView.findViewById(R.id.ibtnEditar);

        }
    }
    public interface OnItemClicked{
        void editarCliente(Cliente cliente);
        void borrarCliente(Cliente cliente);
    }
public void filtrarCliente(List<Cliente> listaFiltrada){
        this.listaCitas = listaFiltrada;
        notifyDataSetChanged();
}
}
