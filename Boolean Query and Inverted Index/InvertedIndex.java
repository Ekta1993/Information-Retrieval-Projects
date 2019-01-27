
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class InvertedIndex {

	static HashMap<String, List<Integer>> myMap = new HashMap<String, List<Integer>>();
	static ArrayList<String> postsize = new ArrayList<String>();
	static String path;
	
	public static void main(String args[]) throws IOException {
		
		path = args[0];
		String output = args[1];
		String input = args[2];
		Path indexdir = Paths.get(path);
		Directory directory = FSDirectory.open(indexdir);
		BytesRef ref;
		PostingsEnum postingsEnum = null;
		int postid;
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(output, true));
			
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// DirectoryReader dirReader = DirectoryReader.open(directory);
		IndexReader reader = DirectoryReader.open(directory);
		// System.out.println(reader.maxDoc());
		Fields fields = MultiFields.getFields(reader);
		Iterator<String> iterator = fields.iterator();
		while (iterator.hasNext()) {
			String field = iterator.next();
			
			if (field.equals("id")) {
				continue;
			} else {
				Terms terms = MultiFields.getTerms(reader, field);
				TermsEnum termsEnum = terms.iterator();
				while ((ref = termsEnum.next()) != null) {

					postingsEnum = MultiFields.getTermDocsEnum(reader, field, ref);
					while ((postid = postingsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
						if (myMap.containsKey(ref.utf8ToString())) {
							myMap.get(ref.utf8ToString()).add(postid);
						} else {
							myMap.put(ref.utf8ToString(), new LinkedList());
							myMap.get(ref.utf8ToString()).add(postid);
						}
					}
				}
			}

		}
		Iterator it = myMap.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			postsize.add((String) pair.getKey());

			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
		
		Termatatime taat=new Termatatime();
		
		Docatatime daat=new Docatatime();
		
//		ArrayList<List<Integer>> valuelist = new ArrayList<List<Integer>>();
		String[] termValue = null;
		File fr = new File(input);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fr), "UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String terms = null;
		try {
			terms =br.readLine();
			while(terms!=null) {
			
			termValue = terms.split(" ");
			ArrayList<List<Integer>> valuelist = new ArrayList<List<Integer>>();
			taat.taatAnd(writer,termValue,valuelist,terms);
			taat.taatOr(writer,termValue,valuelist,terms);
			daat.daatAnd(writer,termValue,valuelist,terms);
			daat.daatOr(writer,termValue,valuelist,terms);
			terms = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.close();
		
	}
	
}
