package yhh.com.project17.fragment.main.mvrx.viewmodel

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import yhh.com.project17.fragment.main.MainFragment
import yhh.com.project17.external.mvrx.MvRxViewModel
import yhh.com.repository.repository.GithubRepository

data class MainFragmentState(
    val pageNumberStateAsync: Async<Int> = Uninitialized,
    val keyword: String = "",
    val itemPerPage: Int = 0
) : MvRxState

class MainFragmentViewModel @AssistedInject constructor(
    @Assisted initialState: MainFragmentState,
    private val githubRepository: GithubRepository
) : MvRxViewModel<MainFragmentState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: MainFragmentState): MainFragmentViewModel
    }

    companion object : MvRxViewModelFactory<MainFragmentViewModel, MainFragmentState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: MainFragmentState
        ): MainFragmentViewModel? {
            return (viewModelContext as FragmentViewModelContext).fragment<MainFragment>()
                .viewModelFactory.create(state)
        }

        private const val NUMBER_OF_ITEM_PER_PAGE = 50
    }

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun getPageCount(keyword: String) {
        Timber.v("getPageCount, keyword: $keyword")
        disposable?.dispose()
        if (keyword.isBlank()) {
            setState { copy(pageNumberStateAsync = Uninitialized, keyword = "", itemPerPage = NUMBER_OF_ITEM_PER_PAGE) }
            return
        }

        disposable = githubRepository
            .getPageCount(keyword,
                NUMBER_OF_ITEM_PER_PAGE
            )
            .subscribeOn(Schedulers.io())
            .doOnError { Timber.w(it, "failed to get page size") }
            .execute {
                Timber.v("pageNumberCount: ${it.invoke()}, state: $it")
                copy(pageNumberStateAsync = it, keyword = keyword, itemPerPage = NUMBER_OF_ITEM_PER_PAGE)
            }
    }
}