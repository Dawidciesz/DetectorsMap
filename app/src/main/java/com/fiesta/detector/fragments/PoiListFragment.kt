package com.fiesta.detector.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiesta.detector.R
import com.fiesta.detector.adapters.PoiListAdapter
import com.fiesta.detector.ui.PoiViewModel
import com.fiesta.detector.utility.ListItems
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_fragment.*
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
        linearLayoutManager = LinearLayoutManager(requireContext())
        recycler_view.layoutManager = linearLayoutManager
        adapters = PoiListAdapter(itemList)
        recycler_view.adapter = adapters
        itemList.add(ListItems("name1","description description description description description description description"))
        itemList.add(ListItems("name2","description description description description description description description"))
        itemList.add(ListItems("name3","description description description description description description description"))
        itemList.add(ListItems("name4","description description description description description description description"))
        itemList.add(ListItems("name5","description description description description description description description"))
        itemList.add(ListItems("name6","description description description description description description description"))
        itemList.add(ListItems("name7","description description description description description description description"))
        itemList.add(ListItems("name8","description description description description description description description"))
        itemList.add(ListItems("name9","description description description description description description description"))
        itemList.add(ListItems("name10","description description description description description description description"))
        itemList.add(ListItems("name11","description description description description description description description"))
        itemList.add(ListItems("name12","description description description description description description description"))
        itemList.add(ListItems("name13","description description description description description description description"))
    }
}