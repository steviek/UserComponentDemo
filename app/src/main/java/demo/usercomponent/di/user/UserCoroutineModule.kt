package demo.usercomponent.di.user

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Qualifier

@InstallIn(UserComponent::class)
@Module
object UserCoroutineModule {
    @Provides
    @UserScoped
    @User
    fun provideUserDefaultScope(lifecycle: UserLifecycle): CoroutineScope {
        val context = Dispatchers.Default + SupervisorJob()
        lifecycle.addOnClearedListener { context.cancel() }
        return CoroutineScope(context)
    }
}

@Qualifier
annotation class User
