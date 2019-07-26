package yhh.com.project17.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class TestLifecycleOwner : LifecycleOwner {

    private val _lifecycle = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = _lifecycle
}
