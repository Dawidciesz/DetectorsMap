package com.fiesta.detector.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fiesta.detector.data.Poi
import com.fiesta.detector.data.PoiDao
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MapViewModel @ViewModelInject constructor(
    private val poiDao: PoiDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val pois = poiDao.getPois().asLiveData()
    val poi = state.get<Poi>("poi")
    private val mapEventChannel = Channel<MapEvents>()
    val mapsEvent = mapEventChannel.receiveAsFlow()

    fun onSaveButtonClick(name: String, poiLatLang: LatLng, uri: String) {

        if (name.isBlank()) {
            showInvalidInputMessage("Nazwa nie może być pusta")
        } else {
            viewModelScope.launch {
                poiDao.insert(
                    Poi(
                        name,
                        "bark opisu",
                        poiLatLang.latitude,
                        poiLatLang.longitude,
                        uri
                    )
                )
                showInvalidInputMessage("Notatka zapisana")
            }
        }
    }

    fun onPoiSelected(poi: Poi) = viewModelScope.launch {
        mapEventChannel.send(MapEvents.NavigateToEditPoiScreen(poi))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        mapEventChannel.send(MapEvents.ShowInputMessage(text))
    }

    sealed class MapEvents {
        data class ShowInputMessage(val message: String) : MapEvents()
        data class NavigateToEditPoiScreen(val poi: Poi) : MapEvents()
    }
}