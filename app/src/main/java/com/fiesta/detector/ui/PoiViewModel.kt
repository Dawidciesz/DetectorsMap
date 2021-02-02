package com.fiesta.detector.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fiesta.detector.data.Poi
import com.fiesta.detector.data.PoiDao
import kotlinx.coroutines.launch

class PoiViewModel @ViewModelInject constructor(
    private val poiDao: PoiDao
) : ViewModel() {
    val pois = poiDao.getPois().asLiveData()
    fun onPoiSwiped(poi: Poi) = viewModelScope.launch {
        poiDao.delete(poi)
    }
}