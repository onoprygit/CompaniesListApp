package com.onopry.lifehackstudiotesttask.app.presentation.screen.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.onopry.lifehackstudiotesttask.R
import com.onopry.lifehackstudiotesttask.app.presentation.utils.safeObserveFlow
import com.onopry.lifehackstudiotesttask.databinding.FragmentDetailsBinding
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
        safeObserveFlow {
            viewModel.screenState.collect { state ->
                when (state) {
                    is DetailsState.Loading -> {}
                    is DetailsState.Content -> {
                        with(binding){
                            toolbar.title = state.company.name
                            companyDescription.text = state.company.description
                            companyPhone.text = state.company.phone
                            companyWeb.text = state.company.www
                            companyLocation.text = "lon: ${state.company.lon} lat: ${state.company.lat}"

                            Glide.with(requireContext())
                                .load("https://lifehack.studio/test_task/${state.company.img}")
                                .placeholder(R.drawable.placeholder_company)
                                .into(binding.companyImage)

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
}