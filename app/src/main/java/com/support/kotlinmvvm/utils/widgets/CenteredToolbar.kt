package com.support.kotlinmvvm.utils.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.support.kotlinmvvm.R

class CenteredToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.toolbarStyle
) : Toolbar(context, attrs, defStyleAttr) {
    private val titleView: TextView = TextView(getContext())

    init {
        // val typeface = ResourcesCompat.getFont(context, R.font.lato_semibold)
        //setCustomTypeFace(titleView, context)
        //titleView.typeface = typeface
        //titleView.typeface = Typeface.DEFAULT_BOLD
        titleView.setTextSize(COMPLEX_UNIT_SP, 18.0F)
        titleView.gravity = Gravity.CENTER
        titleView.maxLines = 1
        addView(titleView, Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        titleView.x = ((width - titleView.width) / 2).toFloat()
    }

    override fun setTitle(title: CharSequence) {
        titleView.text = title
    }
}