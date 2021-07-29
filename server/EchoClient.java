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
			// �T�[�o�ɐڑ�
			Socket socket = new Socket(ipAddress, port);

			// �f�[�^��M�p�I�u�W�F�N�g
			receiver = new Receiver(socket);
			receiver.start();

		}catch(IOException e) {
			System.err.println(e);
		}

	}
}

// �f�[�^��M�p�X���b�h
class Receiver extends Thread{
	public ObjectInputStream ois;
	public ObjectOutputStream oos;

	// �����N���XReceiver�̃R���X�g���N�^
	Receiver (Socket socket){
		try{
			oos = new ObjectOutputStream(socket.getOutputStream()); //�I�u�W�F�N�g�f�[�^���M�p�I�u�W�F�N�g�̗p��
			ois = new ObjectInputStream(socket.getInputStream()); //�I�u�W�F�N�g�f�[�^��M�p�I�u�W�F�N�g�̗p��
		} catch (IOException e) {
				System.err.println("�f�[�^��M���ɃG���[���������܂���: " + e);
		}
	}

	// Receiver�̃��\�b�h
	public void run(){
		try {
			while(true) {
				try {
					Object inputObj = ois.readObject();

					//UserInfo�^�Ȃ�
					if(inputObj instanceof UserInfo) {
						System.out.println("UserInfo�^�f�[�^����M���܂���");
					}

					//GroupInfo�^�Ȃ�
					else if(inputObj instanceof GroupInfo) {
						System.out.println("GroupInfo�^�f�[�^����M���܂���");
					}

					//���̑��Ȃ�receiveMessage()
					else {
						System.out.println("���b�Z�[�W���f�[�^����M���܂��� : " + inputObj.toString());
					}

				} catch (ClassNotFoundException e) {
					System.err.print("�I�u�W�F�N�g��M���ɃG���[���������܂����F" + e);
				}
			}
		}catch(IOException e) {
			System.out.println("�N���C�A���g���ؒf���܂���");
		}
	}
}
