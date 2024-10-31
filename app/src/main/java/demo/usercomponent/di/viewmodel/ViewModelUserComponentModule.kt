package demo.usercomponent.di.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import demo.usercomponent.di.user.UserComponent
import demo.usercomponent.di.user.UserComponentManager
import demo.usercomponent.user.UserId

@InstallIn(ViewModelComponent::class)
@Module
object ViewModelUserComponentModule {
    @Provides
    fun provideUserComponent(
        userComponentManager: UserComponentManager,
        savedStateHandle: SavedStateHandle,
    ): UserComponent {
        val userId = getUserIdFromSavedStateHandle(savedStateHandle)
        return userComponentManager.getUserComponent(userId)
    }

    private fun getUserIdFromSavedStateHandle(savedStateHandle: SavedStateHandle): UserId {
        return savedStateHandle.get<UserId>("user_id")!!
    }
}
