package uz.mobiler.onlinesavdo.fragment

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field.NAME
import com.google.android.libraries.places.api.net.PlacesClient
import org.greenrobot.eventbus.EventBus
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.FragmentMapsBinding
import uz.mobiler.onlinesavdo.model.AddressModel

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private var locationPermissionGranted=false
    private  var lastKnownLocation:Location?=null
    private val TAG = "MapsFragment"

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        updateLocationUi()
        getDeviceLocation()
    }

    companion object{
        val REQUEST_KEY = "request_key"
        val BUNDLE_KEY = "bundle_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        binding.apply {
            confirmAddress.setOnClickListener {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                val addressModel = AddressModel(
                    "",
                    mMap.cameraPosition.target.latitude,
                    mMap.cameraPosition.target.longitude,
                )
                EventBus.getDefault().post(addressModel)
                setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to addressModel))
                Navigation.findNavController(root).popBackStack()
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        Places.initialize(requireContext(), getString(R.string.api_key))
        placesClient = Places.createClient(requireActivity())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val locationPermissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            locationPermissionGranted=true
            updateLocationUi()
            getDeviceLocation()
        }
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), 8.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap.moveCamera(
                            CameraUpdateFactory
                            .newLatLngZoom(LatLng(41.34093109206405,69.28673341870308), 8.toFloat()))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun updateLocationUi() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}