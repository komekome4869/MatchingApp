import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
	static String ipAddress = "localhost";
	static int port = 100;
	static PrintWriter out;
	static Receiver receiver;
	static String str = "";

	public static void main(String args[]) {

		try{
			// サーバに接続
			Socket socket = new Socket(ipAddress, port);

			// データ受信用オブジェクト
			receiver = new Receiver(socket);
			receiver.start();

		}catch(IOException e) {
			System.err.println(e);
		}

	}
}

// データ受信用スレッド
class Receiver extends Thread{
	public ObjectInputStream ois;
	public ObjectOutputStream oos;

	// 内部クラスReceiverのコンストラクタ
	Receiver (Socket socket){
		try{
			oos = new ObjectOutputStream(socket.getOutputStream()); //オブジェクトデータ送信用オブジェクトの用意
			ois = new ObjectInputStream(socket.getInputStream()); //オブジェクトデータ受信用オブジェクトの用意
		} catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
		}
	}

	// Receiverのメソッド
	public void run(){
		try {
			while(true) {
				try {
					Object inputObj = ois.readObject();

					//UserInfo型なら
					if(inputObj instanceof UserInfo) {
						System.out.println("UserInfo型データを受信しました");
					}

					//GroupInfo型なら
					else if(inputObj instanceof GroupInfo) {
						System.out.println("GroupInfo型データを受信しました");
					}

					//その他ならreceiveMessage()
					else {
						System.out.println("メッセージをデータを受信しました : " + inputObj.toString());
					}

				} catch (ClassNotFoundException e) {
					System.err.print("オブジェクト受信時にエラーが発生しました：" + e);
				}
			}
		}catch(IOException e) {
			System.out.println("クライアントが切断しました");
		}
	}
}
