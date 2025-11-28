package com.example.a11_alanbauza;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.example.a11_alanbauza.DB.GestorBD;

public class MainActivity extends AppCompatActivity {

    // Interfaz
    TextInputEditText etTitulo, etDirector, etAnio, etGenero, etResena;
    RatingBar rbCalificacion;
    Button btnAgregar, btnEditar, btnEliminar;
    ListView listaPeliculas;

    // Lógica y Datos
    GestorBD gestor;
    ArrayAdapter<String> adaptador;
    ArrayList<String> listaDatos;
    int idSeleccionado = -1; // Controla qué película se está editando

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializar vistas
        etTitulo = findViewById(R.id.etTitulo);
        etDirector = findViewById(R.id.etDirector);
        etAnio = findViewById(R.id.etAnio);
        etGenero = findViewById(R.id.etGenero);
        etResena = findViewById(R.id.etResena);
        rbCalificacion = findViewById(R.id.rbCalificacion);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        listaPeliculas = findViewById(R.id.listaPeliculas);

        // 2. Inicializar base de datos
        gestor = new GestorBD(this);

        // 3. Cargar datos iniciales
        actualizarLista();

        // --- BOTÓN AGREGAR ---
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarPelicula();
            }
        });

        // --- CLIC EN LA LISTA (Seleccionar) ---
        listaPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // El formato es "ID - Título ⭐ Calificación/5\nFecha: fecha_vista"
                String textoFila = listaDatos.get(position);

                // Extraer solo la primera línea para obtener el ID
                String primeraLinea = textoFila.split("\n")[0];
                String[] partes = primeraLinea.split(" - ");

                // Guardamos el ID y cargamos todos los datos de la película
                idSeleccionado = Integer.parseInt(partes[0]);
                cargarPeliculaSeleccionada(idSeleccionado);

                // Cambiamos estado de botones
                btnAgregar.setEnabled(false);
                btnEditar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        });

        // --- BOTÓN EDITAR ---
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPelicula();
            }
        });

        // --- BOTÓN ELIMINAR ---
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPelicula();
            }
        });
    }

    // Agregar nueva película
    private void agregarPelicula() {
        String titulo = etTitulo.getText().toString().trim();
        float calificacion = rbCalificacion.getRating();

        // Si el titulo o la calificación están vacíos, no permitir agregar
        if (titulo.isEmpty() || calificacion == 0) {
            Toast.makeText(this, "El título y la calificación son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        String director = etDirector.getText().toString().trim();
        int anio = etAnio.getText().toString().isEmpty() ? 0 :
                Integer.parseInt(etAnio.getText().toString());
        String genero = etGenero.getText().toString().trim();
        String resena = etResena.getText().toString().trim();
        String fechaVista = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        gestor.insertarPelicula(titulo, director, anio, genero, calificacion, resena, fechaVista);
        reiniciarFormulario();
        Toast.makeText(this, "Película agregada", Toast.LENGTH_SHORT).show();
    }

    // Cargar datos de la película seleccionada en el formulario
    private void cargarPeliculaSeleccionada(int id) {
        GestorBD.Pelicula pelicula = gestor.obtenerPelicula(id);

        if (pelicula != null) {
            etTitulo.setText(pelicula.titulo);
            etDirector.setText(pelicula.director);
            etAnio.setText(pelicula.anio > 0 ? String.valueOf(pelicula.anio) : "");
            etGenero.setText(pelicula.genero);
            rbCalificacion.setRating(pelicula.calificacion);
            etResena.setText(pelicula.resena);
        }
    }

    // Editar película existente
    private void editarPelicula() {
        if (idSeleccionado == -1) return;

        String titulo = etTitulo.getText().toString().trim();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        String director = etDirector.getText().toString().trim();
        int anio = etAnio.getText().toString().isEmpty() ? 0 :
                Integer.parseInt(etAnio.getText().toString());
        String genero = etGenero.getText().toString().trim();
        float calificacion = rbCalificacion.getRating();
        String resena = etResena.getText().toString().trim();
        String fechaVista = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        gestor.editarPelicula(idSeleccionado, titulo, director, anio, genero,
                calificacion, resena, fechaVista);
        reiniciarFormulario();
        Toast.makeText(this, "Película editada", Toast.LENGTH_SHORT).show();
    }

    // Eliminar película
    private void eliminarPelicula() {
        if (idSeleccionado != -1) {
            gestor.eliminarPelicula(idSeleccionado);
            reiniciarFormulario();
            Toast.makeText(this, "Película eliminada", Toast.LENGTH_SHORT).show();
        }
    }

    // Función para refrescar la ListView
    private void actualizarLista() {
        listaDatos = gestor.consultarPeliculasParaLista();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listaDatos);
        listaPeliculas.setAdapter(adaptador);
    }

    // Función para limpiar campos y reiniciar botones
    private void reiniciarFormulario() {
        etTitulo.setText("");
        etDirector.setText("");
        etAnio.setText("");
        etGenero.setText("");
        etResena.setText("");
        rbCalificacion.setRating(0);
        idSeleccionado = -1;
        btnAgregar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        actualizarLista();
    }
}