package com.nads.shopping.ui.home.bus

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues


import android.content.Context
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentToysBusBinding
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.nads.shopping.utils.permissionRationaleDialog
import com.nads.shopping.viewmodels.DressBusViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

//Getdevice location method is disabled

class ToysBus : Fragment(),OnMapReadyCallback {
    private lateinit var maps: MapView
    private val dressBusViewModel: DressBusViewModel by viewModels()
    private lateinit var googlemaps: GoogleMap
    private lateinit var cameraPosition: CameraPosition
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var binding: FragmentToysBusBinding
    private var locationPermissionGranted = false
    private lateinit  var lastKnownLocation:Location
    private val KEY_CAMERA_POSITION = "camera_position"
    private val KEY_LOCATION = "location"
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private val DEFAULT_ZOOM = 50
    private lateinit var findCurrentPlaceRequest:FindCurrentPlaceRequest
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)
    var calendar = Calendar.getInstance()

    var cal = arrayListOf<Calendar>()
    var cars = cal.toMutableList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationpermissions()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding  = DataBindingUtil.inflate(inflater,
            R.layout.fragment_toys_bus, container, false)
        locationpermissions()
        binding.lifecycleOwner= viewLifecycleOwner
         binding.dressbusviewmodel = dressBusViewModel
        Places.initialize(requireActivity().applicationContext, getString(R.string.google_maps_key));
        placesClient = Places.createClient(requireActivity());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)!!;
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)!!;
        }


        maps = binding.mapView
        maps.getMapAsync(this@ToysBus)
        maps.onCreate(savedInstanceState)
        initialized()
        val appbar = requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)
        appbar.isVisible = false


        dressBusViewModel.getHolidays(2)
        var j = 0
        dressBusViewModel.holidayslot.observe(viewLifecycleOwner) {

            it.forEach {
                val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                calendar[Calendar.YEAR] = date.year
                calendar[Calendar.MONTH] = date.monthValue
                calendar[Calendar.DAY_OF_MONTH] = date.dayOfMonth
                cars = cal.toMutableList()
                cars.add(j,calendar)
              j+=1
            }
           cars.forEach{
               Log.e("NADATES", it.toString())
           }



        }
      //  binding.datetexttoybus.transformIntoDatePicker(requireContext(), "MM/dd/yyyy")
        binding.datetexttoybus.transformIntoDatePicker(requireContext(), "MM/dd/yyyy", Date())

  //back button
        binding.toybusback.setOnClickListener {
            findNavController().navigateUp()
        }


        dressBusViewModel.bookingsuccess.observe(viewLifecycleOwner) {
            if (it.toString() ==
                "Successfully received your booking. we will arrive on time, Thank you")
            {
                Toast.makeText(requireContext(),
                    "Successfully received your booking. we will arrive on time, Thank you", Toast.LENGTH_LONG)
                    .show()
            }
        }






        dressBusViewModel.makevisible.observe(viewLifecycleOwner){

        }
        dressBusViewModel.locationlistened.observe(viewLifecycleOwner){
            binding.locationtext.text = it
        }
        binding.applydressbus.setOnClickListener{
            dressBusViewModel.getBooking("2","b","r","a","n",
                "d","a","t","t","y","b","u","s")
        }
        return binding.root
    }







    fun initialized(){

        val appbar = requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)
        appbar.isVisible = true
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

            if (locationPermissionGranted) {
                updateLocationUI()
                //getDeviceLocation(googlemaps)
                //showCurrentPlace()
            }else{
                locationpermissions()
            }





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
                    Log.e(ContentValues.TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i(ContentValues.TAG, "The user did not grant location permission.")

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
    fun getDeviceLocation(googlemaps: GoogleMap) {

        try {
            if (locationPermissionGranted) {
                val locationResult: Task<Location> = fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener(requireActivity(),
                    OnCompleteListener<Location?> { task ->

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
                            Log.d(ContentValues.TAG, "Current location is null. Using defaults.")
                            Log.e(ContentValues.TAG, "Exception: %s", task.exception)
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

    fun TextInputEditText.transformIntoDatePicker(context: Context,
                                                  format: String,
                                                  maxDate: Date? = null) {
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

            DatePickerDialog.newInstance(datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {

                val cale = cal.toTypedArray()
//                cale.forEach {
//                    Log.e("NadeemDate",it.toString())
//                }
                disabledDays = cale
                show(this@ToysBus.childFragmentManager,"Datepickerdialog")
            }

        }
    }


}