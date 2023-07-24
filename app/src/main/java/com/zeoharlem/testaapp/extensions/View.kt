package com.zeoharlem.testaapp.extensions

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.zeoharlem.testaapp.R

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun <T : View?> View.find(id: Int): T? {
    return this.findViewById<T>(id)
}


fun Fragment.showSnackbar(
    snackbarText: String,
    timeLength: Int = Snackbar.LENGTH_SHORT,
    @ColorInt snackBarColor: Int = Color.BLACK
): Snackbar? {
    activity?.let {
        val snack = Snackbar.make(it.findViewById(android.R.id.content), snackbarText, timeLength)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        view.layoutParams = params
        snack.view.setBackgroundColor(snackBarColor)

        snack.show()
        return snack
    }
    return null
}

fun Activity.showSnackbar(snackbarText: String, timeLength: Int): Snackbar {
    val snack = Snackbar.make(this.findViewById(android.R.id.content), snackbarText, timeLength)
    val view = snack.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
    view.layoutParams = params
    snack.show()
    return snack
}

interface SnackBarAction {
    fun onSnackBarActionClick()
}

fun Fragment.showSnackbarWithAction(
    snackbarText: String,
    timeLength: Int,
    callback: SnackBarAction,
    actionText: String = "Retry"
): Snackbar? {
    activity?.let {
        val snack = Snackbar.make(it.findViewById(android.R.id.content), snackbarText, timeLength)
            .setAction(actionText) {
                callback.onSnackBarActionClick()
            }
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        view.layoutParams = params
        snack.show()
        return snack
    }
    return null
}

fun MaterialCardView.toggleState(unSelectedView: MaterialCardView? = null) {
    unSelectedView?.strokeColor =
        ContextCompat.getColor(this.context, R.color.color_grey_1A424242)
    this.strokeColor = ContextCompat.getColor(this.context, R.color.colorPrimary)
}

fun MaterialCardView.notSelectedState(unSelectedView: MaterialCardView? = null) {
    unSelectedView?.strokeColor =
        ContextCompat.getColor(this.context, R.color.color_grey_1A424242)
    this.strokeColor = ContextCompat.getColor(this.context, R.color.color_grey_1A424242)
}

fun TextView.notSelectedState(unselectedView: TextView? = null) {
    unselectedView?.setTextColor(Color.BLACK)
    val emptyEllipse =
        AppCompatResources.getDrawable(this.context, R.drawable.ic_ellipse_icon)
    emptyEllipse?.let {
        unselectedView?.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            it,
            null
        )
    }

    this.setTextColor(Color.BLACK)
    val filledEllipse =
        AppCompatResources.getDrawable(this.context, R.drawable.ic_ellipse_icon)
    filledEllipse?.let {
        this.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            it,
            null
        )
    }
}

fun TextView.toggleState(unselectedView: TextView? = null) {
    unselectedView?.setTextColor(Color.BLACK)
    val emptyEllipse =
        AppCompatResources.getDrawable(this.context, R.drawable.ic_ellipse_icon)
    emptyEllipse?.let {
        unselectedView?.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            it,
            null
        )
    }

    this.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
    val filledEllipse =
        AppCompatResources.getDrawable(this.context, R.drawable.ic_ellipse_filled)
    filledEllipse?.let {
        this.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            it,
            null
        )
    }
}

fun MaterialButton.setBackgroundColorState() {
    val states = arrayOf(
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf(-android.R.attr.state_enabled)
    )
    val colors = intArrayOf(
        ContextCompat.getColor(this.context, R.color.colorPrimary), // enabled color
        ContextCompat.getColor(this.context, R.color.color_grey_aaa) // disabled color
    )
    this.backgroundTintList = ColorStateList(states, colors)
}

/*fun MaterialButton.enableLoadingState(
    activity: MainActivity,
    @ColorInt progressBarColor: Int? = null
) {
    activity.apply {
        bindProgressButton(this@enableLoadingState)
    }

    // (Optional) Enable fade In / Fade out animations
    this.attachTextChangeAnimator()
    this.showProgress {
        //Optional text for loading state: buttonTextRes = R.string.loading_text
        progressColor = progressBarColor ?: Color.WHITE
    }
    this.isEnabled = false
}

fun MaterialButton.disableLoadingState(btnText: String) {
    this.hideProgress(btnText)
    this.isEnabled = true
}*/

//This will only work on apis above 21
fun Activity.makeStatusBarTransparent() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        statusBarColor = Color.TRANSPARENT
    }
}



/**
 * Returns true if the navigation controller is still pointing at 'this' fragment, or false if it already navigated away.
 */
fun Fragment.mayNavigate(): Boolean {

    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id

    // add tag_navigation_destination_id to your ids.xml so that it's unique:
    /*val destinationIdOfThisFragment = view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController

    // check that the navigation graph is still in 'this' fragment, if not then the app already navigated:
    if (destinationIdInNavController == destinationIdOfThisFragment) {
        view?.setTag(R.id.tag_navigation_destination_id, destinationIdOfThisFragment)
        return true
    } else {
        Log.d("FragmentExtensions", "May not navigate: current destination is not the current fragment.")
        return false
    }*/
    return false
}
