import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * @author paulke
 *
 */
public class WebCrawler {

	/**
	 * 
	 */
	private final Collection<URL> links;

	/**
	 * 
	 */
	private final threadSafeIndex threadSafe;

	/**
	 * 
	 */
	int threads;

	/**
	 * 
	 */
	WorkQueue queue;

	/**
	 * @param threadSafe
	 * @param threads
	 * @param queue
	 */
	public WebCrawler(threadSafeIndex threadSafe, int threads) {

		this.queue = new WorkQueue(threads);
		this.threads = threads;
		this.links = new HashSet<URL>();
		this.threadSafe = threadSafe;
	}

	/**
	 * @param seed
	 * @param limit
	 */
	public void craw(URL seed, int limit) {

		links.add(seed);
		queue.execute(new WebCrawlerTask(seed, limit));
		queue.finish();
//		queue.shutdown();
	}

	/**
	 * @author PaulKe
	 *
	 */
	private class WebCrawlerTask implements Runnable {

		/**
		 * 
		 */
		private final URL singleURL;

		/**
		 * 
		 */
		private final int limit;

		/**
		 * @param url
		 * @param limit
		 */
		public WebCrawlerTask(URL url, int limit) {

			this.singleURL = url;
			this.limit = limit;
		}
		@Override
		public void run() {
			try {

				var HTML = HtmlFetcher.fetchHTML(this.singleURL, 3);
				if(HTML == null) {
					return;
				}

				InvertedIndex local = new InvertedIndex();
				Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
				int start = 1;
				for(String s: TextParser.parse(HtmlCleaner.stripHtml(HTML))) {
					local.add(stemmer.stem(s).toString(), this.singleURL.toString(), start);
					start++;
				}
				threadSafe.addAll(local);

//			System.out.println("threadSAFER");
			synchronized (links) {
				links.add(singleURL);
//				System.out.println("threadSAFER2345");
//				if (links.size() < limit) {
					ArrayList<URL> Alllinks = HtmlCleaner.listLinks(this.singleURL,
							HtmlFetcher.fetchHTML(this.singleURL));
//					System.out.println("threadSAFER2345789");
					for(URL link : Alllinks) {
						if(links.size()>= limit) {
							break;
						}
						else if(links.contains(link) == false) {
							links.add(link); 
//							System.out.println("threadSAFER1223");
							queue.execute((new WebCrawlerTask(link,limit)));
						}
					}
//				}
			}

			} catch (IOException e) {
			}
		}
	}
}