package com.support.kotlinmvvm.utils.widgets.spinner

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.support.kotlinmvvm.R
import com.support.kotlinmvvm.databinding.RowSpinnerItemBinding
import com.support.kotlinmvvm.utils.adapter.ChoiceMode
import com.support.kotlinmvvm.utils.adapter.setUpAdapter
import kotlinx.android.synthetic.main.dialog_spinner.*
import kotlinx.android.synthetic.main.dialog_spinner.view.*


class SpinnerBottomDialogFragment<T>(
    val title: String,
    val items: MutableList<T>,
    val onClick: ((T, Int) -> Unit)
) : BottomSheetDialogFragment() {

    private var mBehavior: BottomSheetBehavior<*>? = null
    private var isSearchOpen = false
    private var mainView : View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        isCancelable = false

        mainView = View.inflate(context, R.layout.dialog_spinner, null)

        val linearLayout = mainView?.findViewById<LinearLayout>(R.id.root)
        val params = linearLayout?.layoutParams as LinearLayout.LayoutParams
        params.height = getScreenHeight()
        linearLayout.layoutParams = params

        mainView?.recyclerView?.apply {
            setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = spinnerAdapter

            /*this.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if(!recyclerView.canScrollVertically(-1)) {
                        // we have reached the top of the list
                        lytHeader.elevation = 0f
                    } else {
                        // we are not at the top yet
                        lytHeader.elevation = 50f
                    }
                }
            });*/
        }

        mainView?.edtSearch?.setOnFocusChangeListener { viewd, b ->
            if (b) {
                if (isSearchOpen) {
                    mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        mainView?.tvTitle?.text = title

        mainView?.btnSearch?.setOnClickListener {
            if (!isSearchOpen) {
                isSearchOpen = true
                mainView?.lytSpinnerNormalHeader?.visibility = View.GONE
                mainView?.lytSpinnerSearchHeader?.visibility = View.VISIBLE
                Handler().postDelayed({
                    mainView?.edtSearch?.requestFocus()
                    mainView?.edtSearch?.showKeyboard()
                }, 100)
            }
        }

        mainView?.btnClose?.setOnClickListener {
            mainView?.edtSearch?.setText("")
        }

        mainView?.btnBack?.setOnClickListener {
            back()
        }

        mainView?.btnCancel?.setOnClickListener {
            dismiss()
            closeKeyboard()
        }

        mainView?.edtSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty() == true) {
                    mainView?.btnClose?.visibility = View.VISIBLE
                    val filterData = spinnerAdapter.getAllItems()?.filter { it.toString().contains(p0, true) }
                    spinnerAdapter.setFilter(filterData as MutableList<T>)
                } else {
                    mainView?.btnClose?.visibility = View.GONE
                    spinnerAdapter.initFilter()
                }
            }

        })

        mainView?.let { dialog.setContentView(it) }

        mBehavior = BottomSheetBehavior.from<View>(mainView?.parent as View)

        mBehavior?.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mainView?.lytHeader?.elevation = 12f
                } else {
                    mainView?.lytHeader?.elevation = 4f
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        dialog.setOnKeyListener(object : View.OnKeyListener, DialogInterface.OnKeyListener {
            override fun onKey(p0: DialogInterface?, keyCode: Int, p2: KeyEvent?): Boolean {
                return if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isSearchOpen) {
                        back()
                    }
                    true
                } else
                    false
            }

            override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
                return if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isSearchOpen) {
                        back()
                    }
                    true
                } else
                    false
            }
        })

        return dialog
    }

    var spinnerAdapter = setUpAdapter(items,
        ChoiceMode.NONE,
        R.layout.row_spinner_item,
        { item, binder: RowSpinnerItemBinding?, position, adapter ->
            binder?.item = item.toString()
            binder?.executePendingBindings()
        },
        { item, position, adapter ->
            dismiss()
            onClick.invoke(item, position)
        }
    )

    fun back(){
        isSearchOpen = false
        mainView?.lytSpinnerNormalHeader?.visibility = View.VISIBLE
        mainView?.lytSpinnerSearchHeader?.visibility = View.GONE
        mainView?.edtSearch?.setText("")
        closeKeyboard()
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun closeKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun View.showKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }


}