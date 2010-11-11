package com.kwegg.interpretor;

import java.io.IOException;
import java.util.List;

import com.kwegg.commons.news.CloudNews;
import com.kwegg.commons.news.FastFeed;

public interface BaseInterpretor {
	public List<CloudNews> getAllCloudNews(FastFeed feed) throws IOException;
}
