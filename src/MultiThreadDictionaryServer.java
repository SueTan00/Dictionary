/* First name: Xueting
 * Surname: Tan
 * student id: 948775
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JTextArea;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

public class MultiThreadDictionaryServer extends Thread{
	private Socket socket;
	private JTextArea textArea;
	private int i;
	private JSONObject dictionary;
	
	//constructor
	public MultiThreadDictionaryServer(JTextArea textArea ,Socket socket, int i, JSONObject dictionary) {
		this.socket = socket;
		this.textArea = textArea;
		this.i= i;
		this.dictionary = dictionary;
	}
	
	@Override
	public void run() {
		try {
			JSONParser parser = new JSONParser();
			//Get the input/output streams for reading/writing data from/to the socket
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("Connected! You can use the dictionary now.");
			out.flush();
			
			//keep listening for request
			while(true) {
				if(in.available()>0) {
					JSONObject command = (JSONObject) parser.parse(in.readUTF());
					
					if(command.get("command_name").equals("add")) {
						textArea.append("client "+i+" wants to add the word: "+command.get("word_name")+"\n");
						if(!dictionary.containsKey(command.get("word_name"))) {
							AddWord(command,dictionary);
							out.writeUTF("The word has been successfully added!");
						}else {
							out.writeUTF("The word has already existed.");
						}
						
					}else if(command.get("command_name").equals("remove")) {
						textArea.append("client "+i+" wants to remove the word: "+command.get("word_name")+"\n");
						if(dictionary.containsKey(command.get("word_name"))) {
							RemoveWord(command,dictionary);
							out.writeUTF("The word has been successfully removed!");
						}else {
							out.writeUTF("The word doesn't exist in this dictionary.");
						}
						
					}else if(command.get("command_name").equals("lookup")) {
						textArea.append("client "+i+" wants to look up the word: "+command.get("word_name")+"\n");
						if(dictionary.containsKey(command.get("word_name"))) {
							String meaning = command.get("word_name") + " means: "+GetMeaning(command,dictionary);
							out.writeUTF(meaning);
						}else {
							out.writeUTF("The word doesn't exist in this dictionary.");
						}
						
					}
					
				}
			}
			
		}
		catch(SocketException e) {
			textArea.append("SocketException.\n");
		}
		catch (IOException e) {
			textArea.append("IOException.\n");
		} 
		catch (ParseException e) {
			textArea.append("ParseException.\n");
		} 
		finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					textArea.append("Cannot close.\n");
				}
			}
		}
	}
	
	//help methods
	private synchronized static void AddWord(JSONObject command, JSONObject dictionary) {
		dictionary.put(command.get("word_name"), command.get("word_meaning"));
	}
	
	private synchronized static void RemoveWord(JSONObject command, JSONObject dictionary) {
		dictionary.remove(command.get("word_name"));
	}
	
	public static String GetMeaning(JSONObject command, JSONObject dictionary) {
		return (String) dictionary.get(command.get("word_name"));
		
	}
}
			
