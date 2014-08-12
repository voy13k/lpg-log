package me.voy13k.lpglog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FillUpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_up);
        if (savedInstanceState == null) {
            Long entryId = getIntent().getLongExtra(FillUpFragment.ARG_ENTRY_ID, 0);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, FillUpFragment.newInstance(entryId)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fill_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_done) {
            FillUpFragment fillUpFragment = (FillUpFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            if (fillUpFragment.save()) {
                showSuccess();
                restartMainAction();
            } else {
                showError();
            }
            return true;
        }
        if (id == R.id.action_cancel) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSuccess() {
        Toast.makeText(this, getResources().getText(R.string.message_save_success),
                Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        Toast toast = Toast.makeText(this,
                getResources().getText(R.string.message_save_failed), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
        toast.show();
    }

    private void restartMainAction() {
        Intent intent = new Intent(this, MainActivity.class);
        // This should cause the MainActivity to be re-created, so that
        // new data is loaded.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
