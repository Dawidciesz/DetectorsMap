package com.fiesta.detector.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.fiesta.detector.R
import com.fiesta.detector.adapters.PoiListAdapter
import com.fiesta.detector.data.Poi
import com.fiesta.detector.databinding.MapFragmentBinding
import com.fiesta.detector.fragments.MapFragmentDirections
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, LocationListener,
    PoiListAdapter.OnDetailsClickListener, PoiListAdapter.OnZoomLocationClickListener {
    private val viewModel: MapViewModel by viewModels()
    private var currentLatitude: Double = 0.0
    private var currentLongtitude: Double = 0.0
    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null
    private lateinit var testfoto: ImageView
    private var europeBorder: LatLngBounds? = LatLngBounds(
        LatLng(37.727207309252655, -5.259574103272386),
        LatLng(69.78641538367935, 28.471198463659192)
    );
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var userCurrentLocation: LatLng
    private lateinit var uri: String
    private lateinit var locationManager: LocationManager
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    checkCurrentUserLocatiom()
                }
            }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.map_fragment, container, false)
        mapFragment = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val poiListAdapter = PoiListAdapter(this, this)
        val binding = MapFragmentBinding.bind(view)

        binding.apply {
            button_add_poi.setOnClickListener(View.OnClickListener {
                showFilterDialog(userCurrentLocation)
            })
            poiListRecyclerView.apply {
                adapter = poiListAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mapsEvent.collect { event ->
                when (event) {
                    is MapViewModel.MapEvents.ShowInputMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    uri = data?.data.toString()
//            content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FDCIM%2FCamera%2FIMG_20210206_142941.jpg
                    Snackbar.make(requireView(), "uri = " + data?.data, Snackbar.LENGTH_LONG)
                        .show()
                    context?.contentResolver?.takePersistableUriPermission(
                        data?.data!!,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    testfoto.setImageURI(data?.data)
                }
            }
        viewModel.pois.observe(viewLifecycleOwner) {
            poiListAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mapsEvent.collect { event ->
                when (event) {
                    is MapViewModel.MapEvents.NavigateToEditPoiScreen -> {

                        val action =
                            MapFragmentDirections.actionMapFragment2ToEditPoiFragment(
                                event.poi
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map;
        googleMap!!.setLatLngBoundsForCameraTarget(europeBorder)
        googleMap!!.setMinZoomPreference(6f)
        getCurrentLocation()
        googleMap!!.setOnMapClickListener {
            showFilterDialog(it)
        }
        fun getBitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {

            val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
            val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)

            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
        googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.style_json))
        viewModel.pois.observe(viewLifecycleOwner) {
            for (item in it) {
                googleMap!!.addMarker(
                    MarkerOptions().position(
                        LatLng(item.latitude, item.longtitude)
                    ).title(item.name)
                        .icon((getBitmapDescriptorFromVector(requireContext(), R.drawable.ic_baseline_location_gold_on_24)))
                )
            }
        }
        if (viewModel.poi != null)
            animateCamera(viewModel.poi!!.latitude, viewModel.poi!!.longtitude)
    }

    fun getCurrentLocation() {
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                checkCurrentUserLocatiom()
            } else {
                googleMap!!.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            52.26700762796704,
                            21.023380241941087
                        )
                    )
                )
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun checkCurrentUserLocatiom() {
        googleMap?.isMyLocationEnabled = true
        val gpsLocation: Location? =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (gpsLocation != null) {
            currentLatitude = gpsLocation.latitude
            currentLongtitude = gpsLocation.longitude
            userCurrentLocation = LatLng(currentLatitude, currentLongtitude)
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(userCurrentLocation))
        }
    }

    override fun onLocationChanged(p0: Location?) {}

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    fun showFilterDialog(poiLatLang: LatLng) {

        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_info_fragment)
        dialog.findViewById<Button>(R.id.dialog_accept).setOnClickListener {
            viewModel.onSaveButtonClick(
                dialog.findViewById<EditText>(R.id.dialog_set_name).text.toString(),
                poiLatLang, ""
            )
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.dialog_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDetailsItemClick(poi: Poi) {
        viewModel.onPoiSelected(poi)
    }

    override fun onZoomLocationItemClick(poi: Poi) {
        animateCamera(poi.latitude, poi.longtitude)
    }

    fun animateCamera(lat: Double, long: Double) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
    }
}