package com.connie.noted.notepage.location

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.connie.noted.BuildConfig
import com.connie.noted.NotedApplication
import com.connie.noted.databinding.FragmentNoteLocationBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.notepage.article.ArticleFragmentArgs
import com.connie.noted.util.Logger
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val ZOOM_SCALE = 17F

class LocationFragment : Fragment(), OnMapReadyCallback {

    private val googleKey = BuildConfig.GOOGLE_KEY

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var latLng: LatLng

    private val viewModel by viewModels<LocationViewModel> {
        getVmFactory(
            ArticleFragmentArgs.fromBundle(
                requireArguments()
            ).noteKey
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentNoteLocationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        mapView = binding.noteMapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try {
            MapsInitializer.initialize(NotedApplication.instance.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync(this)

        viewModel.note.value = viewModel.noteKey
        getLocationFromAddress()

        viewModel.navigateToUrl.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it) {
                    val uri = Uri.parse(viewModel.noteKey.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)

                    viewModel.onUrlIntentCompleted()
                }
            }
        })


        return binding.root
    }



    private fun getLocationFromAddress(){
        val coder = Geocoder(NotedApplication.instance)
        val result: MutableList<Address>
        try {

           result = coder.getFromLocationName(viewModel.note.value?.title, 1)

           latLng = LatLng(result[0].latitude, result[0].longitude)

        } catch (e: Exception) {
            Logger.w(e.toString())
        }
    }



    override fun onMapReady(map: GoogleMap) {

        googleMap = map
        setUpMap()

    }

    private fun setUpMap(){

        val marker = MarkerOptions().position(latLng).title(viewModel.note.value?.title)

        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        val cameraPosition =
            CameraPosition.builder().target(latLng).zoom(ZOOM_SCALE).build()

        googleMap.apply {
            addMarker(marker)

            isBuildingsEnabled = true

            uiSettings.apply {
                isZoomControlsEnabled = true
                isMapToolbarEnabled = true
                setAllGesturesEnabled(true)
            }
            animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }





    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}