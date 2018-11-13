/* First name: Xueting
 * Surname: Tan
 * student id: 948775
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.*;
public class InitialDictionary {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put("Hello", "A greeting in English;");
		map.put("unimelb", "An abbreviation for the University of Melbourne; Australian NO.1 university;");
		map.put("COMP90015", "the course code of Distributed Systems; A very useful course; A very difficult course;");
		map.put("TCP", "Transmission Control Protocol;");
		map.put("UDP", "User Datagram Protocol;");
		JSONObject json = new JSONObject(map);
		//System.out.println(json.toString());
		FileWriter fw = new FileWriter(new File("Dictionary.json"));
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(json.toString());
		bw.flush();
		bw.close();
		
	}

}
