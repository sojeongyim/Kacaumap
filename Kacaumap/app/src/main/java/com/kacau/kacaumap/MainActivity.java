package com.kacau.kacaumap;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TMapGpsManager.onLocationChangedCallback{

    private boolean m_bTrackingMode = true;
    private TMapGpsManager tmapgps = null;
    private TMapView tMapView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout contentTmap = (LinearLayout)findViewById(R.id.contentTmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey( "28f96670-7443-42b8-ba92-6cf3e2b1e8f2" );
        contentTmap.addView( tMapView );
        tMapView.setCenterPoint(126.956895, 37.504242);
        tMapView.setZoomLevel(17);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        //지도

        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        //현재위치마커
        tmapgps= new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); //위에꺼아래꺼 둘중하나
        tmapgps.OpenGps();
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        Button searchbtn = (Button) findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, Search.class);
                        EditText editSearch1 = (EditText)findViewById(R.id.editSearch1);
                        String keyword=editSearch1.getText().toString();
                        intent.putExtra("SearchKeywordOnMap", keyword);
                        startActivity(intent);
                    }
                }
        );
        //버튼누르면 값전달과함께 화면전환

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.nav_navigation) {
            Intent intent = new Intent(MainActivity.this, Navigation.class);
            startActivity(intent);
        } else if (id == R.id.nav_adminlogin) {
            Intent intent = new Intent(MainActivity.this, ManagerLogin.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode){
            tMapView.setLocationPoint(location.getLongitude(),location.getLatitude());
        }
    }
}