package su.pank.solver.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import su.pank.solver.ui.main.another_table.AnotherTable
import su.pank.solver.ui.main.another_table.AnotherTableScreen
import su.pank.solver.ui.main.entropy_table.EntropyTable
import su.pank.solver.ui.main.entropy_table.EntropyTableScreen

val screens = arrayOf(EntropyTable, AnotherTable)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainNavigation(fileName: String, fileText: String) {
    var currentDestination: ScreenOfMain by rememberSaveable {
        mutableStateOf(EntropyTable)
    }

    var isNavigatedExtra by rememberSaveable{
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    NavigationSuiteScaffold(navigationSuiteItems = {
        screens.forEach { screen ->
            item(screen == currentDestination, {
                currentDestination = screen
            }, {
                Icon(screen.icon, null)
            }, label = {
                Text(screen.label)
            })
        }
    }) {
        val navigator = rememberSupportingPaneScaffoldNavigator()

        BoxWithConstraints {

            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                CenterAlignedTopAppBar({ Text(fileName) }, actions = {
                    IconButton(onClick = {
                        if (isNavigatedExtra)
                            navigator.navigateTo(SupportingPaneScaffoldRole.Main)
                        else
                            navigator.navigateTo(SupportingPaneScaffoldRole.Extra)
                        isNavigatedExtra = !isNavigatedExtra
                    }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowRight, null)
                    }
                })
            }) {
                SupportingPaneScaffold(
                    navigator.scaffoldDirective,
                    navigator.scaffoldValue,
                    mainPane = {
                        AnimatedPane(Modifier.safeContentPadding()) {
                            when (currentDestination) {
                                EntropyTable -> {
                                    EntropyTableScreen()
                                }
                                AnotherTable-> {
                                    AnotherTableScreen()
                                }
                            }
                        }
                    },
                    supportingPane = {
                        val scrollState = rememberScrollState()
                        AnimatedPane(Modifier.safeContentPadding()) {
                            Box(
                                Modifier.fillMaxSize().background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(12.dp)
                                ).verticalScroll(scrollState)
                            ) {
                                Text(fileText, modifier = Modifier.padding(10.dp))
                            }
                        }

                    }, extraPane = {
                        val scrollState = rememberScrollState()

                        AnimatedPane(Modifier.safeContentPadding()) {
                            Box(
                                Modifier.fillMaxSize().background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(12.dp)
                                ).verticalScroll(scrollState)
                            ) {
                                Text(fileText, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }, modifier = Modifier.padding(it).padding(16.dp)

                )

            }
        }


    }
}

