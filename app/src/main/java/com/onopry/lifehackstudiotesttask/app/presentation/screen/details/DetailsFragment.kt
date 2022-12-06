package com.onopry.lifehackstudiotesttask.app.presentation.screen.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.onopry.lifehackstudiotesttask.R
import com.onopry.lifehackstudiotesttask.app.presentation.utils.gone
import com.onopry.lifehackstudiotesttask.app.presentation.utils.safeObserveFlow
import com.onopry.lifehackstudiotesttask.databinding.FragmentDetailsBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
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

        MapKitFactory.initialize(requireContext())
        binding.mapView.requestDisallowInterceptTouchEvent(true)
        binding.mapScrollView.addInterceptScrollView(binding.mapView)


        viewModel.sendCompanyId(args.companyId)
        safeObserveFlow {
            viewModel.screenState.collect { state ->
                when (state) {
                    is DetailsState.Loading -> {}
                    is DetailsState.Content -> {
                        with(binding) {
                            toolbar.title = state.company.name
                            companyDescription.text = state.company.description
                            companyPhone.text = state.company.phone
                            companyWeb.text = state.company.www
                            companyLocation.text = state.company.address

                            Glide.with(requireContext())
                                .load("https://lifehack.studio/test_task/${state.company.img}")
                                .placeholder(R.drawable.placeholder_company)
                                .into(binding.companyImage)

                            if (state.company.lat != null && state.company.lon != null) {
                                val locationPoint = Point(state.company.lat, state.company.lon)
                                binding.mapView.map.move(
                                    com.yandex.mapkit.map.CameraPosition(
                                        locationPoint,
                                        14f, 0f, 0f
                                    )
                                )

                                val imagePoint = ImageProvider.fromResource(requireContext(), R.drawable.ic_location_point)
                                binding.mapView.map.mapObjects.addPlacemark(locationPoint, imagePoint)
//                                binding.mapView.map.mapObjects.add
                            }

                            if (state.company.lat == 0.0 && state.company.lon == 0.0) {
                                binding.mapView.gone()
                            }

                        }
                    }
                    is DetailsState.Refreshing -> {}
                    is DetailsState.ErrorState.InternetConnectionStateError -> {}
                    is DetailsState.ErrorState.LoadingError -> {}
                    is DetailsState.Exception -> {}
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
}