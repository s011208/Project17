package yhh.com.project17.util.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class EndlessRecyclerView : RecyclerView {

    companion object {
        private const val VISIBLE_THRESHOLD = 10
    }

    val loadMoreIntent by lazy { PublishSubject.create<Unit>() }

    var isLoading = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        layoutManager = LinearLayoutManager(context)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Timber.e("isLoading: $isLoading")
                if (isLoading) return
                if (layoutManager !is LinearLayoutManager) return
                if (adapter == null) return

                val itemCount = (layoutManager as LinearLayoutManager).itemCount
                val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                if (itemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                    isLoading = true
                    loadMoreIntent.onNext(Unit)
                }
            }
        })
    }
}