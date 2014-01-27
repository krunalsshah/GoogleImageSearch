package com.codepath.googleimagesearch.adapter;

import java.util.List;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.dto.GoogleSearchResult;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GoogleImageSearchAdapter extends
		ArrayAdapter<GoogleSearchResult> {

	public GoogleImageSearchAdapter(Context context, List<GoogleSearchResult> results) {
		super(context, R.layout.image_item_gv, results);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GoogleSearchResult result =  this.getItem(position);
		SmartImageView sivImage;
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			sivImage = (SmartImageView) inflater.inflate(R.layout.image_item_gv, parent, false);
		}else{
			sivImage = (SmartImageView) convertView;
			sivImage.setImageResource(android.R.color.transparent);
		}
		sivImage.setImageUrl(result.getTbUrl());
		return sivImage;
	}

	
}
