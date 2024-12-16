package su.pank.solver.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import su.pank.solver.domain.ProbabilityTableUseCase

/**
 * Состояние экрана
 */
sealed interface MainState {
    data object FileNotPicked : MainState

    data object Loading : MainState

    data class PickedFile(val fileName: String, val fileText: String) : MainState
}

class MainViewModel(val probabilityTableUseCase: ProbabilityTableUseCase) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.FileNotPicked)
    val state = _state.asStateFlow()

    fun onFilePicked(file: PlatformFile) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            probabilityTableUseCase(file)
            _state.value = MainState.PickedFile(file.name, file.readBytes().decodeToString())
        }
    }

    init {

    }
}