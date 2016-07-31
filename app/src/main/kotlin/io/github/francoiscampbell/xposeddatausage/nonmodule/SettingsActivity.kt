package io.github.francoiscampbell.xposeddatausage.nonmodule

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-15.
 */
class SettingsActivity() : AppCompatActivity() {

    private val TAG = "SettingsActivity"
    private val REQUEST_CODE_RESOLUTION = 1
    private lateinit var googleApiClient: GoogleApiClient

    private val connectionCallbacks = object : GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(p0: Bundle?) {
            Log.i(TAG, "API client connected.")
        }

        override fun onConnectionSuspended(p0: Int) {
            Log.i(TAG, "GoogleApiClient connection suspended")
        }
    }

    private val onConnectionFailedListener = { connectionResult: ConnectionResult ->
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION)
            } catch (e: IntentSender.SendIntentException) {
                // Unable to resolve, message user appropriately
            }

        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.errorCode, 0).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        fragmentManager.beginTransaction().replace(R.id.prefs_container, SettingsFragment()).commit()

        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .build()
    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient.disconnect()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            REQUEST_CODE_RESOLUTION -> if (resultCode == RESULT_OK) googleApiClient.connect()
        }
    }
}