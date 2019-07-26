package yhh.com.project17.fragment.mvrx.viewmodel

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import yhh.com.project17.external.mvrx.MvRxViewModel
import yhh.com.project17.fragment.GithubUsersFragment
import yhh.com.repository.entity.GithubUserEntity
import yhh.com.repository.repository.GithubRepository

data class GithubUsersFragmentState(
    val dataAsync: Async<List<GithubUserEntity>> = Uninitialized
) : MvRxState

class GithubUsersFragmentViewModel @AssistedInject constructor(
    @Assisted initialState: GithubUsersFragmentState,
    private val githubRepository: GithubRepository
) : MvRxViewModel<GithubUsersFragmentState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: GithubUsersFragmentState): GithubUsersFragmentViewModel
    }

    companion object : MvRxViewModelFactory<GithubUsersFragmentViewModel, GithubUsersFragmentState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: GithubUsersFragmentState
        ): GithubUsersFragmentViewModel? {
            return (viewModelContext as FragmentViewModelContext).fragment<GithubUsersFragment>()
                .viewModelFactory.create(state)
        }
    }

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun getData(keyword: String, pageNumber: Int, numberPerPage: Int) {
        githubRepository.searchUsers(keyword, pageNumber, numberPerPage)
            .doOnError { Timber.w(it, "failed to get user data") }
            .subscribeOn(Schedulers.io())
            .execute {
                copy(dataAsync = it)
            }
    }
}