package me.voy13k.lpglog.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Usage:
 * <code>
 * public static class MyDialog extends EditTextDialogFragment {
 *     @override
 *     protected boolean onPositiveButtonClick(String text) {
 *         if (!isValid(text)) {
 *             showError(text);
 *             return false;
 *         }
 *         process(text);
 *         return true;
 *     }
 * }
 * 
 * MyDialog myDialog = new MyDialog();
 * MyDialog.initialize(myDialog, R.layout.my_dialog, "My Title", "edited text", "OK");
 * myDialog.show(getActivity().getSupportFragmentManager(), null);
 * </code>
 */
public abstract class EditTextDialogFragment extends DialogFragment {

    private static final String ARG_DIALOG_TITLE = "dialogTitle";
    private static final String ARG_POSITIVE_BUTTON_LABEL = "positiveButtonLabel";
    private static final String ARG_CONTENT_LAYOUT_ID = "contentLayoutId";
    private static final String ARG_INITIAL_TEXT = "initialText";

    private AlertDialog dialog;
    private EditText editText;
    private static DialogInterface.OnClickListener DUMMY_LISTENER = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    };

    public static void setArguments(EditTextDialogFragment fragment, int contentLayoutId,
            String dialogTitle, String initialText, String positiveButtonLabel) {
        Bundle args = new Bundle();
        args.putString(ARG_INITIAL_TEXT, initialText);
        args.putInt(ARG_CONTENT_LAYOUT_ID, contentLayoutId);
        args.putString(ARG_POSITIVE_BUTTON_LABEL, positiveButtonLabel);
        args.putString(ARG_DIALOG_TITLE, dialogTitle);
        fragment.setArguments(args);
    }

    /**
     * event on click of positive button.
     * @param text text user entered into the EditText
     * @return true is dialog should be closed, false if to be kept.
     */
    protected abstract boolean onPositiveButtonClick(String text);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String initialText = args.getString(ARG_INITIAL_TEXT);
        int contentLayoutId = args.getInt(ARG_CONTENT_LAYOUT_ID);
        String positiveButtonLabel = args.getString(ARG_POSITIVE_BUTTON_LABEL);
        String dialogTitle = args.getString(ARG_DIALOG_TITLE);
        View contentView = getActivity().getLayoutInflater().inflate(contentLayoutId, null);
        editText = (EditText) contentView.findViewById(android.R.id.input);
        editText.setText(initialText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);
        builder.setTitle(dialogTitle);
        builder.setPositiveButton(positiveButtonLabel, DUMMY_LISTENER);
        dialog = builder.create();
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPositiveButtonClick(editText.getText().toString())) {
                            dialog.dismiss();
                        }
                    }
                });
        editText.requestFocus();
        editText.selectAll();
        editText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editText, 0);
            }
        });
    }
}