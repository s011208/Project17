package yhh.com.project17.fragment.githubusers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_github_user.*
import yhh.com.project17.R
import yhh.com.project17.fragment.githubusers.epoxy.controller.GithubUserEpoxyController
import yhh.com.project17.fragment.githubusers.mvrx.viewmodel.GithubUsersFragmentViewModel
import yhh.com.project17.util.view.SpacesItemDecoration
import javax.inject.Inject

class GithubUsersFragment : BaseMvRxFragment() {

    companion object {
        const val ARGUMENT_PAGE_NUMBER = "argument_page_number"
        const val ARGUMENT_PAGE_SIZE = "argument_page_size"
        const val ARGUMENT_KEY_WORD = "argument_key_word"
    }

    @Inject
    lateinit var viewModelFactory: GithubUsersFragmentViewModel.Factory

    private val viewModel: GithubUsersFragmentViewModel by fragmentViewModel()

    private val epoxyController = GithubUserEpoxyController()

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        compositeDisposable = CompositeDisposable()
        return inflater.inflate(R.layout.fragment_github_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = epoxyController.adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpacesItemDecoration(12))

        reload()

        compositeDisposable += epoxyController.reloadIntent.subscribe { reload() }
    }

    private fun reload() {
        viewModel.getData(
            arguments?.getString(ARGUMENT_KEY_WORD) ?: "",
            arguments?.getInt(ARGUMENT_PAGE_NUMBER) ?: 0,
            arguments?.getInt(
                ARGUMENT_PAGE_SIZE
            ) ?: 0
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        epoxyController.cancelPendingModelBuild()
    }

    override fun invalidate() {
        withState(viewModel) {
            when (it.dataAsync) {
                is Uninitialized -> {
                    progressBar.visibility = View.INVISIBLE
                }
                is Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Success -> {
                    progressBar.visibility = View.INVISIBLE
                    epoxyController.setData(it.dataAsync.invoke())
                }
                is Fail -> {
                    progressBar.visibility = View.INVISIBLE
                    epoxyController.setData(ArrayList())
                }
            }
        }
    }
}