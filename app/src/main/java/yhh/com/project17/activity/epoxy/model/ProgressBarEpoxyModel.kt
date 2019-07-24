package yhh.com.project17.activity.epoxy.model

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import yhh.com.project17.R
import yhh.com.project17.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_progress_bar)
abstract class ProgressBarEpoxyModel : EpoxyModelWithHolder<ProgressBarEpoxyModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var loadMoreIntent: PublishSubject<Unit>

    override fun bind(holder: Holder) {
        super.bind(holder)
        loadMoreIntent.onNext(Unit)
    }

    class Holder : KotlinEpoxyHolder()
}