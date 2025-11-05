package com.team.ui_xml.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.team.core.event.SingleLiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer =
        object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

    this.observeForever(observer)

    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun <T> SingleLiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
): T {
    var data: T? = null
    val latch = CountDownLatch(1)

    val lifecycleOwner =
        object : LifecycleOwner {
            private val registry = LifecycleRegistry(this)
            override val lifecycle: Lifecycle
                get() =
                    registry.apply {
                        handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
                    }
        }

    this.observe(lifecycleOwner) {
        data = it
        latch.countDown()
    }

    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("SingleLiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
