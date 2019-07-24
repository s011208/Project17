package yhh.com.project17.activity.mvrx.viewmodel

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import yhh.com.project17.activity.MainFragment
import yhh.com.project17.external.mvrx.MvRxViewModel
import yhh.com.repository.entity.GithubUserEntityWrapper
import yhh.com.repository.repository.GithubRepository

data class MainFragmentState(val githubUserEntityWrapperAsync: Async<GithubUserEntityWrapper> = Uninitialized): MvRxState

class MainFragmentViewModel @AssistedInject constructor(
    @Assisted initialState: MainFragmentState,
    private val githubRepository: GithubRepository
) : MvRxViewModel<MainFragmentState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(state: MainFragmentState): MainFragmentViewModel
    }

    companion object : MvRxViewModelFactory<MainFragmentViewModel, MainFragmentState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: MainFragmentState
        ): MainFragmentViewModel? {
            return (viewModelContext as FragmentViewModelContext).fragment<MainFragment>()
                .viewModelFactory.create(state)
        }

        private const val NUMBER_OF_ITEM_PER_PAGE = 100
    }

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun search(key: String, page: Int = 1) {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        if (key.isBlank()) {
            setState { copy(githubUserEntityWrapperAsync = Uninitialized) }
            return
        }
        disposable = githubRepository.searchUsers(key, page, NUMBER_OF_ITEM_PER_PAGE)
            .subscribeOn(Schedulers.io())
            .doOnError { Timber.w(it, "failed to search users") }
            .execute {
                copy(githubUserEntityWrapperAsync = it)
            }
    }

    fun loadMore() {
        Timber.v("loadMore")
    }
}