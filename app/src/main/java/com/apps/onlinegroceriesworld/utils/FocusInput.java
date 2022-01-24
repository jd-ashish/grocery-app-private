package com.apps.onlinegroceriesworld.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class FocusInput {
    Context context;
    EditText focused;
    public FocusInput(Context context, EditText focused) {
        this.context = context;
        this.focused = focused;
    }

    public void focused() {
        focused.requestFocus();
        InputMethodManager imm = (InputMethodManager)   context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focused, InputMethodManager.SHOW_IMPLICIT);
    }
}
