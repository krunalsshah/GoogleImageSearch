package com.codepath.googleimagesearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.codepath.googleimagesearch.dto.GoogleSearchResult;
import com.loopj.android.image.SmartImageView;

public class FullImageDisplayActivity extends Activity {
	ShareActionProvider provider;
	SmartImageView sivResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image_display);
		GoogleSearchResult result = (GoogleSearchResult) getIntent()
				.getSerializableExtra("result");
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(result.getTitleNoFormatting());
		TextView tvContent = (TextView) findViewById(R.id.tvContent);
		tvContent.setText(result.getContentNoFormatting());
		sivResult = (SmartImageView) findViewById(R.id.ivFullImage);
		sivResult.setImageUrl(result.getUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_image_display, menu);
		provider = (ShareActionProvider) menu.findItem(R.id.menu_item_share).getActionProvider();
		return true;
	}

	public void onShareItem(MenuItem mi) {
		// Get access to bitmap image from view
		Bitmap bitmap = ((BitmapDrawable) sivResult.getDrawable()).getBitmap();
		// Write image to default external storage directory
		Uri bmpUri = null;
		try {
			File file = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
					"share_image.png");
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bmpUri != null) {
			// Construct a ShareIntent with link to image
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			shareIntent.setType("image/*");
			// Launch sharing dialog for image
			startActivity(Intent.createChooser(shareIntent, "Share Content"));
		} else {
			// ...sharing failed, handle error
		}
	}

}
