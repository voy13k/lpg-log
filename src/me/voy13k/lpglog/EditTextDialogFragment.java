package me.voy13k.lpglog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

public class EditTextDialogFragment extends DialogFragment {

    private static final String ARG_TEXT = "text";
    private static final String ARG_LAYOUT_ID = "viewId";
    private EditText editText;

    public static EditTextDialogFragment newInstance(int viewId, String text) {
        EditTextDialogFragment fragment = new EditTextDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, viewId);
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String text = getArguments().getString(ARG_TEXT);
        int viewId = getArguments().getInt(ARG_LAYOUT_ID);
        FragmentActivity activity = getActivity();
        View view = activity.getLayoutInflater().inflate(viewId, null);
        editText = (EditText) view.findViewById(android.R.id.input);
        editText.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view)
               .setTitle("LPG Conversion cost");
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText.requestFocus();
    }
}
