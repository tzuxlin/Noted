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
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.databinding.FragmentExploreBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.util.DialogBoxMessageType
import io.grpc.Status.NOT_FOUND

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

        binding.exploreRecommendHelp.setOnClickListener {
            findNavController().navigate(NaviDirections.actionGlobalTagDialog())
        }

        viewModel.recommendBoards.observe(viewLifecycleOwner, Observer {

            Log.e("Connie", "Fragment, recommendBoards = $it")

            it?.let {
                viewModel.popularBoards.value?.let {
                    if (viewModel.status.value == LoadApiStatus.LOADING) {viewModel.loadApiStatusDone()}
                }
            }
        })

        viewModel.popularBoards.observe(viewLifecycleOwner, Observer {

            Log.e("Connie", "Fragment, popularBoards = $it")

            it?.let {
                viewModel.recommendBoards.value?.let {
                    if (viewModel.status.value == LoadApiStatus.LOADING) {viewModel.loadApiStatusDone()}
                }
            }
        })

        viewModel.searchBoards.observe(viewLifecycleOwner, Observer {

            Log.e("Connie", "Fragment, searchBoards = $it")

            it?.let { searchResult ->

                viewModel.loadApiStatusDone()

                when {
                    searchResult.isEmpty() -> {
                        findNavController().navigate(NaviDirections.actionGlobalBoxDialog(DialogBoxMessageType.NOT_FOUND.message))
                    }




                }

            }
        })

//        viewModel.popularBoards.observe(viewLifecycleOwner, Observer {
//            boardPopularRecyclerView.scrollToPosition(0)
//        })

        return binding.root

    }

}