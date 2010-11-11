package com.kwegg.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class ReaderUtils {
	/**
	 * Normalize a URL, by forcing its host name and protocol to lower case.
	 * 
	 * @param url
	 *            The URL to normalize.
	 * 
	 * @return a new <tt>URL</tt> object representing the normalized URL
	 * 
	 * @see #normalizeURL(String)
	 */
	public static URL normalizeURL(URL url) {
		try {
			String protocol = url.getProtocol().toLowerCase();
			String host = url.getHost().toLowerCase();
			int port = url.getPort();
			String file = url.getFile();
			String ref = url.getRef();

			if ((ref != null) && (ref.length() > 0))
				file = file + "#" + ref;

			url = new URL(protocol, host, port, file);
		}

		catch (MalformedURLException ex) {
			// Shouldn't happen
			ex.printStackTrace();
		}

		return url;
	}
}
