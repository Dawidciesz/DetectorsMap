package com.fiesta.detector.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fiesta.detector.data.Poi
import com.fiesta.detector.data.PoiDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class PoiViewModel @ViewModelInject constructor(
    private val poiDao: PoiDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val pois = poiDao.getPois().asLiveData()
    private val poisEventChannel = Channel<PoiEvents>()
    val tasksEvent = poisEventChannel.receiveAsFlow()

    fun onPoiSwiped(poi: Poi) = viewModelScope.launch {
        poiDao.delete(poi)
    }

    fun onPoiDetailsSelected(poi: Poi) = viewModelScope.launch {
        poisEventChannel.send(PoiEvents.NavigateToEditPoiScreen(poi))
    }

    fun onPoiZoomSelected(poi: Poi) = viewModelScope.launch {
        poisEventChannel.send(PoiEvents.NavigateToMapScreen(poi))
    }

    sealed class PoiEvents {
        data class NavigateToEditPoiScreen(val poi: Poi) : PoiEvents()
        data class NavigateToMapScreen(val poi: Poi) : PoiEvents()
    }
}