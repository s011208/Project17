package yhh.com.project17.application

import dagger.Module
import dagger.Provides
import yhh.com.project17.activity.mvrx.viewmodel.MainFragmentState
import yhh.com.repository.repository.GithubRepository
import javax.inject.Singleton

@Module
class MainApplicationModule {

    @Singleton
    @Provides
    fun provideGithubRepository() = GithubRepository()
}