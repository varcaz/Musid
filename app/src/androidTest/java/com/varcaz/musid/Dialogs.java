package com.varcaz.musid;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;

public class Dialogs extends Dialog {
    public Dialogs(@NonNull Context context) {
        super(context);
        //custom dialog declaration
        setContentView(R.layout.dialog_track);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}
