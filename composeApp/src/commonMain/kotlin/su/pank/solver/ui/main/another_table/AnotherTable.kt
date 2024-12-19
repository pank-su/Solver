package su.pank.solver.ui.main.another_table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import su.pank.solver.ui.main.ScreenOfMain


object AnotherTable : ScreenOfMain {
    override val icon: ImageVector = Icons.Default.TableView
    override val label: String = "Таблица №2"
}

// Таблица с другими значениями
@Composable
fun AnotherTableScreen() {
    val viewModel: AnotherTableViewModel = koinViewModel()
    val data by viewModel.data.collectAsState(null)
    if (data == null)
        return

    val launcher = rememberFileSaverLauncher { file ->
        // Handle the saved file
    }
    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)) {

        Column(
            Modifier.widthIn(max = 800.dp).clip(RoundedCornerShape(12.dp)).fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(modifier = Modifier.height(80.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.weight(1f)) {

                }
                Cell {
                    Text("Неопределённость")
                }
                Cell {
                    Text("Разрядность кода")
                }
                Cell {
                    Text("Абсолютная избыточность")
                }
                Cell {
                    Text("Относительная избыточность")
                }
            }
            Row(modifier = Modifier.height(120.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Cell {
                    Text("При кодировании сообщения стандартной кодовой таблицей ASCII")
                }
                Cell {
                    Text("7 бит")
                }
                Cell {
                    Text("7 бит")
                }
                Cell {
                    Text("${7 - data!!.entropy}")
                }
                Cell {
                    Text("${(7 - data!!.entropy) / 7f}")
                }
            }
            Row(modifier = Modifier.height(120.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Cell {
                    Text("При использовании равномерного кода, построенного на основе меры Хартли")
                }
                Cell {
                    Text("${data!!.entropyMax} бит")
                }
                Cell {
                    Text("${data!!.capacity} бит")
                }
                Cell {
                    Text("${data!!.absoluteRedundancy}")
                }
                Cell {
                    Text("${data!!.relativeRedundancy}")
                }
            }
            Button(onClick = {
                coroutineScope.launch {
                    launcher.launch(
                        baseName = "codings",
                        extension = "csv",
                        bytes = viewModel.generateCSV().encodeToByteArray()
                    )
                }
            }) {
                Text("Скачать")
            }
        }
    }
}

@Composable
fun RowScope.Cell(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.weight(1f).fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }

}