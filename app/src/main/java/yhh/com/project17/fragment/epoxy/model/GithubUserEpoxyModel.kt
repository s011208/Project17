package yhh.com.project17.fragment.epoxy.model

import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import yhh.com.project17.R
import yhh.com.project17.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_github_user)
abstract class GithubUserEpoxyModel : EpoxyModelWithHolder<GithubUserEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var avatarUrl: String

    @EpoxyAttribute
    lateinit var userName: String

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.userName.text = userName

        Glide
            .with(holder.avatar)
            .load(avatarUrl)
            .apply(
                RequestOptions()
                    .error(android.R.drawable.ic_delete)
                    .placeholder(CircularProgressDrawable(holder.avatar.context)
                        .apply {
                            strokeWidth = 5f
                            centerRadius = 30f
                            start()
                        }).centerCrop()
            )
            .into(holder.avatar)
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        Glide
            .with(holder.avatar)
            .clear(holder.avatar)
    }

    class Holder : KotlinEpoxyHolder() {
        val avatar by bind<ImageView>(R.id.avatar)
        val userName by bind<TextView>(R.id.userName)
    }
}