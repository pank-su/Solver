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
import kotlin.math.exp
import kotlin.math.floor

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


fun DrawScope.draw(
    huffmanSymbolEncode: HuffmanSymbolEncode,
    color: Color,
    textMeasurer: TextMeasurer,
    position: Offset = center.copy(y = size.minDimension / 100f),
    step: Int = 1,

    ) {
val radius = size.minDimension / 30f / exp(step.toFloat() / 1.5f)
    //drawText(textMeasurer, "${floor(huffmanSymbolEncode.probability * 1000f) / 1000f}", position.copy(y=position.y - radius * 4f,x = position.x - radius * 4f))
    drawCircle(color, radius, position)
    huffmanSymbolEncode.childrenPair?.let {
        val newAddition = (size.minDimension / exp(step.toFloat() / 1.5f)).coerceAtLeast(5f)
        val rightPos = position.copy(position.x + newAddition / 2, position.y + newAddition)
        val leftPos = rightPos.copy(position.x - newAddition / 2, position.y + newAddition)

        drawLine(color, position, rightPos)
        drawLine(color, position, leftPos)

        draw(it.first, color, textMeasurer, leftPos, step + 1)
        draw(it.second, color, textMeasurer, rightPos, step + 1)

    } ?: run {
        drawText(textMeasurer, "${huffmanSymbolEncode.char}", position)
    }
}