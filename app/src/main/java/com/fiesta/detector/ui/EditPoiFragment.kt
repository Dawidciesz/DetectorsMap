package com.fiesta.detector.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fiesta.detector.R
import com.fiesta.detector.databinding.EditPoiFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EditPoiFragment : androidx.fragment.app.Fragment(R.layout.edit_poi_fragment) {

    private val viewModel: EditPoiViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = EditPoiFragmentBinding.bind(view)

        binding.apply {
            editPoiName.setText(viewModel.poiName)
            editPoiDescription.setText(viewModel.poiDescription)
            editPoiName.addTextChangedListener {
                viewModel.poiName = it.toString()
            }

            editPoiDescription.addTextChangedListener {
                viewModel.poiDescription = it.toString()
            }
            editSaveButton.setOnClickListener {
                viewModel.onSaveButtonClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editPoiEvent.collect { event ->
                when (event) {
                    is EditPoiViewModel.EditPoiEvent.ShowInputMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}