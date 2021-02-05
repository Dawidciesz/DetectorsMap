package com.fiesta.detector.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiesta.detector.R
import androidx.navigation.fragment.findNavController
import com.fiesta.detector.adapters.PoiListAdapter
import com.fiesta.detector.data.Poi
import com.fiesta.detector.databinding.ListFragmentBinding
import com.fiesta.detector.ui.PoiViewModel
import com.fiesta.detector.utility.SwipeToDeleteCallback
import com.fiesta.detector.utility.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PoiListFragment : Fragment(), PoiListAdapter.OnItemClickListener {
    private val viewModel: PoiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ListFragmentBinding.bind(view)
        val poiListAdapter = PoiListAdapter(this)
        binding.apply {
            poiListRecyclerView.apply {
                adapter = poiListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val poi = poiListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onPoiSwiped(poi)
                }

            }).attachToRecyclerView(poiListRecyclerView)
        }

        viewModel.pois.observe(viewLifecycleOwner) {
            poiListAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is PoiViewModel.PoiEvents.NavigateToEditPoiScreen -> {

                        val action =
                            PoiListFragmentDirections.actionPoiListFragment2ToEditPoiFragment(
                                event.poi
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }

    override fun onItemClick(poi: Poi) {
        viewModel.onPoiSelected(poi)
    }

}