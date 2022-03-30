import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.sun.tools.javac.Main;

public class Client {

	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	private String recievedMessage;
	private Test test;
	
	public Client(Socket socket, String username, Test test) {
		try {
			this.test = test;
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.username = username;
		} catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);
		}
	}

	public void sendMessage(String messageToSend) {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			while(socket.isConnected()) {
				if(messageToSend.equals("quit")) {
					bufferedWriter.write("SERVER : " + username + " has left the chat");
					bufferedWriter.newLine();
					bufferedWriter.flush();
					closeEverything(socket,bufferedReader,bufferedWriter);
					Runtime.getRuntime().halt(-1);
				} else {
					bufferedWriter.write("["+username+"] : " + messageToSend);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
			}
		} catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);
		}
	}
	
	public void listenForMessage() {
		//Test theInterface = Test.getInstance();
		new Thread(new Runnable() {

			@Override
			public void run() {				
				while(socket.isConnected()) {
					try {
						recievedMessage = bufferedReader.readLine();
						test.setMessage(recievedMessage);
						System.out.println(recievedMessage);
					} catch (IOException e) {
						closeEverything(socket,bufferedReader,bufferedWriter);
					}
				}
				
			}
			
		}).start();
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			
			if(socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your username for the group chat :");
		String username = scanner.nextLine();
		Socket socket = new Socket("localhost",1234);
	//	Client client = new Client(socket, username);
	}

	public String getRecievedMessage() {
		return recievedMessage;
	}

	public void setRecievedMessage(String recievedMessage) {
		this.recievedMessage = recievedMessage;
	}
	
	
}

