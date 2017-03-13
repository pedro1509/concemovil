package com.concemovilchile.concemovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class MapsActivity extends ActionBarActivity {
    MenuItem menuItem;
    private DrawerLayout mDrawerLayout; //declaracion de Layout para contener el menu
    private ActionBarDrawerToggle drawerToggle; //accion q permite esconder el menu
    private Toolbar toolbar;//declaracion de toolbar barra superior
    httpHandler handler = new httpHandler();
    GoogleMap map;
    MarkerOptions markerOptions;
    LatLng latLng;
    RequestQueue requestQueue,sitio;
    String region,namelinea,namemovi;
    TextView name;

    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;

    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

         final View v = getLayoutInflater().inflate(R.layout.windowlayout, null);

        name = (TextView) v.findViewById(R.id.nombre);


        tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();

        // Enable MyLocation Button in the Map
        map.setMyLocationEnabled(true);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getMyLocation();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        //  Location location = mMap.getMyLocation();

        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 17));

            // Creates a CameraPosition from the builder

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);////instanciar toolbar
        if (toolbar != null) {//ver si no es nula
            setSupportActionBar(toolbar);//utilizar toolbar como barra principal
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);//asignar icono de menu
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//instanciar layout del menu
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);//dar valores para utilizar menu
        mDrawerLayout.setDrawerListener(drawerToggle);//asignar el menu para poder esconderlo


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//ver que el menu no sea nulo
        if (navigationView != null) {
            setupDrawerContent(navigationView);//metodo para crear el menu
        }


        Button btn_find = (Button) findViewById(R.id.btn_find);


        View.OnClickListener findClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                // Getting user input location
                String location = etLocation.getText().toString()+"concepcion, chile";

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };

        btn_find.setOnClickListener(findClickListener);

        try {
            Bundle bundle = getIntent().getExtras();
            Toast.makeText(getApplicationContext(), "Sitios: "+bundle.getString("region"), Toast.LENGTH_LONG).show();


            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(-36.805595, -73.042580), 13));

                String posicionsitios = "http://www.jorgesotovillegas.com/pruebasitios.php?region=8";


                sitio = Volley.newRequestQueue(getApplicationContext());

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Toast.makeText(getApplicationContext(),"Has pinchado un Sitio",Toast.LENGTH_LONG).show();


                    map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {

                            // Getting the position from the marker
                            LatLng latLng = marker.getPosition();

                            // Getting reference to the TextView to set latitude
                            TextView latitud = (TextView) v.findViewById(R.id.latitud);

                            // Getting reference to the TextView to set longitude
                            TextView longitud = (TextView) v.findViewById(R.id.longitud);

                            // Setting the latitude
                            latitud.setText("Latitud:" + latLng.latitude);

                            // Setting the longitude
                            longitud.setText("Longitud:" + latLng.longitude);
                            return v;
                        }

                        // Defines the contents of the InfoWindow
                        @Override
                        public View getInfoContents(Marker marker) {

                            // Getting view from the layout file info_window_layout



                            // Returning the view containing InfoWindow contents
                            return null;

                        }
                    });



                    return false;
                }
            });
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  posicionsitios, new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray asitios = response.getJSONArray("users");
                            for (int i=0;i<asitios.length();i++ ) {
                                JSONObject Objectp = asitios.getJSONObject(i);
                                String usuario = Objectp.getString("latitud");
                                String nombre = Objectp.getString("longitud");
                                String type = Objectp.getString("tipo");

                                name.setText(Objectp.getString("Nombre"));


                                Double latjson = Double.parseDouble(usuario);
                                Double lngjson = Double.parseDouble(nombre);


                                LatLng coojson = new LatLng(latjson,lngjson);
                                Marker sitios = map.addMarker(new MarkerOptions().position(coojson).icon(BitmapDescriptorFactory.fromResource(R.drawable.sitios))
                                                .title("Sitios Octava Region")
                                .snippet(""));



                                };
                            }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                sitio.add(jsonObjectRequest);
        }catch (Exception e){

        }


    }
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
    }


    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setupDrawerContent(NavigationView navigationView) {//metodo de botones de menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.navigation_sub_item_8:

                        SharedPreferences preferences = getSharedPreferences("Preferencia_usuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        break;

                    case R.id.navigation_sub_item_4:
                        startActivity(new Intent(getApplicationContext(), recorridos.class));

                        break;

                    default:
                        return true;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }
}
