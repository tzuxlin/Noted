package com.connie.noted.explore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.connie.noted.NaviDirections
import com.connie.noted.databinding.FragmentExploreBinding
import com.connie.noted.ext.getVmFactory

class ExploreFragment : Fragment() {

    private val viewModel by viewModels<ExploreViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExploreBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val boardPopularRecyclerView = binding.explorePopularRecycler

        boardPopularRecyclerView.adapter =
            ExplorePopularAdapter(ExplorePopularAdapter.OnClickListener { board ->

                Log.i("Connie", "Board is clicked, $board")
                findNavController().navigate(
                    NaviDirections.actionGlobalBoardPageFragment(
                        board
                    )
                )

            }, viewModel)


        val boardRecommendRecyclerView = binding.exploreRecommendRecycler

        boardRecommendRecyclerView.adapter =
            ExploreRecommendAdapter(ExploreRecommendAdapter.OnClickListener { board ->

                Log.i("Connie", "Board is clicked, $board")
                findNavController().navigate(
                    NaviDirections.actionGlobalBoardPageFragment(
                        board
                    )
                )

            }, viewModel)

//        viewModel.popularBoards.observe(viewLifecycleOwner, Observer {
//            boardPopularRecyclerView.scrollToPosition(0)
//        })

        return binding.root

    }

}