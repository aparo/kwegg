package com.kwegg.commons.news.service;

import java.io.IOException;
import java.net.URL;

import com.kwegg.common.utils.CircularLinkedList;
import com.kwegg.commons.news.FastFeed;
import com.kwegg.models.FeedTableHandler;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * ReaderService is
 * - Thread Safe, Application Restart Safe, Will handle all feeds and start thread/s for dumping news to db
 * @author parag
 *
 */
public class NewsReaderService {
	private static int THREAD_SLEEP_TIMEOUT = 1000;
	public volatile boolean isStop = true;
	private NewsDumperThread newsDumperThread = new NewsDumperThread();
	private static NewsReaderService instance;
	
	private CircularLinkedList<FastFeed> fdQueue = new CircularLinkedList<FastFeed>();
	
	private NewsReaderService() {
		newsDumperThread = new NewsDumperThread();
		grabAllFeeds();
	}
	
	public static NewsReaderService getInstance() {
		if(null==instance) {
			instance = new NewsReaderService();
		}
		return instance;
	}
	
	public void onStop() {
		isStop = true;
		newsDumperThread.doStop();
	}
	
	public void onStart() {
		isStop = false;
		newsDumperThread.doStart();
	}
	
	public void pauseNewsDumping() {
		newsDumperThread.pause();
	}
	
	public void resumeNewsDumping() {
		newsDumperThread.unPause();
	}
	
	/**
	 * updates all feeds from db and thus refresh the queue maintaining only db entries
	 */
	private void grabAllFeeds() {
		try {
			fdQueue = FeedTableHandler.getInstance().getAllFastFeeds();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized int addFeed(String url) throws IOException {
		FastFeed feed = new FastFeed(new URL(url));
		int id = FeedTableHandler.getInstance().getFeedId(url);
		if(id==0)
		{
			SyndFeedInput syndFeedInput = new SyndFeedInput();
			SyndFeed syndFeed = null;
			XmlReader xmlReader = new XmlReader(feed.getURL());
			try {
				syndFeed = syndFeedInput.build(xmlReader);
				feed.setName(syndFeed.getTitle());
				feed.setLastModifiedTime(syndFeed.getPublishedDate().getTime());
				feed.setLastCrawledTime(0l);
				try {
					FeedTableHandler.getInstance().saveFeed(feed);
					id = FeedTableHandler.getInstance().getFeedId(url);
					feed.setId(id);
					feed.dumpNews();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FeedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return id;
	}
	
	private class NewsDumperThread extends Thread{
		private volatile boolean isStop = false;
		private volatile boolean paused = false;
		
		public void run()
        {
            System.out.println("NewsDumperThread run called.");
            while (!Thread.currentThread().isInterrupted())
            {
                try
                {
                    if (isStop)
                    {
                        System.out.println("NewsDumperThread stopped");
                        break;
                    }
                    if (paused)
                    {
                        Thread.sleep(THREAD_SLEEP_TIMEOUT);
                        continue;
                    }
                    fdQueue.getNext().dumpNews();
                    if(!isStop)
                        Thread.sleep(THREAD_SLEEP_TIMEOUT);
                }
                catch (Exception e)
                {
                    System.out.println("Exception in NewsDumperThread-" + e);
                }
            }
        }
		
		public void pause()
        {
            if (!paused)
            {
                paused = true;
                System.out.println("NewsDumperThread paused");
            }
        }

        public void unPause()
        {
            if (paused)
            {
                paused = false;
                System.out.println("NewsDumperThread unpaused");
            }
        }
        
        public void doStop() {
        	if(!isStop) {
        		isStop = true;
        		System.out.println("NewsDumperThread stopped");
        	}
        }
        
        public void doStart() {
        	if(isStop) {
        		isStop = false;
        		System.out.println("NewsDumperThread started");
        	}
        	this.start();
        }
	}

}
