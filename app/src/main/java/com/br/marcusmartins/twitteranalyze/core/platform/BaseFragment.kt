package com.br.marcusmartins.twitteranalyze.core.platform

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.br.marcusmartins.twitteranalyze.AndroidApplication
import com.br.marcusmartins.twitteranalyze.R.color
import com.br.marcusmartins.twitteranalyze.core.di.ApplicationComponent
import com.br.marcusmartins.twitteranalyze.core.extension.appContext
import com.br.marcusmartins.twitteranalyze.core.extension.viewContainer
import kotlinx.android.synthetic.main.toolbar.progress
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    abstract fun layoutId(): Int

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(layoutId(), container, false)

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) =
            with(activity) { if (this is BaseActivity) this.progress.visibility = viewStatus }

    internal fun notify(@StringRes message: Int) =
            Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        val snackBar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(actionText) { _ -> action.invoke() }
        snackBar.setActionTextColor(ContextCompat.getColor(appContext,
                color.white_color))
        snackBar.show()
    }

    internal fun showSnackbar(@StringRes message: Int, @ColorRes backGroundColor: Int, @ColorRes textColor: Int) {
        val snackbar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(activity!!, backGroundColor))
        val textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.textSize = 30f
        textView.setTextColor(ContextCompat.getColor(activity!!, textColor))
        snackbar.show()
    }
}
