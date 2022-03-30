import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {

	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.username = username;
		} catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);
		}
	}

	public void sendMessage() {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			Scanner scanner = new Scanner(System.in);
			
			while(socket.isConnected()) {
				String messageToSend = scanner.nextLine();
				if(messageToSend.equals("quit")) {
					bufferedWriter.write(username+":quit");
					bufferedWriter.newLine();
					bufferedWriter.flush();
					System.exit(1);
				} else {
					bufferedWriter.write("["+username+"]>" + messageToSend);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
			}
		} catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);
		}
	}
	
	public void listenForMessage() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String messageFromGroupChat;
				
				while(socket.isConnected()) {
					try {
						messageFromGroupChat = bufferedReader.readLine();
						if(messageFromGroupChat.split(":")[0].equals("SERVER")) {
							System.out.println(messageFromGroupChat.split(":")[1] + " joined the chat");
						} else
						
						if(messageFromGroupChat.split(":")[1].equals("quit")) {
							System.out.println(messageFromGroupChat.split(":")[0] + " has left the chat");
						} else {
							String[] messageSplit = messageFromGroupChat.split(">")[1].split(":");
							String[] destinataires = messageSplit[0].split(";");
							
							if(destinataires.length == 1 && destinataires[0].equals("all")) {
								System.out.println(messageFromGroupChat.split(">")[0] + " " +messageSplit[1]);
							} else {
								for(int i=0;i<destinataires.length;i++) {
									if(destinataires[i].equals(username)) {
										System.out.println(messageFromGroupChat.split(">")[0]+messageSplit[1]);
									}
								}
							}
							messageSplit = null;
							destinataires = null;
						} 	
						
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
		Client client = new Client(socket, username);
		client.listenForMessage();
		client.sendMessage();
	}
}

