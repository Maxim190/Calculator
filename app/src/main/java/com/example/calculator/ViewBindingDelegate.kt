package com.example.calculator

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


fun <T : ViewBinding> viewBinding(binder: (View) -> T) = ViewBindingDelegate(binder)

class ViewBindingDelegate<T : ViewBinding>(
    private val binder: (View) -> T
) : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

    private var viewBinding: T? = null

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        viewBinding = null
        super.onDestroy(owner)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return viewBinding ?: binder(thisRef.requireView()).also {
            viewBinding = it
            thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
        }
    }
}
