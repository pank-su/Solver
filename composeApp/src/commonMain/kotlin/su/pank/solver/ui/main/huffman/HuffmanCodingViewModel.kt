package su.pank.solver.ui.main.huffman

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import su.pank.solver.data.huffman.HuffmanCodingRepository

// TODO: часть данных это копирка ShanonViewModel, может объединить под один абстрактный класс
class HuffmanCodingViewModel(private val huffmanCodingRepository: HuffmanCodingRepository) : ViewModel(){
    val result = huffmanCodingRepository.current.map {
        it.copy(tableData = it.tableData.sortedByDescending { it.probability })
    }.shareIn(viewModelScope, SharingStarted.Lazily, 1)

    var currentJob: Job? = null


    var message: String by mutableStateOf("")
    var encodedMessage: String by mutableStateOf("")

    fun encodeMessage() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val symbols =
                this@HuffmanCodingViewModel.result.first().tableData.associate { symbolEncoded -> symbolEncoded.char to symbolEncoded.code }
            encodedMessage = message.map {
                symbols[it]!!
            }.joinToString("")

        }
    }
}