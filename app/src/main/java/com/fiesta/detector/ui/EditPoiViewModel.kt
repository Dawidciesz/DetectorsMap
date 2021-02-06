package com.fiesta.detector.ui

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiesta.detector.data.Poi
import com.fiesta.detector.data.PoiDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditPoiViewModel @ViewModelInject constructor(
    private val poiDao: PoiDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val poi = state.get<Poi>("poi")

    var poiName = state.get<String>("poiName") ?: poi?.name ?: ""
        set(value) {
            field = value
            state.set("poiName", value)
        }
    private val editPoiEventChannel = Channel<EditPoiEvent>()
    val editPoiEvent = editPoiEventChannel.receiveAsFlow()

    var poiDescription = state.get<String>("poiDescription") ?: poi?.description ?: ""
        set(value) {
            field = value
            state.set("poiDescription", value)
        }
    fun onSaveButtonClick() {
        if (poiName.isBlank()) {
            showInvalidInputMessage("Nazwa nie może być pusta")
            }
        if (poi != null) {
            viewModelScope.launch {
                showInvalidInputMessage("Notatka zapisana")
                poiDao.update(poi.copy(name = poiName, description = poiDescription))
            }
        }

    }

    fun onSetImageButtonClick(uri: String) {
        viewModelScope.launch {
            poiDao.update(poi?.copy(uri = uri)!!)
            showInvalidInputMessage("Notatka zapisana")
        }
    }
    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        editPoiEventChannel.send(EditPoiEvent.ShowInputMessage(text))
    }
    sealed class EditPoiEvent {
        data class ShowInputMessage(val message: String) : EditPoiEvent()
    }
}