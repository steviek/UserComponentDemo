package demo.usercomponent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import demo.usercomponent.di.user.CopyModuleEntryPoint
import demo.usercomponent.di.user.UserComponentManager
import demo.usercomponent.ktx.getEntryPoint
import demo.usercomponent.user.UserId

fun <T> userComponent(
    factory: CopyModuleEntryPoint.() -> T
): Lazy<T> = lazy {
    // Use a test rule to set up the user in your tests
    val testUserId = getTestUserId()
    // Make use of the current test Context
    val context = ApplicationProvider.getApplicationContext<Context>()
    // Get this from an entry point
    val userComponentManager = getUserComponentManager(context)
    val component = userComponentManager.getUserComponent(testUserId)
    factory(getEntryPoint<CopyModuleEntryPoint>(component))
}

private fun getTestUserId(): UserId {
    return 1
}

private fun getUserComponentManager(context: Context): UserComponentManager {
    return getEntryPoint<UserComponentManagerEntryPoint>(context).getUserComponentManager()
}

@InstallIn(SingletonComponent::class)
@EntryPoint
interface UserComponentManagerEntryPoint {
    fun getUserComponentManager(): UserComponentManager
}