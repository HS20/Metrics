package Lucene.Metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import recherchetextuelle.util.CustomSimilarity;

public class Searcher {
	static IndexReader reader;
	static IndexSearcher searcher;
	static TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
	static FileReader synonymFileReader;
	CustomAnalyzer analyzer;
	private ClassicSimilarity TfSimilarity = new ClassicSimilarity();

	// private CustomSimilarity noTfSimilarity = new CustomSimilarity();
	public Searcher(String indexDir, String synonymFilePath, String MultiWordTermFilePath) throws IOException, java.text.ParseException {
		FileReader synonymFileReader = new FileReader(new File(synonymFilePath));
		FileReader MultiWordTermReader = new FileReader(new File(MultiWordTermFilePath));
		analyzer = new CustomAnalyzer(synonymFileReader,MultiWordTermReader);
		reader = DirectoryReader.open(FSDirectory.open(new File(indexDir).toPath()));
		searcher = new IndexSearcher(reader);
		searcher.setSimilarity(TfSimilarity);
		// searcher.setSimilarity(noTfSimilarity);
	}



	public void findConcept() throws ParseException, IOException {
		// -----Create all susDecs query-----
		int distance = 2;
		// test

		ArrayList<Integer> acr1DocIds = new ArrayList<>();
		ArrayList<Float> acr1Scores = new ArrayList<>();
		PrintWriter pWriter = new PrintWriter(new FileWriter("./data/ClairancedelaCreatinine.txt", true));

		TermQuery acr1 = new TermQuery(new Term("libelle", "clairance_de_la_créatinine"));
		System.out.println(acr1 );
		searcher.search(acr1, collector);
		ScoreDoc[] acr1hit = collector.topDocs().scoreDocs;
		System.out.println(acr1hit.length);
		for (int i = 0; i < acr1hit.length; ++i) {
			int docId = acr1hit[i].doc;
			acr1DocIds.add(docId);
			acr1Scores.add((float) acr1hit[i].score);
		}
		for (int i = 0; i < acr1DocIds.size(); ++i) {
			int docId = acr1DocIds.get(i);
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("label") + "  libelle : " + d.get("libelle") + "\tscore= "
					+ acr1Scores.get(i));
			System.out.println("Explain  docId: " + searcher.explain(acr1, docId));
			 pWriter.print((i + 1) + ". " +"DocId : "+docId+ " | Concept_CD : "+d.get("label")+" | NAME_CHAR : "+d.get("libelle")+"\r\n");
		}
		 pWriter.close();
	}


	 
	//
	// boolean ordered = false;
	//
	// SpanQuery susdec = new SpanTermQuery(new Term("contents", "sus_dec"));
	// SpanQuery negation = new SpanTermQuery(new Term("contents", "pas"));
	// SpanQuery negationSusdec = new SpanNearQuery(new SpanQuery[] { negation,
	// susdec },
	// distance, ordered);
	// Query susDecTot = new SpanNotQuery(susdec, negationSusdec,distance);
	// System.out.println(susDecTot);
	// //-----created all susDecs query-----
	//
	//
	// //-----Create susDecs and acr query-----
	//
	// Builder susDecAndAcrBQ = new BooleanQuery.Builder();
	// susDecAndAcrBQ.add(susDecTot, Occur.MUST);
	// TermQuery acr = new TermQuery(new Term("contents","acr"));
	// susDecAndAcrBQ.add(acr, Occur.MUST);
	//
	// //-----created susDecs and acr query-----
	//
	// //-----Obtain susDecsAndAcr docIDs-----
	// ArrayList<Integer> susDecAndAcrDocIds = new ArrayList<>();
	// ArrayList<Float> susDecAndAcrScores = new ArrayList<>();
	//
	// searcher.search(susDecAndAcrBQ.build(), collector);
	// System.out.println(susDecAndAcrBQ);
	// ScoreDoc[] susDecAndAcrHits = collector.topDocs().scoreDocs;
	//
	// for(int i=0;i<susDecAndAcrHits.length;++i) {
	// int docId = susDecAndAcrHits[i].doc;
	// susDecAndAcrDocIds.add(docId);
	// susDecAndAcrScores.add((float) susDecAndAcrHits[i].score);
	// }
	//
	// //-----obtained susDecsAndAcr docIDs-----
	//
	// //-----Create susDecsNoAcr query-----
	//
	// Builder susDecNoAcrBQ = new BooleanQuery.Builder();
	// susDecNoAcrBQ.add(susDecTot, Occur.MUST);
	// susDecNoAcrBQ.add(acr, Occur.MUST_NOT);
	//
	// //-----created susDecsNoAcr query-----
	//
	// //-----Obtain susDecsNoAcr docIDs-----
	// ArrayList<Integer> susDecNoAcrDocIds = new ArrayList<>();
	//
	// ArrayList<Float> susDecNoAcrScores = new ArrayList<>();
	//
	// collector = TopScoreDocCollector.create(150);
	// searcher.search(susDecNoAcrBQ.build(), collector);
	//
	// ScoreDoc[] susDecNoAcrHits = collector.topDocs().scoreDocs;
	//
	// for(int i=0;i<susDecNoAcrHits.length;++i) {
	// int docId = susDecNoAcrHits[i].doc;
	//
	// susDecNoAcrDocIds.add(docId);
	// susDecNoAcrScores.add((float) susDecNoAcrHits[i].score);
	//
	// }
	// //-----obtained susDecsNoAcr docIDs-----
	//
	// //-----Display susDecNoAcr -----
	// System.out.println("Textes avec susDecs et pas de ACR:");
	// for(int i=0;i<susDecNoAcrDocIds.size();++i) {
	// int docId = susDecNoAcrDocIds.get(i);
	// Document d = searcher.doc(docId);
	// System.out.println((i + 1) + ". " + d.get("path") + "\tscore= "+
	// susDecNoAcrScores.get(i) );
	// System.out.println("Explain docId:
	// "+searcher.explain(susDecNoAcrBQ.build(),docId));
	// }
	//
	// //-----displayed susDecNoAcr -----
	// //-----Display susDecAndAcr -----
	// System.out.println("Textes avec susDecs et ACR:");
	// for(int i=0;i<susDecAndAcrDocIds.size();++i) {
	// int docId = susDecAndAcrDocIds.get(i);
	// Document d = searcher.doc(docId);
	// System.out.println((i + 1) + ". " + d.get("path") + "\tscore= "+
	// susDecAndAcrScores.get(i));
	// }
	// //-----displayed susDecAndAcr -----
	// }
	
	public void query(String queryString) throws ParseException, IOException {
		collector = TopScoreDocCollector.create(150);

		Query q = new ComplexPhraseQueryParser("libelle", analyzer).parse(queryString);

		System.out.println("query = " + q);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		System.out.println("Trouv� " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
		}
	}

	public void phraseQuery(ArrayList<String> termsList) throws IOException {
		PhraseQuery.Builder builder = new PhraseQuery.Builder();

		termsList.stream().map(x -> new Term("contents", x)).forEach(builder::add);
		System.out.println(builder.build());

		searcher.search(builder.build(), collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
		}
	}

}