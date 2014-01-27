package de.metafinanz.mixmatch.activities;

import org.springframework.util.StringUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import de.metafinanz.mixmatch.R;
import de.metafinanz.mixmatch.service.MixMatchService;

/**
 * Activity, zum Speichern des Nutzernamens.
 * 
 * @author ulf
 * 
 */
public class SettingsActivity extends MixMatchActivity implements
		OnItemSelectedListener {

	private EditText userText;
	private Spinner spinner;
	private String spinnerSelectedValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		userText = (EditText) findViewById(R.id.textSettingsUsername);

		userText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userText.setText("");
				userText.setTextColor(Color.BLACK);
			}
		});

		String username = getUsername();

		if (username != null && username.length() > 0) {
			userText.setText(getUsername());
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initSpinner();
	}

	private void initSpinner() {
		spinner = (Spinner) findViewById(R.id.spinnerSettingsDataService);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.dataservice_list,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		// Den gespierchert Service als vorselektiert setzen
		String savedItem = getString(MixMatchService.DATA_SERVICE_KEY);
		if (StringUtils.hasText(savedItem)) {
			int pos = adapter.getPosition(savedItem);
			spinner.setSelection(pos, true);
		}
	}

	public void save(View view) {
		EditText userText = (EditText) findViewById(R.id.textSettingsUsername);
		String username = userText.getText().toString();
		setUsername(username);
		userText.setTextColor(Color.DKGRAY);
		storeString(MixMatchService.DATA_SERVICE_KEY, spinnerSelectedValue);
		MixMatchService.init(this);
		finish();
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String selectedValue = (String) parent.getItemAtPosition(pos);
		spinnerSelectedValue = selectedValue;
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

}
