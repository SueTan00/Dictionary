/* First name: Xueting
 * Surname: Tan
 * student id: 948775
 */

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.json.simple.JSONObject;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ServerWindow {
	static int i = 0; //counter to keep track of the number of clients
	private JFrame frame;
	private static JTextField textField;
	private static JTextArea textArea;
	private static Dictionary dict;
	private static JSONObject dictionary;
	private static ServerSocket listeningSocket= null;
 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow window = new ServerWindow();
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					System.out.println("Cannot login server.\n");
				}
			}
		});
		if(args.length==2) {
			dict = new Dictionary(args[1]);
			dictionary = dict.getDictionary();
		}else {
			System.out.println("Wrong quantity of parameters. Enter again.");
			System.exit(0);
		}
		Socket clientSocket = null;
		
		try {
			int port = Integer.parseInt(args[0]);
			listeningSocket = new ServerSocket(port);
		} catch(Exception e) {
			System.out.println("Something wrong with port number. Try again\n");
			System.exit(0);
		}
		//Create a socket per connect.
		//Listen for incoming connections forever 
		
		while (true) {
			
			//Accept an incoming client connection request 
			try {
				clientSocket = listeningSocket.accept();
			} catch (IOException e) {
				textArea.append("Fail to connect a client.");
			} //This method will block until a connection request is received
			i++;
			MultiThreadDictionaryServer thread = new MultiThreadDictionaryServer(textArea , clientSocket,i, dictionary);
			thread.start();
			printINFO(clientSocket);
		}
		}
	


	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(dictionary!=null) {
					dict.updateDictionary(dictionary);
				}
				
			}
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					listeningSocket.close();
				} catch (IOException e1) {
					
					textArea.append("cannot close.\n");
				}
			}
		});
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUserActivityLog = new JLabel("User Activity Log:");
		lblUserActivityLog.setBounds(33, 24, 119, 16);
		frame.getContentPane().add(lblUserActivityLog);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(33, 53, 316, 144);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(33,53,316,144);
		frame.getContentPane().add(scrollPane);
		
		JLabel lblTheNumberOf = new JLabel("The number of users online:");
		lblTheNumberOf.setBounds(213, 245, 187, 16);
		frame.getContentPane().add(lblTheNumberOf);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(400, 240, 31, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(Integer.toString(i));;
	}
	
	//a help method
	private static void printINFO(Socket clientSocket) {
		textField.setText(Integer.toString(i));
		textArea.append("Client conection number " + i + " accepted:\n");
		textArea.append("Remote Port: " + clientSocket.getPort()+"\n");
		textArea.append("Remote Hostname: " + clientSocket.getInetAddress().getHostName()+"\n");
		textArea.append("Local Port: " + clientSocket.getLocalPort()+"\n");
	}
}
