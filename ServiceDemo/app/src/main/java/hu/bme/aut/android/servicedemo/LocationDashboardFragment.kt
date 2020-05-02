package hu.bme.aut.android.servicedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

class LocationDashboardFragment : Fragment() {

    private lateinit var tvProviderValue: TextView
    private lateinit var tvLatValue: TextView
    private lateinit var tvLngValue: TextView
    private lateinit var tvSpeedValue: TextView
    private lateinit var tvAltValue: TextView
    private lateinit var tvPosTimeValue: TextView

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
    }

    private fun initField(fragmentView: View, @IdRes fieldId: Int, @StringRes headTextRes: Int): TextView {
        val viewField = fragmentView.findViewById<View>(fieldId)

        val tvHead = viewField.findViewById<TextView>(R.id.tvHead)
        tvHead.setText(headTextRes)

        return viewField.findViewById(R.id.tvValue)
    }

}