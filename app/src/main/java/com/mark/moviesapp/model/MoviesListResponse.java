package com.mark.moviesapp.model;

import com.google.gson.annotations.SerializedName;
import com.mark.moviesapp.db.entity.MovieEntity;

import java.util.ArrayList;
import java.util.List;

public class MoviesListResponse {

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<MovieEntity> results;

	@SerializedName("total_results")
	private int totalResults;

	public MoviesListResponse() {
		results = new ArrayList<>();
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<MovieEntity> results){
		this.results = results;
	}

	public List<MovieEntity> getResults(){
		return results;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
 	public String toString(){
		return 
			"TheMovieDBResponseDao{" +
			"page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + results + '\'' + 
			",total_results = '" + totalResults + '\'' + 
			"}";
		}
}