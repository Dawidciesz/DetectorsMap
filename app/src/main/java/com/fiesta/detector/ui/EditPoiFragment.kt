package com.fiesta.detector.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fiesta.detector.R
import com.fiesta.detector.databinding.EditPoiFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EditPoiFragment : androidx.fragment.app.Fragment(R.layout.edit_poi_fragment) {

    private val viewModel: EditPoiViewModel by viewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = EditPoiFragmentBinding.bind(view)

        binding.apply {
            editPoiName.setText(viewModel.poiName)
            editPoiDescription.setText(viewModel.poiDescription)
            editImage.setImageURI(Uri.parse(viewModel.poi?.uri))
            editPoiName.addTextChangedListener {
                viewModel.poiName = it.toString()
            }

            editPoiDescription.addTextChangedListener {
                viewModel.poiDescription = it.toString()
            }
            editSaveButton.setOnClickListener {
                viewModel.onSaveButtonClick()
            }
            editSetImage.setOnClickListener {

                val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                gallery.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                resultLauncher.launch(gallery)



            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editPoiEvent.collect { event ->
                when (event) {
                    is EditPoiViewModel.EditPoiEvent.ShowInputMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
//                uri =
                context?.contentResolver?.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                testfoto.setImageURI(data?.data)
                viewModel.onSetImageButtonClick(data?.data.toString())
                Snackbar.make(requireView(), data?.data.toString(), Snackbar.LENGTH_LONG).show()

            }
        }
    }
}