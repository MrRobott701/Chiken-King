package com.example.clientes;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientes.database.AppDatabase;
import com.example.clientes.databinding.FragmentAdminClientesBinding;
import com.example.clientes.models.Cliente;
import com.example.clientes.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdminClientesFragment extends Fragment implements ClienteAdapter.OnItemClicked {

    FragmentAdminClientesBinding binding;
    List<Cliente> listaCitas = new ArrayList<>();
    ClienteAdapter clienteAdapter = new ClienteAdapter(listaCitas, this);

    AppDatabase db;
    Cliente cliente = new Cliente();
    Boolean isValido = false;

    Boolean isEditando = false;

    public AdminClientesFragment() {
        // Required empty public constructor
    }

    public static AdminClientesFragment newInstance(String param1, String param2) {
        AdminClientesFragment fragment = new AdminClientesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminClientesBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("CLIENTES CHIKEN KING");
        db = new Utils().getAppDatabase(getContext());

        setupToolbarMenu();
        obtenerClientes();

        binding.svcCliente.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarCliente(newText);
                return false;
            }
        });
        return vista;
    }
    private void filtrarCliente(String texto){
        ArrayList<Cliente> listaFiltrada = new ArrayList<>();
        for(Cliente cliente : listaCitas){
            if(cliente.telCliente.toLowerCase().contains(texto.toLowerCase()) || cliente.nomCliente.toLowerCase().contains(texto.toLowerCase())){
                listaFiltrada.add(cliente);
            }
        }
        clienteAdapter.filtrarCliente(listaFiltrada);
    }

    private void setupRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvCliente.setLayoutManager(layoutManager);
        clienteAdapter = new ClienteAdapter(listaCitas, this);
        binding.rvCliente.setAdapter(clienteAdapter);
    }


    private void setupToolbarMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.action_agregar){
                   lanzarAlertDialogCita(getActivity());
                return true;
                }

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void lanzarAlertDialogCita(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater= activity.getLayoutInflater();
        View vista = inflater.inflate(R.layout.alrt_dialog_add_update_cliente, null);
        builder.setView(vista);
        builder.setCancelable(false);

        EditText etNomCliente,etTelCliente,etDireccionCliente,etReferenciasCliente,etComprasCliente;
        TextView tvTituloAlert;

        etNomCliente=vista.findViewById(R.id.etNomCliente);
        etTelCliente=vista.findViewById(R.id.etTelCliente);
        etDireccionCliente=vista.findViewById(R.id.etDireccionCliente);
        etReferenciasCliente=vista.findViewById(R.id.etReferenciasCliente);
        etComprasCliente=vista.findViewById(R.id.etComprasCliente);
        tvTituloAlert = vista.findViewById(R.id.tvTituloAlert);

        String[] listaDias = activity.getResources().getStringArray(R.array.dias_semana);
        ArrayAdapter arrayAdapter = new ArrayAdapter(activity,R.layout.item_rv_clientes,listaDias);
        //spi.setAdapter(arrayAdapter)

        if (isEditando){
            tvTituloAlert.setText("ACTUALIZAR CLIENTE");
            etNomCliente.setText(cliente.nomCliente);
            etTelCliente.setText(cliente.telCliente);
            etDireccionCliente.setText(cliente.direccionCliente);
            etReferenciasCliente.setText(cliente.referenciaCliente);
            etComprasCliente.setText(cliente.comprasCliente);
        }

        builder.setPositiveButton("Aceptar",(dialogInterface, i) -> {
            if(!isEditando){
                cliente.idCliente = String.valueOf(System.currentTimeMillis());
            }

            cliente.nomCliente = etNomCliente.getText().toString().trim();
            cliente.telCliente = etTelCliente.getText().toString().trim();
            cliente.direccionCliente = etDireccionCliente.getText().toString().trim();
            cliente.referenciaCliente = etReferenciasCliente.getText().toString().trim();
            cliente.comprasCliente = etComprasCliente.getText().toString().trim();

            validarCampos();
            if(isValido){
                if(isEditando){
                    actualizarCliente();
                    isEditando = false;
                }else{
                    agregarCliente();
                }
            }else{
                Toasty.error(getContext(),"FALTAN CAMPOS OBLIGATORIOS",Toast.LENGTH_SHORT, true).show();
            }
        });

        builder.setNegativeButton("Cancelar",(dialogInterface, i) -> {
            Toast.makeText(activity,"CANCELAR", Toast.LENGTH_SHORT).show();
        });
        builder.create();
        builder.show();
    }

    private void validarCampos(){
        if(cliente.nomCliente.isEmpty()
        || cliente.telCliente.isEmpty()
        ||cliente.direccionCliente.isEmpty()
        ){
            isValido = false;
        }else{
            isValido = true;
        }
    }


    private void obtenerClientes(){
        AsyncTask.execute( () -> {
            listaCitas = db.clienteDao().obtenerCitas();
            getActivity().runOnUiThread(() -> {
                setupRecyclerView();
            });
        });
    }
    private void agregarCliente(){
        AsyncTask.execute( () -> {
            db.clienteDao().agregarClientes(cliente);
            listaCitas = db.clienteDao().obtenerCitas();
            getActivity().runOnUiThread( () -> {
                setupRecyclerView();
            });
        });
    }

    private void actualizarCliente(){
        AsyncTask.execute( () -> {
            db.clienteDao().actualizarCliente(cliente);
            listaCitas = db.clienteDao().obtenerCitas();
            getActivity().runOnUiThread( () ->{
                setupRecyclerView();
            });
        });
    }
    @Override
    public void editarCliente(Cliente cliente) {
        isEditando = true;
        this.cliente = cliente;
        lanzarAlertDialogCita(getActivity());
    }

    @Override
    public void borrarCliente(Cliente cliente) {

        AsyncTask.execute( ()->{
            db.clienteDao().eliminarClientes(cliente);
            listaCitas = db.clienteDao().obtenerCitas();
            getActivity().runOnUiThread( () ->{
                setupRecyclerView();
            });
        });
        Toasty.info(getContext(),"CLIENTE ELIMINADO", Toasty.LENGTH_SHORT).show();

    }
}