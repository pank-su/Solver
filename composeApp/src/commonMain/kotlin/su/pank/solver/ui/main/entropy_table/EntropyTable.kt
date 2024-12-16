package su.pank.solver.ui.main.entropy_table


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BackupTable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import su.pank.solver.ui.main.ScreenOfMain

object EntropyTable : ScreenOfMain {
    override val icon: ImageVector = Icons.Default.BackupTable
    override val label: String = "Таблица энтропии"
}


@Composable
fun TableRow(
    nestedScrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier.height(50.dp),
    content: LazyListScope.() -> Unit,

) {
    val coroutineScope = rememberCoroutineScope()
    // TODO: сделать нормальный scroll
    LazyRow(
        modifier = modifier.nestedScroll(nestedScrollConnection),
        verticalAlignment = Alignment.CenterVertically

    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EntropyTableScreen() {

    val vm: EntropyTableViewModel = koinViewModel()
    val data by vm.data.collectAsStateWithLifecycle(null)
    if (data == null) {
        CircularProgressIndicator()
        return
    }

    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return super.onPreScroll(available, source)
            }
        }
    }

    val lazyRowScrollState = rememberLazyListState()
    val rowHeight = 50.dp
    val columnWidths = listOf(50.dp, 80.dp, 80.dp, 80.dp, 200.dp, 200.dp)
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
        ) {
            item {
                TableRow(nestedScrollConnection = connection) {
                    item {
                        Box(modifier = Modifier.width(columnWidths[0])) {
                            Text("№", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.width(columnWidths[1])) {
                            Text("Символ", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.width(columnWidths[2])) {
                            Text("Код символа", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.width(columnWidths[3])) {
                            Text("Число вхождения в текст", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.width(columnWidths[4])) {
                            Text("Вероятность", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.width(columnWidths[5])) {
                            Text("I", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
            items(data!!.symbols.size) {
                val symbol = data!!.symbols[it]

                TableRow(connection) {
                    item {
                        Box(modifier = Modifier.size(columnWidths[0], rowHeight)) {
                            Text("${it + 1}", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {

                        Box(modifier = Modifier.size(columnWidths[1], rowHeight)) {
                            Text("${symbol.char}", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.size(columnWidths[2], rowHeight)) {
                            Text("${symbol.char.code}", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.size(columnWidths[3], rowHeight)) {
                            Text("${symbol.occurrences}", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.size(columnWidths[4], rowHeight)) {
                            Text("${symbol.probability}", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    item {
                        Box(modifier = Modifier.width(columnWidths[5])) {
                            Text("${symbol.information}", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }

        }
        Box(
            modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().background(
                MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(12.dp)
            )
        ) {

            TableRow(connection, Modifier) {
                item {
                    Box(modifier = Modifier.size(columnWidths[0], rowHeight)) {
                    }
                }
                item {
                    Box(modifier = Modifier.size(columnWidths[1], rowHeight)) {
                    }
                }
                item {
                    Box(modifier = Modifier.width(columnWidths[2])) {
                    }
                }
                item {
                    Box(modifier = Modifier.width(columnWidths[3])) {
                        Text("${data!!.symbolsCount}")
                    }
                }
                item {
                    Box(modifier = Modifier.width(columnWidths[4])) {
                        Text(
                            "${data!!.probabilitySum}",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                item {
                    Box(modifier = Modifier.width(columnWidths[5])){
                        Text("${data!!.infoAvg}", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

