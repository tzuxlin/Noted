package com.connie.noted.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.connie.noted.MainViewModel
import com.connie.noted.NaviDirections
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.databinding.FragmentExploreBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.login.UserManager
import com.connie.noted.util.DialogBoxMessageType
import com.connie.noted.util.Logger

class ExploreFragment : Fragment() {

    private val viewModel by viewModels<ExploreViewModel> { getVmFactory() }
    private lateinit var mainViewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExploreBinding.inflate(inflater, container, false)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val boardPopularRecyclerView = binding.explorePopularRecycler

        boardPopularRecyclerView.adapter =
            ExplorePopularAdapter(ExplorePopularAdapter.OnClickListener { board ->

                Logger.i("Board is clicked, $board")
                findNavController().navigate(
                    NaviDirections.actionGlobalBoardPageFragment(
                        board
                    )
                )

            }, viewModel)


        val boardRecommendRecyclerView = binding.exploreRecommendRecycler

        boardRecommendRecyclerView.adapter =
            ExploreRecommendAdapter(ExploreRecommendAdapter.OnClickListener { board ->

                Logger.i("Board is clicked, $board")
                findNavController().navigate(
                    NaviDirections.actionGlobalBoardPageFragment(
                        board
                    )
                )

            }, viewModel)

        binding.exploreRecommendHelp.setOnClickListener {
            findNavController().navigate(NaviDirections.actionGlobalTagDialog())
        }

        viewModel.popularBoards.observe(viewLifecycleOwner, Observer {

            Logger.d("Fragment, popularBoards = $it")

            it?.let {
                if (viewModel.status.value == LoadApiStatus.LOADING) {
                    viewModel.loadApiStatusDone()
                }
            }
        })

        checkTags()

        viewModel.doObserveSearch.observe(viewLifecycleOwner, Observer { b ->

            b?.let {

                if (it) {


                    viewModel.searchBoards.observe(viewLifecycleOwner, Observer { board ->

                        Logger.d("Fragment, searchBoards = $board")

                        board?.let { searchResult ->

                            viewModel.loadApiStatusDone()

                            if (searchResult.isEmpty()) {
                                findNavController().navigate(
                                    NaviDirections.actionGlobalBoxDialog(
                                        DialogBoxMessageType.NOT_FOUND.message
                                    )
                                )
                            }


                        }
                    })

                    viewModel.onSearchedObserved()
                }
            }
        })

        mainViewModel.userIsSynced.observe(viewLifecycleOwner, Observer {

            it?.let { b ->
                if (b) {
                    Logger.d("viewModel.getRecommendBoards()")

                    viewModel.getRecommendBoards()
                }
            }
        })

        viewModel.doObserveRecommend.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    viewModel.recommendBoards.observe(viewLifecycleOwner, Observer { boards ->
                        (boardRecommendRecyclerView.adapter as ExploreRecommendAdapter).submitList(
                            boards
                        )

                    })


                    viewModel.onRecommendObserved()

                }
            }
        })

        return binding.root

    }

    private fun checkTags() {

        val followingTags = (UserManager.user.value?.followingTags) ?: listOf<String?>()

        if (followingTags.isEmpty()) {
            findNavController().navigate(NaviDirections.actionGlobalTagDialog())
        }
    }

}