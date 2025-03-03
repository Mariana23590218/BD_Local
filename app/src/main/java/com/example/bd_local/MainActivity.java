package com.example.bd_local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText ed1, ed2, ed3, ed4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ed1 = (EditText) findViewById(R.id.ncontrol);
        ed2 = (EditText) findViewById(R.id.nombre);
        ed3 = (EditText) findViewById(R.id.semestre);
        ed4 = (EditText) findViewById(R.id.carrera);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void altas(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = ed1.getText().toString();
        String nombre = ed2.getText().toString();
        String semestre = ed3.getText().toString();
        String carrera = ed4.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("ncontrol", ncontrol);
        registro.put("nombre", nombre);
        registro.put("semestre", semestre);
        registro.put("carrera", carrera);

        bd.insert("usuario", null, registro);
        bd.close();
        Toast.makeText(this, "Datos del usuario cargados con exito", Toast.LENGTH_SHORT).show();
        limpiar();
    }

    public void consulta(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = ed1.getText().toString();
        Cursor fila = bd.rawQuery("select nombre, semestre, carrera from usuario where ncontrol = ?", new String[]{ncontrol});
        if (fila.moveToFirst()){
            ed2.setText(fila.getString(0));
            ed3.setText(fila.getString(1));
            ed4.setText(fila.getString(2));
        } else {
            Toast.makeText(this, "No existe ningun usuario con ese dni", Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }

    public void baja(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = ed1.getText().toString();
        //aqui borro la base de datos del usuario por el dni
        int cant = bd.delete("usuario", "ncontrol=?", new String[]{ncontrol});
        bd.close();
        limpiar();
        if (cant==1){
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    public void modificacion(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = ed1.getText().toString();
        String nombre = ed2.getText().toString();
        String semestre = ed3.getText().toString();
        String carrera = ed4.getText().toString();
        ContentValues registro = new ContentValues();

        registro.put("nombre",nombre);
        registro.put("semestre",semestre);
        registro.put("carrera",carrera);
        int cant = bd.update("usuario", registro, "ncontrol=?", new String[]{ncontrol});
        bd.close();
        if (cant == 1){
            Toast.makeText(this, "Datos modificados con exito", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }
    public void limpiar(){
        ed1.setText("");
        ed2.setText("");
        ed3.setText("");
        ed4.setText("");
    }
}