package com.example.ankolayoutexample.widgets

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import com.example.ankolayoutexample.R

class LinkedCheckBox(context : Context , attrs : AttributeSet) : CheckBox(context , attrs){
    private var onLinkClickListener: ((url: String) -> Unit)? = null
    private var attributes: TypedArray =
        context.obtainStyledAttributes(attrs, R.styleable.LinkedCheckBox, 0, 0)

    init {

        val linkText = attributes.getString(R.styleable.LinkedCheckBox_link_text)
        setHtmlText(linkText)
        attributes.recycle()

    }

    fun setHtmlText(linkText: String?) {
        linkText?.let {
            val sequence: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(linkText, Html.FROM_HTML_MODE_LEGACY)
            } else Html.fromHtml(linkText ,  HtmlCompat.FROM_HTML_MODE_LEGACY)

            val strBuilder = SpannableStringBuilder(sequence)
            val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
            for (span in urls) {
                makeLinkClickable(strBuilder, span)
            }
            text = strBuilder
            movementMethod = LinkMovementMethod.getInstance()
        }

        invalidate()

    }

    fun setHtmlText(@StringRes resId : Int) {
        setHtmlText(context.getString(resId))
    }


    fun setOnLinkClickListener(onLinkClickListener: ((url: String) -> Unit)?) {
        this.onLinkClickListener = onLinkClickListener
    }


    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                onLinkClickListener?.invoke(span.url)
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }
}