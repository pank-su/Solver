package su.pank.solver.ui.main.shanon_fano

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import su.pank.solver.domain.SymbolEncoded
import su.pank.solver.ui.main.ScreenOfMain


@Serializable
object ShanonFanoCoding : ScreenOfMain {
    override val icon: ImageVector = Icons.Default.Code
    override val label: String = "Кодирование Шеннона-Фано"
}

@Composable
fun ShanonFanoCodingScreen() {
    val vm: ShanonFanoCodingViewModel = koinViewModel()
    val code by vm.fanoCode.collectAsState(null)
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        OutlinedTextField(vm.message, {
            vm.message = it
        }, placeholder = { Text("Введите сообщение") })

        Spacer(modifier = Modifier.height(100.dp))
        if (code == null) {
            CircularProgressIndicator()
        } else {
            ShanonCodeTable(code!!.encodedSymbols)
        }

    }
}

@Composable
fun ShanonCodeTable(encoded: List<SymbolEncoded>, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ShanonCodeCell {
                Text("Символ")
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
                    Text(it.code)
                }
            }
        }
    }
}

@Composable
fun RowScope.ShanonCodeCell(content: @Composable () -> Unit) {
    Box(modifier = Modifier.weight(1f).height(60.dp).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
        content()
    }
}