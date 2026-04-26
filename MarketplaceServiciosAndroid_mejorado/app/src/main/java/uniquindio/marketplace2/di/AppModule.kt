package uniquindio.marketplace2.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Módulo Hilt principal.
 *
 * Los repositorios (RepositorioAuth, RepositorioOfertas, RepositorioSolicitudes)
 * y SessionManager se auto-proveen mediante @Singleton + @Inject constructor,
 * por lo que no necesitan @Provides explícito aquí.
 *
 * Si en el futuro se añaden dependencias externas (Retrofit, Room, Firebase),
 * sus @Provides van aquí.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
