package yhh.com.project17.external.dagger2

import dagger.Module
import dagger.android.ContributesAndroidInjector
import yhh.com.project17.activity.MainActivity
import yhh.com.project17.fragment.main.MainFragment
import yhh.com.project17.fragment.githubusers.GithubUsersFragment

@Module
abstract class ActivityFragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun bindGithubUsersFragment(): GithubUsersFragment
}
