package com.fiesta.detector.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.fiesta.detector.data.PoiDao

class PoiViewModel @ViewModelInject constructor(
    private val PoiDao: PoiDao
) : ViewModel() {
}