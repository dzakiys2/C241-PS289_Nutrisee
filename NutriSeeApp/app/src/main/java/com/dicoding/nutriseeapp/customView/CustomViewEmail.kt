package com.dicoding.nutriseeapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.nutriseeapp.R

class CustomViewEmail: AppCompatEditText {
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
        hint = "your.email@gmail.com"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!text.isNullOrBlank()){
                    this@CustomViewEmail.error = if (!isValidEmail(text.toString())) {
                        context.getString(R.string.email_blank)
                    } else{
                        null
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                if (text.isNullOrEmpty()){
                    this@CustomViewEmail.error = context.getString(R.string.email_blank)
                }
            }

        })
    }

    fun isValidEmail(email: String): Boolean {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
