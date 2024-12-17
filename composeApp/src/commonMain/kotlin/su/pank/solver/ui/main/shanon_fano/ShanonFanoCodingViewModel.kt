package su.pank.solver.ui.main.shanon_fano

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import su.pank.solver.data.fano.ShanonFanoCodingRepository

class ShanonFanoCodingViewModel(shanonFanoCodingRepository: ShanonFanoCodingRepository) : ViewModel() {
    val fanoCode = shanonFanoCodingRepository.current.shareIn(viewModelScope, SharingStarted.Lazily, 1)

    var currentJob: Job? = null

    var message: String by mutableStateOf("")
    var encodedMessage: String by mutableStateOf("")

    fun encodeMessage() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val symbols =
                this@ShanonFanoCodingViewModel.fanoCode.first().encodedSymbols.associate { symbolEncoded -> symbolEncoded.char to symbolEncoded.code }
            encodedMessage = message.map {
                symbols[it]!!
            }.joinToString("")

        }
    }
}