package com.onopry.lifehackstudiotesttask.app.presentation.screen.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.onopry.lifehackstudiotesttask.R
import com.onopry.lifehackstudiotesttask.app.presentation.utils.NetworkConst
import com.onopry.lifehackstudiotesttask.app.presentation.utils.gone
import com.onopry.lifehackstudiotesttask.app.presentation.utils.hide
import com.onopry.lifehackstudiotesttask.app.presentation.utils.safeFlowCallScope
import com.onopry.lifehackstudiotesttask.app.presentation.utils.show
import com.onopry.lifehackstudiotesttask.data.model.CompanyDetails
import com.onopry.lifehackstudiotesttask.databinding.FragmentDetailsBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        viewModel.sendCompanyId(args.companyId)

        binding.shimmerPlace.shimmer.show()
        binding.shimmerPlace.shimmer.startShimmer()
        binding.contentLayout.hide()


        setupMap()
        handleState()

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun setupMap() {
        MapKitFactory.initialize(requireContext())
        binding.mapScrollView.addInterceptScrollView(binding.mapView)
        safeFlowCallScope {
            viewModel.showMapFlow.collect { showMap: Boolean ->
                if (showMap) binding.mapView.show()
                else binding.mapView.gone()
            }
        }
    }

    private fun handleState() {
        safeFlowCallScope {
            viewModel.screenState.collect { state ->
                when (state) {
                    is DetailsState.Loading -> handleLoadingState()
                    is DetailsState.Content -> handleContentState(state.company)
                    is DetailsState.ErrorState.LoadingError -> handleErrorState(state.msg)
                    is DetailsState.Exception -> handleExceptionState(state)
                }
            }
        }
    }

    override fun onStart() {
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.mapScrollView.removeInterceptScrollView(binding.mapView)
        super.onDestroyView()
    }


    private fun handleContentState(company: CompanyDetails) {
        with(binding) {

            toolbar.title = company.name
            companyDescription.text = company.description
            companyPhone.text = company.phone
            companyWeb.text = company.www
            companyLocation.text = company.address

            Glide.with(requireContext())
                .load(NetworkConst.COMPANY_API_ENDPOINT + company.img)
                .centerCrop()
                .into(companyImage)
            setPositionMap(company)

            shimmerPlace.shimmer.stopShimmer()
            shimmerPlace.shimmer.gone()
            contentLayout.show()
        }
    }

    private fun setPositionMap(company: CompanyDetails) {
        if (company.lat != null && company.lon != null) {
            val locationPoint = Point(company.lat, company.lon)
            binding.mapView.map.move(CameraPosition(locationPoint, 14f, 0f, 0f))
            val imagePoint =
                ImageProvider.fromResource(requireContext(), R.drawable.ic_location_point)
            binding.mapView.map.mapObjects.addPlacemark(locationPoint, imagePoint)
        }
    }

    private fun handleLoadingState() {
        binding.contentLayout.hide()
        binding.shimmerPlace.shimmer.startShimmer()
        binding.shimmerPlace.shimmer.show()
    }

    private fun handleErrorState(errorMessage: String) {
        binding.contentLayout.gone()
        binding.shimmerPlace.shimmer.gone()
        with(binding.errorPart) {
            errorImage.show()
            errorMessageTv.show()
            tryAgainButton.show()

            errorMessageTv.text = errorMessage
            tryAgainButton.setOnClickListener {
                viewModel.refresh()
            }
        }
    }

    private fun handleExceptionState(state: DetailsState.Exception) {
        binding.contentLayout.hide()
        with(binding.errorPart) {
            errorImage.show()
            errorMessageTv.show()
            tryAgainButton.show()

            errorMessageTv.text = state.msg
        }
    }
}

