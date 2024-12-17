package su.pank.solver.ui.main.shanon_fano

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import su.pank.solver.data.fano.ShanonFanoCodingRepository
import su.pank.solver.domain.SymbolEncoded

class ShanonFanoCodingViewModel(shanonFanoCodingRepository: ShanonFanoCodingRepository): ViewModel() {
    val fanoCode = shanonFanoCodingRepository.current.shareIn(viewModelScope, SharingStarted.Lazily, 1)

    var message: String by mutableStateOf("")
}