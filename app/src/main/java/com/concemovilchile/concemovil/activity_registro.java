package com.concemovilchile.concemovil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class activity_registro extends ActionBarActivity {

    EditText etUsuario, etNombre, etApellido, etClave, etClave2;
    Drawable checkicon;
    Button btnregistro;

    httpHandler handler = new httpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro);

        etUsuario = (EditText) findViewById(R.id.email_registro);
        etNombre = (EditText) findViewById(R.id.nombre_registro);
        etApellido = (EditText) findViewById(R.id.apellido_registro);
        etClave = (EditText) findViewById(R.id.clave_registro);
        etClave2 = (EditText) findViewById(R.id.clave_registro2);

        btnregistro = (Button) findViewById(R.id.registro);

        etUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkicon = getResources().getDrawable(R.drawable.check);

                if (valida() == true) {
                    etUsuario.setError("Correo Valido", checkicon);
                    btnregistro.setEnabled(true);

                } else {
                    etUsuario.setError("Correo No valido");
                    btnregistro.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public boolean valida() {

        String usuario = etUsuario.getText().toString().trim();

        if (usuario.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$")) {


            return true;

        } else {

            return false;
        }

    }

    public void Registrar(View view) {


        String usuario = etUsuario.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String clave = etClave.getText().toString().trim();
        String clave2 = etClave2.getText().toString().trim();


        if (usuario.equals("") || nombre.equals("") || apellido.equals("") || clave.equals("") || clave2.equals("")) {
            Toast.makeText(this, "Ingrese sus datos de datos", Toast.LENGTH_LONG).show();

        } else {
/*
            if (usuario.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$")){
                Toast.makeText(this,"jajaajja",Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this,"O_O",Toast.LENGTH_SHORT).show();
            }*/

            if (clave.equals(clave2)) {
                try {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    String registro = handler.get("http://www.jorgesotovillegas.com/Registro.php?email=" + usuario + "&nombre=" + nombre + "&apellido=" + apellido + "&clave=" + clave + "&tipo=1");

                    switch (registro) {
                        case "Usuario existente":
                            Toast.makeText(this, "El usuario ya esta registrado", Toast.LENGTH_LONG).show();
                            break;
                        case "Usuario registrado":
                            Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(this,recorridos.class);
                            startActivity(i);
                            break;
                        default:
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                            break;
                    }

                } catch (Exception e) {
                    Toast.makeText(this, " " + e, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Las claves no coinciden", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_registro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
