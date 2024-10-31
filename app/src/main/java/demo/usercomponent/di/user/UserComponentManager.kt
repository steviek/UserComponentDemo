package demo.usercomponent.di.user

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.RetainedLifecycle.OnClearedListener
import demo.usercomponent.ktx.getEntryPoint
import demo.usercomponent.user.UserId
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserComponentManager @Inject constructor(
    private val userComponentBuilder: UserComponentBuilder
) {
    private val userComponents = ConcurrentHashMap<UserId, UserComponent>()

    fun getUserComponent(userId: UserId): UserComponent {
        return userComponents.computeIfAbsent(userId) {
            userComponentBuilder.userId(userId).userLifecycle(UserLifecycleImpl()).build()
        }
    }

    fun logOut(userId: UserId) {
        val component = userComponents.remove(userId) ?: return
        val lifecycle = getEntryPoint<UserLifecycleEntryPoint>(component).getLifecycle()
        (lifecycle as UserLifecycleImpl).clear()
    }
}

@InstallIn(UserComponent::class)
@EntryPoint
interface UserLifecycleEntryPoint {
    fun getLifecycle(): UserLifecycle
}

private class UserLifecycleImpl : UserLifecycle {
    private val listeners = mutableSetOf<OnClearedListener>()

    override fun addOnClearedListener(listener: OnClearedListener) {
        listeners.add(listener)
    }

    override fun removeOnClearedListener(listener: OnClearedListener) {
        listeners.remove(listener)
    }

    fun clear() {
        listeners.forEach { it.onCleared() }
    }
}
