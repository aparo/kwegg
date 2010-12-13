package com.kwegg.experio.service;

import java.io.IOException;
import java.net.URL;

import com.kwegg.common.utils.CircularLinkedList;
import com.kwegg.commons.news.FastFeed;
import com.kwegg.models.FeedTableHandler;

/**
 * ExperioPersistingService is
 * - Thread Safe, Application Restart Safe, Will handle all feeds and start thread/s for dumping experio to db
 * handles adding of new feeds
 * @author parag
 *
 */
public class ExperioPersistingService {
	private static int THREAD_SLEEP_TIMEOUT = 1000;
	public volatile boolean isStop = true;
	private ExperioDumperThread experioDumperThread;
	private static ExperioPersistingService instance;
	
	private CircularLinkedList<FastFeed> fdQueue = new CircularLinkedList<FastFeed>();
	
	private ExperioPersistingService() {
		experioDumperThread = new ExperioDumperThread();
	}
	
	public static ExperioPersistingService getInstance() {
		if(null==instance) {
			instance = new ExperioPersistingService();
		}
		return instance;
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
	
	/*
	 * updates feeds while taking care thread operation does not break
	 */
	public synchronized int addFeed(String url) throws IOException {
		FastFeed feed = new FastFeed(new URL(url));
		int id = FeedTableHandler.getInstance().getFeedId(url);
		if(id==0)
		{
			
		}
		
		return id;
	}
	
	
	private class ExperioDumperThread extends Thread{
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
                    System.out.println("Exception in NewsDumperThread- " + e);
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
