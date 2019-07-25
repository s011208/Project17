package yhh.com.project17.activity.epoxy.model

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import yhh.com.project17.R
import yhh.com.project17.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_progress_bar)
abstract class ProgressBarEpoxyModel : EpoxyModelWithHolder<ProgressBarEpoxyModel.Holder>() {

    class Holder : KotlinEpoxyHolder()
}