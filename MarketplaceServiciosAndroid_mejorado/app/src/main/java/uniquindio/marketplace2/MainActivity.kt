package uniquindio.marketplace2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import uniquindio.marketplace2.core.theme.MarketplaceTheme
import dagger.hilt.android.AndroidEntryPoint
import uniquindio.marketplace2.core.datastore.SessionManager
import uniquindio.marketplace2.core.navegacion.ConfiguracionNavegacion
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MarketplaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConfiguracionNavegacion(
                        sessionManager = sessionManager
                    )
                }
            }
        }
    }
}