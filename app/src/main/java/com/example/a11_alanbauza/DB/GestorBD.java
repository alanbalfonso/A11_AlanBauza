package com.example.a11_alanbauza.DB;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class GestorBD {

    private AdminBD adminBD;

    public GestorBD(Context context) {
        this.adminBD = new AdminBD(context);
    }

    // ---------------- SECCIÓN: CRUD PELICULAS ----------------

    // INSERTAR película
    public void insertarPelicula(String titulo, String director, int anio, String genero,
                                 float calificacion, String resena, String fechaVista) {
        SQLiteDatabase db = adminBD.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("director", director);
        valores.put("anio", anio);
        valores.put("genero", genero);
        valores.put("calificacion", calificacion);
        valores.put("resena", resena);
        valores.put("fecha_vista", fechaVista);
        db.insert("peliculas", null, valores);
        db.close();
    }

    // EDITAR película
    public void editarPelicula(int id, String titulo, String director, int anio,
                               String genero, float calificacion, String resena, String fechaVista) {
        SQLiteDatabase db = adminBD.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("director", director);
        valores.put("anio", anio);
        valores.put("genero", genero);
        valores.put("calificacion", calificacion);
        valores.put("resena", resena);
        valores.put("fecha_vista", fechaVista);
        db.update("peliculas", valores, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ELIMINAR película
    public void eliminarPelicula(int id) {
        SQLiteDatabase db = adminBD.getWritableDatabase();
        db.delete("peliculas", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // LEER (Para llenar la lista visual)
    public ArrayList<String> consultarPeliculasParaLista() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = adminBD.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM peliculas ORDER BY fecha_vista DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String titulo = cursor.getString(1);
                float calificacion = cursor.getFloat(5);
                String fechaVista = cursor.getString(7);
                /*
                 *  Formato para la lista:
                 *  ID - Título ⭐ Calificación/5
                 *  Fecha: fecha_vista
                 */
                lista.add(id + " - " + titulo + " ⭐ " + calificacion + "/5\nFecha: " + fechaVista);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // OBTENER detalles completos de una película por ID
    public Pelicula obtenerPelicula(int id) {
        SQLiteDatabase db = adminBD.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM peliculas WHERE id=?",
                new String[]{String.valueOf(id)});

        Pelicula pelicula = null;
        if (cursor.moveToFirst()) {
            pelicula = new Pelicula(
                    cursor.getInt(0),       // id
                    cursor.getString(1),    // titulo
                    cursor.getString(2),    // director
                    cursor.getInt(3),       // anio
                    cursor.getString(4),    // genero
                    cursor.getFloat(5),     // calificacion
                    cursor.getString(6),    // resena
                    cursor.getString(7)     // fecha_vista
            );
        }
        cursor.close();
        db.close();
        return pelicula;
    }

    // Clase auxiliar para representar una película
    public static class Pelicula {
        public int id;
        public String titulo;
        public String director;
        public int anio;
        public String genero;
        public float calificacion;
        public String resena;
        public String fechaVista;

        public Pelicula(int id, String titulo, String director, int anio,
                        String genero, float calificacion, String resena, String fechaVista) {
            this.id = id;
            this.titulo = titulo;
            this.director = director;
            this.anio = anio;
            this.genero = genero;
            this.calificacion = calificacion;
            this.resena = resena;
            this.fechaVista = fechaVista;
        }
    }
}
