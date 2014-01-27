package com.codepath.googleimagesearch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.codepath.googleimagesearch.dto.GoogleSearchResult;
import com.loopj.android.image.SmartImageView;

public class FullImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image_display);
		GoogleSearchResult result =  (GoogleSearchResult) getIntent().getSerializableExtra("result");
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(result.getTitleNoFormatting());
		TextView tvContent = (TextView) findViewById(R.id.tvContent);
		tvContent.setText(result.getContentNoFormatting());
		SmartImageView sivResult =  (SmartImageView) findViewById(R.id.ivFullImage);
		sivResult.setImageUrl(result.getUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_image_display, menu);
		return true;
	}

}
