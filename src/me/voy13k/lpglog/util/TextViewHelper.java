package me.voy13k.lpglog.util;

import android.view.View;
import android.widget.TextView;

public class TextViewHelper {
    View container;

    public TextViewHelper(View container) {
        this.container = container;
    }

    public void setText(int id, java.text.Format format, Object value) {
        TextView textView = (TextView) container.findViewById(id);
        textView.setText(format.format(value));
    }
}