package com.support.kotlinmvvm.utils.statelayout

import android.content.Context
import androidx.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.support.kotlinmvvm.R
import com.support.kotlinmvvm.utils.statelayout.StateLayout.State.*

class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var contentLayout: View? = null
    private var loadingLayout: View? = null
    private var infoLayout: View? = null

    private var state: State = CONTENT

    @LayoutRes
    private var loadingLayoutRes: Int = R.layout.layout_state_loading
    @LayoutRes
    private var infoLayoutRes: Int = R.layout.layout_state_info

    init {
        if (isInEditMode) {
            state = CONTENT
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupContentState()
        setupLoadingState()
        setupInfoState()

        updateWithState()
        checkChildCount()
    }

    private fun setupContentState() {
        contentLayout = getChildAt(0)
        contentLayout?.gone()
    }

    private fun setupLoadingState() {
        loadingLayout = inflate(loadingLayoutRes)
        loadingLayout?.gone()
        addView(loadingLayout)
    }

    private fun setupInfoState() {
        infoLayout = inflate(infoLayoutRes)
        infoLayout?.gone()
        addView(infoLayout)
    }

    private fun updateWithState() {
        when (state) {
            LOADING -> loading()
            CONTENT -> content()
            INFO, ERROR, EMPTY -> info()
            LOADING_WITH_CONTENT -> loadingWithContent()
            NONE -> hideAll()
        }
    }

    private fun checkChildCount() {
        if (childCount > 4 || childCount == 0) {
            throwChildCountException()
        }
    }

    private fun hideAll() {
        contentLayout.gone()
        infoLayout.gone()
    }

    private fun throwChildCountException(): Nothing =
        throw IllegalStateException("StateLayout can host only one direct child")

    fun loading(): StateLayout {
        state = LOADING
        loadingLayout.visible()
        contentLayout.gone()
        infoLayout.gone()
        return this
    }

    fun loading(@LayoutRes layoutId: Int) {
        this.loadingLayoutRes = layoutId
        removeView(loadingLayout)
        setupLoadingState()
        showState(provideLoadingStateInfo())
    }

    fun content(): StateLayout {
        state = CONTENT
        contentLayout.visible()
        infoLayout.gone()
        loadingLayout.gone()
        return this
    }

    fun infoImage(imageRes: Int): StateLayout {
        infoLayout.findView<ImageView>(R.id.imageView_state_layout_info) {
            setImageResource(imageRes)
            visibility = View.VISIBLE
        }
        return info()
    }

    fun infoTitle(title: String): StateLayout {
        infoLayout.findView<TextView>(R.id.textView_state_layout_info_title) {
            text = title
            visibility = View.VISIBLE
        }
        return info()
    }

    fun infoMessage(message: String): StateLayout {
        infoLayout.findView<TextView>(R.id.textView_state_layout_info_message) {
            text = message
            visibility = View.VISIBLE
        }
        return info()
    }

    fun infoButtonListener(block: () -> Unit) {
        infoLayout.findView<Button>(R.id.button_state_layout_info) {
            setOnClickListener { block.invoke() }
        }
        info()
    }

    fun infoButtonText(buttonText: String): StateLayout {
        if (buttonText.isEmpty()){
            infoLayout.findView<Button>(R.id.button_state_layout_info) {
                visibility = View.GONE
            }
        }else {
            infoLayout.findView<Button>(R.id.button_state_layout_info) {
                text = buttonText
                visibility = View.VISIBLE
            }
        }
        return info()
    }

    fun infoButton(buttonText: String, block: () -> Unit): StateLayout {
        infoLayout.findView<Button>(R.id.button_state_layout_info) {
            text = buttonText
            setOnClickListener { block.invoke() }
            visibility = View.VISIBLE
        }
        return info()
    }

    fun info(): StateLayout {
        state = INFO
        contentLayout.gone()
        loadingLayout.gone()
        infoLayout.visible()
        return this
    }

    fun info(@LayoutRes layoutId: Int) {
        this.infoLayoutRes = layoutId
        removeView(infoLayout)
        setupInfoState()
        showState(provideInfoStateInfo())
    }

    fun loadingWithContent(): StateLayout {
        state = LOADING_WITH_CONTENT
        contentLayout.visible()
        infoLayout.gone()
        loadingLayout.visible()
        return this
    }

    fun showLoading(stateInfo: StateInfo?) = showState(stateInfo)

    fun showContent(stateInfo: StateInfo?) = showState(stateInfo)

    fun showInfo(stateInfo: StateInfo?) = showState(stateInfo)

    fun showLoadingWithContent(stateInfo: StateInfo?) = showState(stateInfo)

    fun showError(stateInfo: StateInfo?) = showState(stateInfo)

    fun showEmpty(stateInfo: StateInfo?) = showState(stateInfo)

    fun showState(stateInfo: StateInfo?) {
        when (stateInfo?.state) {
            LOADING -> loading()
            CONTENT -> content()
            LOADING_WITH_CONTENT -> loadingWithContent()
            INFO, ERROR, EMPTY -> {
                stateInfo.infoButtonText?.let { infoButtonText(it) }
                stateInfo.infoImage?.let { infoImage(it) }
                stateInfo.infoTitle?.let { infoTitle(it) }
                stateInfo.infoMessage?.let { infoMessage(it) }
                stateInfo.infoButtonText?.let { infoButtonText(it) }
            }
            null, NONE -> hideAll()
        }
    }

    companion object {
        @JvmStatic
        fun provideLoadingStateInfo() = StateInfo(state = LOADING)

        @JvmStatic
        fun provideContentStateInfo() = StateInfo(state = CONTENT)

        @JvmStatic
        fun provideErrorStateInfo() = StateInfo(state = ERROR)

        @JvmStatic
        fun provideLoadingWithContentStateInfo() = StateInfo(state = LOADING_WITH_CONTENT)

        @JvmStatic
        fun provideInfoStateInfo() = StateInfo(state = INFO)

        @JvmStatic
        fun provideEmptyStateInfo() = StateInfo(state = EMPTY)

        @JvmStatic
        fun provideNoneStateInfo() = StateInfo(state = NONE)
    }

    enum class State {
        LOADING, CONTENT, INFO, LOADING_WITH_CONTENT, ERROR, EMPTY, NONE
    }

    data class StateInfo(
        val infoImage: Int? = null,
        val infoTitle: String? = null,
        val infoMessage: String? = null,
        val infoButtonText: String? = null,
        val state: StateLayout.State = INFO,
        val onInfoButtonClick: (() -> Unit)? = null
    )
}