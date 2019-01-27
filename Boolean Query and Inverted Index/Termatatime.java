import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Termatatime extends InvertedIndex {

	public void taatOr(BufferedWriter writer, String[] termValue, ArrayList<List<Integer>> valuelist, String terms) {
		
		for (int m = 0; m < termValue.length; m++) {
			if (myMap.containsKey(termValue[m])) {
				valuelist.add(myMap.get(termValue[m]));
				Collections.sort((List<Integer>) valuelist.get(m));
			}
		}

		try {
			writer.write("TaatOr\n");
			writer.write(terms + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println("valuelist = " + valuelist);
		int pointer1 = 0;
		int pointer2 = 0;
		int count = 0;
		int docNum = 0;
		List list1 = null;
		List list2 = null;
		List<Integer> list3 = new ArrayList<Integer>();

		for (int p = 0; p < termValue.length; p++) {
			list1 = new ArrayList<>();
			list2 = new ArrayList<>();

			list1 = valuelist.get(p);
			list2.addAll(list3);
			list3.removeAll(list3);

			for (int i = 0, j = 0, k = 0; k < list1.size() + list2.size(); k++) {

				if (i < list1.size() && j < list2.size()) {

					pointer1 = (int) list1.get(i);
					pointer2 = (int) list2.get(j);
					if (pointer1 <= pointer2) {
						if (pointer1 == pointer2) {
							j++;
							k++;
						}
						list3.add(pointer1);
						i++;
						count = count + 1;
					} else {
						list3.add(pointer2);
						j++;
						count = count + 1;
					}
				} else {
					if (i < list1.size()) {
						count = count + 1;
						while(i < list1.size())
						{
						pointer1 = (int) list1.get(i);
						list3.add(pointer1);
						i++;
						k++;
						
						}k--;
					} else if(i <= list1.size()){
						while(j < list2.size())
						{
						pointer2 = (int) list2.get(j);
						list3.add(pointer2);
						j++;
						k++;
						}
						k--;
						count = count + 1;
					}
				}

			}
			docNum = list1.size() + list2.size();

			//System.out.println(list3);
			//System.out.println(docNum);
		}

		try {
			writer.write("Results: ");
			if (list3.size() > 0) {
				for (int l = 0; l < list3.size(); l++) {
					writer.write(list3.get(l) + " ");
				}
			} else {
				writer.write("empty");
			}
			writer.newLine();
			writer.write("Number of documents in results: " + list3.size() + "\n");
			writer.write("Number of comparisons: " + count);
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(count);

	}

	public void taatAnd(BufferedWriter writer, String[] termValue, ArrayList<List<Integer>> valuelist, String terms) {
		for (int m = 0; m < termValue.length; m++) {
			if (myMap.containsKey(termValue[m])) {
				valuelist.add(myMap.get(termValue[m]));
			}
			Collections.sort((List<Integer>) valuelist.get(m));
			try {
				writer.write("GetPostings\n");
				writer.write(termValue[m] + "\n");
				writer.write("Postings list: ");
				
					List<Integer> temp = valuelist.get(m);
					for (int q = 0; q < temp.size(); q++) {
						writer.write(temp.get(q) + " ");
					}
				
				writer.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer.write("TaatAnd\n");
			writer.write(terms + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("valuelist = " + valuelist);
		List<Integer> answer_list = new ArrayList<Integer>();
		int pointer1 = 0;
		int pointer2 = 0;
		int comparisionCount = 0;
		int docNum = 0;
		List list1 = null;
		List list2 = null;
		for (int p = 0; p < termValue.length-1; p++) {

			list2 = new ArrayList<>();

			if (p == 0) {
				list1 = new ArrayList<>();
				list1 = valuelist.get(p);
			} else {
				list1.addAll(answer_list);
				answer_list.removeAll(answer_list);
				
			}
			list2 = valuelist.get(p + 1);
			int list1_skiprange = (int) Math.sqrt(list1.size());
			int list2_skiprange = (int) Math.sqrt(list2.size());

			for (int i = 0, j = 0; i < list1.size() && j < list2.size();) {
				pointer1 = (int) list1.get(i);
				pointer2 = (int) list2.get(j);
				if (pointer1 < pointer2) {
					comparisionCount++;
					if(i % list1_skiprange == 0 && i + list1_skiprange < list1.size()
						&& ((int) list1.get(i + list1_skiprange)) < pointer2) {
					i += list1_skiprange;
					comparisionCount++;
				}}

				if (pointer2 < pointer1)
					{
					if(j % list2_skiprange == 0 && j + list2_skiprange < list2.size()
						&& ((int) list2.get(j + list2_skiprange)) < pointer1) {
					j += list2_skiprange;
					comparisionCount++;
				}}

				if (pointer1 == pointer2) {
					answer_list.add(pointer1);
					comparisionCount=comparisionCount+1;
					i++;
					j++;
				} else if (pointer1 < pointer2) {
					i++;
					comparisionCount=comparisionCount+1;
				} else {
					j++;
					comparisionCount=comparisionCount+1;
				}
			}
		}
		docNum = list1.size() + list2.size();

		try {
			writer.write("Results: ");
			if (answer_list.size() > 0) {
				for (int l = 0; l < answer_list.size(); l++) {
					writer.write(answer_list.get(l)+" ");
				}
			} else {
				writer.write("empty");
			}
			writer.newLine();
			writer.write("Number of documents in results: " + answer_list.size() + "\n");
			writer.write("Number of comparisons: " + comparisionCount);
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(docNum);
		//System.out.println(answer_list);
	//	System.out.println(comparisionCount);
	}
}
