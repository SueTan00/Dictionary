/* First name: Xueting
 * Surname: Tan
 * student id: 948775
 */

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class ClientWindow {

	private JFrame frmClient;
	private JTextField textField;
	JSONParser parser = new JSONParser();
	Socket socket = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextArea textArea;
	
	/**
	 * Launch the application Window.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow(args);
					window.frmClient.setVisible(true);
				} catch (Exception e) {
					System.out.println("Cannot initialize. Enter right parameters and try gain.");
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Create the application. The constructor is called by main() method.
	 */
	public ClientWindow(String[] args) {
		initialize(args);
	}

	/**
	 * Initialize the frame. The method is called by constructor.
	 */
	private void initialize(String[] args) {
		frmClient = new JFrame();
		frmClient.setTitle("Client");
		frmClient.setBounds(100, 100, 450, 300);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClient.getContentPane().setLayout(null);
		
		JLabel lblChooseYourOperation = new JLabel("LookUp/Remove: ");
		lblChooseYourOperation.setBounds(23, 85, 112, 16);
		frmClient.getContentPane().add(lblChooseYourOperation);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		
		comboBox.addItem("lookup");
		comboBox.addItem("remove");
		comboBox.setBounds(134, 81, 104, 27);
		frmClient.getContentPane().add(comboBox);
		
		JLabel lblThisIsA = new JLabel("Hi ! This is a simple dictionary service.");
		lblThisIsA.setBounds(23, 16, 250, 27);
		frmClient.getContentPane().add(lblThisIsA);
		
		JLabel lblConnectToUs = new JLabel("Click 'Connect' button !");
		lblConnectToUs.setBounds(23, 41, 230, 27);
		frmClient.getContentPane().add(lblConnectToUs);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(socket==null) {
						socket = new Socket(args[0], Integer.parseInt(args[1]));
						in = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						textArea.append(in.readUTF()+"\n");
				}
					else {
						textArea.append("Already connected.\n");
					}
				} catch (UnknownHostException e1) {
					textArea.append("Unknown Host.\n");
				} catch (IOException e2) {
					textArea.append("Cannot connect to server.\n");
				} catch (NumberFormatException e3) {
					textArea.append("Port has to be numbers. Try again.\n");	
				} catch(Exception e4) {
					textArea.append("Fail to connect.\n");
					if(args.length!=2) {
						textArea.append("Error in the number of parameters, try again.\n");
					}
				}
			}
		});
		btnConnect.setBounds(312, 16, 117, 42);
		frmClient.getContentPane().add(btnConnect);
		
		textField = new JTextField();
		textField.setBounds(279, 80, 87, 26);
		frmClient.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblWord = new JLabel("Word:");
		lblWord.setBounds(243, 85, 41, 16);
		frmClient.getContentPane().add(lblWord);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 113, 369, 88);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frmClient.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
		JButton btnConfirm = new JButton("Confirm");
		
		btnConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String operation = (String) comboBox.getSelectedItem();
				String word = textField.getText();
				if(socket==null) {
					textArea.append("Please connect first !\n");
				}else {
					if(word.equals("")) {
						textArea.append("Please enter a word !\n");
					}else {
						JSONObject command = new JSONObject();
						command.put("command_name", operation);
						command.put("word_name", word);
						textArea.append(LookUpOrRemove(command)+"\n");
						
					}
				}
			}
		});
		btnConfirm.setBounds(369, 80, 75, 29);
		frmClient.getContentPane().add(btnConfirm);
		
		JLabel lblAddANew = new JLabel("Add a new word:");
		lblAddANew.setBounds(23, 213, 117, 16);
		frmClient.getContentPane().add(lblAddANew);
		
		textField_1 = new JTextField();
		textField_1.setBounds(236, 208, 130, 26);
		frmClient.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(236, 233, 130, 26);
		frmClient.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblWord_1 = new JLabel("Word:");
		lblWord_1.setBounds(167, 213, 61, 16);
		frmClient.getContentPane().add(lblWord_1);
		
		JLabel lblMeaning = new JLabel("Meaning:");
		lblMeaning.setBounds(167, 238, 61, 16);
		frmClient.getContentPane().add(lblMeaning);
		
		JButton btnNewButton = new JButton("ADD");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(socket==null) {
					textArea.append("Please connect first !\n");
				}
				else {
					if(textField_1.getText().equals("") || textField_2.getText().equals("")) {
						textArea.append("Please enter both the Word and the Meaning!\n");
					}else {
						JSONObject command = new JSONObject();
						command.put("command_name", "add");
						command.put("word_name", textField_1.getText());
						command.put("word_meaning",textField_2.getText());
						try {
							out.writeUTF(command.toJSONString());
							out.flush();
							textArea.append(in.readUTF()+"\n");
						} catch (IOException e1) {
					
							textArea.append("Service offline!\n");
						}
					}
				}
				
			}
		});
		btnNewButton.setBounds(23, 233, 99, 29);
		frmClient.getContentPane().add(btnNewButton);
		
		
		
	}
	
	private String LookUpOrRemove(JSONObject command) {
		String meaning = "";
		try {
			out.writeUTF(command.toJSONString());
			out.flush();
			meaning = in.readUTF();
		} catch (IOException e1) {
	
			textArea.append("Service offline!\n");
		}
		return meaning; 
		
	}
}
