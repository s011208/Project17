package yhh.com.project17.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import yhh.com.project17.R
import yhh.com.project17.activity.epoxy.controller.GithubUserEpoxyController
import yhh.com.project17.activity.mvrx.viewmodel.MainFragmentViewModel
import yhh.com.project17.util.view.SpacesItemDecoration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragment : BaseMvRxFragment() {

    @Inject
    lateinit var viewModelFactory: MainFragmentViewModel.Factory

    private val viewModel: MainFragmentViewModel by fragmentViewModel()

    private val epoxyController = GithubUserEpoxyController()

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        compositeDisposable = CompositeDisposable()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        usersRecyclerView.adapter = epoxyController.adapter
        usersRecyclerView.setHasFixedSize(true)
        usersRecyclerView.addItemDecoration(SpacesItemDecoration(12))

        compositeDisposable += search
            .afterTextChangeEvents()
            .skipInitialValue()
            .throttleFirst(500, TimeUnit.MICROSECONDS)
            .subscribe { viewModel.search(it.editable?.toString() ?: "") }

        compositeDisposable += usersRecyclerView
            .loadMoreIntent
            .subscribe { viewModel.loadMore() }
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
    }

    override fun invalidate() {
        withState(viewModel) {

            if (it.githubUserEntityWrapperAsync is Loading && !it.isLoadingMore) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }

            when (it.githubUserEntityWrapperAsync) {
                is Success -> epoxyController.setData(
                    true,
                    it.isLoadingMore,
                    it.users
                )
                is Fail -> {
                    Toast
                        .makeText(requireContext(), R.string.activity_main_search_failed_toast, Toast.LENGTH_LONG)
                        .show()
                    epoxyController.setData(false, it.isLoadingMore, it.users)
                }
                is Uninitialized ->
                    epoxyController.setData(
                        false,
                        it.isLoadingMore,
                        ArrayList()
                    )
                is Loading -> {
                    epoxyController.setData(
                        false,
                        it.isLoadingMore,
                        it.users
                    )
                }
            }

            usersRecyclerView.isLoading = it.isLoadingMore
        }
    }
}