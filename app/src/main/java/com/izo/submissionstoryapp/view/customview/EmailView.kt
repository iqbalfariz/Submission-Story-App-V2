package com.izo.submissionstoryapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class EmailView : AppCompatEditText {

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (s.toString() == android.util.Patterns.EMAIL_ADDRESS.toString()) error == null else error = "Masukkan format alamat email dengan benar"
                s?.apply {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches())
                        error = null
                    else
                        error = "Masukkan format email yang benar"
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

    }

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}