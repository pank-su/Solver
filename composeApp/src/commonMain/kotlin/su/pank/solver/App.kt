package su.pank.solver

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.KoinApplication
import su.pank.solver.data.di.dataModule
import su.pank.solver.domain.di.domainModule
import su.pank.solver.ui.GeneralNavigation
import su.pank.solver.ui.di.uiModule
import su.pank.solver.ui.theme.SolverTheme

@Composable
fun App() {
    SolverTheme {

        KoinApplication({
            modules(
                uiModule,
                domainModule,
                dataModule
            )
        }) {
            Surface(modifier = Modifier.fillMaxSize()) {
                GeneralNavigation()
            }
        }

    }
}