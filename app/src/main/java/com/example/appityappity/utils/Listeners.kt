package com.example.appityappity.utils

import android.widget.RadioButton

interface OnListItemClickListener {
    fun onClickListItem(filename: String)
}

interface OnRadioButtonClickListener {
    fun onRadioButtonClick(btn: RadioButton)
}