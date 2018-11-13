/* First name: Xueting
 * Surname: Tan
 * student id: 948775
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Dictionary {
	private String dictionaryFile;
	public Dictionary() {}
	
	//constructor
	public Dictionary(String dictionaryFile) {
		this.dictionaryFile = dictionaryFile;
	}
	
	//read dictionary file 
	public JSONObject getDictionary() {
		JSONParser parser = new JSONParser();
		try {
			Reader in = new FileReader(dictionaryFile);
			JSONObject dictionary = (JSONObject) parser.parse(in);
			in.close();
			return dictionary;
		}catch(Exception e){
			System.out.println("Cannot load file. Enter right parameters, please.");
			System.exit(0);
			return null;
		}
		
	}
	
	//rewrite dictionary file
	public void updateDictionary(JSONObject dictionary){
		try {
			FileWriter file = new FileWriter(dictionaryFile);
			file.write(dictionary.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}
}
