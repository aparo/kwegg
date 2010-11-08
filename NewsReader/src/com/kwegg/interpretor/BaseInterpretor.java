package com.kwegg.interpretor;

import java.io.IOException;
import java.util.List;

import com.kwegg.commons.news.FastFeed;
import com.kwegg.commons.news.FastNews;

public interface BaseInterpretor {
	public List<FastNews> getAllFastNews(FastFeed feed) throws IOException;
}
