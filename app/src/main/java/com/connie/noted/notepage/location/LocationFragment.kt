package com.connie.noted.notepage.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.connie.noted.BuildConfig
import com.connie.noted.databinding.FragmentNoteLocationBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.notepage.article.ArticleFragmentArgs
import com.connie.noted.util.Util.getWindowWidth
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class LocationFragment : Fragment() {

    private val googleKey = BuildConfig.GOOGLE_KEY
    private val width = getWindowWidth()
    private val zoomSize = 16

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

        Log.i("Connie", "Location Fragment")

        "https://maps.googleapis.com/maps/api/staticmap?center=25.036382,121.5441433&zoom=${zoomSize}&size=${width}x400&key=$googleKey"


        val binding = FragmentNoteLocationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel



        viewModel.noteKey.images.add(0, "https://maps.googleapis.com/maps/api/staticmap?center=25.036382,121.5441433&zoom=${zoomSize}&size=${width}x400&key=$googleKey")
        viewModel.note.value = viewModel.noteKey





        return binding.root
    }


//    protected override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Retrieve the content view that renders the map.
//        setContentView(R.layout.activity_maps)
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

//    }

}