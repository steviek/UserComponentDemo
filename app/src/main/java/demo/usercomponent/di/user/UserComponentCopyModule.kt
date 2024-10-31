package demo.usercomponent.di.user

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import demo.usercomponent.ktx.getEntryPoint
import demo.usercomponent.user.UserProfileRepository

@InstallIn(ActivityComponent::class, ViewModelComponent::class)
@Module
object CopyModule {
    @Provides
    @Reusable
    fun provideEntryPoint(component: UserComponent): CopyModuleEntryPoint {
        return getEntryPoint<CopyModuleEntryPoint>(component)
    }

    @Provides
    fun provideUserProfileRepository(entryPoint: CopyModuleEntryPoint): UserProfileRepository {
        return entryPoint.getUserProfileRepository()
    }
}

@InstallIn(UserComponent::class)
@EntryPoint
interface CopyModuleEntryPoint {
    fun getUserProfileRepository(): UserProfileRepository
}
