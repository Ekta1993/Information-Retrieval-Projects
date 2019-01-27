import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Docatatime extends InvertedIndex {
	
	public void daatOr(BufferedWriter writer, String[] termValue, ArrayList<List<Integer>> valuelist, String terms) {
		  
		  for (int m = 0; m <termValue.length; m++) {
				if(myMap.containsKey(termValue[m])) {
					valuelist.add(myMap.get(termValue[m]));
					Collections.sort((List<Integer>) valuelist.get(m));
					}
			}
		  try {
				writer.write("DaatOr\n");
				writer.write(terms+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		//System.out.println("valuelist = " + valuelist);
		int[] index_array = new int[termValue.length];
		List<Integer> answer = new ArrayList<Integer>();
		int count = 0;

		while (1 == 1) {
			int min_index = Integer.MAX_VALUE;
			boolean found_flag = false;
			int min_value = Integer.MAX_VALUE;

			for (int i = 0; i < termValue.length; i++) {
				if (valuelist.get(i).size() > index_array[i]) {
					if (valuelist.get(i).get(index_array[i]) < min_value) {
						min_value = valuelist.get(i).get(index_array[i]);
						min_index = i;
						found_flag = true;
					}
					count++;
				}
			}
			if (found_flag == false)
				break;
			else {
				if (answer.size() == 0 || answer.get(answer.size() - 1) != min_value)
					answer.add(min_value);
				index_array[min_index]++;
				found_flag = false;

			}

		}
		try {
			writer.write("Results: ");
			if(answer.size()>0) {
			for(int l=0;l<answer.size();l++) {
			writer.write(answer.get(l)+" ");
			}}
			else {
				writer.write("empty");
			}
			writer.newLine();
			writer.write("Number of documents in results: "+answer.size()+"\n");
			writer.write("Number of comparisons: "+count);
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(answer);
		//System.out.println(count);

	}
	
	public void daatAnd(BufferedWriter writer, String[] termValue, ArrayList<List<Integer>> valuelist, String terms) {
		  

		for (int m = 0; m < termValue.length; m++) {
			if (myMap.containsKey(termValue[m])) {
				valuelist.add(myMap.get(termValue[m]));
			}
			Collections.sort((List<Integer>) valuelist.get(m));
			
		}
		try {
			writer.write("DaatAnd\n");
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
					comparisionCount=comparisionCount+2;
				} else {
					j++;
					comparisionCount=comparisionCount+2;
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
	//	System.out.println(answer_list);
		//System.out.println(comparisionCount);
	}
}
