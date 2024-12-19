package su.pank.solver.ui.main.huffman

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import su.pank.solver.domain.HuffmanStep
import su.pank.solver.domain.HuffmanSymbolEncode
import su.pank.solver.ui.main.ScreenOfMain
import su.pank.solver.ui.main.shanon_fano.CodeTable
import kotlin.math.ceil
import kotlin.math.min

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
        val launcher = rememberFileSaverLauncher { file ->
            // Handle the saved file
        }
        val coroutineScope = rememberCoroutineScope()

        val textMeasurer = rememberTextMeasurer()
        if (false)
            Button(onClick = {
                val imageBitmap = ImageBitmap(1500, 500)
                val canvas = androidx.compose.ui.graphics.Canvas(imageBitmap)
                val drawScope = CanvasDrawScope()
                drawScope.draw(
                    density = Density(1f),
                    layoutDirection = LayoutDirection.Ltr,
                    canvas = canvas,
                    size = Size(1500.toFloat(), 500.toFloat())
                ) {
                    drawHuffmanTreeFitted(result!!.graphData, color = Color.Black, textMeasurer = textMeasurer)
                }
                // TODO: сделать скачивание графа
            }) {
                Text("Скачать граф")
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


        Button({
            coroutineScope.launch {
                launcher.launch(baseName = "decode_h", extension = "csv", bytes = vm.generateDecodeCSV().encodeToByteArray())
            }
        }){
            Text("Скачать декодировку")
        }
        Button({
            coroutineScope.launch {
                launcher.launch(baseName = "huffman", extension = "csv", bytes = vm.generateCSV().encodeToByteArray())
            }
        }){
            Text("Скачать таблицу")
        }
        if (result == null) {
            CircularProgressIndicator()
        } else if (!vm.isGraphShowing) {
            Row {
                CodeTable(result!!.tableData, modifier = Modifier.weight(1f).widthIn(max = 300.dp))
                HuffmanProcessTable(result!!.steps, Modifier.weight(1f))
            }
            Text("Среднее число двоичных разрядов: ${result?.averageNumberOfBinaryDigits}")

        } else {
            HuffmanGraph(result!!.graphData)
        }

    }
}

@Composable
fun HuffmanProcessTable(steps: List<HuffmanStep>, modifier: Modifier = Modifier) {
    LazyRow(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed(steps) { index, step ->
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.size(55.dp, 60.dp), contentAlignment = Alignment.Center) {
                    Text("${index + 1}", )
                }
                step.symbols.forEach {
                    Box(modifier = Modifier.size(55.dp, 60.dp), contentAlignment = Alignment.Center) {
                        Text("${ceil(it.probability * 10000f) / 10000f}", )
                    }
                }
            }

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
        drawHuffmanTreeFitted(huffmanSymbolEncode, color, textMeasurer)
    }


}


// Далее код писал не я, а так называемый ИИ, а именно код графа, который не пересекается
data class NodeLayout(
    val position: Offset,
    val width: Float
)

// Подсчёт количества листьев
fun HuffmanSymbolEncode.countLeaves(): Int = childrenPair?.let { (l, r) -> l.countLeaves() + r.countLeaves() } ?: 1

// Подсчёт максимальной глубины
fun HuffmanSymbolEncode.maxDepth(currentDepth: Int = 1): Int =
    childrenPair?.let { (l, r) ->
        maxOf(l.maxDepth(currentDepth + 1), r.maxDepth(currentDepth + 1))
    } ?: currentDepth

// Вычисляем ширину поддерева. Здесь width — это ширина в условных единицах,
// которая определяет горизонтальное расстояние между узлами.
// Возвращаем ширину поддерева.
fun HuffmanSymbolEncode.subtreeWidth(nodeGap: Float): Float = childrenPair?.let { (left, right) ->
    val leftW = left.subtreeWidth(nodeGap)
    val rightW = right.subtreeWidth(nodeGap)
    // Ширина поддерева = сумма ширин левый + правый + промежуток
    leftW + rightW + nodeGap
} ?: (nodeGap) // Лист имеет минимальную ширину = nodeGap (или можно взять меньше)

// Рекурсивно вычисляем позиции узлов, возвращаем позицию текущего узла и ширину поддерева
fun HuffmanSymbolEncode.layoutTree(
    topPosition: Offset,
    verticalSpacing: Float,
    nodeGap: Float
): NodeLayout {
    if (childrenPair == null) {
        // Листовой узел
        return NodeLayout(topPosition, nodeGap)
    }

    val (left, right) = childrenPair
    val leftW = left.subtreeWidth(nodeGap)
    val rightW = right.subtreeWidth(nodeGap)
    val totalW = leftW + rightW + nodeGap

    // Позиция текущего узла будет по центру поддерева
    val currentNodeX = topPosition.x
    val currentNodeY = topPosition.y

    // Вычисляем стартовую позицию для левого поддерева
    val leftStartX = currentNodeX - totalW / 2f + leftW / 2f
    val rightStartX = currentNodeX + totalW / 2f - rightW / 2f

    val leftPos = Offset(leftStartX, currentNodeY + verticalSpacing)
    val rightPos = Offset(rightStartX, currentNodeY + verticalSpacing)

    val leftLayout = left.layoutTree(leftPos, verticalSpacing, nodeGap)
    val rightLayout = right.layoutTree(rightPos, verticalSpacing, nodeGap)

    return NodeLayout(Offset(currentNodeX, currentNodeY), totalW)
}

// Отрисовка дерева с уже рассчитанными позициями
fun DrawScope.drawHuffmanTree(
    huffmanSymbolEncode: HuffmanSymbolEncode,
    color: Color,
    textMeasurer: TextMeasurer,
    layout: NodeLayout,
    verticalSpacing: Float,
    nodeGap: Float
) {
    val radius = 5f
    drawCircle(color, radius, layout.position)
    drawText(
        textMeasurer,
        "${ceil(huffmanSymbolEncode.probability * 1000f) / 1000f}",
        layout.position.copy(y = layout.position.y + 20f, x = layout.position.x - 20f)
    )

    huffmanSymbolEncode.childrenPair?.let { (left, right) ->
        val leftW = left.subtreeWidth(nodeGap)
        val rightW = right.subtreeWidth(nodeGap)
        val totalW = leftW + rightW + nodeGap


        val currentX = layout.position.x
        val currentY = layout.position.y

        val leftStartX = currentX - totalW / 2f + leftW / 2f
        val rightStartX = currentX + totalW / 2f - rightW / 2f

        val leftPos = Offset(leftStartX, currentY + verticalSpacing)
        val rightPos = Offset(rightStartX, currentY + verticalSpacing)

        // Линии к потомкам
        drawLine(color, layout.position, leftPos, strokeWidth = 2f)
        drawLine(color, layout.position, rightPos, strokeWidth = 2f)

        // Отрисовываем "1" на линии к левому потомку
        val leftMid = Offset((layout.position.x + leftPos.x) / 2f, (layout.position.y + leftPos.y) / 2f)
        drawText(textMeasurer, "1", leftMid.copy(y = leftMid.y - 10f))

        // Отрисовываем "0" на линии к правому потомку
        val rightMid = Offset((layout.position.x + rightPos.x) / 2f, (layout.position.y + rightPos.y) / 2f)
        drawText(textMeasurer, "0", rightMid.copy(y = rightMid.y - 10f))

        // Рекурсивно рисуем поддеревья
        drawHuffmanTree(left, color, textMeasurer, NodeLayout(leftPos, leftW), verticalSpacing, nodeGap)
        drawHuffmanTree(right, color, textMeasurer, NodeLayout(rightPos, rightW), verticalSpacing, nodeGap)
    } ?: run {
        // Лист
        drawText(textMeasurer, "${huffmanSymbolEncode.char}", layout.position)
    }
}

// Основная функция отрисовки с масштабированием и вписыванием в область
fun DrawScope.drawHuffmanTreeFitted(
    huffmanSymbolEncode: HuffmanSymbolEncode,
    color: Color,
    textMeasurer: TextMeasurer
) {
    // Зададим небольшой базовый интервал между узлами
    val nodeGap = 50f
    val verticalSpacing = 80f

    // Сначала определим ширину и глубину дерева
    val treeWidth = huffmanSymbolEncode.subtreeWidth(nodeGap)
    val treeDepth = huffmanSymbolEncode.maxDepth()

    // Рассчитаем желаемый размер (примерно)
    val desiredWidth = treeWidth
    val desiredHeight = treeDepth * verticalSpacing

    // Вычисляем масштаб
    val scaleFactor = min(
        (size.width * 0.9f) / desiredWidth.coerceAtLeast(1f),
        (size.height * 0.9f) / desiredHeight.coerceAtLeast(1f)
    ).coerceAtMost(1f)

    withTransform({
        val dx = center.x
        val dy = size.height / 20f
        scale(scaleFactor, Offset(dx, dy))
    }) {
        // Вычисляем позиции узлов без отрисовки
        val layout = huffmanSymbolEncode.layoutTree(
            topPosition = Offset(center.x, size.height / 20f),
            verticalSpacing = verticalSpacing,
            nodeGap = nodeGap
        )
        // Теперь рисуем дерево по рассчитанным координатам
        drawHuffmanTree(
            huffmanSymbolEncode,
            color,
            textMeasurer,
            layout,
            verticalSpacing,
            nodeGap
        )
    }
}


fun DrawScope.drawText(
    textMeasurer: TextMeasurer,
    text: String,
    position: Offset
) {
    // Используем максимально возможные ограничения, чтобы избежать отрицательных значений
    val constraints = Constraints(
        minWidth = 0,
        maxWidth = Int.MAX_VALUE,
        minHeight = 0,
        maxHeight = Int.MAX_VALUE
    )

    val measured = textMeasurer.measure(
        text = AnnotatedString(text),
        style = TextStyle(fontSize = 14.sp, color = Color.Black),
        constraints = constraints
    )

    // Отрисовываем текст по заданной позиции
    // Обычно текст рисуется так, чтобы top-left был в указанной точке
    // Можно сместить, если надо по центру узла
    drawText(measured, topLeft = position)
}
