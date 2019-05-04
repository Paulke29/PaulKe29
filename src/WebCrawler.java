import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;


/**
 * @author PaulKe
 *
 */
public class WebCrawler {
	/**
	 * 
	 */
	private final threadSafeIndex wordindex;
	/**
	 * @param wordindex
	 */
	WebCrawler(threadSafeIndex wordindex){
		this.wordindex = wordindex;
	}
	/**
	 * Initial mulidIndex method
	 * @param url 
	 * @param limit 
	 * @throws IOException 
	 */
	public void craw(URL url, int limit) throws IOException {
			WorkQueue task = new WorkQueue();
			task.execute(new WebCrawlerTask(url, limit));
			task.finish();
			task.shutdown();
	}

	/**
	 * @author PaulKe
	 *
	 */
	private static class WebCrawlerTask implements Runnable {
		
		/**
		 * 
		 */
		private final URL singleURL;
		/**
		 * 
		 */
		private final int limit;
		/**
		 * 
		 */
		threadSafeIndex index = new threadSafeIndex();
	
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
				InvertedIndex local = new InvertedIndex();
				Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
				int start = 1;
				for(String s: TextParser.parse(HtmlCleaner.stripHtml(HTML))) {
					local.add(stemmer.stem(s).toString(), this.singleURL.toString(), start);
					start++;
				}
				wordindex.addAll(local);
			}catch (IOException e) {
			}
		}
	}

}
