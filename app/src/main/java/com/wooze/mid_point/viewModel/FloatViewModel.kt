package com.wooze.mid_point.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FloatViewModel : ViewModel() {
    private val _isOpen: MutableState<Boolean> = mutableStateOf<Boolean>(true)
    val isOpen: State<Boolean> = _isOpen

    fun toggleOpen() {
        _isOpen.value = !_isOpen.value
    }

    fun close() {
        if (_isOpen.value) {
            _isOpen.value = false
        }
    }

    fun open() {
        if (!_isOpen.value) {
            _isOpen.value = true
        }
    }
}