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
import com.onopry.lifehackstudiotesttask.app.presentation.utils.safeObserveFlow
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
        handleScreenState()
    }

    private fun setupRefresh() {
        binding.swipeToRefresh.setOnRefreshListener {
            debugLog("Refresh gesture captured")
            viewModel.loadRefreshState(true)
        }
        safeObserveFlow {
            viewModel.isRefreshState.collectLatest { isRefreshing ->
                debugLog("RefreshState is [$isRefreshing]")
                binding.swipeToRefresh.isRefreshing = isRefreshing
            }
        }
    }

    private fun handleScreenState() {
        binding.companiesRecycler.adapter = adapter
        setupRefresh()
        safeObserveFlow {
            viewModel.screenState.collect { state ->
                when (state) {
                    is ListState.Content -> handleContentState(state)
                    is ListState.Loading -> handleLoadingState()
                    is ListState.Exception -> handleExceptionState(state)
                    is ListState.Refreshing -> handleRefreshingState()
                    is ListState.ErrorState -> {
                        if (state is ListState.ErrorState.LoadingError) handleErrorState(state)
                    }

                }
            }
        }
    }

    private fun handleContentState(state: ListState.Content) {
        binding.errorPart.errorImage.gone()
        binding.errorPart.errorMessageTv.gone()
        binding.errorPart.tryAgainButton.gone()
        binding.companiesRecycler.show()
        setupRecycler(state)
    }

    private fun handleErrorState(state: ListState.ErrorState.LoadingError) {
        binding.companiesRecycler.hide()
        with(binding.errorPart) {
            errorImage.show()
            errorMessageTv.show()
            errorMessageTv.text = state.msg
            tryAgainButton.show()
            tryAgainButton.setOnClickListener {
                viewModel.loadRefreshState(true)
            }
        }
    }

    private fun handleLoadingState() {
        binding.swipeToRefresh.isRefreshing = false
    }

    private fun handleExceptionState(state: ListState.Exception) {
        binding.companiesRecycler.hide()
        binding.errorPart.tryAgainButton.gone()
        binding.errorPart.errorImage.show()
        binding.errorPart.errorMessageTv.show()
        binding.errorPart.errorMessageTv.text = state.msg
    }

    private fun handleRefreshingState() {



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