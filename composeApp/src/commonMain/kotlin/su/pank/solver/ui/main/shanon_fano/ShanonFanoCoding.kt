package su.pank.solver.ui.main.shanon_fano

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import su.pank.solver.domain.ShanonStep
import su.pank.solver.domain.SymbolEncoded
import su.pank.solver.ui.main.ScreenOfMain


@Serializable
object ShanonFanoCoding : ScreenOfMain {
    override val icon: ImageVector = Icons.Default.Code
    override val label: String = "Кодирование \nШеннона-Фано"
}

@Composable
fun ShanonFanoCodingScreen() {
    val vm: ShanonFanoCodingViewModel = koinViewModel()
    val code by vm.fanoCode.collectAsState(null)
    val scrollState = rememberScrollState()
    val launcher = rememberFileSaverLauncher { file ->
        // Handle the saved file
    }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
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

        if (code == null) {
            CircularProgressIndicator()
        } else {

            Button(onClick = {
                coroutineScope.launch {
                    launcher.launch(
                        baseName = "decode",
                        extension = "csv",
                        bytes = vm.generateDecodeCSV().encodeToByteArray()
                    )
                }
            }) {
                Text("Скачать таблицу расшифровки")
            }
            Button(onClick = {
                coroutineScope.launch {
                    launcher.launch(
                        baseName = "shanon",
                        extension = "csv",
                        bytes = vm.generateCSV().encodeToByteArray()
                    )
                }
            }) {
                Text("Скачать таблицу")
            }
            Row {
                CodeTable(code!!.encodedSymbols, modifier = Modifier.weight(1f).widthIn(max = 400.dp))
                ShanonFanoSteps(steps = code!!.steps, code!!.encodedSymbols, modifier = Modifier.weight(1f))
            }
            Text("Среднее число двоичных разрядов: ${code?.averageNumberOfBinaryDigits}")
        }

    }
}

@Composable
fun CodeTable(encoded: List<SymbolEncoded>, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ShanonCodeCell {
                Text("Символ")
            }
            ShanonCodeCell {
                Text("p")
            }
            ShanonCodeCell {
                Text("Код")
            }
        }
        encoded.forEach {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ShanonCodeCell {
                    Text("${it.char}")
                }
                ShanonCodeCell {
                    Text("${it.probability}")
                }
                ShanonCodeCell {
                    Text(it.code)
                }
            }
        }
    }
}

@Composable
fun RowScope.ShanonCodeCell(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.weight(1f).height(60.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ShanonFanoSteps(steps: List<ShanonStep>, code: List<SymbolEncoded>, modifier: Modifier = Modifier) {
    LaunchedEffect(steps) {
        steps.forEach {
            println("${it.part1.size} ${it.part2.size} ${it.part1.last().char} ${it.part2.first().char}")

        }
    }
    LazyRow(modifier) {
        itemsIndexed(steps) { index, step ->
            Column {
                Box(Modifier.size(20.dp, 60.dp), contentAlignment = Alignment.Center) {
                    Text("$index")
                }
                val index1 = code.indexOfFirst { it.char == step.part1.last().char }

                val index2 = code.indexOfFirst { it.char == step.part2.first().char }

                repeat(index1 + 1) {
                    Spacer(Modifier.height(72.dp))
                }
                HorizontalDivider(Modifier.width(20.dp), thickness = 2.dp, color = Color.Red)
            }

        }
    }
}