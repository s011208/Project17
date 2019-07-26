package yhh.com.project17.fragment.githubusers.epoxy.model

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.reactivex.subjects.PublishSubject
import yhh.com.project17.R
import yhh.com.project17.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_empty_result)
abstract class EmptyResultEpoxyModel : EpoxyModelWithHolder<EmptyResultEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var reloadIntent: PublishSubject<Unit>

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.text.setOnClickListener { reloadIntent.onNext(Unit) }
    }

    class Holder : KotlinEpoxyHolder() {
        val text by bind<TextView>(R.id.text)
    }
}