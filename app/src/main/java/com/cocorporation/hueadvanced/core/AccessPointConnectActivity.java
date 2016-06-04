package com.cocorporation.hueadvanced.core;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cocorporation.hueadvanced.R;
import com.cocorporation.hueadvanced.core.lightgroup.LightGroupsActivity;
import com.cocorporation.hueadvanced.hue.AccessPointListAdapter;
import com.cocorporation.hueadvanced.hue.WizardAlertDialog;
import com.cocorporation.hueadvanced.hue.data.HueSharedPreferences;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;

public class AccessPointConnectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    public static final String TAG = "Listener";

    private AccessPointListAdapter adapter;
    private PHHueSDK phHueSDK;
    private HueSharedPreferences prefs;
    private PHAccessPoint accessPoint;

    private boolean lastSearchWasIPScan = false;

    private TextView selectBridge;

    private ProgressBar pbar;
    private static final int MAX_TIME = 30;
    private boolean isDialogShowing;
    private boolean authenticationStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_point_connect);
        initializeFloatingActionButton();
        initializeDrawerLayout();
        initializeNavigationView();
        initializeHueAttributes();

        selectBridge = (TextView)findViewById(R.id.select_bridge_textView);

        prefs = HueSharedPreferences.getInstance(getApplicationContext());
        connectIfNotPresent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        phHueSDK.getNotificationManager().unregisterSDKListener(listener);
    }

    /********************
     * Initializers
     ********************/

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
        phHueSDK.getNotificationManager().registerSDKListener(listener);

        adapter = new AccessPointListAdapter(getApplicationContext(), phHueSDK.getAccessPointsFound());

        ListView accessPointList = (ListView) findViewById(R.id.bridge_list);
        accessPointList.setOnItemClickListener(this);
        accessPointList.setAdapter(adapter);

        pbar = (ProgressBar) findViewById(R.id.countdownPB);
        pbar.setVisibility(View.GONE);
        isDialogShowing = false;
        authenticationStarted = false;
    }

    /********************
     * Hue Connect
     ********************/

    private void connectIfNotPresent() {
        accessPoint = prefs.retreiveAccessPoint();

        if (accessPoint != null) {
            if (!phHueSDK.isAccessPointConnected(accessPoint)) {
                WizardAlertDialog.getInstance().showProgressDialog(R.string.connecting, AccessPointConnectActivity.this);
                phHueSDK.connect(accessPoint);
            }
        } else {  // First time use, so perform a bridge search.
            doBridgeSearch();
        }
    }

    public void doBridgeSearch() {
        WizardAlertDialog.getInstance().showProgressDialog(R.string.search_progress, AccessPointConnectActivity.this);
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

        if (id == R.id.nav_quick_remote) {

        } else if (id == R.id.nav_light_details) {
            Intent intent = new Intent(getApplicationContext(), LightGroupsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_connect_hub) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /********************
     * OnItemClickListener
     ********************/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PHAccessPoint accessPoint = (PHAccessPoint) adapter.getItem(position);

        PHBridge connectedBridge = phHueSDK.getSelectedBridge();

        if (connectedBridge != null) {
            String connectedIP = connectedBridge.getResourceCache().getBridgeConfiguration().getIpAddress();
            if (connectedIP != null) {   // We are already connected here:-
                phHueSDK.disableHeartbeat(connectedBridge);
                phHueSDK.disconnect(connectedBridge);
            }
        }
        WizardAlertDialog.getInstance().showProgressDialog(R.string.connecting, AccessPointConnectActivity.this);
        phHueSDK.connect(accessPoint);
    }

    /********************
     * PHSDKListener
     ********************/
    private PHSDKListener listener = new PHSDKListener() {

        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
            Log.w(TAG, "onCacheUpdated");
        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String username) {
            Log.w(TAG, "onBridgeConnected");

            phHueSDK.setSelectedBridge(phBridge);
            phHueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);
            phHueSDK.getLastHeartbeat().put(phBridge.getResourceCache().getBridgeConfiguration() .getIpAddress(), System.currentTimeMillis());
            prefs.setLastConnectedIPAddress(phBridge.getResourceCache().getBridgeConfiguration().getIpAddress());
            prefs.setUsername(username);
            WizardAlertDialog.getInstance().closeProgressDialog();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbar.setVisibility(View.GONE);
                }
            });
            Snackbar.make(findViewById(R.id.countdownPB), R.string.snackbar_connected, Snackbar.LENGTH_LONG)
                    .show();

        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
            Log.w(TAG, "onAuthenticationRequired");
            phHueSDK.startPushlinkAuthentication(phAccessPoint);
            //pbar.setMax(MAX_TIME);
            authenticationStarted = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //pbar.setVisibility(View.VISIBLE);
                    selectBridge.setText(R.string.click_on_bridge);
                    WizardAlertDialog.getInstance().closeProgressDialog();
                    WizardAlertDialog.getInstance().showProgressDialogWithBar(R.string.click_on_bridge, AccessPointConnectActivity.this, MAX_TIME);
                }
            });

            //TODO: Here, instead of starting a new activity, we could create a popup with the image showing "push the button on the bridge"
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
            Log.w(TAG, "onAccessPointsFound");
            WizardAlertDialog.getInstance().closeProgressDialog();
            if (accessPoints != null && accessPoints.size() > 0) {
                phHueSDK.getAccessPointsFound().clear();
                phHueSDK.getAccessPointsFound().addAll(accessPoints);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(phHueSDK.getAccessPointsFound());
                    }
                });
            }
        }

        @Override
        public void onError(int code, final String message) {
            Log.e(TAG, "on Error Called : " + code + ":" + message);

            if (!authenticationStarted) {
                handleErrorSearchPhase(code, message);
            } else {
                handleErrorAuthenticationPhase(code, message);
            }
        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {
            Log.v(TAG, "onConnectionResumed" + phBridge.getResourceCache().getBridgeConfiguration().getIpAddress());
            if (AccessPointConnectActivity.this.isFinishing())
                return;

            phHueSDK.getLastHeartbeat().put(phBridge.getResourceCache().getBridgeConfiguration().getIpAddress(), System.currentTimeMillis());
            for (int i = 0; i < phHueSDK.getDisconnectedAccessPoint().size(); i++) {

                if (phHueSDK.getDisconnectedAccessPoint().get(i).getIpAddress().equals(phBridge.getResourceCache().getBridgeConfiguration().getIpAddress())) {
                    phHueSDK.getDisconnectedAccessPoint().remove(i);
                }
            }
        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {
            Log.v(TAG, "onConnectionLost : " + phAccessPoint.getIpAddress());
            if (!phHueSDK.getDisconnectedAccessPoint().contains(accessPoint)) {
                phHueSDK.getDisconnectedAccessPoint().add(accessPoint);
            }
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {
            Log.w(TAG, "onParsingErrors");
            for (PHHueParsingError parsingError : list) {
                Log.e(TAG, "ParsingError : " + parsingError.getMessage());
            }
        }
    };

    private void handleErrorAuthenticationPhase(int code, final String message) {
        if (code == PHMessageType.PUSHLINK_BUTTON_NOT_PRESSED) { //From this point, this is the responses to consider whenever we are trying to connect
            incrementProgress();
        } else if (code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
            incrementProgress();

            if (!isDialogShowing) {
                isDialogShowing = true;
                AccessPointConnectActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AccessPointConnectActivity.this);
                        builder.setMessage(message).setNeutralButton(R.string.btn_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });

                        builder.create();
                        builder.show();
                    }
                });
            }
        }
    }

    private void handleErrorSearchPhase(int code, final String message) {
        if (code == PHHueError.NO_CONNECTION) {
            Log.w(TAG, "On No Connection");
        } else if (code == PHHueError.AUTHENTICATION_FAILED || code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
            WizardAlertDialog.getInstance().closeProgressDialog();
            WizardAlertDialog.getInstance().showErrorDialog(AccessPointConnectActivity.this, getApplicationContext().getString(R.string.authentication_failed_message), R.string.btn_ok);
        } else if (code == PHHueError.BRIDGE_NOT_RESPONDING) {
            Log.w(TAG, "Bridge Not Responding . . . ");
            WizardAlertDialog.getInstance().closeProgressDialog();
            AccessPointConnectActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WizardAlertDialog.showErrorDialog(AccessPointConnectActivity.this, message, R.string.btn_ok);
                }
            });

        } else if (code == PHMessageType.BRIDGE_NOT_FOUND) { //TODO: it seems that there is a bug, when there is no connection, we receive this error
            if (!lastSearchWasIPScan) {  // Perform an IP Scan (backup mechanism) if UPNP and Portal Search fails.
                phHueSDK = PHHueSDK.getInstance();
                PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
                sm.search(false, false, true);
                lastSearchWasIPScan = true;
            } else {
                WizardAlertDialog.getInstance().closeProgressDialog();
                AccessPointConnectActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WizardAlertDialog.showErrorDialog(AccessPointConnectActivity.this, message + "\n" + getApplicationContext().getString(R.string.ask_connected), R.string.btn_ok);
                    }
                });
            }
        }
    }

    public void incrementProgress() {
        WizardAlertDialog.getInstance().incrementProgressDialogWithBar(1);
    }
}
