package demo.usercomponent.di.activity

import android.app.Activity
import android.content.Intent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import demo.usercomponent.di.user.UserComponent
import demo.usercomponent.di.user.UserComponentManager

@InstallIn(ActivityComponent::class)
@Module
object ActivityUserComponentModule {
    @Provides
    fun provideUserComponent(
        userComponentManager: UserComponentManager,
        intent: Intent,
    ): UserComponent {
        val userId = getUserIdFromIntent(intent)
        return userComponentManager.getUserComponent(userId)
    }

    private fun getUserIdFromIntent(intent: Intent): Int {
        return intent.getIntExtra("user_id", -1)
    }

    @Provides
    fun provideIntent(activity: Activity): Intent {
        // This is just for demo purposes
        return activity.intent.also {
            if (!it.hasExtra("user_id")) {
                it.putExtra("user_id", 1)
            }
        }
    }
}
