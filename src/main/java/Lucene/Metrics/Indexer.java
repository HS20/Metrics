package Lucene.Metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import recherchetextuelle.util.CustomSimilarity;


public class Indexer {


	private  CustomAnalyzer analyzer ;/*= new FrenchAnalyzer(FrenchAnalyzer.getDefaultStopSet());*/
	private  IndexWriter writer;
	private ArrayList<File> queue= new ArrayList<File>();;
	private ClassicSimilarity TfSimilarity = new ClassicSimilarity();
//	private CustomSimilarity noTfSimilarity = new CustomSimilarity();
	public Indexer() {
		// TODO Auto-generated constructor stub
	}
	public Indexer(String indexDir, String synonymFilePath, String MultiWordTermFilePath) throws IOException, ParseException {
		FileReader synonymFileReader = new FileReader(new File(synonymFilePath));
		FileReader MultiWordTermReader = new FileReader(new File(MultiWordTermFilePath));
		analyzer = new CustomAnalyzer(synonymFileReader,MultiWordTermReader);
		FSDirectory dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
//		config.setSimilarity(noTfSimilarity);
		config.setSimilarity(TfSimilarity);
		writer = new IndexWriter(dir, config);
		writer.deleteAll();
}
	public Indexer(String indexDir) throws IOException {
		analyzer = new CustomAnalyzer();
		FSDirectory dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, config);
	}

	public static Document ConceptCdtoDocument(Concept_cd ConceptCd) {

		String Id = Integer.toString(ConceptCd.getID());

		Document doc = new Document();
		
		doc.add(new StringField("Id", Id, Field.Store.YES));
		doc.add(new StringField("label", ConceptCd.getConceptCdlabel(), Field.Store.YES));
//		doc.add(new StringField("libelle", ConceptCd.getConceptCdLib(), Field.Store.YES));
		
		 doc.add(new TextField("libelle",ConceptCd.getConceptCdLib(),Field.Store.YES));
		return doc;
	}
	
	// creation de l'index 
//	Indexer(String indexDir) throws IOException {
//
//		FSDirectory dir = FSDirectory.open(Paths.get(indexDir));
//		IndexWriterConfig config = new IndexWriterConfig(analyzer);
//		writer = new IndexWriter(dir, config);
//	}

	// ajout de document dans l'index
	public  void indexDocument(Document doc) throws IOException {
		writer.addDocument(doc);	
		
	}

	public IndexWriter getWriter() {
		return writer;
	}


	public void deleteAll() throws IOException {
		writer.deleteAll();
	}

	public void closeIndex() throws IOException {
		writer.close();
	}

	private void addFiles(File file) {

	    if (!file.exists()) {
	      System.out.println(file + " n'existe ps.");
	    }
	    if (file.isDirectory()) {
	      for (File f : file.listFiles()) {
	        addFiles(f);
	      }
	    } else {
	      String filename = file.getName().toLowerCase();
	      //===================================================
	      // Only index text files
	      //===================================================
	      if (filename.endsWith(".htm") || filename.endsWith(".html") || 
	              filename.endsWith(".xml") || filename.endsWith(".txt")) {
	        queue.add(file);
	        System.out.println("added to queue: " + file.toString());
	      } else {
	        System.out.println("Ignor� " + filename);
	      }
	    }
	  }

	public void indexFileOrDirectory(String fileName) throws IOException {
	    //===================================================
	    //gets the list of files in a folder (if user has submitted
	    //the name of a folder) or gets a single file name (is user
	    //has submitted only the file name) 
	    //===================================================
		
	    this.addFiles(new File(fileName));
	    
	    for (File f : queue) {
	      FileReader fr = null;
	      try {
		    fr = new FileReader(f);
	    	  	Document doc = fileToDoc(fr, f.getPath(), f.getName());
	        writer.addDocument(doc);
	        
	        System.out.println("Ajout�: " + f);
	      } catch (Exception e) {
	        System.out.println("Impossible d'ajouter: " + f);
	      } finally {
	        fr.close();
	      }
	    }
	    
	    queue.clear();
	  }
	
	public static CharArraySet getStopWords(){
		System.out.println("Enter the path to the stop word file");
		BufferedReader br = new BufferedReader(
	            new InputStreamReader(System.in));
		String path="";
		
		String str; 
		ArrayList<String> wordList = new ArrayList<String>();
		
		try {
			path = br.readLine();
			BufferedReader in = new BufferedReader(new FileReader(path));
			while ((str=in.readLine()) != null) { 
				  wordList.add(str);
				}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new CharArraySet(wordList, true);
	}
	
	private Document fileToDoc(FileReader fr,String filePath, String fileName)  {
		Document doc = new Document();

        //===================================================
        // add contents of file
        //===================================================
        doc.add(new TextField("contents", fr));
        doc.add(new StringField("path", filePath, Field.Store.YES));
        doc.add(new StringField("filename", fileName, Field.Store.YES));
		return doc;
}


	
	
}


