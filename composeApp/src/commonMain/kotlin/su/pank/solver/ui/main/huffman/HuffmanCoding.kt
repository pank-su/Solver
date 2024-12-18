package su.pank.solver.ui.main.huffman

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import su.pank.solver.domain.HuffmanSymbolEncode
import su.pank.solver.ui.main.ScreenOfMain
import su.pank.solver.ui.main.shanon_fano.CodeTable

@Serializable
object HuffmanCoding : ScreenOfMain {
    override val icon: ImageVector = Icons.Default.QrCode

    override val label: String
        get() = "Хаффман"
}


@Composable
fun HuffmanCodingScreen() {
    val vm = koinViewModel<HuffmanCodingViewModel>()
    val scrollState = rememberScrollState()
    val result by vm.result.collectAsState(null)

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(vm.isGraphShowing, { vm.isGraphShowing = it })
            Text("Я хочу граф")
        }

        Row {
            OutlinedTextField(vm.message, {
                vm.message = it
            }, placeholder = { Text("Введите сообщение") })
            IconButton(onClick = { vm.encodeMessage() }) {
                Icon(Icons.Default.Done, null)
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Text(vm.encodedMessage)
        Spacer(modifier = Modifier.height(50.dp))

        if (result == null) {
            CircularProgressIndicator()
        } else if (!vm.isGraphShowing) {
            CodeTable(result!!.tableData)
        } else {
            HuffmanGraph(result!!.graphData)
        }


    }
}

@Composable
fun HuffmanGraph(
    huffmanSymbolEncode: HuffmanSymbolEncode,
    modifier: Modifier = Modifier.height(10000.dp).fillMaxWidth(),

    ) {
    val color = MaterialTheme.colorScheme.primary
    val scrollState = rememberScrollState()
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier) {

        draw(huffmanSymbolEncode, color, textMeasurer)

    }


}


// Функция для подсчёта количества листовых узлов в поддереве
fun HuffmanSymbolEncode.countLeaves(): Int = childrenPair?.let { (left, right) ->
    left.countLeaves() + right.countLeaves()
} ?: 1

fun DrawScope.draw(
    huffmanSymbolEncode: HuffmanSymbolEncode,
    color: Color,
    textMeasurer: TextMeasurer,
    position: Offset = center.copy(y = size.minDimension / 100f),
    horizontalSpacing: Float = size.minDimension / 5f,
    verticalSpacing: Float = size.minDimension / 10f
) {
    val radius = 5f
    drawCircle(color, radius, position)

    huffmanSymbolEncode.childrenPair?.let { (left, right) ->
        // Подсчитываем, сколько листьев в левом и правом поддереве
        val leftLeaves = left.countLeaves()
        val rightLeaves = right.countLeaves()
        val totalLeaves = leftLeaves + rightLeaves

        // Вычисляем горизонтальное расстояние, пропорциональное количеству листьев
        val childSpacing = horizontalSpacing * totalLeaves

        // Позиции для левого и правого потомка
        val leftPos = position.copy(
            x = position.x - (childSpacing * (leftLeaves.toFloat() / totalLeaves)),
            y = position.y + verticalSpacing
        )

        val rightPos = position.copy(
            x = position.x + (childSpacing * (rightLeaves.toFloat() / totalLeaves)),
            y = position.y + verticalSpacing
        )

        // Рисуем связи к потомкам
        drawLine(color, position, leftPos, strokeWidth = 2f)
        drawLine(color, position, rightPos, strokeWidth = 2f)

        // Рекурсивно рисуем поддеревья, уменьшая базовое горизонтальное расстояние
        draw(left, color, textMeasurer, leftPos, horizontalSpacing / 2f, verticalSpacing)
        draw(right, color, textMeasurer, rightPos, horizontalSpacing / 2f, verticalSpacing)
    } ?: run {
        // Если потомков нет, рисуем символ
        //drawText(textMeasurer, "${huffmanSymbolEncode.char}", position)
    }
}

