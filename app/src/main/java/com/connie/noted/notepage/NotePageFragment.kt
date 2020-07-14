package com.connie.noted.notepage

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.connie.noted.R

class NotePageFragment : Fragment() {

    companion object {
        fun newInstance() = NotePageFragment()
    }

    private lateinit var viewModel: NotePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotePageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}