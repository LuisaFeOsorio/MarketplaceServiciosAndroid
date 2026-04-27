package uniquindio.marketplace2.core.navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.first
import uniquindio.marketplace2.core.datastore.SessionManager
import uniquindio.marketplace2.features.auth.login.PantallaLogin
import uniquindio.marketplace2.features.auth.olvidar_contrasenia.OlvidarContrasenaScreen
import uniquindio.marketplace2.features.auth.recuperar_contrasenia.RecuperarContrasenaScreen
import uniquindio.marketplace2.features.auth.registro.PantallaRegistro
import uniquindio.marketplace2.features.home.HomeScreen
import uniquindio.marketplace2.features.mensajes.PantallaMensajes


@Composable
fun ConfiguracionNavegacion(
    sessionManager: SessionManager,
    onNavigateToLogin: () -> Unit = {}
) {
    val navController = rememberNavController()
    var isLoading by remember { mutableStateOf(true) }
    var startDest by remember { mutableStateOf<Any>(Login) }

    LaunchedEffect(Unit) {
        val isLoggedIn = sessionManager.isLoggedIn().first()
        startDest = if (isLoggedIn) {
            val rol = sessionManager.getUserRol().first()
            if (rol != null) Home(rol) else Login
        } else {
            Login
        }
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {

        composable<Login> {
            PantallaLogin(
                onNavigateToRegister = { navController.navigate(Registro) },
                onNavigateToForgotPassword = { navController.navigate(OlvidarContrasena) },
                onNavigateToHome = { rol ->
                    navController.navigate(Home(rol)) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Registro> {
            PantallaRegistro(
                onNavigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(Registro) { inclusive = true }
                    }
                },
                onNavigateToHome = { rol ->
                    navController.navigate(Home(rol)) {
                        popUpTo(Registro) { inclusive = true }
                    }
                }
            )
        }

        composable<Home> { backStackEntry ->
            val home = backStackEntry.toRoute<Home>()
            HomeScreen(
                rolUsuario = home.rol,
                onNavigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<OlvidarContrasena> {
            OlvidarContrasenaScreen(
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable<RecuperarContrasena> { backStackEntry ->
            val route = backStackEntry.toRoute<RecuperarContrasena>()
            RecuperarContrasenaScreen(
                onNavigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        // ===== NUEVA RUTA: MENSAJES =====
        composable<Mensajes> { backStackEntry ->
            val route = backStackEntry.toRoute<Mensajes>()
            PantallaMensajes(
                solicitudId = route.solicitudId,
                tituloServicio = route.tituloServicio,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}
