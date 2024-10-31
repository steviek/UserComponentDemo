package demo.usercomponent.di.singleton

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SingletonCoroutineModule {
    @Provides
    @Singleton
    @Application
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
}

@Qualifier
annotation class Application