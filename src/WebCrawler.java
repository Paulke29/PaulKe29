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
	private final WorkQueue queue;

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
		queue.shutdown();
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
				if (HTML == null) {
					return;
				}

				InvertedIndex local = new InvertedIndex();
				Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
				int start = 1;
				for (String s : TextParser.parse(HtmlCleaner.stripHtml(HTML))) {
					local.add(stemmer.stem(s).toString(), this.singleURL.toString(), start);
					start++;
				}
				threadSafe.addAll(local);
				synchronized (links) {
					links.add(singleURL);
					if (links.size() < limit) {
						ArrayList<URL> Alllinks = HtmlCleaner.listLinks(this.singleURL,
								HtmlFetcher.fetchHTML(this.singleURL));
						for (URL link : Alllinks) {
							System.out.println("link:"+link);
							if (links.size() <= limit && links.contains(link) == false) {
								links.add(link);
								queue.execute((new WebCrawlerTask(link, limit)));
							}
						}
					}
				}

			} catch (IOException e) {
			}
		}
	}
}