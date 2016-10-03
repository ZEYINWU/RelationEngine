package zeyin.cis550.hw5.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zeyin.cis550.hw5.data.Relation;
import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.schema.TypeException;

/**
 * Load the contents of a set of Relations.
 * '
 * @author zives
 *
 */
public class DataLoader {
	Map<String, Relation> relationsByName;
	
	public DataLoader(Map<String, Relation> relations) {
		relationsByName = relations;
	}
	
	List<String> getValues(String content) {
		List<String> list = new ArrayList<String>();
		
		String regex = "\'(([^\']|\'\')*)\',?|([^,]+),?";

	    Matcher m = Pattern.compile(regex).matcher(content);
	    while (m.find()) {
	        if (m.group(1) != null) {
	        	list.add(m.group(1));
	        } else {
	        	list.add(m.group(3).trim());
	        }
	    }
		return list;
	}
	
	/**
	 * Read data values from an SQL dump file written for MySQL
	 * 
	 * @param file
	 * @throws IOException 
	 */
	public void load(BufferedReader file) throws IOException {
		String line = file.readLine();
		
		while (line != null) {
			if (line.startsWith("insert into ")) {
				String remainder = line.substring("insert into ".length());
				
				String table = remainder.substring(0, remainder.indexOf(' '));
				table = table.substring(0, table.indexOf('('));
				String contents = remainder.substring(remainder.indexOf("values (")
						+ "values ".length());
				
				contents = contents.substring(1, contents.length() - 2);
				
				List<String> items = getValues(contents);
				
				if (relationsByName.containsKey(table)) {
					Relation r = relationsByName.get(table);
					
					Tuple next = r.addTuple();
					
					try {
						next.setValue(items);
					} catch (TypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			line = file.readLine();
		}
	}
}
