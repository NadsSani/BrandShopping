package com.nads.shopping.ui.home.bus

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.nads.shopping.databinding.FragmentDressBusBinding
import java.text.SimpleDateFormat
import java.util.*


import androidx.fragment.app.viewModels
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.nads.shopping.R
import com.nads.shopping.utils.permissionRationaleDialog
import com.nads.shopping.viewmodels.DressBusViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class DressBus : Fragment(),OnMapReadyCallback,LocationListener {


    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var maps: MapView
    private val dressBusViewModel: DressBusViewModel by viewModels()
    private lateinit var googlemaps: GoogleMap
    private lateinit var cameraPosition: CameraPosition
    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
     private lateinit var placesClient: PlacesClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private val DEFAULT_ZOOM = 50
    private var locationPermissionGranted = false
    private lateinit  var lastKnownLocation:Location
    private val KEY_CAMERA_POSITION = "camera_position"
    private val KEY_LOCATION = "location"

    private lateinit var findCurrentPlaceRequest:FindCurrentPlaceRequest

    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)
   private lateinit var  binding:FragmentDressBusBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CAMERA_POSITION, googlemaps.getCameraPosition())
        outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        super.onSaveInstanceState(outState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_dress_bus, container, false)
        locationpermissions()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dressbusviewmodel = dressBusViewModel

        maps = binding.mapView
        maps.onCreate(savedInstanceState)
        maps.getMapAsync(this)
        Places.initialize(requireActivity().applicationContext, getString(R.string.google_maps_key));
        placesClient = Places.createClient(requireActivity());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)!!;
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)!!;
        }

        initialized()

        //back button
        binding.dressBusback.setOnClickListener {
            findNavController().navigateUp()
        }

//


        // binding.datetext.transformIntoDatePicker(requireContext(), "MM/dd/yyyy")

            binding.datetext.transformIntoDatePicker(requireContext(), "MM/dd/yyyy", Date())

        // make the timeslot visible
        dressBusViewModel.makevisible.observe(viewLifecycleOwner){

        }
       dressBusViewModel.bookingsuccess.observe(viewLifecycleOwner) {
          if (it.toString() ==
              "Successfully received your booking. we will arrive on time, Thank you")
          {
              Toast.makeText(requireContext(),
                  "Successfully received your booking. we will arrive on time, Thank you",Toast.LENGTH_LONG)
                  .show()
          }
       }
        dressBusViewModel.locationlistened.observe(viewLifecycleOwner){
            binding.locationtext.text = it

        }

        binding.applydressbus.setOnClickListener{
            dressBusViewModel.getBooking("1","b","r","a","n",
                "d","a","t","t","y","b","u","s")
        }

        return binding.root
    }




    fun initialized(){
        val appbar = requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)
        appbar.isVisible = false

        val bottomnavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomnavigation.isVisible =true

    }

    override fun onMapReady(googlemap: GoogleMap) {
           this.googlemaps = googlemap
        // info window contents.
        this.googlemaps?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents,
                    binding.mapView, false)
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })
        this.googlemaps.setOnMapClickListener {
            val defaultLocations = LatLng(it.latitude,it.longitude)

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            var addressList: List<Address>? = null
            addressList = geocoder.getFromLocation(it.latitude,it.longitude,1)
            addressList[0]?.thoroughfare?.toString()?.let { it1 ->
                dressBusViewModel.locationlistened.value = it1 }
            googlemaps.clear()
            googlemaps?.addMarker(
                MarkerOptions()
                    .title("Your Delivery Location")
                    .position(defaultLocations)
                    .snippet("Maps"))
        }

        locationpermissions()
        Handler().postDelayed({
            if (locationPermissionGranted) {
                updateLocationUI()
               getDeviceLocation(googlemaps)
             //   showCurrentPlace()
            }else{
                locationpermissions()
            }
        },2000)



    }


fun locationpermissions(){
    Dexter.withContext(requireActivity())
        .withPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(permissions: MultiplePermissionsReport?) {
                if (permissions?.areAllPermissionsGranted() == true)
                {


                    locationPermissionGranted = true


                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: MutableList<PermissionRequest>?,
                token: PermissionToken?,
            ) {
                requireActivity().permissionRationaleDialog(
                    token,
                    "Location Permission",
                    "Please allow us to access your location to locate bikes near you."
                )
            }

        }).check()

}






    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (googlemaps == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < 5) {
                        likelyPlaces.placeLikelihoods.size
                    } else {
                        5
                    }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                openPlacesDialog()
                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            googlemaps?.addMarker(
                MarkerOptions()
                .title("Your Location")
                .position(defaultLocation)
                .snippet("Maps"))

            // Prompt the user for permission.
      locationpermissions()
        }
    }






    override fun onPause() {
        super.onPause()
        maps.onPause()
    }

    override fun onStart() {
        super.onStart()
        maps.onStart()
        locationpermissions()
    }

    override fun onStop() {
        super.onStop()
        maps.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        maps.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        maps.onLowMemory()
    }





//==============================
    private fun updateLocationUI() {
        if (googlemaps == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                Log.e("Nadeem","all enabled")
                googlemaps.setMyLocationEnabled(true)
                googlemaps.getUiSettings().setMyLocationButtonEnabled(true)
                googlemaps.getUiSettings().isZoomControlsEnabled = true
                googlemaps.uiSettings.setAllGesturesEnabled(true)

            } else {
                googlemaps.setMyLocationEnabled(false)
                googlemaps.getUiSettings().setMyLocationButtonEnabled(false)
                locationpermissions()

            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.toString())
        }
    }

//================Just for suspend api ============================

 suspend fun getLastknownLocation():Location{
     return suspendCoroutine { continuation ->
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
         fusedLocationProviderClient.lastLocation.addOnCompleteListener{
            task->
            continuation.resume(task.result)
        }
     }

 }
//====================================
 fun getDeviceLocation(googlemaps: GoogleMap) {

    try {
        if (locationPermissionGranted) {
            val locationResult: Task<Location> = fusedLocationProviderClient.lastLocation

            locationResult.addOnCompleteListener(requireActivity(),
                OnCompleteListener<Location?> { task ->
                    Log.e("nadeem",task.isSuccessful.toString())
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device
                        task.result.let { lastKnownLocation = it }
                        lastKnownLocation.let {  googlemaps.moveCamera(
                            it.let {  CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                ), DEFAULT_ZOOM.toFloat()
                            )
                            }
                        ) }

                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        googlemaps.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        googlemaps.getUiSettings().setMyLocationButtonEnabled(false)
                    }
                })
        }
    } catch (e: SecurityException) {
        Log.e("Exception: %s", e.message, e)
    }
}




//===============================================
private fun openPlacesDialog() {
    // Ask the user to choose the place where they are now.
    val listener = DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
        val markerLatLng = likelyPlaceLatLngs[which]
        var markerSnippet = likelyPlaceAddresses[which]
        if (likelyPlaceAttributions[which] != null) {
            markerSnippet = """
                $markerSnippet
                ${likelyPlaceAttributions[which]}
                """.trimIndent()
        }

        // Add a marker for the selected place, with an info window
        // showing information about that place.
        googlemaps?.addMarker(MarkerOptions()
            .title(likelyPlaceNames[which])
            .position(markerLatLng!!)
            .snippet(markerSnippet))

        // Position the map's camera at the location of the marker.
        googlemaps?.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
            DEFAULT_ZOOM.toFloat()))
    }

    // Display the dialog.
    AlertDialog.Builder(this.requireContext())
        .setTitle("Pick A place")
        .setItems(likelyPlaceNames, listener)
        .show()
}

    override fun onLocationChanged(p0: Location?) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if (locationPermissionGranted) {
            val locationResult: Task<Location> = fusedLocationProviderClient.lastLocation

            locationResult.addOnCompleteListener(this.requireActivity(),
                OnCompleteListener<Location?> { task ->
                    Log.e("nadeem",task.isSuccessful.toString())
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            googlemaps.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation.latitude,
                                        lastKnownLocation.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        googlemaps.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        googlemaps.getUiSettings().setMyLocationButtonEnabled(false)
                    }
                })
        }

    }


    //=====================================

    fun TextInputEditText.transformIntoDatePicker(context: Context,
                                                  format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))


                val calendar = Calendar.getInstance()
                calendar[Calendar.YEAR]
                calendar[Calendar.MONTH]
                calendar[Calendar.DAY_OF_MONTH]

                val date = calendar.time
                val formatted = sdf.format(date)

                Log.e("DateFormatted",formatted.toString())
                dressBusViewModel.getTimeSloter("1",formatted.toString())
            }


        setOnClickListener {

            val calendar = Calendar.getInstance()
            dressBusViewModel.getHolidays(1)
            dressBusViewModel.holidayslot.observe(viewLifecycleOwner) {
                calendar[Calendar.DAY_OF_MONTH] = 20//it.date.toInt()
                it.forEach {
                    val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDate.parse(it.date.toString(), DateTimeFormatter.ISO_DATE)
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    calendar[Calendar.YEAR] = date.year
                    calendar[Calendar.MONTH] = date.monthValue
                    calendar[Calendar.DAY_OF_MONTH] = date.dayOfMonth
                    Log.e("Dates", calendar.toString())
                }
                DatePickerDialog.newInstance(
                    datePickerOnDataSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).run {
                    disabledDays = arrayOf(calendar)
                    show(this@DressBus.childFragmentManager, "Datepickerdialog")
                }

            }

        }


    }

}