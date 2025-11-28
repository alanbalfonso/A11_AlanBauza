package com.example.a11_alanbauza.DB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminBD extends SQLiteOpenHelper {

    // Configuración del archivo
    private static final String NOMBRE_BD = "MisPeliculas.db";
    private static final int VERSION_BD = 2; // Incrementar versión

    // Tabla principal: Películas vistas
    private static final String TABLA_PELICULAS = "CREATE TABLE peliculas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "titulo TEXT NOT NULL, " +
            "director TEXT, " +
            "anio INTEGER, " +
            "genero TEXT, " +
            "calificacion REAL, " +
            "resena TEXT, " +
            "fecha_vista TEXT)";

    public AdminBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_PELICULAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas antiguas y crear la nueva
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS peliculas");
        db.execSQL("DROP TABLE IF EXISTS rentas");
        onCreate(db);
    }
}
