package com.codepath.googleimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.googleimagesearch.dto.SearchCriteria;

public class SearchCriteriaActivity extends Activity {

	private Spinner spImageSize;
	private Spinner spImageColor;
	private Spinner spImageType;
	private EditText etSiteFilter;
	private Button btnSave;
	private String query;
	public static final String SEARCH_CRITERIA = "searchCriteria";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_criteria);
		initializeView();
		initializeListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_criteria, menu);
		return true;
	}

	private void initializeView() {
		query = getIntent().getStringExtra(
				GoogleImageSearchActivity.SEARCH_TERM);
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		spImageColor = (Spinner) findViewById(R.id.spImageColor);
		spImageType = (Spinner) findViewById(R.id.spImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		btnSave = (Button) findViewById(R.id.btnSave);
	}

	private void initializeListeners() {
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchCriteria searchCriteria = createSearchCriteria(query, etSiteFilter.getText().toString(),
						spImageColor.getSelectedItem().toString(), spImageSize
								.getSelectedItem().toString(), spImageType
								.getSelectedItem().toString());
				Intent i = new Intent(SearchCriteriaActivity.this, GoogleImageSearchActivity.class);
				i.putExtra(SEARCH_CRITERIA, searchCriteria);
				startActivity(i);
			}
		});
	}

	private SearchCriteria createSearchCriteria(String query, String site,
			String color, String size, String type) {

		return new SearchCriteria(isEmptyNullAll(query) ? null : query,
				isEmptyNullAll(site) ? null : Uri.encode(site),
				isEmptyNullAll(color) ? null : color,
				isEmptyNullAll(size) ? null : size, isEmptyNullAll(type) ? null
						: type);

	}

	private boolean isEmptyNullAll(String value) {
		return value == null || value.isEmpty() || value.contains("all");
	}

}
