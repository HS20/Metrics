package Lucene.Metrics;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.management.Query;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.util.ElisionFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.ScoreDoc;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;


import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.CharsRef;

import DAO.i2b2Query;



/**
 * Hello world!
 *
 */
public class App 
{
	
	public static String jdbcUrl = "jdbc:oracle:thin:@siad01:1529:INFOPAT";
	public static  String USER = "i2b2demodata";
	public static  String PASS = "i2b2demodata_ucaim";
	private static int i =0;
	private static int l =0;
	private static int k =0;
	public static  Connection connection = null;
	public static Statement stmt = null;
	
	
	static CustomAnalyzer  analyzer = new CustomAnalyzer();
	static Document D = new Document();
	 FileReader synonymFileReader;
	SolrSynonymParser synonymParser = new SolrSynonymParser(true, true, new SynonymsAnalyzer());
    public static void main( String[] args ) throws SQLException, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException
    { 
    	
    	
    	 Set <Concept_cd> ConceptCds = new HashSet <Concept_cd>();
    	 
    	 // requête concept_cd i2b2 puis ajout dans liste d'objet concep_cd
    	 ConceptCds = i2b2Query.QueryConceptCd();

    	 // Creation d'un index 
    	 Indexer PG = new Indexer("C:/Users/sentenh/Desktop/ProjetGayo/indexi2b2", "./data/synonymsfa.txt","./data/tokenAnalyzer.txt");
      
    	 // ajout des concepts_cd dans des documents puis ajout des documents dans un index 
    	 ConceptCds.forEach(c -> {
//    	
    		 D = Indexer.ConceptCdtoDocument(c);
    		 try {
    			 PG.indexDocument(D);
    		 } catch (IOException e) {
    			 // TODO Auto-generated catch block
    			 e.printStackTrace();
    		 }
    	 });
    	 PG.closeIndex();
    	  Searcher search = new Searcher("C:/Users/sentenh/Desktop/ProjetGayo/indexi2b2", "./data/synonymsfa.txt","./data/tokenAnalyzer.txt");
		  search.findConcept();
      
    // Connection	
//try {			
//			System.out.println("Connecting to database...");
//			connection = DriverManager.getConnection(jdbcUrl, USER , PASS);
//			// Do something with the Connection
//		} catch (SQLException ex) {
//			// handle any errors
//			System.out.println("SQLException: " + ex.getMessage());
//			System.out.println("SQLState: " + ex.getSQLState());
//			System.out.println("VendorError: " + ex.getErrorCode());
//			
//		}
//				if (connection != null) {
//          System.out.println("You made it, take control your database now!");
//     } else {
//          System.out.println("Failed to make connection!");
//     }
    	
    	// requête concept_cd
				
//		   stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//			        ResultSet.CONCUR_READ_ONLY);	
//			        String ConceptCD = "select CONCEPT_CD, NAME_CHAR from i2b2demodata.concept_dimension f ";	
//			        ResultSet res = stmt.executeQuery(ConceptCD);
//			        Set <Concept_cd> ConceptCds = new HashSet <Concept_cd> ();
//			        stmt.setMaxRows(50);
			       
//			        while (res.next()) {
//						
//			        	Concept_cd Conceptcd = new Concept_cd();
//			        	Conceptcd.setID(i);
//			        	Conceptcd.setConceptCdlabel(res.getString("Concept_cd"));
//			        	Conceptcd.setConceptCdLib(res.getString("NAME_CHAR"));
////			        	System.out.println(res.getString("NAME_CHAR"));
//			        	ConceptCds.add(Conceptcd);
//			        
////			        	System.out.println(res.getString("NAME_CHAR"));
////			        
//			       PrintWriter pWriter = new PrintWriter(new FileWriter("./data/22.txt", true));
//	    	        pWriter.println("aa");
//		    	        pWriter.close() ;
//		    	        i=i+1;
//			    		}	
	

    	
    	
			        
			        
//				  Indexer PG = new Indexer("C:/Users/sentenh/Desktop/ProjetGayo/indexi2b2", "C:/Users/sentenh/Desktop/ProjetGayo/synonymsfa.txt");
//	
//				  ConceptCds.forEach(c -> {
//			    	// ajout des conceptCd dans des documents
//			    	D = Indexer.ConceptCdtoDocument(c);
//			    	//  System.out.println(D.toString());
//			    	try {
//			    		// ajout des documents conceptCd à l'index 
//			    	PG.indexDocument(D);
//			    	} catch (IOException e) {
//			    		// TODO Auto-generated catch block
//			    		e.printStackTrace();
//			    	}
//			    });
//				  PG.closeIndex();
//				  Searcher search = new Searcher("C:/Users/sentenh/Desktop/ProjetGayo/indexi2b2", "./data/synonymsfa.txt");
//				  search.findConcept();
				    	
				  
				    	   	
   // Creatin index
			        
        
			        
			        
//   Indexer index = new Indexer("C:/Users/sentenh/Desktop/Metrics/IndexConceptCd");
//    Indexer indexfa  = new Indexer("C:/Users/sentenh/Desktop/Metrics/IndexConceptCd","C:/Users/sentenh/Desktop/Metrics/file/FA2.txt");	
//  
//    ConceptCds.forEach(c -> {
//    	// ajout des conceptCd dans des documents
//    	D = Indexer.ConceptCdtoDocument(c);
//    	//  System.out.println(D.toString());
//    	try {
//    		// ajout des documents conceptCd à l'index 
//    		indexfa.indexDocument(D);
//    	} catch (IOException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
//    });
//    indexfa.closeIndex();
   
     
//    index.closeIndex();
//   indexfa.closeIndex();
////   IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("C:/Users/sentenh/Desktop/Metrics/IndexConceptCd").toPath()));
////  System.out.println(reader.toString());
////    
////    	Searcher fa = new Searcher("C:/Users/sentenh/Desktop/Metrics/IndexConceptCd", "C:/Users/sentenh/Desktop/Metrics/file/FA.txt");
////    	fa.query("sus decalage segment st");
//////    CustomAnalyzer c = new CustomAnalyzer();
//////    c.createComponents("C:/Users/sentenh/Desktop/Metrics/file/FA.txt");
////    	
////    	
//    	
//   
//
//
//        
//   IndexReader r = DirectoryReader.open( FSDirectory.open(new File("C:/Users/sentenh/Desktop/Metrics/IndexConceptCd").toPath()));
//   IndexSearcher searcher1 = new IndexSearcher(r);
//   
//   Searcher r1 = new Searcher("C:/Users/sentenh/Desktop/Metrics/IndexConceptCd", "C:/Users/sentenh/Desktop/Metrics/file/FA2.txt");
//  
//  
//   FileReader synonymFileReader = new FileReader("C:/Users/sentenh/Desktop/Metrics/file/FA2.txt");
//   
//	
// 
//   
//		   
////		   
//// r1.findSusDec();
// r1.query("syndrome");
//   
   
 
   
//   Builder booleanQuery = new BooleanQuery.Builder();
//   FuzzyQuery query1 = new FuzzyQuery(new Term("libelle", "Syndrome post-cardiotomie"));
//   booleanQuery.add(query1, Occur.MUST);
//   TopScoreDocCollector collector = TopScoreDocCollector.create(10);
//   ArrayList<Document> returnedDocs = new ArrayList<>();
//   try {
//	   searcher1.search(booleanQuery.build(),collector);
//	   ScoreDoc[] hits = collector.topDocs().scoreDocs;
//
//	   for(int i=0;i<hits.length;++i) {
//		   int docId = hits[i].doc;
//		   Document d = searcher1.doc(docId);
//		   returnedDocs.add(d);
//		   System.out.println((i + 1) + ". " + d.get("libelle") + d.get("Id")+ " score=" + hits[i].score);
//	   }
//
//   } catch (IOException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//   }   
//    
//
//   SolrSynonymParser synonymParser = new SolrSynonymParser(true, true, new SynonymsAnalyzer());
//    	
//   final Tokenizer source = new WhitespaceTokenizer();
//   TokenStream result = new StandardFilter(source);
//   result = new LowerCaseFilter(result);
//   //result = new StopFilter(result, stopwords);
//   @SuppressWarnings("deprecation")
//	CharArraySet phrases = new org.apache.lucene.analysis.util.CharArraySet(Arrays.asList(
//           "sus dec", "sus decalage", "sus ST", "no flow","arret cardio respiratoire"
//           ), false);
//	result = new AutoPhrasingTokenFilter(result, phrases, false);
//;
//	    try {	
//	    		SynonymMap map = synonymParser.build();
//	    	
//			result = new SynonymGraphFilter(result, map, false);
//			System.out.println("used synonym map");
//
//		} catch (IOException e) {
//				// TODO Auto-generated catch block
//			System.out.println("couldn't build synonym map from synonym file");
//			e.printStackTrace();
//		}
//   
   
//  Indexer PG = new Indexer("C:/Users/sentenh/Desktop/ProjetGayo/indexi2b2", "C:/Users/sentenh/Desktop/ProjetGayo/synonymsfa.txt");
//  PG.indexFileOrDirectory("C:/Users/sentenh/Desktop/ProjetGayo/corpusi2b2");
//
//  PG.closeIndex();
//  Searcher search = new Searcher("C:/Users/sentenh/Desktop/ProjetGayo/indexi2b2", "C:/Users/sentenh/Desktop/ProjetGayo/synonymsfa.txt");
//  search.findSusDec();
    	
//  
    	   	
    	
    	
    	
    	
    	
    	
    	
}
}
