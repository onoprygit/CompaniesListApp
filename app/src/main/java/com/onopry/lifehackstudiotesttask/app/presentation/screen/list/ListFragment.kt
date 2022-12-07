package com.onopry.lifehackstudiotesttask.app.presentation.screen.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.onopry.lifehackstudiotesttask.R
import com.onopry.lifehackstudiotesttask.app.presentation.adapter.CompaniesPreviewAdapter
import com.onopry.lifehackstudiotesttask.app.presentation.utils.debugLog
import com.onopry.lifehackstudiotesttask.app.presentation.utils.gone
import com.onopry.lifehackstudiotesttask.app.presentation.utils.hide
import com.onopry.lifehackstudiotesttask.app.presentation.utils.safeFlowCallScope
import com.onopry.lifehackstudiotesttask.app.presentation.utils.show
import com.onopry.lifehackstudiotesttask.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()
    private val adapter: CompaniesPreviewAdapter by lazy {
        CompaniesPreviewAdapter { openDetailsScreen(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        binding.companiesRecycler.gone()
        binding.shimmer.show()
        binding.shimmer.startShimmer()
        handleScreenState()
    }

    private fun setupRefresh() {
        binding.swipeToRefresh.setOnRefreshListener {
            debugLog("Refresh gesture captured")
            viewModel.loadRefreshState(true)
        }
        safeFlowCallScope {
            viewModel.isRefreshState.collectLatest { isRefreshing ->
                debugLog("RefreshState is [$isRefreshing]")
                binding.swipeToRefresh.isRefreshing = isRefreshing
                if (isRefreshing) {
                    binding.companiesRecycler.gone()
                    binding.shimmer.show()
                } else {
                    binding.shimmer.gone()
                }
            }
        }
    }

    private fun handleScreenState() {
        binding.companiesRecycler.adapter = adapter
        setupRefresh()
        safeFlowCallScope {
            viewModel.screenState.collect { state ->
                when (state) {
                    is ListState.Content -> handleContentState(state)
                    is ListState.Loading -> handleLoadingState()
                    is ListState.Exception -> handleExceptionState(state)
                    is ListState.Error -> handleErrorState(state.msg)

                }
            }
        }
    }

    private fun handleContentState(state: ListState.Content) {
        binding.errorPart.errorImage.gone()
        binding.errorPart.errorMessageTv.gone()
        binding.errorPart.tryAgainButton.gone()
        binding.companiesRecycler.show()
        binding.shimmer.stopShimmer()
        binding.shimmer.gone()
        setupRecycler(state)
    }

    private fun handleErrorState(msg: String) {
        binding.companiesRecycler.hide()
        binding.shimmer.stopShimmer()
        binding.shimmer.gone()
        with(binding.errorPart) {
            errorImage.show()
            errorMessageTv.show()
            errorMessageTv.text = msg
            tryAgainButton.show()
            tryAgainButton.setOnClickListener {
                viewModel.loadRefreshState(true)
            }
        }
        binding.shimmer.stopShimmer()
        binding.shimmer.gone()
    }

    private fun handleLoadingState() {
        binding.errorPart.errorImage.gone()
        binding.errorPart.errorMessageTv.gone()
        binding.errorPart.tryAgainButton.gone()

        binding.companiesRecycler.gone()
        binding.shimmer.startShimmer()
        binding.shimmer.show()
        //        binding.swipeToRefresh.isRefreshing = false
    }

    private fun handleExceptionState(state: ListState.Exception) {
        binding.companiesRecycler.hide()
        binding.shimmer.stopShimmer()
        binding.shimmer.gone()
        binding.errorPart.tryAgainButton.gone()
        binding.errorPart.errorImage.show()
        binding.errorPart.errorMessageTv.show()
        binding.errorPart.errorMessageTv.text = state.msg
    }


    private fun setupRecycler(state: ListState.Content) {
        val adapter = CompaniesPreviewAdapter { openDetailsScreen(it) }
        binding.companiesRecycler.adapter = adapter
        adapter.setList(state.companies)
    }

    private fun openDetailsScreen(id: String) {
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment(id)
        findNavController().navigate(action)
    }
}