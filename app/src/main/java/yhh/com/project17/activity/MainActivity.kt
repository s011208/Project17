package yhh.com.project17.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.BaseMvRxActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import yhh.com.project17.R
import yhh.com.project17.fragment.main.MainFragment
import javax.inject.Inject

class MainActivity : BaseMvRxActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment())
                .commitAllowingStateLoss()
        }
    }
}
