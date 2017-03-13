package com.concemovilchile.concemovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Principal extends ActionBarActivity {
    MenuItem menuItem;
    private DrawerLayout mDrawerLayout; //declaracion de Layout para contener el menu
    private ActionBarDrawerToggle drawerToggle; //accion q permite esconder el menu
    private Toolbar toolbar;//declaracion de toolbar barra superior

    SearchView Buscar;


    private GoogleMap mMap;

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        setUpMapIfNeeded();

        LatLng inacap = new LatLng(-36.781818, -73.075157);
        LatLng unab = new LatLng(-36.78009918, -73.076080);


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

/////----------------------------------Zooming camera to position user-----------------


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

      //  Location location = mMap.getMyLocation();

        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 17));

                          // Creates a CameraPosition from the builder

        }

        /////----------------------------------Zooming camera to position user-----------------



        mMap.addMarker(new MarkerOptions()
                .position(unab)
                .title("UNAB")
                .snippet("UNIVERSIDAD ANDRES BELLO")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));




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

        Buscar = (SearchView) findViewById(R.id.search);



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
