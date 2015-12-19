package com.cocorporation.hueadvanced.core;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cocorporation.hueadvanced.R;
import com.cocorporation.hueadvanced.hue.AccessPointConnectListener;
import com.cocorporation.hueadvanced.hue.AccessPointListAdapter;
import com.cocorporation.hueadvanced.hue.PHWizardAlertDialog;
import com.cocorporation.hueadvanced.hue.data.HueSharedPreferences;
import com.cocorporation.hueadvanced.messages.MessageReceiver;
import com.cocorporation.hueadvanced.messages.MessagesManager;
import com.cocorporation.hueadvanced.messages.MessagesType;
import com.cocorporation.hueadvanced.shared.Util;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;

public class AccessPointConnectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, MessageReceiver {

    private AccessPointListAdapter adapter;
    private PHHueSDK phHueSDK;
    private HueSharedPreferences prefs;
    private PHAccessPoint accessPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_point_connect);
        initializeFloatingActionButton();
        initializeDrawerLayout();
        initializeNavigationView();
        initializeHueAttributes();

        MessagesManager.getInstance().registerForMessage(MessagesType.UPDATE_LIST_OF_ACCESS_POINTS, this);
        prefs = HueSharedPreferences.getInstance(getApplicationContext());
        connectIfNotPresent();
    }

    private void initializeNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeDrawerLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initializeHueAttributes() {
        phHueSDK = PHHueSDK.getInstance();

        // Set the Device Name (name of your app). This will be stored in your bridge whitelist entry.
        phHueSDK.setAppName("HueAdvancedApp");
        phHueSDK.setDeviceName(android.os.Build.MODEL);

        // Register the PHSDKListener to receive callbacks from the bridge.
        AccessPointConnectListener listener = new AccessPointConnectListener();
        phHueSDK.getNotificationManager().registerSDKListener(listener);

        adapter = new AccessPointListAdapter(getApplicationContext(), phHueSDK.getAccessPointsFound());

        ListView accessPointList = (ListView) findViewById(R.id.bridge_list);
        accessPointList.setOnItemClickListener(this);
        accessPointList.setAdapter(adapter);
    }

    private void connectIfNotPresent(){
        accessPoint = prefs.retreiveAccessPoint();

        if (Util.isIPAddressValid(accessPoint.getIpAddress())) {
            if (!phHueSDK.isAccessPointConnected(accessPoint)) {
                PHWizardAlertDialog.getInstance().showProgressDialog(R.string.connecting, AccessPointConnectActivity.this);
                phHueSDK.connect(accessPoint);
            }
        }
        else {  // First time use, so perform a bridge search.
            doBridgeSearch();
        }
    }

    public void doBridgeSearch() {
        PHWizardAlertDialog.getInstance().showProgressDialog(R.string.search_progress, AccessPointConnectActivity.this);
        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        // Start the UPNP Searching of local bridges.
        sm.search(true, true);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_connect_hub) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void receiveMessage(Object message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.updateData(phHueSDK.getAccessPointsFound());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
