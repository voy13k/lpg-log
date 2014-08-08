package me.voy13k.lpglog.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import me.voy13k.lpglog.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class DatePickerButton extends Button {

    private static final DateFormat DEFAULT_LABEL_FORMAT = DateFormat.getDateInstance();

    private Calendar calendar = Calendar.getInstance();
    private String pattern;
    private String pickerTitle;

    private OnClickListener myOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentActivity activity = (FragmentActivity) getContext();
            FragmentManager fragMgr = activity.getSupportFragmentManager();
            DialogFragment f = PickerFragment.newInstance(getId(), pickerTitle, calendar);
            f.show(fragMgr, "whatevs");
        }
    };

    public DatePickerButton(Context context) {
        this(context, null, 0);
    }

    public DatePickerButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        updateLabel();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        updateLabel();
    }

    public String getPickerTitle() {
        return pickerTitle;
    }

    public void setPickerTitle(String pickerTitle) {
        this.pickerTitle = pickerTitle;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        obtainAttributes(context, attrs, defStyleAttr);
        setOnClickListener(myOnClickListener);
    }

    private void obtainAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DatePickerButton, defStyleAttr, 0);
        try {
            setPattern(a.getString(R.styleable.DatePickerButton_pattern));
            setPickerTitle(a.getString(R.styleable.DatePickerButton_pickerTitle));
        } finally {
            a.recycle();
        }
    }

    private void updateLabel() {
        DateFormat labelFormat = DEFAULT_LABEL_FORMAT;
        if (pattern != null) {
            labelFormat = new SimpleDateFormat(pattern, Locale.US);
        }
        setText(labelFormat.format(calendar.getTime()));
    }

    private static class PickerFragment extends DialogFragment {

        private static final String ARG_CALENDAR = "calendar";
        private static final String ARG_BUTTON_ID = "buttonId";
        private static final String ARG_PICKER_TITLE = "pickerTitle";

        static PickerFragment newInstance(int buttonId, String pickerTitle, Calendar calendar) {
            Bundle args = new Bundle();
            args.putSerializable(PickerFragment.ARG_CALENDAR, calendar);
            args.putInt(PickerFragment.ARG_BUTTON_ID, buttonId);
            args.putString(PickerFragment.ARG_PICKER_TITLE, pickerTitle);
            PickerFragment fragment = new PickerFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            OnDateSetListener listener = newListener(args.getInt(ARG_BUTTON_ID));
            Calendar calendar = (Calendar) args.get(ARG_CALENDAR);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year,
                    month, day) {
                // Workaround for
                // https://code.google.com/p/android/issues/detail?id=34833
                @Override
                protected void onStop() {
                }
            };
            String pickerTitle = args.getString(ARG_PICKER_TITLE);
            if (pickerTitle != null) {
                dialog.setTitle(pickerTitle);
            }
            return dialog;
        }

        private OnDateSetListener newListener(final int buttonId) {
            return new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    DatePickerButton button = (DatePickerButton) getActivity().findViewById(
                            buttonId);
                    button.setCalendar(new GregorianCalendar(year, month, day));
                }
            };
        }

    }

}
