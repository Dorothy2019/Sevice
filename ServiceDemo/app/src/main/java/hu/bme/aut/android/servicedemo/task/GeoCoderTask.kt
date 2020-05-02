package hu.bme.aut.android.servicedemo.task

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.widget.Toast
import java.util.*

@Suppress("FoldInitializerAndIfToElvis")
@SuppressLint("StaticFieldLeak")
class GeoCoderTask(private val context: Context) : AsyncTask<Location, Unit, String>() {

    override fun doInBackground(vararg params: Location): String {
        val result = StringBuilder()

        val location = params[0]

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address: Address? = geocoder.getFromLocation(location.latitude, location.longitude, 1).firstOrNull()

            if (address == null) {
                throw RuntimeException("No address found")
            }

            for (i in 0..address.maxAddressLineIndex) {
                result.append(address.getAddressLine(i))

                if (i != address.maxAddressLineIndex) {
                    result.append("\n")
                }
            }
        } catch (e: Exception) {
            result.append("No address: ")
            result.append(e.message)
        }

        return result.toString()
    }

    override fun onPostExecute(address: String) {
        Toast.makeText(context, address, Toast.LENGTH_LONG).show()
    }

}