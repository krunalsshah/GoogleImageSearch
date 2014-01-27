package com.codepath.googleimagesearch.dto;

public class SearchCriteria {
	
	private String query;
	private String site;//as_sitesearch
	private String color; //imgcolor
	private String size; // imgsz
	private String type;//imgtype
	
	
	public SearchCriteria(String query, String site, String color, String size,
			String type) {
		super();
		this.query = query;
		this.site = site;
		this.color = color;
		this.size = size;
		this.type = type;
	}
	
	public String getQuery() {
		return query;
	}
	public String getSite() {
		return site;
	}
	public String getColor() {
		return color;
	}
	public String getSize() {
		return size;
	}
	public String getType() {
		return type;
	}
	
	

}
