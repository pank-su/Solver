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

    var isGraphShowing: Boolean by mutableStateOf(false)

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

    suspend fun generateCSV(): String = "Символ,p,Код\n" +
            result.first().tableData.joinToString("\n") {
                "\"${it.char}\",${it.probability},${it.code}"
            }

    suspend fun generateDecodeCSV(): String {
        var result = "Шаг,Комбинация,Кол-во,Символ\n"
        val symbolsEncoded = this.result.first().tableData
        var str = ""
        var step = 1
        encodedMessage.forEach {
            str += it
            val symbolCounts = symbolsEncoded.count { it.code.startsWith(str) }
            result += "${step++},$str,$symbolCounts,${
                if (symbolCounts == 1) kotlin.run {
                    val symbol = symbolsEncoded.first { it.code == str }.char; str = "";symbol
                } else "-"
            }\n"
        }
        return result
    }
}