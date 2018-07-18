package Lucene.Metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TextFileIndexer {

	// private static EnglishAnalyzer analyzer = new
	// EnglishAnalyzer(Version.LUCENE_40, EnglishAnalyzer.getDefaultStopSet());
	private static EnglishAnalyzer analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
	private static IndexWriter writer;
	private ArrayList<File> queue = new ArrayList<File>();

	public static void main(String[] args) throws IOException, ParseException {

		String indexLocation = null;
		TextFileIndexer indexer = null;
		
		// ******* "Enter the path where the index will be created: (e.g. /tmp/index or c:\temp\index)"); ****** //

		
		
//			indexLocation = "C:/Users/sentenh/Desktop/pacha/Index";
//			indexer = new TextFileIndexer(indexLocation);
//
////						"Enter the full path to add into the index (q=quit): (e.g. /home/ron/mydir or c:\\Users\\ron\\mydir)");
////				System.out.println("[Acceptable file types: .xml, .html, .html, .txt]");
//
//		
//				String PathIndex = "C:/Users/sentenh/Desktop/pacha/Document/FA.txt";
//				// try to add file into the index
//				indexer.indexFileOrDirectory(PathIndex);

		indexLocation = "C:/Users/sentenh/Desktop/Metrics/test";
		indexer = new TextFileIndexer(indexLocation);

		String PathIndex = "C:/Users/sentenh/Desktop/Corpus/0.txt";
		String PathIndex1 = "C:/Users/sentenh/Desktop/Corpus/1.txt";
		String PathIndex2 = "C:/Users/sentenh/Desktop/Corpus/2.txt";
	
		indexer.indexFileOrDirectory(PathIndex);
		indexer.indexFileOrDirectory(PathIndex1);
		indexer.indexFileOrDirectory(PathIndex2);
   	
				
		
			IndexReader r = DirectoryReader.open( FSDirectory.open(new File("C:/Users/sentenh/Desktop/Metrics/test").toPath()));
			IndexSearcher searcher1 = new IndexSearcher(r);
		   
		   Searcher r1 = new Searcher("C:/Users/sentenh\\Desktop\\Corpus", "C:/Users/sentenh/Desktop/Metrics/file/FA2.txt");
		
		
		
		
		
		
		
		
		
		
		
		// ===================================================
		// after adding, we always have to call the
		// closeIndex, otherwise the index is not created
		// ===================================================

//			indexer.closeIndex();
	}

	TextFileIndexer(String indexDir) throws IOException {
		// the boolean true parameter means to create a new index everytime,
		// potentially overwriting any existing files there.
		
		//FSDirectory dir = FSDirectory.open(new File(indexDir));
		FSDirectory dir = FSDirectory.open(Paths.get(indexDir));
		//IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, config);
	}

	/**
	 * Indexes a file or directory
	 * 
	 * @param fileName
	 *            the name of a text file or a folder we wish to add to the index
	 * @throws java.io.IOException
	 *             when exception
	 */
	
	
	public void indexFileOrDirectory(String fileName) throws IOException {
		// ===================================================
		// gets the list of files in a folder (if user has submitted
		// the name of a folder) or gets a single file name (is user
		// has submitted only the file name)
		// ===================================================
		addFiles(new File(fileName));

		int originalNumDocs = writer.numDocs();
		for (File f : queue) {
			FileReader fr = null;
			try {
				Document doc = new Document();

				// ===================================================
				// add contents of file
				// ===================================================
				fr = new FileReader(f);
				doc.add(new TextField("contents", fr));
				doc.add(new StringField("path", f.getPath(), Field.Store.YES));
				doc.add(new StringField("filename", f.getName(), Field.Store.YES));

				writer.addDocument(doc);
				System.out.println("Added: " + f);
			} catch (Exception e) {
				System.out.println("Could not add: " + f);
			} finally {
				fr.close();
			}
		}

		int newNumDocs = writer.numDocs();
		System.out.println("");
		System.out.println("************************");
		System.out.println((newNumDocs - originalNumDocs) + " documents added.");
		System.out.println("************************");

		queue.clear();
	}

	private void addFiles(File file) {

		if (!file.exists()) {
			System.out.println(file + " does not exist.");
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				addFiles(f);
			}
		} else {
			String filename = file.getName().toLowerCase();
			// ===================================================
			// Only index text files
			// ===================================================
			if (filename.endsWith(".htm") || filename.endsWith(".html") || filename.endsWith(".xml")
					|| filename.endsWith(".txt")) {
				queue.add(file);
			} else {
				System.out.println("Skipped " + filename);
			}
		}
	}

	/**
	 * Close the index.
	 * 
	 * @throws java.io.IOException
	 *             when exception closing
	 */
	public void closeIndex() throws IOException {
		writer.close();
	}

}
