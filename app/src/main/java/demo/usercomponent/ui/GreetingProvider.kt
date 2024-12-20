package demo.usercomponent.ui

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class GreetingProvider @Inject constructor(private val eventNameProvider: EventNameProvider) {
    fun getGreeting(): String {
        return "Hello, " + eventNameProvider.eventName
    }
}

// Will be part of the SingletonComponent generated by Hilt as long as
// this file is on the classpath
@InstallIn(SingletonComponent::class)
@Module
object EventNameProviderModule {
    @Provides
    fun provideEventNameProvider() = EventNameProvider("Droidcon")
}
