package uniquindio.marketplace2.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.toRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uniquindio.marketplace2.R
import uniquindio.marketplace2.core.navegacion.*
import uniquindio.marketplace2.data.modelos.Usuario
import uniquindio.marketplace2.features.dashboard.PantallaDashboard
import uniquindio.marketplace2.features.feed.PantallaFeedLista
import uniquindio.marketplace2.features.feed.PantallaFeedMapa
import uniquindio.marketplace2.features.mensajes.PantallaMensajes
import uniquindio.marketplace2.features.misOfertas.PantallaMisOfertas
import uniquindio.marketplace2.features.misOfertas.PantallaEditarOferta
import uniquindio.marketplace2.features.moderador.PantallaPanelModerador
import uniquindio.marketplace2.features.moderador.PantallaHistorialModerador
import uniquindio.marketplace2.features.notificaciones.PantallaNotificaciones
import uniquindio.marketplace2.features.ofertas.crear.PantallaCrearOferta
import uniquindio.marketplace2.features.ofertas.detalle.PantallaDetalleOferta
import uniquindio.marketplace2.features.perfil.PantallaPerfil
import uniquindio.marketplace2.features.solicitudes.crear.PantallaSolicitarServicio
import uniquindio.marketplace2.features.solicitudes.detalle.PantallaDetalleSolicitud
import uniquindio.marketplace2.features.solicitudes.lista.PantallaListaSolicitudes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rolUsuario: String,
    onNavigateToLogin: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val sesion by viewModel.sesion.collectAsState()

    val usuarioActual: Usuario? = sesion?.let {
        Usuario(id = it.userId, nombre = it.nombre, email = it.email, rol = it.rol)
    }

    data class NavItem(val label: String, val icon: ImageVector, val dest: Any)

    val items: List<NavItem> = when (rolUsuario) {
        "trabajador" -> listOf(
            NavItem(stringResource(R.string.nav_inicio), Icons.Default.Home, Dashboard),
            NavItem(stringResource(R.string.nav_mis_ofertas), Icons.Default.List, MisOfertas),
            NavItem(stringResource(R.string.nav_solicitudes), Icons.Default.Inbox, Solicitudes),
            NavItem(stringResource(R.string.nav_perfil), Icons.Default.Person, Perfil)
        )
        "moderador" -> listOf(
            NavItem(stringResource(R.string.nav_panel), Icons.Default.AdminPanelSettings, PanelModerador),
            NavItem("Historial", Icons.Default.History, HistorialModerador),
            NavItem(stringResource(R.string.nav_perfil), Icons.Default.Person, Perfil)
        )
        else -> listOf(
            NavItem(stringResource(R.string.nav_inicio), Icons.Default.Home, Feed),
            NavItem(stringResource(R.string.nav_explorar), Icons.Default.Map, Buscar),
            NavItem(stringResource(R.string.nav_solicitudes), Icons.Default.Assignment, Solicitudes),
            NavItem(stringResource(R.string.nav_perfil), Icons.Default.Person, Perfil)
        )
    }

    val startDest: Any = when (rolUsuario) {
        "trabajador" -> Dashboard
        "moderador"  -> PanelModerador
        else         -> Feed
    }

    fun isSelected(dest: Any): Boolean {
        val destName = dest::class.simpleName ?: ""
        return currentRoute?.contains(destName) == true
    }

    Scaffold(
        bottomBar = {
            NavigationBar(tonalElevation = 4.dp) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = isSelected(item.dest),
                        onClick = {
                            navController.navigate(item.dest) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontWeight = if (isSelected(item.dest)) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDest,
            modifier = Modifier.padding(paddingValues)
        ) {
            // ─── COMPARTIDO ───
            composable<Feed> {
                PantallaFeedLista(
                    onOfertaClick = { navController.navigate(DetalleOferta(it)) }
                )
            }
            composable<Buscar> {
                PantallaFeedMapa(onOfertaClick = { navController.navigate(DetalleOferta(it)) })
            }
            composable<Perfil> {
                PantallaPerfil(
                    onBackPressed = { navController.popBackStack() },
                    onCerrarSesion = onNavigateToLogin
                )
            }
            composable<Notificaciones> {
                PantallaNotificaciones(onBackPressed = { navController.popBackStack() })
            }
            composable<DetalleOferta> { back ->
                val r = back.toRoute<DetalleOferta>()
                PantallaDetalleOferta(
                    ofertaId = r.ofertaId,
                    onBackPressed = { navController.popBackStack() },
                    onSolicitarClick = { navController.navigate(SolicitarServicio(it)) }
                )
            }
            composable<SolicitarServicio> { back ->
                val r = back.toRoute<SolicitarServicio>()
                PantallaSolicitarServicio(
                    ofertaId = r.ofertaId,
                    onBackPressed = { navController.popBackStack() },
                    onEnviarSolicitud = { navController.popBackStack() }
                )
            }

            // ─── DETALLE SOLICITUD con navegación a Mensajes ───
            composable<DetalleSolicitud> { back ->
                val r = back.toRoute<DetalleSolicitud>()
                PantallaDetalleSolicitud(
                    solicitudId = r.solicitudId,
                    rolUsuario = rolUsuario,
                    onBackPressed = { navController.popBackStack() },
                    onNavigateToMensajes = { solicitudId, tituloServicio ->
                        navController.navigate(Mensajes(solicitudId, tituloServicio))
                    }
                )
            }

            composable<Solicitudes> {
                PantallaListaSolicitudes(
                    onBackPressed = { navController.popBackStack() },
                    onSolicitudClick = { navController.navigate(DetalleSolicitud(it)) },
                    onAceptarSolicitud = {},
                    onRechazarSolicitud = {}
                )
            }

            // ─── MENSAJES ───
            composable<Mensajes> { back ->
                val r = back.toRoute<Mensajes>()
                PantallaMensajes(
                    solicitudId = r.solicitudId,
                    tituloServicio = r.tituloServicio,
                    onBackPressed = { navController.popBackStack() }
                )
            }

            composable<CrearOferta> {
                if (usuarioActual != null) {
                    PantallaCrearOferta(
                        usuario = usuarioActual,
                        onBackPressed = { navController.popBackStack() },
                        onOfertaCreada = { navController.popBackStack() }
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            // ─── TRABAJADOR ───
            composable<Dashboard> {
                PantallaDashboard(
                    onCrearOfertaClick = { navController.navigate(CrearOferta) },
                    onSolicitudesClick = { navController.navigate(Solicitudes) },
                    onNotificacionesClick = { navController.navigate(Notificaciones) },
                    onOfertaClick = { navController.navigate(DetalleOferta(it)) }
                )
            }
            composable<MisOfertas> {
                PantallaMisOfertas(
                    onCrearOfertaClick = { navController.navigate(CrearOferta) },
                    onOfertaClick = { navController.navigate(DetalleOferta(it)) },
                    onEditarOfertaClick = { navController.navigate(EditarOferta(it)) }
                )
            }
            composable<EditarOferta> { back ->
                val r = back.toRoute<EditarOferta>()
                val vm: EditarOfertaViewModel = hiltViewModel()
                PantallaEditarOferta(
                    ofertaId = r.ofertaId,
                    repositorioOfertas = vm.repositorioOfertas,
                    onBackPressed = { navController.popBackStack() },
                    onOfertaActualizada = { navController.popBackStack() }
                )
            }

            // ─── MODERADOR ───
            composable<PanelModerador> {
                PantallaPanelModerador(onBackPressed = { navController.popBackStack() })
            }
            composable<HistorialModerador> {
                PantallaHistorialModerador()
            }
        }
    }
}
