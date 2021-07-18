import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Server extends JFrame implements ActionListener{

	static PrintWriter out; //�f�[�^���M�p�I�u�W�F�N�g
	static Receiver receiver; //�f�[�^��M�p�I�u�W�F�N�g
	static ServerSocket ss;

	static UserInfo users[] = new UserInfo[1000];
	static HashMap<String, UserInfo> activeUsers =new HashMap<>();
	static int userFileNum = 0;	//���[�U�t�@�C����

	static GroupInfo groups[] = new GroupInfo[1000];
	static HashMap<UUID, GroupInfo> activeGroups =new HashMap<>();
	static int groupFileNum = 0;	//���[�U�t�@�C����

	//�����p
	static ArrayList<UserInfo> userlist;
	static ArrayList<UserInfo> user_buf;
	static ArrayList<GroupInfo> grouplist;
	static ArrayList<GroupInfo> group_buf;

	int w = 400;
	int h = 650;

	//�R���X�g���N�^
	public Server(String title){
		setTitle(title);

		JLabel menu = new JLabel("�T�[�o���j���[");
		JButton admit_button = new JButton("�V�K����F��");
		admit_button.setPreferredSize(new Dimension(250, 35));
		JButton search_button = new JButton("�������");
		search_button.setPreferredSize(new Dimension(250, 35));

		JPanel p = new JPanel();
		p.add(menu);
		p.add(admit_button);
		p.add(search_button);

		JPanel p2 = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		p2.setLayout(layout);
		JButton end = new JButton("�I��");
		end.setPreferredSize(new Dimension(70, 30));
		p2.add(end);

		admit_button.addActionListener(this);
		search_button.addActionListener(this);
		end.addActionListener(this);

		getContentPane().add(p, BorderLayout.CENTER);
		getContentPane().add(p2, BorderLayout.PAGE_END);

		setSize(350, 200);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//�t�@�C����ǂݍ���Ńn�b�V���}�b�v�ɒǉ�
		readAllUserFiles();

		try {
			ss = new ServerSocket(50);
		} catch (IOException e) {
			System.err.println("�T�[�o�\�P�b�g�쐬���ɃG���[���������܂���: " + e);
		}
	}

	//���C�����\�b�h
	public static void main(String args[]) {
		Server server = new Server("MS_Server");
		server.acceptClient();
	}

	//�{�^������
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("�V�K����F��")) {

			new Authentificate();
		}
		if(cmd.equals("�������")) {
			new searchUsers();
		}
		if(cmd.equals("�I��")) {
			System.exit(0);
		}
	}

	//���[�U�t�@�C����S�ēǂݍ���
	public static void readAllUserFiles() {
		userFileNum = 0;
		users = null;
		activeUsers.clear();

		File dir = new File(System.getProperty("user.dir") + "\\ID");
		File image_dir = new File(System.getProperty("user.dir") + "\\ID\\images");

		if(!dir.exists()) dir.mkdir();
		if(!image_dir.exists()) image_dir.mkdir();

		File[] files = dir.listFiles();
		if( files == null )
			return;
		for( File file : files ) {
			if( !file.exists() )
				continue;
			else if( file.isDirectory() )
				continue;
			else if( file.isFile() ) {
				readUserFile(file);
			}
		}
		userFileNum--;
	}

	//���[�U�t�@�C���ǂݍ���
	static void readUserFile(File file) {
        FileReader fr;
        BufferedReader br;
		try {
			fr = new FileReader(file);
	        br = new BufferedReader(fr);
	        String line;
			int line_counter = 0;

			while((line = br.readLine()) != null) {
				line_counter++;
				switch(line_counter) {

					case 1 :
						users[userFileNum].studentNumber = Integer.parseInt(line);

						//�摜�̓ǂݍ���
						File studentCard = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_card.png");
						File main_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_main.png");
						File sub1_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub1.png");
						File sub2_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub2.png");
						File sub3_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub3.png");
						File sub4_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub4.png");
						BufferedImage card = ImageIO.read(studentCard);
						BufferedImage main = ImageIO.read(main_image);
						BufferedImage sub1 = ImageIO.read(sub1_image);
						BufferedImage sub2 = ImageIO.read(sub2_image);
						BufferedImage sub3 = ImageIO.read(sub3_image);
						BufferedImage sub4 = ImageIO.read(sub4_image);

						users[userFileNum].studentCard = card;
						users[userFileNum].mainPhoto = main;
						users[userFileNum].subPhoto[0] = sub1;
						users[userFileNum].subPhoto[1] = sub2;
						users[userFileNum].subPhoto[2] = sub3;
						users[userFileNum].subPhoto[3] = sub4
								;
						break;

					case 2 :
						users[userFileNum].password = line;
						break;

					case 3 :
						users[userFileNum].name = line;
						break;

					case 4 :
						users[userFileNum].gender = Integer.parseInt(line);
						break;

					case 5 :
						users[userFileNum].grade = Integer.parseInt(line);
						break;

					case 6 :
						users[userFileNum].faculty = Integer.parseInt(line);
						break;

					case 7 :
						users[userFileNum].birth = Integer.parseInt(line);
						break;

					case 8 :
						users[userFileNum].circle = Integer.parseInt(line);
						break;

					case 9 :
						users[userFileNum].hobby = line;
						break;

					case 10 :
						String sendStudents[] = line.split(" ");
						for(int i=0; i<sendStudents.length; i++) {
							users[userFileNum].sendGood[i] = Integer.parseInt(sendStudents[i]);
						}
						break;

					case 11 :
						String receiveStudents[] = line.split(" ");
						for(int i=0; i<receiveStudents.length; i++) {
							users[userFileNum].receiveGood[i] = Integer.parseInt(receiveStudents[i]);
						}
						break;

					case 12 :
						String matchingStudents[] = line.split(" ");
						for(int i=0; i<matchingStudents.length; i++) {
							users[userFileNum].matchedUser[i] = Integer.parseInt(matchingStudents[i]);
						}
						break;

					case 13 :
						String joiningGroups[] = line.split(" ");
						for(int i=0; i<joiningGroups.length; i++) {
							users[userFileNum].joiningGroup[i] = UUID.fromString(joiningGroups[i]);
						}
						break;

					case 14 :
						String invitedGroups[] = line.split(" ");
						for(int i=0; i<invitedGroups.length; i++) {
							users[userFileNum].invitedGroup[i] = UUID.fromString(invitedGroups[i]);
						}
						break;

					case 15 :
						users[userFileNum].isAuthentificated = Integer.parseInt(line);
						break;

					case 16 :
						users[userFileNum].lineId = line;
						break;

					case 17 :
						users[userFileNum].isPublic = Boolean.parseBoolean(line);
						break;

				}

				userlist.add(users[userFileNum]);
				activeUsers.put(String.valueOf(users[userFileNum].studentNumber),users[userFileNum]);
				userFileNum++;

			}
		} catch (IOException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}

	//�O���[�v�t�@�C����S�ēǂݍ���
	public static void readAllGroupFiles() {
		groupFileNum = 0;
		groups = null;
		activeGroups.clear();

		File dir = new File(System.getProperty("user.dir") + "\\Group");
		File image_dir = new File(System.getProperty("user.dir") + "\\Group\\images");

		if(!dir.exists()) dir.mkdir();
		if(!image_dir.exists()) image_dir.mkdir();

		File[] files = dir.listFiles();
		if( files == null )
			return;
		for( File file : files ) {
			if( !file.exists() )
				continue;
			else if( file.isDirectory() )
				continue;
			else if( file.isFile() ) {
				readGroupFile(file);
			}
		}
		groupFileNum--; //�v�f�ƈ�v������
	}

	//�O���[�v�t�@�C���ǂݍ���
	public static void readGroupFile(File file) {
	       FileReader fr;
	        BufferedReader br;
			try {
				fr = new FileReader(file);
		        br = new BufferedReader(fr);
		        String line;
				int line_counter = 0;

				while((line = br.readLine()) != null) {
					line_counter++;
					switch(line_counter) {

						case 1 :
							groups[groupFileNum].groupNumber = UUID.fromString(line);

							//�摜�̓ǂݍ���
							File main_image = new File(System.getProperty("user.dir") + "\\Group\\images\\" + line + "_main.png");
							BufferedImage main = ImageIO.read(main_image);

							groups[groupFileNum].mainPhoto = main;
									;
							break;

						case 2 :
							groups[groupFileNum].name = line;
							break;

						case 3 :
							groups[groupFileNum].relation = line;
							break;

						case 4 :
							String sendGood[] = line.split(" ");
							for(int i=0; i<sendGood.length; i++) {
								groups[groupFileNum].sendGood[i] = UUID.fromString(sendGood[i]);
							}
							break;

						case 5 :
							String receiveGood[] = line.split(" ");
							for(int i=0; i<receiveGood.length; i++) {
								groups[groupFileNum].receiveGood[i] = UUID.fromString(receiveGood[i]);
							}
							break;

						case 6 :
							String matchedGood[] = line.split(" ");
							for(int i=0; i<matchedGood.length; i++) {
								groups[groupFileNum].matchedGroup[i] = UUID.fromString(matchedGood[i]);
							}
							break;

						case 7 :
							groups[groupFileNum].hostUser = Integer.parseInt(line);
							break;

						case 8 :
							String nonhostUser[] = line.split(" ");
							for(int i=0; i<nonhostUser.length; i++) {
								groups[groupFileNum].nonhostUser[i] = Integer.parseInt(nonhostUser[i]);
							}
							break;

						case 9 :
							groups[groupFileNum].purpose = Integer.parseInt(line);
							break;

						case 10 :
							groups[groupFileNum].comment = line;
							break;

						case 11 :
							groups[groupFileNum].numberOfMember = Integer.parseInt(line);
							break;

						case 12 :
							groups[groupFileNum].isGathered = Boolean.parseBoolean(line);
							break;

					}

					grouplist.add(groups[groupFileNum]);
					activeGroups.put(groups[userFileNum].groupNumber, groups[groupFileNum]);
					userFileNum++;

				}
			} catch (IOException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
	}

	//�f�[�^��M�p�X���b�h(�����N���X)
	class Receiver extends Thread {
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private PrintWriter out_buf; //���M����L�^
		// �����N���XReceiver�̃R���X�g���N�^
		Receiver (Socket socket){
			try{
				oos = new ObjectOutputStream(socket.getOutputStream()); //�I�u�W�F�N�g�f�[�^���M�p�I�u�W�F�N�g�̗p��
				ois = new ObjectInputStream(socket.getInputStream()); //�I�u�W�F�N�g�f�[�^��M�p�I�u�W�F�N�g�̗p��
				//sisr = new InputStreamReader(socket.getInputStream());
			} catch (IOException e) {
					System.err.println("�f�[�^��M���ɃG���[���������܂���: " + e);
			}
		}

		//���b�Z�[�W�̏���
		public void receiveMessage(String inputLine) {
				if (inputLine != null){ //�f�[�^����M������
					String act[] = inputLine.split(","); //�J���}�̑O��ŕ�����𕪊�
					System.out.println("receiveMessage���N��:"+inputLine);	//�m�F�p

					try {
						switch(act[0]){
						case "lg": //�V�K�o�^����
							if(checkPassword(act[1],act[2] /*(�w�Дԍ�,�p�X���[�h)*/) == true) {
								oos.writeObject("1");
								oos.flush();
							}
							else {
								oos.writeObject("0");
								oos.flush();
								/*out_buf.print("0");
								out_buf.flush();*/
							}
							break;

						case "ui": //���[�U���̎擾
							try {
								oos.writeObject(activeUsers.get(act[1]));
								oos.flush();
							} catch (IOException e) {
								System.err.print("���[�U��񑗐M���ɃG���[���������܂����F" + e);
							}
							break;

						case "us": //3�l�����M
							try {
								oos.writeObject(sendUserInfo(Integer.parseInt(act[1])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("���[�U��񑗐M���ɃG���[���������܂����F" + e);
							}
							break;

						case "uj": //���[�U��������
							try {
								oos.writeObject(searchUsers(Integer.parseInt(act[1]), Integer.parseInt(act[2]), Integer.parseInt(act[3]), Integer.parseInt(act[4]), Integer.parseInt(act[5]), Integer.parseInt(act[6])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("���[�U��񑗐M���ɃG���[���������܂����F" + e);
							}
							break;

						case "gi": //�O���[�v���̎擾
							try {
								oos.writeObject(activeGroups.get(UUID.fromString(act[1])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("�O���[�v��񑗐M���ɃG���[���������܂����F" + e);
							}
							break;

						case "gs": //3�O���[�v�����M
							try {
								oos.writeObject(sendGroupInfo(Integer.parseInt(act[1])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("�O���[�v��񑗐M���ɃG���[���������܂����F" + e);
							}
							break;

						case "gj": //�O���[�v��������
							try {
								oos.writeObject(searchGroups(Integer.parseInt(act[1]), Integer.parseInt(act[2]), Integer.parseInt(act[3])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("�O���[�v��񑗐M���ɃG���[���������܂����F" + e);
							}
							break;

						case "ug": //���[�U�ɂ����˂𑗂�
							if(goodUser(act[0],act[1])) {
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}

							break;

						case "gg": //�O���[�v�ɂ����˂𑗂�
							if(goodGroup(act[0],act[1])) {
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						case "jg": //�O���[�v�ɎQ��
							if(joinGroup(act[1], act[2])){
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						case "rg": //�O���[�v�Q������
							if(deleteGroup(act[2])){
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						}
					}catch(IOException e) {
						System.err.print("�I�u�W�F�N�g��M���ɃG���[���������܂����F" + e);
					}
				}
		}

		// �����N���X Receiver�̃��\�b�h
		public void run(){
			try {
				while(true) {
					try {
						Object inputObj = ois.readObject();
						System.out.println("�f�[�^����M���܂���");
						//UserInfo�^�Ȃ�
						if(inputObj instanceof UserInfo) {
							UserInfo ui = new UserInfo();
							ui = (UserInfo)inputObj;
							//�V�K�o�^
							if(ui.state == 0) {
								signUp(ui);
							}
							//
						}

						//GroupInfo�^�Ȃ�
						else if(inputObj instanceof GroupInfo) {
							GroupInfo gi = new GroupInfo();
							gi = (GroupInfo)inputObj;
							//�O���[�v�쐬
							if(gi.state == 0) {
								makeGroup(gi);
							}
						}

						//���̑��Ȃ�receiveMessage()
						else {
							//br = new BufferedReader(sisr);
							//String inputLine = br.readLine();//�f�[�^����s���ǂݍ���
							String inputLine = inputObj.toString();
							System.out.println(inputLine);	//�m�F�p
							receiveMessage(inputLine);
						}

					} catch (ClassNotFoundException e) {
						System.err.print("�I�u�W�F�N�g��M���ɃG���[���������܂����F" + e);
					}
				}
			}catch(IOException e) {
				System.err.println("�N���C�A���g���ؒf���܂���");
			}
		}
	}

	//�N���C�A���g�ɐڑ�
	public void acceptClient(){ //�N���C�A���g�̐ڑ�(�T�[�o�̋N��)
		try {
			while (true) {
				Socket socket = ss.accept(); //�V�K�ڑ����󂯕t����
				System.out.println("�N���C�A���g�Ɛڑ����܂����D"); //�e�X�g�p�o��
				out = new PrintWriter(socket.getOutputStream(), true);//�f�[�^���M�I�u�W�F�N�g��p��
				receiver = new Receiver(socket);//�f�[�^��M�I�u�W�F�N�g(�X���b�h)��p��
				receiver.start();//�f�[�^���M�I�u�W�F�N�g(�X���b�h)���N��
			}
		} catch (Exception e) {
			System.err.println("�\�P�b�g�쐬���ɃG���[���������܂���: " + e);
		}
	}

	//����
	public static UserInfo[] searchUsers(int page, int gender, int grade, int faculty, int birth, int circle) {

		user_buf.clear();

		UserInfo res[] = new UserInfo[3];

		for(int i = 0; i < userlist.size(); i++){
			UserInfo user = userlist.get(i);

			//���S��v
		    if(user.gender == gender && user.grade == grade && user.faculty == faculty && user.birth == birth && user.circle == circle) {
		    	user_buf.add(user);
		    }

		}

		//���Ȃ��Ȃ�null��Ԃ�
		if(user_buf == null) return null;

		//��₪����ꍇ
		else {
			if(3*page - 2 <= user_buf.size()) res[0] = user_buf.get(3*page - 2);
			else res[0] = null;

			if(3*page - 1 <= user_buf.size()) res[1] = user_buf.get(3*page - 1);
			else res[1] = null;

			if(3*page <= user_buf.size()) res[2] = user_buf.get(3*page);
			else res[2] = null;

		}

		return res;
	}

	//UserInfo���M
	public static UserInfo[] sendUserInfo(int page) {
		UserInfo res[] = new UserInfo[3];

		//�Ȃ��Ȃ�null��Ԃ�
		if(users == null) return null;

		//���[�U������ꍇ
		else {
			if(users[3*page - 2] != null) res[0] = users[3*page - 2];
			else res[0] = null;

			if(users[3*page - 1] != null) res[1] = users[3*page - 1];
			else res[1] = null;

			if(users[3*page] != null) res[2] = users[3*page];
			else res[2] = null;
		}

		//���̃y�[�W�Ƀ��[�U�����Ȃ����null��Ԃ�
		if(res[0] == null && res[1] == null && res[2] == null) return null;

		return res;
	}

	//�O���[�v����
	public GroupInfo[] searchGroups(int page, int purpose, int num) {

		group_buf.clear();

		GroupInfo res[] = new GroupInfo[3];

		for(int i = 0; i < grouplist.size(); i++){
			GroupInfo group = grouplist.get(i);

			//���S��v
		    if(group.purpose == purpose || group.numberOfMember == num) {
		    	group_buf.add(group);
		    }

		}

		//���Ȃ��Ȃ�null��Ԃ�
		if(group_buf == null) return null;

		//��₪����ꍇ
		else {
			if(3*page - 2 <= group_buf.size()) res[0] = group_buf.get(3*page - 2);
			else res[0] = null;

			if(3*page - 1 <= group_buf.size()) res[1] = group_buf.get(3*page - 1);
			else res[1] = null;

			if(3*page <= group_buf.size()) res[2] = group_buf.get(3*page);
			else res[2] = null;

		}

		return res;

	}

	//GroupInfo���M
	public GroupInfo[] sendGroupInfo(int page) {
		GroupInfo res[] = new GroupInfo[3];

		//�Ȃ��Ȃ�null��Ԃ�
		if(groups == null) return null;

		//���[�U������ꍇ
		else {
			if(groups[3*page - 2] != null) res[0] = groups[3*page - 2];
			else res[0] = null;

			if(groups[3*page - 1] != null) res[1] = groups[3*page - 1];
			else res[1] = null;

			if(groups[3*page] != null) res[2] = groups[3*page];
			else res[2] = null;
		}

		//���̃y�[�W�Ƀ��[�U�����Ȃ����null��Ԃ�
		if(res[0] == null && res[1] == null && res[2] == null) return null;

		return res;
	}

	//�p�X���[�h�`�F�b�N
	public static boolean checkPassword(String num, String pass) {
		String path = System.getProperty("user.dir") + "\\ID"; //�f�B���N�g��
		File LoginFile = new File(path + "\\" + num + ".txt"); //���[�U�t�@�C��
		File dir = new File(path);
		BufferedReader br = null;
		FileReader fr = null;

		//�f�B���N�g���܂��̓��[�U�t�@�C�������݂��Ȃ��ꍇ
		if(!dir.exists() || !LoginFile.exists()) return false;
		//�f�B���N�g������у��[�U�t�@�C�������݂���ꍇ
		else {
			try {
				fr = new FileReader(LoginFile);
				br = new BufferedReader(fr);
				String str = br.readLine();				//1�s�ړǂݍ���
				br.close();
				String res[] = str.split(" ");			//�󔒂ŕ���

				//�����p�X���[�h����v���Ă����true
				if(res[1] == pass) return true;
				//�����łȂ����false
				else return false;

			}catch(IOException e) {
				System.err.println("�G���[���������܂����F" + e);
				return false;
			}
		}
	}

	//�V�K�o�^
	public static void signUp(UserInfo ui) {
		String path = System.getProperty("user.dir") + "\\ID"; //�f�B���N�g��
		File LoginFile = new File(path + "\\" + ui.studentNumber + ".txt"); //���[�U�t�@�C��
		File dir = new File(path);
		File image_dir = new File(path + "\\images");
		File image_user_dir = new File(path + "\\images" + "\\" + ui.studentNumber);
		File studentCard = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_card.png");
		File main_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_main.png");
		File sub1_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub1.png");
		File sub2_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub2.png");
		File sub3_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub3.png");
		File sub4_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub4.png");

		//�f�B���N�g���̍쐬
		if(!dir.exists()) {
			dir.mkdir();
			image_dir.mkdir();
		}

		//�t�@�C���̍쐬
		if(!LoginFile.exists()) {
			try {
				LoginFile.createNewFile();
				image_user_dir.mkdir();
			} catch (IOException e) {
				System.err.println("�t�@�C���쐬���ɗ\�����ʃG���[���������܂���");
				return;
			}
		}

		try {
			//���[�U���t�@�C�����쐬
			FileWriter fw = new FileWriter(LoginFile);
			fw.write(ui.studentNumber + "\n" +
					 ui.password + "\n" +
					 ui.name + "\n" +
					 ui.gender + "\n" +
					 ui.grade + "\n" +
					 ui.faculty + "\n" +
					 ui.birth + "\n" +
					 ui.circle + "\n" +
					 ui.hobby + "\n" +
					 /*�w�Дԍ�*/"\n" +
					 /*�w�Дԍ�*/"\n" +
					 /*�w�Дԍ�*/"\n" +
					 /*UUID*/"\n" +
					 /*UUID*/"\n" +
					 ui.isAuthentificated +"\n"+
					 ui.lineId + "\n" +
					 ui.isPublic + "\n"
					 );
			fw.close();

			//�摜��ۑ�
			ImageIO.write(ui.studentCard, "png", studentCard);
			ImageIO.write(ui.mainPhoto, "png", main_image);
			ImageIO.write(ui.subPhoto[0], "png", sub1_image);
			ImageIO.write(ui.subPhoto[1], "png", sub2_image);
			ImageIO.write(ui.subPhoto[2], "png", sub3_image);
			ImageIO.write(ui.subPhoto[3], "png", sub4_image);

			//�z��ɒǉ�
			readUserFile(LoginFile);

		} catch (IOException e) {
			System.err.print("�V�K�o�^�̍ۂɃG���[���������܂����F" + e);
			return;
		}
	}

	//���[�U�̏��ύX
	public static void userinfoChange(String studentNum, UserInfo ui) {
		BufferedReader br = null;
		FileReader fr = null;
		PrintWriter pw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

		String newinfo = new String(ui.studentNumber + "\n" +
							ui.password + "\n" +
							ui.name + "\n" +
							ui.gender + "\n" +
							ui.grade + "\n" +
							ui.faculty + "\n" +
							ui.birth + "\n" +
							ui.circle + "\n" +
							ui.hobby + "\n" +
							/*�w�Дԍ�*/"\n" +
							/*�w�Дԍ�*/"\n" +
							/*�w�Дԍ�*/"\n" +
							/*UUID*/"\n" +
							/*UUID*/"\n" +
							ui.isAuthentificated +"\n"+
							ui.lineId + "\n" +
							ui.isPublic + "\n");


		try {
			//�t�@�C����ǂݍ���
			File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			pw = new PrintWriter(file);

			while((line = br.readLine())!=null)
				pw.println(line.replaceAll(line, newinfo));



		}catch(IOException e) {
			System.err.print("���[�U���ύX�Ɋւ��鏈���ŃG���[���������܂����F" + e);
			return;

		}finally {
			try {
				fr.close();
				br.close();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			return;
			}
		}

		return;

	}

	//�O���[�v�̏��ύX
	public static void groupinfoChange(String uuid, GroupInfo gi) {
		BufferedReader br = null;
		FileReader fr = null;
		PrintWriter pw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

		String newinfo = new String(gi.name + "\n" +
					 gi.relation + "\n" +
					 /*UUID*/ "\n" +
					 /*UUID*/ "\n" +
					 /*UUID*/ "\n" +
					 gi.hostUser + "\n" +
					 gi.nonhostUser[0] + " " + gi.nonhostUser[1] + " " + gi.nonhostUser[2] + " " + gi.nonhostUser[3] + "\n" +
					 gi.comment + "\n" +
					 gi.numberOfMember + "\n");

		try {
			//�t�@�C����ǂݍ���
			File file = new File(System.getProperty("user.dir") + "\\Group\\" + uuid + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			pw = new PrintWriter(file);

			while((line = br.readLine())!=null)
				line = line.replaceAll(line, newinfo);
		}catch(IOException e) {
			System.err.print("�O���[�v���ύX�Ɋւ��鏈���ŃG���[���������܂����F" + e);
			return;

		}finally {
			try {
				fr.close();
				br.close();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			return;
			}
		}

		return;


	}

	//�O���[�v�쐬
	public static void makeGroup(GroupInfo gi) {

		String path = System.getProperty("user.dir") + "\\Group"; //�f�B���N�g��
		gi.groupNumber = UUID.randomUUID(); //UUID�̍쐬
		File GroupFile = new File(path + "\\" + gi.groupNumber.toString() + ".txt");
		File dir = new File(path);
		File image_dir = new File(path + "\\images");
		File main_image = new File(path + "\\images" + "\\" + gi.groupNumber + "_main.png");

		//�f�B���N�g���̍쐬
		if(!dir.exists()) {
			dir.mkdir();
			image_dir.mkdir();
		}

		//�t�@�C���̍쐬
		if(!GroupFile.exists()) {
			try {
				GroupFile.createNewFile();
			} catch (IOException e) {
				System.err.println("�t�@�C���쐬���ɗ\�����ʃG���[���������܂���");
				return;
			}
		}

		joinGroup(String.valueOf(gi.hostUser), gi.groupNumber.toString());
		for(int i=0; i<gi.numberOfMember-1; i++) {
			inviteUsers(String.valueOf(gi.hostUser), gi.groupNumber.toString());
		}

		String nonhost = gi.nonhostUser[0] + " " + gi.nonhostUser[1] + " " + gi.nonhostUser[2] + " " + gi.nonhostUser[3];
		nonhost.replace(" 0","");

		try {
			//�O���[�v���t�@�C�����쐬
			FileWriter fw = new FileWriter(GroupFile);
			fw.write(gi.name + "\n" +
					 gi.relation + "\n" +
					 /*UUID*/ "\n" +
					 /*UUID*/ "\n" +
					 /*UUID*/ "\n" +
					 gi.hostUser + "\n" +
					 nonhost + "\n" +
					 gi.comment + "\n" +
					 gi.numberOfMember + "\n"
					 );
			fw.close();

			//�摜��ۑ�
			ImageIO.write(gi.mainPhoto, "png", main_image);

			//�ēx�ǂݍ���
			readAllUserFiles();
			readAllGroupFiles();

		} catch (IOException e) {
			System.err.print("�V�K�o�^�̍ۂɃG���[���������܂����F" + e);
			return;
		}
	}

	//�O���[�v�쐬���ɏ���
	public static void inviteUsers(String studentNum, String uuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

		try {
			//�t�@�C����ǂݍ���
			File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int line_counter = 0;

			//�Y���s������
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 14) break;
				strbuf.append(line + "\n");
			}

			//�U���Ă���O���[�v(13�s��)�ɒǉ�
			if(line == "") { 	//���܂ŗU���Ă��Ȃ������ꍇ
				strbuf.append(uuid + "\n");
			}else {				//���łɗU���Ă����ꍇ
				strbuf.append(line + " " + uuid + "\n");
			}

			//�Ō�܂œǂݍ���
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//�Q�������O���[�v���S���W�܂������m�F
			judgeAllGathered(uuid);

			//��������
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			//�ēx�ǂݍ���
			readAllUserFiles();
			readAllGroupFiles();

		}catch(IOException e) {
			System.err.print("�O���[�v�Q���Ɋւ��鏈���ŃG���[���������܂����F" + e);
		}finally {
			try {
				fr.close();
				br.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//�O���[�v�Q��
	public static boolean joinGroup(String studentNum, String uuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

		try {
			//�t�@�C����ǂݍ���
			File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int line_counter = 0;

			//�Y���s������
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 13) break;
				strbuf.append(line + "\n");
			}

			//�Q�����Ă���O���[�v(13�s��)�ɒǉ�
			if(line == "") { //���܂ŎQ�����ĂȂ������ꍇ
				strbuf.append(uuid + "\n");
			}else {				//���łɎQ���������Ƃ�����ꍇ
				strbuf.append(line + " " + uuid + "\n");
			}

			line = br.readLine(); //���̍s

			//�U���Ă���O���[�v(14�s��)����폜
			line = line.replace(uuid, ""); //UUID���폜
			line = line.replace("  "," "); //���񂾋󔒂��폜
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //�擪�̋󔒂��폜
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //�Ō�̋󔒂��폜
			strbuf.append(line + "\n");

			//�Ō�܂œǂݍ���
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//�Q�������O���[�v���S���W�܂������m�F
			judgeAllGathered(uuid);

			//��������
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			//�ēx�ǂݍ���
			readAllUserFiles();
			readAllGroupFiles();

		}catch(IOException e) {
			System.err.print("�O���[�v�Q���Ɋւ��鏈���ŃG���[���������܂����F" + e);
			return false;

		}finally {
			try {
				fr.close();
				br.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	//�S���W�܂�����
	public static void judgeAllGathered(String uuid) {
        BufferedReader br = null;
        FileReader fr = null;
		FileWriter fw = null;
        String line;
        StringBuffer strbuf = new StringBuffer("");

        try {
        	File file = new File(System.getProperty("user.dir") + "\\Group\\" + uuid + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int line_counter = 0;

			//�Y���s������
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 8) break;
				strbuf.append(line + "\n");
			}

			String students[] = line.split(" ");
			int index = students.length + 1; //�Q�����Ă���l��

			strbuf.append(line + "\n");

			//�Y���s������
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 11) break;
				strbuf.append(line + "\n");
			}

			boolean judge = false;
			if(index == Integer.parseInt(line)) judge = true;
			strbuf.append(line + "\n");

			line = br.readLine(); //���̍s
			if(judge) {
				strbuf.append("true\n");
			}else {
				strbuf.append(line + "\n");
			}

			//��������
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			//�ēx�ǂݍ���
			readAllUserFiles();
			readAllGroupFiles();

        }catch(IOException e) {

        }
	}

	//�O���[�v�Q�����ہE�O���[�v�폜
	public static boolean deleteGroup(String uuid) {
        BufferedReader br = null;
        FileReader fr = null;
        String line;

		try {
			//�t�@�C����ǂݍ���
			File file = new File(System.getProperty("user.dir") + "\\Group\\" + uuid + ".txt");
			File main_image = new File(System.getProperty("user.dir") + "\\Group\\images\\" + uuid + "_main.png");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int line_counter = 0;

			//�Y���s������
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 4) break;
			}

			//�����˂����O���[�v�B
			String Groups[] = line.split(" ");

			//�����ł����˂𑗂����E����ꂽ�O���[�v�t�@�C���̂����˂��炱�̃O���[�v���폜
			for(int i = 0; i < Groups.length; i++) //refuseGood(Groups[i],uuid); //�����˂����O���[�v�̃t�@�C�����玩���������������˂�����(�����˂��ꂽ�O���[�v,����)
			for(int j = 0; j < Groups.length; j++) //refuseGood(uuid,Groups[i]); //�����˂��ꂽ�O���[�v�̃t�@�C�����玩���ɂ����˂����L�^������(����,�����˂����O���[�v)

			//�z�X�g���[�U�̃O���[�v�Ɋւ���L�^���폜
			deleteGroupLog(line,uuid);

			//���̍s
			line = br.readLine();

			//��������X�y�[�X�ŕ���
			String nonhoststudents[] = line.split(" ");

			//�l���̍s�܂ŃX�L�b�v
			for(int i = 0; i<3; i++) {
				line = br.readLine();
			}

			//�O���[�v�̐l�����L�^
			int num = Integer.parseInt(line);

			//���[�U��񂩂�O���[�v�Ɋւ���L�^���폜
			for(int i = 0; i < num - 1; i++) {
				deleteGroupLog(nonhoststudents[num],uuid);
			}

			//�O���[�v�t�@�C���폜
			file.delete();

			//�摜�폜
			main_image.delete();

			//�ēx�ǂݍ���
			readAllUserFiles();
			readAllGroupFiles();

		}catch(IOException e) {
			System.err.print("�O���[�v�Q�����ۂɊւ��鏈���ŃG���[���������܂����F" + e);
			return false;

		}finally {
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	//���[�U�̃O���[�v�̃��O���폜
	public static void deleteGroupLog(String studentNum, String uuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

			try {
				//�t�@�C����ǂݍ���
				File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				int line_counter = 0;

				//�Y���s������
				while((line = br.readLine()) != null) {
					line_counter++;
					if(line_counter == 13) break;
					strbuf.append(line + "\n");
				}

				//���łɎQ�����Ă���ꍇ
				if(line.contains(uuid)) {
					line = line.replace(uuid, ""); //UUID���폜
					line = line.replace("  "," "); //���񂾋󔒂��폜
					if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //�擪�̋󔒂��폜
					if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //�Ō�̋󔒂��폜
					strbuf.append(line + "\n");
				}

				line = br.readLine(); //���̍s

				//�U���Ă���i�K�̏ꍇ
				if(line.contains(uuid)) {
					line = line.replace(uuid, ""); //UUID���폜
					line = line.replace("  "," "); //���񂾋󔒂��폜
					if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //�擪�̋󔒂��폜
					if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //�Ō�̋󔒂��폜
					strbuf.append(line + "\n");
				}

				//�Ō�܂œǂݍ���
				while((line = br.readLine()) != null) {
					strbuf.append(line + "\n");
				}

				//��������
				fw = new FileWriter(file);
				fw.write(strbuf.toString());

				//�ēx�ǂݍ���
				readAllUserFiles();
				readAllGroupFiles();

			}catch(IOException e) {
				System.err.print("�O���[�v���ҍ폜�Ɋւ��鏈���ŃG���[���������܂����F" + e);
			}finally {
				try {
					fr.close();
					br.close();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

	//���[�U�폜
	public static void deleteUser(String studentNum) {
		BufferedReader br = null;
		FileReader fr = null;
		File image_user_dir = new File(System.getProperty("user.dir") + "\\ID\\images\\" + studentNum);
		String line;
		String SentGoodStudents[] = new String[100]; //�����˂������ЂƁA�Ƃ肠����100�l�܂�
		String BeingSentGoodStudents[] = new String[100]; //�����˂����ꂽ�l�A100�l
		String MatchingStudents[] = new String[100]; //�}�b�`���O�����l�A100�l
		String Groups[] = new String[100]; //�Q�����Ă�O���[�v�A�Ƃ肠����100�܂�
		String InvitedGroups[] = new String[100]; //�U���Ă�O���[�v�A�Ƃ肠����100�܂�

			try {
				//�t�@�C����ǂݍ���
				File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				int line_counter = 0;

				//�Y���s������
				while((line = br.readLine()) != null) {
					line_counter++;
					if(line_counter == 10) break;
				}

				//�󔒂ŕ������ĕۑ��A�����˂𑗂����Ђ�
				SentGoodStudents = line.split(" ");
				//�����ł����ˍ폜

				//���̍s
				line = br.readLine();

				//�󔒂ŕ������ĕۑ��A�����˂����ꂽ�l
				BeingSentGoodStudents = line.split(" ");
				//�����ł����ˍ폜

				//���̍s
				line = br.readLine();

				//�󔒂ŕ������ĕۑ��A�}�b�`���O�����l
				MatchingStudents = line.split(" ");
				//deleteMatching(); �}�b�`���O���Ă鑊�������

				//���̍s
				line = br.readLine();

				//�󔒂ŕ������ĕۑ��A�Q�����Ă�O���[�v
				Groups = line.split(" ");
				for(int i=0; i<Groups.length; i++) {
					deleteGroup(Groups[i]);
				}

				//���̍s
				line = br.readLine();

				//�󔒂ŕ������ĕۑ��A�U���Ă�O���[�v
				InvitedGroups = line.split(" ");
				for(int i=0; i<InvitedGroups.length; i++) {
					deleteGroup(InvitedGroups[i]);
				}

//�g��Ȃ�����
//				//�z��̃C���f�b�N�X���擾
//				int index = ArrayUtils.indexOf(users,activeUsers.get(studentNum));
//
//				//�z������炷
//				for(int i=index; i<=userFileNum - 1; i++) {
//					users[i] = users[i+1];
//				}
//				users[userFileNum] = null;
//				userFileNum--;

				//�폜
				file.delete();
				image_user_dir.delete();

				//�ēx�ǂݍ���
				readAllUserFiles();
				readAllGroupFiles();

			}catch(IOException e) {
				System.err.print("���[�U�폜�Ɋւ��鏈���ŃG���[���������܂����F" + e);
			}finally {
				try {
					fr.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

	//������
 	public static boolean goodUser(String my_num, String your_num) {
 		try {
 			File file = new File(my_num + ".txt");
 			FileReader filereader = new FileReader(file);
 			BufferedReader br = new BufferedReader(filereader);

 			int count = 0;
 			int flag = 0;
 			String[] str = new String[100];


 			while(str[count] != null) {
 				str[count] = br.readLine();
 				count++;
 			}

    			int check = str[10].indexOf(your_num);
    			if(check!=-1) {    //�����˂���Ă�
    				str[10] = str[10].replace(your_num,"");
    				flag = 1;
    			}
    			else
    				str[9] += " your_num";

    			if(flag == 1)   //�}�b�`����
    				str[11] += " your_num";

   			FileWriter filewriter = new FileWriter(file);  //��������
   			BufferedWriter bw = new BufferedWriter(filewriter);
   			for (int i=0;i<17;i++) {
	   			bw.write(str[i]);
	   			bw.newLine();
   			}

   			File file2 = new File(your_num + ".txt");
   			FileReader filereader2 = new FileReader(file2);
   			BufferedReader br2 = new BufferedReader(filereader2);

   			int count2 = 1;
   			String[] str2 = new String[100];
   			str2[count-1] = br2.readLine();
   			while(str2[count2-1] != null) {
	   			count2++;
	   			str2[count2-1] = br.readLine();
   			}
   			if(flag == 1) { //�}�b�`������
	   			str[9] = str[9].replace(your_num,"");
	   			str[11] += " your_num";
   			}
   			else {
	   			str[10] += " your_num";
   			}

   			FileWriter filewriter2 = new FileWriter(file2);  //��������
   			BufferedWriter bw2 = new BufferedWriter(filewriter2);
   			for (int i=0;i<17;i++) {
	   			bw2.write(str[i]);
	   			bw2.newLine();
   			}

   			readAllUserFiles();
   			readAllGroupFiles();

  		}catch(IOException e) {
   			System.out.println(e);
   			return false;
   		}

 		return true;
 	}

	//�O���[�v������
 	public static boolean goodGroup(String my_num, String your_num) {
 		try {
 			File file = new File(my_num + ".txt");
 			FileReader filereader = new FileReader(file);
 			BufferedReader br = new BufferedReader(filereader);

 			int count = 0;
 			int flag = 0;
 			String[] str = new String[100];


 			while(str[count] != null) {
 				str[count] = br.readLine();
 				count++;
 			}

 			int check = str[4].indexOf(your_num);
 			if(check!=-1) {    //�����˂���Ă�
 				str[4] = str[10].replace(your_num,"");
 				flag = 1;
 			}
 			else
 				str[3] += " your_num";

 			if(flag == 1)   //�}�b�`����
 				str[5] += " your_num";

 			FileWriter filewriter = new FileWriter(file);  //��������
 			BufferedWriter bw = new BufferedWriter(filewriter);
 			for (int i=0;i<11;i++) {
 				bw.write(str[i]);
 				bw.newLine();
 			}

 			File file2 = new File(your_num + ".txt");
 			FileReader filereader2 = new FileReader(file2);
 			BufferedReader br2 = new BufferedReader(filereader2);

 			int count2 = 1;
 			String[] str2 = new String[100];
 			str2[count-1] = br2.readLine();
 			while(str2[count2-1] != null) {
 				count2++;
 				str2[count2-1] = br.readLine();
 			}
 			if(flag == 1) { //�}�b�`������
 				str[9] = str[9].replace(your_num,"");
 				str[11] += " your_num";
 			}
 			else {
 				str[10] += " your_num";
 			}

 			FileWriter filewriter2 = new FileWriter(file2);  //��������
 			BufferedWriter bw2 = new BufferedWriter(filewriter2);
 			for (int i=0;i<11;i++) {
 				bw2.write(str[i]);
 				bw2.newLine();
 			}
 			readAllUserFiles();
 			readAllGroupFiles();

 			return true;

 		}catch(IOException e) {
 			System.out.println(e);
 			return false;
 		}
	}

	//���[�U�����ˋ���
	public static boolean badUser(String my_num, String your_num) {
		try {
			File file = new File(my_num + ".txt");
			FileReader filereader = new FileReader(file);
			BufferedReader br = new BufferedReader(filereader);

			int count = 0;
			int flag = 0;
			String[] str = new String[100];


			while(str[count] != null) {
				str[count] = br.readLine();
				count++;
			}

 				str[10] = str[10].replace(your_num,"");
 				str[10] = str[10].replace("  "," ");
				if(str[10].charAt(0) == ' ')  str[10] = str[10].substring(1, str[10].length());
				if(str[10].charAt(str[10].length()) == ' ')  str[10] = str[10].substring(1, str[10].length()-1);

			FileWriter filewriter = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(filewriter);
			for (int i=0;i<17;i++) {
	   			bw.write(str[i]);
	   			bw.newLine();
			}

			readAllUserFiles();
			readAllGroupFiles();

			return true;

		}catch(IOException e) {
			System.out.println(e);
			return false;
		}

	}

	//�O���[�v�����ˋ���
	public static boolean badGroup(String my_num, String your_num) {
			try {
				File file = new File(my_num + ".txt");
				FileReader filereader = new FileReader(file);
				BufferedReader br = new BufferedReader(filereader);

				int count = 0;
				String[] str = new String[100];

				while(str[count] != null) {
					str[count] = br.readLine();
					count++;
				}

	 				str[4] = str[4].replace(your_num,"");
	 				str[4] = str[4].replace("  "," ");
					if(str[4].charAt(0) == ' ')  str[4] = str[4].substring(1, str[4].length());
					if(str[4].charAt(str[4].length()) == ' ')  str[4] = str[4].substring(1, str[4].length()-1);

				FileWriter filewriter = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(filewriter);
				for (int i=0;i<11;i++) {
		   			bw.write(str[i]);
		   			bw.newLine();
				}

				readAllUserFiles();
				readAllGroupFiles();

				return true;

			}catch(IOException e) {
				System.out.println(e);
				return false;
			}

		}

	//�F�ؓ����N���X
	class Authentificate extends JFrame implements ActionListener{
		JPanel cardPanel;
		CardLayout cardLayout;

		int pageAuthen=-1;
		File[] notAuthentificatededUsers;

	    JTextField tfStudentNumberSearch = new JTextField(20);
		JLabel lUserNameAuthen = new JLabel("");
	    JLabel lStudentCardAuthen = new JLabel("");
	    JLabel lUserNumberAuthen = new JLabel("");
	    JLabel lUserLineIdAuthen = new JLabel("");


		public Authentificate(){

			super("���[�U�[�F��");
			cardPanel = new JPanel();
		    cardLayout = new CardLayout();
		    cardPanel.setLayout(cardLayout);

		    prepareAuthen();
		    commitAuthen();
		    nextPage();

		    cardLayout.show(cardPanel,"commitAuthen");
		    getContentPane().add(cardPanel, BorderLayout.CENTER);
		    setSize(w,h);
			setResizable(false);
		    setVisible(true);
		}

		public void prepareAuthen() {
			String path = System.getProperty("user.dir") + "\\ID"; //�f�B���N�g��
			File[] fileList = new File(path).listFiles();

			if (fileList != null) {
				File file = null;
				BufferedReader br = null;
		        FileReader fr = null;
		        String line;
		        int j=0;

		        for (int i = 0; i < fileList.length; i++) {
		            try {
						//�t�@�C����ǂݍ���
						file = fileList[i];
						fr = new FileReader(file);
						br = new BufferedReader(fr);
						int line_counter = 0;

						//�Y���s������
						while((line = br.readLine()) != null) {
							line_counter++;

							if(line_counter == 15) {
								if(line=="0") {
									notAuthentificatededUsers[j]=file;
									j++;
								}
								break;
							}
						}
					}
		            catch(IOException e) {
						System.err.print("�F�؂Ɋւ��鏈���ŃG���[���������܂����F" + e);

					}
		            finally {
		            	try {
							fr.close();
							br.close();
						}
						catch (IOException e) {
							e.printStackTrace();
						}
		            }
		        }
			}
		}

		public void nextPage() {
			File file = null;
			BufferedReader br = null;
	        FileReader fr = null;
	        String line;

			pageAuthen++;

			try {
				//�t�@�C����ǂݍ���
				file = notAuthentificatededUsers[pageAuthen];
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				line = br.readLine(); //���̍s
				lUserNumberAuthen.setText(line);

				line = br.readLine(); //���̍s
				line = br.readLine(); //���̍s
				lUserNameAuthen.setText(line);

				//�Y���s������
				int line_counter = 3;
				while((line = br.readLine()) != null) {
					line_counter++;
					if(line_counter == 16) {
						lUserNameAuthen.setText(line);
						break;
					}
				}
			}
            catch(IOException e) {
				System.err.print("�F�؂Ɋւ��鏈���ŃG���[���������܂����F" + e);

			}
			finally {
            	try {
					fr.close();
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
            }

		}

		public void commitAuthen() {
			JPanel card=new JPanel();
			card.setLayout(null);

			JLabel lTitleAuthen = new JLabel("�F��");
			lTitleAuthen.setBounds(w/4,h/15,w/2,h/15);
			lTitleAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/10));
			lTitleAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lTitleAuthen);


	        lStudentCardAuthen.setBounds(w/10,h/3,9*w/10,h/5);
	        lStudentCardAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lStudentCardAuthen);

	        JLabel lNameAuthen = new JLabel("���O");
	        lNameAuthen.setBounds(w/10,7*h/15,w/5,h/15);
	        lNameAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        lNameAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lNameAuthen);

	        lUserNameAuthen.setBounds(2*w/5,7*h/15,3*w/5,h/15);
	        lUserNameAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        lUserNameAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lUserNameAuthen);

	        JLabel lNumberAuthen = new JLabel("�w�Дԍ�");
	        lNumberAuthen.setBounds(w/10,8*h/15,w/5,h/15);
	        lNumberAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        lNumberAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lNumberAuthen);

	        lUserNumberAuthen.setBounds(2*w/5,8*h/15,3*w/5,h/15);
	        lUserNumberAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        lUserNumberAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lUserNumberAuthen);

	        JLabel lLineIdAuthen = new JLabel("LINE��ID");
	        lLineIdAuthen.setBounds(w/10,9*h/15,w/5,h/15);
	        lLineIdAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        lLineIdAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lLineIdAuthen);

	        lUserLineIdAuthen.setBounds(2*w/5,9*h/15,3*w/5,h/15);
	        lUserLineIdAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        lUserLineIdAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lUserLineIdAuthen);

	        JButton bAcceptAuthen=new JButton("�F��");
	        bAcceptAuthen.setBounds(w/5,5*h/6,w/4,h/15);
	        bAcceptAuthen.addActionListener(this);
	        bAcceptAuthen.setActionCommand("�F��");
	        bAcceptAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        card.add(bAcceptAuthen);

	        JButton bRejectAuthen=new JButton("�p��");
	        bRejectAuthen.setBounds(11*w/20,5*h/6,w/4,h/15);
	        bRejectAuthen.addActionListener(this);
	        bRejectAuthen.setActionCommand("�p��");
	        bRejectAuthen.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        card.add(bRejectAuthen);

	        cardPanel.add(card,"Authen");
		}

		public void actionPerformed(ActionEvent ae) {
			String cmd=ae.getActionCommand();

			if(cmd=="�F��") {
				 BufferedReader br = null;
			        FileReader fr = null;
			        FileWriter fw = null;
			        String line;
			        StringBuffer strbuf = new StringBuffer("");

					try {
						//�t�@�C����ǂݍ���
						File file = notAuthentificatededUsers[pageAuthen];
						fr = new FileReader(file);
						br = new BufferedReader(fr);
						int line_counter = 0;

						//�Y���s������
						while((line = br.readLine()) != null) {
							line_counter++;
							if(line_counter == 15) {
								strbuf.append("2\n");
							}
							else {
								strbuf.append(line + "\n");
							}
						}

						//�Ō�܂œǂݍ���
						while((line = br.readLine()) != null) {
							strbuf.append(line + "\n");
						}

						//��������
						fw = new FileWriter(file);
						fw.write(strbuf.toString());
						readAllUserFiles();

					}
					catch(IOException e) {
						System.err.print("�F�؂Ɋւ��鏈���ŃG���[���������܂����F" + e);

					}
					if(pageAuthen==notAuthentificatededUsers.length-1) {
						this.dispose();
						//TODO �F�؃E�C���h�E�����������B�Ԉ���Ă�\��������
					}
					else {
						nextPage();
					}
			}
			else if(cmd=="�p��") {
				if(pageAuthen==notAuthentificatededUsers.length-1) {
					this.dispose();
					//TODO �F�؃E�C���h�E�����������B�Ԉ���Ă�\��������
				}
				else {
					nextPage();
				}
			}
		}
	}

	//������������N���X
	class searchUsers extends JFrame implements ActionListener,ChangeListener{
		JPanel cardPanel;
		CardLayout cardLayout;

		 JTextField tfStudentNumberSearch = new JTextField(20);

		 JLabel lMainPhotoUserInfo = new JLabel("");
	     JLabel[] lSubPhotoUserInfo = new JLabel[4];
	     JToggleButton tbDeleteUserInfo = new JToggleButton("No");

		 String[] items = {"���O","����","�w�N","�w��","�o�g","�T�[�N��","�","LINE��ID"};
		 int row = items.length;// �\�̍s��

	     JTable tTableUserInfo = new JTable(row,2); // �v���t�B�[���̕\

		public searchUsers(){

			super("���[�U�[����");
			cardPanel = new JPanel();
		    cardLayout = new CardLayout();
		    cardPanel.setLayout(cardLayout);

		    search();
		    aboutUser();

		    cardLayout.show(cardPanel,"search");
		    pack();
		    getContentPane().add(cardPanel, BorderLayout.CENTER);
		    setSize(w,h);
			setResizable(false);
		    setVisible(true);
		}

		public void search() {
			JPanel card=new JPanel();
			card.setLayout(null);

			JLabel lTItleSearch = new JLabel("���[�U����");
	        lTItleSearch.setBounds(w/4,h/15,w/2,h/15);
	        lTItleSearch.setFont(new Font("�l�r ����", Font.PLAIN, w/30));
	        lTItleSearch.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lTItleSearch);

			JLabel lStudentNumberSearch = new JLabel("�w�Дԍ�");
	        lStudentNumberSearch.setBounds(w/10,2*h/5,w/5,h/15);
	        lStudentNumberSearch.setFont(new Font("�l�r ����", Font.PLAIN, w/30));
	        lStudentNumberSearch.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lStudentNumberSearch);

	        tfStudentNumberSearch.setBounds(3*w/10,2*h/5,3*w/5,h/15);
	        tfStudentNumberSearch.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        card.add(tfStudentNumberSearch);

			JButton bSearchSearch=new JButton("����");
	        bSearchSearch.setBounds(w/4,2*h/3,w/2,h/15);
	        bSearchSearch.addActionListener(this);
	        bSearchSearch.setActionCommand("����");
	        bSearchSearch.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        card.add(bSearchSearch);


	        //����������ʂɖ��O�t���B���\�b�h���Ɠ�������Ȃ��Ă����v�����ǁA�����̂ق���������₷�������B
			cardPanel.add(card,"search");
		}

		public void aboutUser() {

			JPanel card = new JPanel();
			card.setLayout(null);

			ImageIcon iLeft=new ImageIcon("./img/left.jpeg");

			JButton bPrePage = new JButton(iLeft);
	        bPrePage.setBounds(w/14,h/30,w/11,h/20);
	        bPrePage.addActionListener(this);
	        bPrePage.setActionCommand("�߂�");
	        card.add(bPrePage);

	        JLabel lTitleUserInfo = new JLabel("���[�U���");
			lTitleUserInfo.setBounds(w/4,h/60,w/2,h/15);
			lTitleUserInfo.setFont(new Font("�l�r ����", Font.PLAIN, w/25));
			lTitleUserInfo.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lTitleUserInfo);

	        lMainPhotoUserInfo.setBounds(w/4,6*h/60,w/2,h/6);
	        lMainPhotoUserInfo.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        card.add(lMainPhotoUserInfo);

	        for(int i=0;i<4;i++) {
	        	lSubPhotoUserInfo[i] = new JLabel();
	        	lSubPhotoUserInfo[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
	            lSubPhotoUserInfo[i].setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	            card.add(lSubPhotoUserInfo[i]);
	        }

	        for(int i=0;i<row;i++) {
	        	tTableUserInfo.setValueAt(items[i], i, 0);
	        }

	        for(int i=0;i<row;i++) {
	        	tTableUserInfo.setValueAt("a", i, 1);
	        }
	        // �X�N���[���o�[
	        JScrollPane sp = new JScrollPane(tTableUserInfo);
			sp.setBounds(w/4,27*h/65,w/2,h/4);
			card.add(sp);

			// �A�J�E���g�폜
			/*JLabel lDeleteUserInfo = new JLabel("�A�J�E���g�폜");
			lDeleteUserInfo.setBounds(w/10,40*h/60,w/2,h/15);
			lDeleteUserInfo.setFont(new Font("�l�r ����", Font.PLAIN, w/25));
			lDeleteUserInfo.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lDeleteUserInfo);

	        tbDeleteUserInfo.setBounds(6*w/10,41*h/60,w/7,h/20);
	        tbDeleteUserInfo.addChangeListener(this);
	        tbDeleteUserInfo.setFont(new Font("�l�r ����", Font.PLAIN, w/35));
			card.add(tbDeleteUserInfo);*/

			JButton bDecideUserInfo=new JButton("BAN");
			bDecideUserInfo.setBounds(w/4,24*h/30,w/2,h/15);
			bDecideUserInfo.addActionListener(this);
			bDecideUserInfo.setActionCommand("BAN");
			bDecideUserInfo.setFont(new Font("�l�r ����", Font.PLAIN, w/20));
	        card.add(bDecideUserInfo);

			cardPanel.add(card,"UserInfo");
		}

		public void stateChanged(ChangeEvent e) {
			JToggleButton btn = (JToggleButton)e.getSource();
			if (btn.isSelected()) {
				btn.setText("Yes");
			} else {
				btn.setText("No");
			}
		}

		public void actionPerformed(ActionEvent ae) {
			String cmd=ae.getActionCommand();

			if(cmd=="����") {
				String studentNum=tfStudentNumberSearch.getText();
				BufferedReader br = null;
		        FileReader fr = null;
		        String line;

				try {
					//�t�@�C����ǂݍ���
					File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
					fr = new FileReader(file);
					br = new BufferedReader(fr);

					//�Y���s������
					line = br.readLine();
					line = br.readLine();
					for(int i=0;i<7;i++) {
						line = br.readLine();
						tTableUserInfo.setValueAt(line, i, 1);
					}

					int line_counter = 9;
					while((line = br.readLine()) != null) {
						line_counter++;
						if(line_counter == 16) {
							tTableUserInfo.setValueAt(line, 7, 1);
							break;
						}
					}
				}
				catch(IOException e) {
					System.err.print("���[�U�����Ɋւ��鏈���ŃG���[���������܂����F" + e);

				}
			}
			else if(cmd=="BAN") {
				deleteUser(tfStudentNumberSearch.getText());
				cardLayout.show(cardPanel,"search");
			}
			else if(cmd=="�߂�") {
				cardLayout.show(cardPanel,"search");
			}

		}
	}

}
