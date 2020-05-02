package hu.bme.aut.android.servicedemo

import android.Manifest
import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import hu.bme.aut.android.servicedemo.service.LocationService
import hu.bme.aut.android.servicedemo.task.GeoCoderTask
import kotlinx.android.synthetic.main.fragment_location_dashboard.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*

@RuntimePermissions
class LocationDashboardFragment : Fragment() {

    private lateinit var tvProviderValue: TextView
    private lateinit var tvLatValue: TextView
    private lateinit var tvLngValue: TextView
    private lateinit var tvSpeedValue: TextView
    private lateinit var tvAltValue: TextView
    private lateinit var tvPosTimeValue: TextView

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val currentLocation = intent.getParcelableExtra<Location>(LocationService.KEY_LOCATION)

            tvLatValue.text = currentLocation.latitude.toString()
            tvLngValue.text = currentLocation.longitude.toString()
            tvAltValue.text = currentLocation.altitude.toString()
            tvSpeedValue.text = currentLocation.speed.toString()
            tvProviderValue.text = currentLocation.provider
            tvPosTimeValue.text = Date(currentLocation.time).toString()
        }
    }

    private var locationServiceBinder: LocationService.ServiceLocationBinder? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, binder: IBinder) {
            locationServiceBinder = binder as LocationService.ServiceLocationBinder
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            locationServiceBinder = null
        }
    }

    override fun onStart() {
        super.onStart()

        val context = requireContext()

        val intent = Intent(context, LocationService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        registerReceiverWithPermissionCheck()
    }

    @NeedsPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun registerReceiver() {
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(locationReceiver, IntentFilter(LocationService.BR_NEW_LOCATION))
    }

    override fun onStop() {
        val context = requireContext()

        if (locationServiceBinder != null) {
            context.unbindService(serviceConnection)
        }

        LocalBroadcastManager.getInstance(context)
            .unregisterReceiver(locationReceiver)

        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvProviderValue = initField(view, R.id.fieldProvider, R.string.txt_provider)
        tvLatValue = initField(view, R.id.fieldLat, R.string.txt_latitude)
        tvLngValue = initField(view, R.id.fieldLng, R.string.txt_longitude)
        tvSpeedValue = initField(view, R.id.fieldSpeed, R.string.txt_speed)
        tvAltValue = initField(view, R.id.fieldAlt, R.string.txt_alt)
        tvPosTimeValue = initField(view, R.id.fieldPosTime, R.string.txt_position_time)

        btnGeocode.setOnClickListener {
            val location = locationServiceBinder?.service?.lastLocation
            if (location != null) {
                GeoCoderTask(requireContext().applicationContext).execute(location)
            }
        }
    }

    private fun initField(fragmentView: View, @IdRes fieldId: Int, @StringRes headTextRes: Int): TextView {
        val viewField = fragmentView.findViewById<View>(fieldId)

        val tvHead = viewField.findViewById<TextView>(R.id.tvHead)
        tvHead.setText(headTextRes)

        return viewField.findViewById(R.id.tvValue)
    }

}