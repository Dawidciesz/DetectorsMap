package com.fiesta.detector.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiesta.detector.R
import com.fiesta.detector.adapters.PoiListAdapter
import com.fiesta.detector.databinding.ListFragmentBinding
import com.fiesta.detector.ui.PoiViewModel
import com.fiesta.detector.utility.ListItems
import com.fiesta.detector.utility.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoiListFragment : Fragment() {
    private val viewModel: PoiViewModel by viewModels()
    private  var itemList : ArrayList<ListItems> = ArrayList<ListItems>()
    private lateinit var adapters: PoiListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ListFragmentBinding.bind(view)

        val poiListAdapter = PoiListAdapter()

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
    }
}