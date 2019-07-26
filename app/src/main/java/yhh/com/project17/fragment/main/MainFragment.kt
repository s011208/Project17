package yhh.com.project17.fragment.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.mvrx.*
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_main.*
import yhh.com.project17.R
import yhh.com.project17.fragment.main.mvrx.viewmodel.MainFragmentViewModel
import yhh.com.project17.fragment.main.view.GithubUsersFragmentStatePagerAdapter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragment : BaseMvRxFragment() {

    @Inject
    lateinit var viewModelFactory: MainFragmentViewModel.Factory

    private val viewModel: MainFragmentViewModel by fragmentViewModel()

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        compositeDisposable = CompositeDisposable()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable += search
            .afterTextChangeEvents()
            .skipInitialValue()
            .throttleLast(250, TimeUnit.MICROSECONDS)
            .subscribe { viewModel.getPageCount(it.editable?.toString() ?: "") }
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    override fun invalidate() {
        withState(viewModel) {
            when (it.pageNumberStateAsync) {
                is Loading -> {
                    progressBar.visibility = View.VISIBLE

                    viewPager.adapter =
                        GithubUsersFragmentStatePagerAdapter(fragmentManager!!)
                    indicator.attachToPager(viewPager)
                }
                is Success -> {
                    progressBar.visibility = View.INVISIBLE

                    if (it.pageNumberStateAsync.invoke() == 0) {
                        Toast.makeText(requireContext(), R.string.fragment_main_empty_result, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        viewPager.adapter = GithubUsersFragmentStatePagerAdapter(
                            fragmentManager!!
                        )
                            .apply {
                                itemPerPage = it.itemPerPage
                                keyword = it.keyword
                                itemSize = it.pageNumberStateAsync.invoke()
                            }
                        indicator.attachToPager(viewPager)
                    }
                }
                is Fail -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), R.string.fragment_main_search_failed, Toast.LENGTH_LONG)
                        .show()

                    viewPager.adapter =
                        GithubUsersFragmentStatePagerAdapter(fragmentManager!!)
                    indicator.attachToPager(viewPager)
                }
                is Uninitialized -> {
                    progressBar.visibility = View.INVISIBLE

                    viewPager.adapter =
                        GithubUsersFragmentStatePagerAdapter(fragmentManager!!)
                    indicator.attachToPager(viewPager)
                }
            }
        }
    }
}