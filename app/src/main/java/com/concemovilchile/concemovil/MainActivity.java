package com.concemovilchile.concemovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    httpHandler handler = new httpHandler();
    EditText edEmail, edClave;
    Button btregistro;


    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edEmail = (EditText) findViewById(R.id.etUser);
        edClave = (EditText) findViewById(R.id.etPassword);

        btregistro = (Button) findViewById(R.id.btregistrarse);
        //btnlogin = (Button) findViewById(R.id.btlogin);



        btregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), activity_registro.class));
            }
        });

        consultaSesion();



    }
    public void iniciar(View view) {

        String user = edEmail.getText().toString().trim();
        String pass = edClave.getText().toString().trim();

        if (user.equals("") || pass.equals("") ){
          Toast.makeText(this,"Ingrese sus datos de usuario",Toast.LENGTH_LONG).show();

        }else {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();//permite utilizar html en el codigo
                StrictMode.setThreadPolicy(policy);

                String iniciars= handler.get("http://www.jorgesotovillegas.com/Login.php?usuario="+user+"&clave="+pass);

                switch (iniciars){
                    case "Bienvenido":
                        Toast.makeText(this, "Bienvenido",Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = getSharedPreferences("Preferencia_usuario", Context.MODE_WORLD_WRITEABLE);//guardar datos de user en el celular
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("usuario", user);

                        editor.putBoolean("sesion", true);

                        editor.apply();
                        startActivity(new Intent(MainActivity.this, MapsActivity.class));//ir a perfil luego del login

                        break;
                    case "Usuario o Clave Incorrectos":
                        Toast.makeText(this,"Usuario o clave incorrecto",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Toast.makeText(this," "+ e,Toast.LENGTH_LONG).show();

            }
        }


    }

    public void consultaSesion(){//metodo ve si la sesion esta iniciada

        SharedPreferences preferences = getSharedPreferences("Preferencia_usuario", Context.MODE_PRIVATE);

        if (preferences.contains("sesion")){
            startActivity(new Intent(this, MapsActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
