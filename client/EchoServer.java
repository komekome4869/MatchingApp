import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		int port = 50;
		try {
		    ServerSocket server = new ServerSocket(port);
		    Socket sock = null;
		    System.out.println("サーバが起動しました");
		    while(true) {
			try {
			    sock = server.accept();

			    System.out.println("クライアントと接続しました");
			    ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			    ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			    Object s;
			    try{
			    	while((s = in.readObject()) != null) {
			    		out.writeObject("テストメッセージ");
			    		out.flush();
			    	System.out.println(s);
			    	}
			    	sock.close();
			    	System.out.println("クライアントと切断しました。");
			    }catch(ClassNotFoundException e) {
			    	System.err.print("オブジェクト受信時にエラーが発生しました：" + e);
			    }
			} catch (IOException e) {
			    System.err.println(e);
			}
		    }
		}catch (IOException e) {
		    System.err.println(e);
		}
	}

}
