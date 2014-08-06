package me.voy13k.lpglog;

import me.voy13k.lpglog.util.ListenerRegistry;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class FillUpActivity extends ActionBarActivity implements ListenerRegistry.ListenerRegistryHost {

	private ListenerRegistry fragmentRegistry = new ListenerRegistry(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_up);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new FillUpFragment()).commit();
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
			finish();
			return true;
		}
		if (id == R.id.action_cancel) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public ListenerRegistry getListenerRegistry() {
		return fragmentRegistry ;
	}

}
