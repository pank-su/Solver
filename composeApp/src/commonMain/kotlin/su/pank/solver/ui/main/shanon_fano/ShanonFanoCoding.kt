package su.pank.solver.ui.main.shanon_fano

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import su.pank.solver.ui.main.ScreenOfMain


@Serializable
object ShanonFanoCoding: ScreenOfMain {
    override val icon: ImageVector = Icons.Default.Code
    override val label: String = "Кодирование Шеннона-Фано"
}