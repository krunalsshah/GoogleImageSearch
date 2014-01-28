package com.codepath.googleimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.googleimagesearch.adapter.GoogleImageSearchAdapter;
import com.codepath.googleimagesearch.dto.GoogleSearchResult;
import com.codepath.googleimagesearch.dto.SearchCriteria;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GoogleImageSearchActivity extends Activity {

	EditText etSearchTerm;
	Button btnGo;
	GridView gvResult;
	private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images";
	private static final String AMP = "&", EQ = "=", Q = "?", QUERY = "q",
			TYPE = "imgtype", SITE = "as_sitesearch", COLOR = "imgcolor",
			SIZE = "imgsz", RES_SIZE = "rsz", START = "start", VER = "v",
			RESPONSE_DATA = "responseData", RESULTS = "results";
	public static final String SEARCH_TERM = "searchTerm";
	private SearchCriteria serachCriteria;
	private TextView tvFilterSizeL, tvFilterColorL, tvFilterTypeL,
			tvFilterSiteL;

	ArrayList<GoogleSearchResult> imageResults = new ArrayList<GoogleSearchResult>();
	GoogleImageSearchAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_image_search);
		if (getIntent().getSerializableExtra(
				SearchCriteriaActivity.SEARCH_CRITERIA) != null) {
			serachCriteria = (SearchCriteria) getIntent().getSerializableExtra(
					SearchCriteriaActivity.SEARCH_CRITERIA);
		} else {
			serachCriteria = new SearchCriteria(null, null, null, null, null);
		}
		initializeViews();
		if (!isNullEmpty(serachCriteria.getQuery())) {
			etSearchTerm.setText(serachCriteria.getQuery());
		}
		setAdapters();
		setListeners();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_image_search, menu);
		return true;
	}

	private void initializeViews() {
		etSearchTerm = (EditText) findViewById(R.id.etSearchTerm);
		btnGo = (Button) findViewById(R.id.btnGo);
		gvResult = (GridView) findViewById(R.id.gvResult);
		tvFilterSizeL = (TextView) findViewById(R.id.tvFilterSizeL);
		tvFilterColorL = (TextView) findViewById(R.id.tvFilterColorL);
		tvFilterTypeL = (TextView) findViewById(R.id.tvFilterTypeL);
		tvFilterSiteL = (TextView) findViewById(R.id.tvFilterSiteL);
	}

	private void setListeners() {
		gvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent,
					int position, long rowId) {
				Intent i = new Intent(getApplicationContext(),
						FullImageDisplayActivity.class);
				i.putExtra("result", imageResults.get(position));
				startActivity(i);
			}

		});
	}

	private void setAdapters() {
		imageAdapter = new GoogleImageSearchAdapter(this, imageResults);
		gvResult.setAdapter(imageAdapter);
		gvResult.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				 customLoadMoreDataFromApi(page); 				
			}
		});
	}

	public void showFilterCriteria(MenuItem mi) {
		Intent i = new Intent(GoogleImageSearchActivity.this,
				SearchCriteriaActivity.class);
		i.putExtra(SEARCH_TERM, etSearchTerm.getText().toString());
		startActivity(i);
	}

	public void queryForImage(View v) {
		if (isNullEmpty(etSearchTerm.getText().toString())) {
			Toast.makeText(getApplicationContext(),
					"Please enter the Image Search Key Word", Toast.LENGTH_LONG)
					.show();
			return;
		}
		changeSelectedFilterVisibility();
		serachCriteria.setQuery(etSearchTerm.getText().toString());
		makeRestCall(0);
	}
	
	public void makeRestCall(int offset){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(getQueryUrl(serachCriteria, offset), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					JSONArray joArray = response.getJSONObject(RESPONSE_DATA)
							.getJSONArray(RESULTS);
					imageResults.clear();
					imageAdapter.addAll(GoogleSearchResult
							.fromJSONArray(joArray));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}

	private void changeSelectedFilterVisibility() {
		tvFilterSizeL.setVisibility(View.VISIBLE);
		tvFilterColorL.setVisibility(View.VISIBLE);
		tvFilterTypeL.setVisibility(View.VISIBLE);
		tvFilterSiteL.setVisibility(View.VISIBLE);

		if (isNullEmpty(serachCriteria.getSize())) {
			tvFilterSizeL.setText("Size Filter : all");
		} else {
			tvFilterSizeL.setText("Size Filter : " + serachCriteria.getSize());
		}
		if (isNullEmpty(serachCriteria.getColor())) {
			tvFilterColorL.setText("Color Filter : all");
		} else {
			tvFilterColorL.setText("Color Filter : "
					+ serachCriteria.getColor());
		}
		if (isNullEmpty(serachCriteria.getType())) {
			tvFilterTypeL.setText("Type Filter : all");
		} else {
			tvFilterTypeL.setText("Type Filter : " + serachCriteria.getType());
		}
		if (isNullEmpty(serachCriteria.getSite())) {
			tvFilterSiteL.setText("Site Filter : all");
		} else {
			tvFilterSiteL.setText("Site Filter : " + serachCriteria.getSite());
		}		
	}

	private String getQueryUrl(SearchCriteria searchCriteria, int offset) {
		StringBuffer sb = new StringBuffer();
		if (searchCriteria == null || isNullEmpty(searchCriteria.getQuery())) {
			return null;
		}
		sb.append(BASE_URL + Q + RES_SIZE + EQ + 8);
		sb.append(AMP + START + EQ + offset);
		sb.append(AMP + VER + EQ + 1.0);
		sb.append(AMP + QUERY + EQ + Uri.encode(searchCriteria.getQuery()));
		if (!isNullEmpty(searchCriteria.getColor())) {
			sb.append(AMP + COLOR + EQ + searchCriteria.getColor());
		}
		if (!isNullEmpty(searchCriteria.getSite())) {
			sb.append(AMP + SITE + EQ + Uri.encode(searchCriteria.getSite()));
		}
		if (!isNullEmpty(searchCriteria.getSize())) {
			sb.append(AMP + SIZE + EQ + searchCriteria.getSize());
		}
		if (!isNullEmpty(searchCriteria.getType())) {
			sb.append(AMP + TYPE + EQ + searchCriteria.getType());
		}
		Log.d("DEBUG", sb.toString());
		return sb.toString();

	}

	private boolean isNullEmpty(String value) {
		return (value == null || value.isEmpty());
	}
	
	public void customLoadMoreDataFromApi(int offset) {
		makeRestCall(offset);
	}

}
