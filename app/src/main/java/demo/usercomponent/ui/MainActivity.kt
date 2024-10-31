package demo.usercomponent.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import demo.usercomponent.di.user.UserComponentManager
import demo.usercomponent.theme.UserComponentDemoTheme
import demo.usercomponent.user.UserProfileRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userComponentManager: UserComponentManager

    @Inject
    lateinit var greetingProvider: GreetingProvider

    @Inject
    lateinit var repository: UserProfileRepository

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UserComponentDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val text = viewModel.state.collectAsState()
                        Text(
                            text = text.value ?: "Loading...",
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                }
            }
        }
    }
}
