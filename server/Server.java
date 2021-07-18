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

	static PrintWriter out; //データ送信用オブジェクト
	static Receiver receiver; //データ受信用オブジェクト
	static ServerSocket ss;

	static UserInfo users[] = new UserInfo[1000];
	static HashMap<String, UserInfo> activeUsers =new HashMap<>();
	static int userFileNum = 0;	//ユーザファイル数

	static GroupInfo groups[] = new GroupInfo[1000];
	static HashMap<UUID, GroupInfo> activeGroups =new HashMap<>();
	static int groupFileNum = 0;	//ユーザファイル数

	//検索用
	static ArrayList<UserInfo> userlist;
	static ArrayList<UserInfo> user_buf;
	static ArrayList<GroupInfo> grouplist;
	static ArrayList<GroupInfo> group_buf;

	int w = 400;
	int h = 650;

	//コンストラクタ
	public Server(String title){
		setTitle(title);

		JLabel menu = new JLabel("サーバメニュー");
		JButton admit_button = new JButton("新規会員認証");
		admit_button.setPreferredSize(new Dimension(250, 35));
		JButton search_button = new JButton("会員検索");
		search_button.setPreferredSize(new Dimension(250, 35));

		JPanel p = new JPanel();
		p.add(menu);
		p.add(admit_button);
		p.add(search_button);

		JPanel p2 = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		p2.setLayout(layout);
		JButton end = new JButton("終了");
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

		//ファイルを読み込んでハッシュマップに追加
		readAllUserFiles();

		try {
			ss = new ServerSocket(50);
		} catch (IOException e) {
			System.err.println("サーバソケット作成時にエラーが発生しました: " + e);
		}
	}

	//メインメソッド
	public static void main(String args[]) {
		Server server = new Server("MS_Server");
		server.acceptClient();
	}

	//ボタン操作
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("新規会員認証")) {

			new Authentificate();
		}
		if(cmd.equals("会員検索")) {
			new searchUsers();
		}
		if(cmd.equals("終了")) {
			System.exit(0);
		}
	}

	//ユーザファイルを全て読み込み
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

	//ユーザファイル読み込み
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

						//画像の読み込み
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
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	//グループファイルを全て読み込み
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
		groupFileNum--; //要素と一致させる
	}

	//グループファイル読み込み
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

							//画像の読み込み
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
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
	}

	//データ受信用スレッド(内部クラス)
	class Receiver extends Thread {
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private PrintWriter out_buf; //送信先を記録
		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket){
			try{
				oos = new ObjectOutputStream(socket.getOutputStream()); //オブジェクトデータ送信用オブジェクトの用意
				ois = new ObjectInputStream(socket.getInputStream()); //オブジェクトデータ受信用オブジェクトの用意
				//sisr = new InputStreamReader(socket.getInputStream());
			} catch (IOException e) {
					System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}

		//メッセージの処理
		public void receiveMessage(String inputLine) {
				if (inputLine != null){ //データを受信したら
					String act[] = inputLine.split(","); //カンマの前後で文字列を分割
					System.out.println("receiveMessageが起動:"+inputLine);	//確認用

					try {
						switch(act[0]){
						case "lg": //新規登録する
							if(checkPassword(act[1],act[2] /*(学籍番号,パスワード)*/) == true) {
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

						case "ui": //ユーザ情報の取得
							try {
								oos.writeObject(activeUsers.get(act[1]));
								oos.flush();
							} catch (IOException e) {
								System.err.print("ユーザ情報送信時にエラーが発生しました：" + e);
							}
							break;

						case "us": //3人分送信
							try {
								oos.writeObject(sendUserInfo(Integer.parseInt(act[1])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("ユーザ情報送信時にエラーが発生しました：" + e);
							}
							break;

						case "uj": //ユーザ条件検索
							try {
								oos.writeObject(searchUsers(Integer.parseInt(act[1]), Integer.parseInt(act[2]), Integer.parseInt(act[3]), Integer.parseInt(act[4]), Integer.parseInt(act[5]), Integer.parseInt(act[6])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("ユーザ情報送信時にエラーが発生しました：" + e);
							}
							break;

						case "gi": //グループ情報の取得
							try {
								oos.writeObject(activeGroups.get(UUID.fromString(act[1])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("グループ情報送信時にエラーが発生しました：" + e);
							}
							break;

						case "gs": //3グループ分送信
							try {
								oos.writeObject(sendGroupInfo(Integer.parseInt(act[1])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("グループ情報送信時にエラーが発生しました：" + e);
							}
							break;

						case "gj": //グループ条件検索
							try {
								oos.writeObject(searchGroups(Integer.parseInt(act[1]), Integer.parseInt(act[2]), Integer.parseInt(act[3])));
								oos.flush();
							} catch (IOException e) {
								System.err.print("グループ情報送信時にエラーが発生しました：" + e);
							}
							break;

						case "ug": //ユーザにいいねを送る
							if(goodUser(act[0],act[1])) {
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}

							break;

						case "gg": //グループにいいねを送る
							if(goodGroup(act[0],act[1])) {
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						case "jg": //グループに参加
							if(joinGroup(act[1], act[2])){
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						case "rg": //グループ参加拒否
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
						System.err.print("オブジェクト受信時にエラーが発生しました：" + e);
					}
				}
		}

		// 内部クラス Receiverのメソッド
		public void run(){
			try {
				while(true) {
					try {
						Object inputObj = ois.readObject();
						//UserInfo型なら
						if(inputObj instanceof UserInfo) {
							UserInfo ui = new UserInfo();
							ui = (UserInfo)ois.readObject();
							//新規登録
							if(ui.state == 0) {
								signUp(ui);
							}
							//
						}

						//GroupInfo型なら
						else if(inputObj instanceof GroupInfo) {
							GroupInfo gi = new GroupInfo();
							gi = (GroupInfo)ois.readObject();
							//グループ作成
							if(gi.state == 0) {
								makeGroup(gi);
							}
						}

						//その他ならreceiveMessage()
						else {
							//br = new BufferedReader(sisr);
							//String inputLine = br.readLine();//データを一行分読み込む
							String inputLine = inputObj.toString();
							System.out.println(inputLine);	//確認用
							receiveMessage(inputLine);
						}

					} catch (ClassNotFoundException e) {
						System.err.print("オブジェクト受信時にエラーが発生しました：" + e);
					}
				}
			}catch(IOException e) {
				System.err.println("クライアントが切断しました");
			}
		}
	}

	//クライアントに接続
	public void acceptClient(){ //クライアントの接続(サーバの起動)
		try {
			while (true) {
				Socket socket = ss.accept(); //新規接続を受け付ける
				System.out.println("クライアントと接続しました．"); //テスト用出力
				out = new PrintWriter(socket.getOutputStream(), true);//データ送信オブジェクトを用意
				receiver = new Receiver(socket);//データ受信オブジェクト(スレッド)を用意
				receiver.start();//データ送信オブジェクト(スレッド)を起動
			}
		} catch (Exception e) {
			System.err.println("ソケット作成時にエラーが発生しました: " + e);
		}
	}

	//検索
	public static UserInfo[] searchUsers(int page, int gender, int grade, int faculty, int birth, int circle) {

		user_buf.clear();

		UserInfo res[] = new UserInfo[3];

		for(int i = 0; i < userlist.size(); i++){
			UserInfo user = userlist.get(i);

			//完全一致
		    if(user.gender == gender && user.grade == grade && user.faculty == faculty && user.birth == birth && user.circle == circle) {
		    	user_buf.add(user);
		    }

		}

		//候補なしならnullを返す
		if(user_buf == null) return null;

		//候補がいる場合
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

	//UserInfo送信
	public static UserInfo[] sendUserInfo(int page) {
		UserInfo res[] = new UserInfo[3];

		//なしならnullを返す
		if(users == null) return null;

		//ユーザがいる場合
		else {
			if(users[3*page - 2] != null) res[0] = users[3*page - 2];
			else res[0] = null;

			if(users[3*page - 1] != null) res[1] = users[3*page - 1];
			else res[1] = null;

			if(users[3*page] != null) res[2] = users[3*page];
			else res[2] = null;
		}

		//そのページにユーザがいなければnullを返す
		if(res[0] == null && res[1] == null && res[2] == null) return null;

		return res;
	}

	//グループ検索
	public GroupInfo[] searchGroups(int page, int purpose, int num) {

		group_buf.clear();

		GroupInfo res[] = new GroupInfo[3];

		for(int i = 0; i < grouplist.size(); i++){
			GroupInfo group = grouplist.get(i);

			//完全一致
		    if(group.purpose == purpose || group.numberOfMember == num) {
		    	group_buf.add(group);
		    }

		}

		//候補なしならnullを返す
		if(group_buf == null) return null;

		//候補がいる場合
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

	//GroupInfo送信
	public GroupInfo[] sendGroupInfo(int page) {
		GroupInfo res[] = new GroupInfo[3];

		//なしならnullを返す
		if(groups == null) return null;

		//ユーザがいる場合
		else {
			if(groups[3*page - 2] != null) res[0] = groups[3*page - 2];
			else res[0] = null;

			if(groups[3*page - 1] != null) res[1] = groups[3*page - 1];
			else res[1] = null;

			if(groups[3*page] != null) res[2] = groups[3*page];
			else res[2] = null;
		}

		//そのページにユーザがいなければnullを返す
		if(res[0] == null && res[1] == null && res[2] == null) return null;

		return res;
	}

	//パスワードチェック
	public static boolean checkPassword(String num, String pass) {
		String path = System.getProperty("user.dir") + "\\ID"; //ディレクトリ
		File LoginFile = new File(path + "\\" + num + ".txt"); //ユーザファイル
		File dir = new File(path);
		BufferedReader br = null;
		FileReader fr = null;

		//ディレクトリまたはユーザファイルが存在しない場合
		if(!dir.exists() || !LoginFile.exists()) return false;
		//ディレクトリおよびユーザファイルが存在する場合
		else {
			try {
				fr = new FileReader(LoginFile);
				br = new BufferedReader(fr);
				String str = br.readLine();				//1行目読み込み
				br.close();
				String res[] = str.split(" ");			//空白で分割

				//もしパスワードが一致していればtrue
				if(res[1] == pass) return true;
				//そうでなければfalse
				else return false;

			}catch(IOException e) {
				System.err.println("エラーが発生しました：" + e);
				return false;
			}
		}
	}

	//新規登録
	public static void signUp(UserInfo ui) {
		String path = System.getProperty("user.dir") + "\\ID"; //ディレクトリ
		File LoginFile = new File(path + "\\" + ui.studentNumber + ".txt"); //ユーザファイル
		File dir = new File(path);
		File image_dir = new File(path + "\\images");
		File image_user_dir = new File(path + "\\images" + "\\" + ui.studentNumber);
		File studentCard = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_card.png");
		File main_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_main.png");
		File sub1_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub1.png");
		File sub2_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub2.png");
		File sub3_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub3.png");
		File sub4_image = new File(path + "\\images" + "\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub4.png");

		//ディレクトリの作成
		if(!dir.exists()) {
			dir.mkdir();
			image_dir.mkdir();
		}

		//ファイルの作成
		if(!LoginFile.exists()) {
			try {
				LoginFile.createNewFile();
				image_user_dir.mkdir();
			} catch (IOException e) {
				System.err.println("ファイル作成時に予期せぬエラーが発生しました");
				return;
			}
		}

		try {
			//ユーザ情報ファイルを作成
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
					 /*学籍番号*/"\n" +
					 /*学籍番号*/"\n" +
					 /*学籍番号*/"\n" +
					 /*UUID*/"\n" +
					 /*UUID*/"\n" +
					 ui.isAuthentificated +"\n"+
					 ui.lineId + "\n" +
					 ui.isPublic + "\n"
					 );
			fw.close();

			//画像を保存
			ImageIO.write(ui.studentCard, "png", studentCard);
			ImageIO.write(ui.mainPhoto, "png", main_image);
			ImageIO.write(ui.subPhoto[0], "png", sub1_image);
			ImageIO.write(ui.subPhoto[1], "png", sub2_image);
			ImageIO.write(ui.subPhoto[2], "png", sub3_image);
			ImageIO.write(ui.subPhoto[3], "png", sub4_image);

			//配列に追加
			readUserFile(LoginFile);

		} catch (IOException e) {
			System.err.print("新規登録の際にエラーが発生しました：" + e);
			return;
		}
	}

	//ユーザの情報変更
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
							/*学籍番号*/"\n" +
							/*学籍番号*/"\n" +
							/*学籍番号*/"\n" +
							/*UUID*/"\n" +
							/*UUID*/"\n" +
							ui.isAuthentificated +"\n"+
							ui.lineId + "\n" +
							ui.isPublic + "\n");


		try {
			//ファイルを読み込み
			File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			pw = new PrintWriter(file);

			while((line = br.readLine())!=null)
				pw.println(line.replaceAll(line, newinfo));



		}catch(IOException e) {
			System.err.print("ユーザ情報変更に関する処理でエラーが発生しました：" + e);
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

	//グループの情報変更
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
			//ファイルを読み込み
			File file = new File(System.getProperty("user.dir") + "\\Group\\" + uuid + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			pw = new PrintWriter(file);

			while((line = br.readLine())!=null)
				line = line.replaceAll(line, newinfo);
		}catch(IOException e) {
			System.err.print("グループ情報変更に関する処理でエラーが発生しました：" + e);
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

	//グループ作成
	public static void makeGroup(GroupInfo gi) {

		String path = System.getProperty("user.dir") + "\\Group"; //ディレクトリ
		gi.groupNumber = UUID.randomUUID(); //UUIDの作成
		File GroupFile = new File(path + "\\" + gi.groupNumber.toString() + ".txt");
		File dir = new File(path);
		File image_dir = new File(path + "\\images");
		File main_image = new File(path + "\\images" + "\\" + gi.groupNumber + "_main.png");

		//ディレクトリの作成
		if(!dir.exists()) {
			dir.mkdir();
			image_dir.mkdir();
		}

		//ファイルの作成
		if(!GroupFile.exists()) {
			try {
				GroupFile.createNewFile();
			} catch (IOException e) {
				System.err.println("ファイル作成時に予期せぬエラーが発生しました");
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
			//グループ情報ファイルを作成
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

			//画像を保存
			ImageIO.write(gi.mainPhoto, "png", main_image);

			//再度読み込み
			readAllUserFiles();

		} catch (IOException e) {
			System.err.print("新規登録の際にエラーが発生しました：" + e);
			return;
		}
	}

	//グループ作成時に招待
	public static void inviteUsers(String studentNum, String uuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

		try {
			//ファイルを読み込み
			File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 14) break;
				strbuf.append(line + "\n");
			}

			//誘われているグループ(13行目)に追加
			if(line == "") { 	//今まで誘われていなかった場合
				strbuf.append(uuid + "\n");
			}else {				//すでに誘われていた場合
				strbuf.append(line + " " + uuid + "\n");
			}

			//最後まで読み込み
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//参加したグループが全員集まったか確認
			judgeAllGathered(uuid);

			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			//再度読み込み
			readAllUserFiles();
			readAllGroupFiles();

		}catch(IOException e) {
			System.err.print("グループ参加に関する処理でエラーが発生しました：" + e);
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

	//グループ参加
	public static boolean joinGroup(String studentNum, String uuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

		try {
			//ファイルを読み込み
			File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 13) break;
				strbuf.append(line + "\n");
			}

			//参加しているグループ(13行目)に追加
			if(line == "") { //今まで参加してなかった場合
				strbuf.append(uuid + "\n");
			}else {				//すでに参加したことがある場合
				strbuf.append(line + " " + uuid + "\n");
			}

			line = br.readLine(); //次の行

			//誘われているグループ(14行目)から削除
			line = line.replace(uuid, ""); //UUIDを削除
			line = line.replace("  "," "); //並んだ空白を削除
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
			strbuf.append(line + "\n");

			//最後まで読み込み
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//参加したグループが全員集まったか確認
			judgeAllGathered(uuid);

			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			//再度読み込み
			readAllUserFiles();

		}catch(IOException e) {
			System.err.print("グループ参加に関する処理でエラーが発生しました：" + e);
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

	//全員集まったか
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

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 8) break;
				strbuf.append(line + "\n");
			}

			String students[] = line.split(" ");
			int index = students.length + 1; //参加している人数

			strbuf.append(line + "\n");

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 11) break;
				strbuf.append(line + "\n");
			}

			boolean judge = false;
			if(index == Integer.parseInt(line)) judge = true;
			strbuf.append(line + "\n");

			line = br.readLine(); //次の行
			if(judge) {
				strbuf.append("true\n");
			}else {
				strbuf.append(line + "\n");
			}

			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			//再度読み込み
			readAllUserFiles();
			readAllGroupFiles();

        }catch(IOException e) {

        }
	}

	//グループ参加拒否・グループ削除
	public static boolean deleteGroup(String uuid) {
        BufferedReader br = null;
        FileReader fr = null;
        String line;

		try {
			//ファイルを読み込み
			File file = new File(System.getProperty("user.dir") + "\\Group\\" + uuid + ".txt");
			File main_image = new File(System.getProperty("user.dir") + "\\Group\\images\\" + uuid + "_main.png");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 4) break;
			}

			//いいねしたグループ達
			String Groups[] = line.split(" ");

			//ここでいいねを送った・送られたグループファイルのいいねからこのグループを削除
			for(int i = 0; i < Groups.length; i++) //refuseGood(Groups[i],uuid); //いいねしたグループのファイルから自分が送ったいいねを消す(いいねされたグループ,自分)
			for(int j = 0; j < Groups.length; j++) //refuseGood(uuid,Groups[i]); //いいねされたグループのファイルから自分にいいねした記録を消す(自分,いいねしたグループ)

			//ホストユーザのグループに関する記録を削除
			deleteGroupLog(line,uuid);

			//次の行
			line = br.readLine();

			//文字列をスペースで分割
			String nonhoststudents[] = line.split(" ");

			//人数の行までスキップ
			for(int i = 0; i<3; i++) {
				line = br.readLine();
			}

			//グループの人数を記録
			int num = Integer.parseInt(line);

			//ユーザ情報からグループに関する記録を削除
			for(int i = 0; i < num - 1; i++) {
				deleteGroupLog(nonhoststudents[num],uuid);
			}

			//グループファイル削除
			file.delete();

			//画像削除
			main_image.delete();

			//再度読み込み
			readAllUserFiles();
			readAllGroupFiles();

		}catch(IOException e) {
			System.err.print("グループ参加拒否に関する処理でエラーが発生しました：" + e);
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

	//ユーザのグループのログを削除
	public static void deleteGroupLog(String studentNum, String uuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		String line;
		StringBuffer strbuf = new StringBuffer("");

			try {
				//ファイルを読み込み
				File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				int line_counter = 0;

				//該当行を検索
				while((line = br.readLine()) != null) {
					line_counter++;
					if(line_counter == 13) break;
					strbuf.append(line + "\n");
				}

				//すでに参加している場合
				if(line.contains(uuid)) {
					line = line.replace(uuid, ""); //UUIDを削除
					line = line.replace("  "," "); //並んだ空白を削除
					if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
					if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
					strbuf.append(line + "\n");
				}

				line = br.readLine(); //次の行

				//誘われている段階の場合
				if(line.contains(uuid)) {
					line = line.replace(uuid, ""); //UUIDを削除
					line = line.replace("  "," "); //並んだ空白を削除
					if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
					if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
					strbuf.append(line + "\n");
				}

				//最後まで読み込み
				while((line = br.readLine()) != null) {
					strbuf.append(line + "\n");
				}

				//書き込み
				fw = new FileWriter(file);
				fw.write(strbuf.toString());

				//再度読み込み
				readAllUserFiles();
				readAllGroupFiles();

			}catch(IOException e) {
				System.err.print("グループ招待削除に関する処理でエラーが発生しました：" + e);
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

	//ユーザ削除
	public static void deleteUser(String studentNum) {
		BufferedReader br = null;
		FileReader fr = null;
		File image_user_dir = new File(System.getProperty("user.dir") + "\\ID\\images\\" + studentNum);
		String line;
		String SentGoodStudents[] = new String[100]; //いいねをしたひと、とりあえず100人まで
		String BeingSentGoodStudents[] = new String[100]; //いいねをくれた人、100人
		String MatchingStudents[] = new String[100]; //マッチングした人、100人
		String Groups[] = new String[100]; //参加してるグループ、とりあえず100個まで
		String InvitedGroups[] = new String[100]; //誘われてるグループ、とりあえず100個まで

			try {
				//ファイルを読み込み
				File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				int line_counter = 0;

				//該当行を検索
				while((line = br.readLine()) != null) {
					line_counter++;
					if(line_counter == 10) break;
				}

				//空白で分割して保存、いいねを送ったひと
				SentGoodStudents = line.split(" ");
				//ここでいいね削除

				//次の行
				line = br.readLine();

				//空白で分割して保存、いいねをくれた人
				BeingSentGoodStudents = line.split(" ");
				//ここでいいね削除

				//次の行
				line = br.readLine();

				//空白で分割して保存、マッチングした人
				MatchingStudents = line.split(" ");
				//deleteMatching(); マッチングしてる相手を消す

				//次の行
				line = br.readLine();

				//空白で分割して保存、参加してるグループ
				Groups = line.split(" ");
				for(int i=0; i<Groups.length; i++) {
					deleteGroup(Groups[i]);
				}

				//次の行
				line = br.readLine();

				//空白で分割して保存、誘われてるグループ
				InvitedGroups = line.split(" ");
				for(int i=0; i<InvitedGroups.length; i++) {
					deleteGroup(InvitedGroups[i]);
				}

//使わないかも
//				//配列のインデックスを取得
//				int index = ArrayUtils.indexOf(users,activeUsers.get(studentNum));
//
//				//配列をずらす
//				for(int i=index; i<=userFileNum - 1; i++) {
//					users[i] = users[i+1];
//				}
//				users[userFileNum] = null;
//				userFileNum--;

				//削除
				file.delete();
				image_user_dir.delete();

				//再度読み込み
				readAllUserFiles();
				readAllGroupFiles();

			}catch(IOException e) {
				System.err.print("ユーザ削除に関する処理でエラーが発生しました：" + e);
			}finally {
				try {
					fr.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

	//いいね
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
    			if(check!=-1) {    //いいねされてた
    				str[10] = str[10].replace(your_num,"");
    				flag = 1;
    			}
    			else
    				str[9] += " your_num";

    			if(flag == 1)   //マッチした
    				str[11] += " your_num";

   			FileWriter filewriter = new FileWriter(file);  //書き換え
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
   			if(flag == 1) { //マッチしたら
	   			str[9] = str[9].replace(your_num,"");
	   			str[11] += " your_num";
   			}
   			else {
	   			str[10] += " your_num";
   			}

   			FileWriter filewriter2 = new FileWriter(file2);  //書き換え
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

	//グループいいね

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
 			if(check!=-1) {    //いいねされてた
 				str[4] = str[10].replace(your_num,"");
 				flag = 1;
 			}
 			else
 				str[3] += " your_num";

 			if(flag == 1)   //マッチした
 				str[5] += " your_num";

 			FileWriter filewriter = new FileWriter(file);  //書き換え
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
 			if(flag == 1) { //マッチしたら
 				str[9] = str[9].replace(your_num,"");
 				str[11] += " your_num";
 			}
 			else {
 				str[10] += " your_num";
 			}

 			FileWriter filewriter2 = new FileWriter(file2);  //書き換え
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

	//ユーザいいね拒否
	public static void badUser(String my_num, String your_num) {
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

 				str[10] = str[10].replace(your_num,""); //縺輔ｌ縺溯｡後°繧画ｶ医☆
 				str[10] = str[10].replace("  "," "); //荳ｦ繧薙□遨ｺ逋ｽ繧貞炎髯､
				if(str[10].charAt(0) == ' ')  str[10] = str[10].substring(1, str[10].length()); //蜈磯�ｭ縺ｮ遨ｺ逋ｽ繧貞炎髯､
				if(str[10].charAt(str[10].length()) == ' ')  str[10] = str[10].substring(1, str[10].length()-1); //譛�蠕後�ｮ遨ｺ逋ｽ繧貞炎髯､

			FileWriter filewriter = new FileWriter(file);  //譖ｸ縺肴鋤縺�
			BufferedWriter bw = new BufferedWriter(filewriter);
			for (int i=0;i<17;i++) {
	   			bw.write(str[i]);
	   			bw.newLine();
			}

			readAllUserFiles();

		}catch(IOException e) {
			System.out.println(e);
		}

	}

	//グループいいね拒否
	public static void badGroup(String my_num, String your_num) {
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

	 				str[4] = str[4].replace(your_num,""); //縺輔ｌ縺溯｡後°繧画ｶ医☆
	 				str[4] = str[4].replace("  "," "); //荳ｦ繧薙□遨ｺ逋ｽ繧貞炎髯､
					if(str[4].charAt(0) == ' ')  str[4] = str[4].substring(1, str[4].length()); //蜈磯�ｭ縺ｮ遨ｺ逋ｽ繧貞炎髯､
					if(str[4].charAt(str[4].length()) == ' ')  str[4] = str[4].substring(1, str[4].length()-1); //譛�蠕後�ｮ遨ｺ逋ｽ繧貞炎髯､

				FileWriter filewriter = new FileWriter(file);  //譖ｸ縺肴鋤縺�
				BufferedWriter bw = new BufferedWriter(filewriter);
				for (int i=0;i<11;i++) {
		   			bw.write(str[i]);
		   			bw.newLine();
				}

				readAllUserFiles();

			}catch(IOException e) {
				System.out.println(e);
			}

		}

	//認証内部クラス
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

			super("ユーザー認証");
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
			String path = System.getProperty("user.dir") + "\\ID"; //ディレクトリ
			File[] fileList = new File(path).listFiles();

			if (fileList != null) {
				File file = null;
				BufferedReader br = null;
		        FileReader fr = null;
		        String line;
		        int j=0;

		        for (int i = 0; i < fileList.length; i++) {
		            try {
						//ファイルを読み込み
						file = fileList[i];
						fr = new FileReader(file);
						br = new BufferedReader(fr);
						int line_counter = 0;

						//該当行を検索
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
						System.err.print("認証に関する処理でエラーが発生しました：" + e);

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
				//ファイルを読み込み
				file = notAuthentificatededUsers[pageAuthen];
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				line = br.readLine(); //次の行
				lUserNumberAuthen.setText(line);

				line = br.readLine(); //次の行
				line = br.readLine(); //次の行
				lUserNameAuthen.setText(line);

				//該当行を検索
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
				System.err.print("認証に関する処理でエラーが発生しました：" + e);

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

			JLabel lTitleAuthen = new JLabel("認証");
			lTitleAuthen.setBounds(w/4,h/15,w/2,h/15);
			lTitleAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/10));
			lTitleAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lTitleAuthen);


	        lStudentCardAuthen.setBounds(w/10,h/3,9*w/10,h/5);
	        lStudentCardAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lStudentCardAuthen);

	        JLabel lNameAuthen = new JLabel("名前");
	        lNameAuthen.setBounds(w/10,7*h/15,w/5,h/15);
	        lNameAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lNameAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lNameAuthen);

	        lUserNameAuthen.setBounds(2*w/5,7*h/15,3*w/5,h/15);
	        lUserNameAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lUserNameAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lUserNameAuthen);

	        JLabel lNumberAuthen = new JLabel("学籍番号");
	        lNumberAuthen.setBounds(w/10,8*h/15,w/5,h/15);
	        lNumberAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lNumberAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lNumberAuthen);

	        lUserNumberAuthen.setBounds(2*w/5,8*h/15,3*w/5,h/15);
	        lUserNumberAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lUserNumberAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lUserNumberAuthen);

	        JLabel lLineIdAuthen = new JLabel("LINEのID");
	        lLineIdAuthen.setBounds(w/10,9*h/15,w/5,h/15);
	        lLineIdAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lLineIdAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lLineIdAuthen);

	        lUserLineIdAuthen.setBounds(2*w/5,9*h/15,3*w/5,h/15);
	        lUserLineIdAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lUserLineIdAuthen.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lUserLineIdAuthen);

	        JButton bAcceptAuthen=new JButton("認証");
	        bAcceptAuthen.setBounds(w/5,5*h/6,w/4,h/15);
	        bAcceptAuthen.addActionListener(this);
	        bAcceptAuthen.setActionCommand("認証");
	        bAcceptAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        card.add(bAcceptAuthen);

	        JButton bRejectAuthen=new JButton("却下");
	        bRejectAuthen.setBounds(11*w/20,5*h/6,w/4,h/15);
	        bRejectAuthen.addActionListener(this);
	        bRejectAuthen.setActionCommand("却下");
	        bRejectAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        card.add(bRejectAuthen);

	        cardPanel.add(card,"Authen");
		}

		public void actionPerformed(ActionEvent ae) {
			String cmd=ae.getActionCommand();

			if(cmd=="認証") {
				 BufferedReader br = null;
			        FileReader fr = null;
			        FileWriter fw = null;
			        String line;
			        StringBuffer strbuf = new StringBuffer("");

					try {
						//ファイルを読み込み
						File file = notAuthentificatededUsers[pageAuthen];
						fr = new FileReader(file);
						br = new BufferedReader(fr);
						int line_counter = 0;

						//該当行を検索
						while((line = br.readLine()) != null) {
							line_counter++;
							if(line_counter == 15) {
								strbuf.append("2\n");
							}
							else {
								strbuf.append(line + "\n");
							}
						}

						//最後まで読み込み
						while((line = br.readLine()) != null) {
							strbuf.append(line + "\n");
						}

						//書き込み
						fw = new FileWriter(file);
						fw.write(strbuf.toString());
						readAllUserFiles();

					}
					catch(IOException e) {
						System.err.print("認証に関する処理でエラーが発生しました：" + e);

					}
					if(pageAuthen==notAuthentificatededUsers.length-1) {
						this.dispose();
						//TODO 認証ウインドウだけ閉じたい。間違ってる可能性が高い
					}
					else {
						nextPage();
					}
			}
			else if(cmd=="却下") {
				if(pageAuthen==notAuthentificatededUsers.length-1) {
					this.dispose();
					//TODO 認証ウインドウだけ閉じたい。間違ってる可能性が高い
				}
				else {
					nextPage();
				}
			}
		}
	}

	//会員検索内部クラス
	class searchUsers extends JFrame implements ActionListener,ChangeListener{
		JPanel cardPanel;
		CardLayout cardLayout;

		 JTextField tfStudentNumberSearch = new JTextField(20);

		 JLabel lMainPhotoUserInfo = new JLabel("");
	     JLabel[] lSubPhotoUserInfo = new JLabel[4];
	     JToggleButton tbDeleteUserInfo = new JToggleButton("No");

		 String[] items = {"名前","性別","学年","学部","出身","サークル","趣味","LINEのID"};
		 int row = items.length;// 表の行数

	     JTable tTableUserInfo = new JTable(row,2); // プロフィールの表

		public searchUsers(){

			super("ユーザー検索");
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

			JLabel lTItleSearch = new JLabel("ユーザ検索");
	        lTItleSearch.setBounds(w/4,h/15,w/2,h/15);
	        lTItleSearch.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
	        lTItleSearch.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lTItleSearch);

			JLabel lStudentNumberSearch = new JLabel("学籍番号");
	        lStudentNumberSearch.setBounds(w/10,2*h/5,w/5,h/15);
	        lStudentNumberSearch.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
	        lStudentNumberSearch.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lStudentNumberSearch);

	        tfStudentNumberSearch.setBounds(3*w/10,2*h/5,3*w/5,h/15);
	        tfStudentNumberSearch.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        card.add(tfStudentNumberSearch);

			JButton bSearchSearch=new JButton("検索");
	        bSearchSearch.setBounds(w/4,2*h/3,w/2,h/15);
	        bSearchSearch.addActionListener(this);
	        bSearchSearch.setActionCommand("検索");
	        bSearchSearch.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        card.add(bSearchSearch);


	        //自分が作る画面に名前付け。メソッド名と同じじゃなくても大丈夫だけど、同じのほうが分かりやすいかも。
			cardPanel.add(card,"search");
		}

		public void aboutUser() {

			JPanel card = new JPanel();
			card.setLayout(null);

			ImageIcon iLeft=new ImageIcon("./img/left.jpeg");

			JButton bPrePage = new JButton(iLeft);
	        bPrePage.setBounds(w/14,h/30,w/11,h/20);
	        bPrePage.addActionListener(this);
	        bPrePage.setActionCommand("戻る");
	        card.add(bPrePage);

	        JLabel lTitleUserInfo = new JLabel("ユーザ情報");
			lTitleUserInfo.setBounds(w/4,h/60,w/2,h/15);
			lTitleUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
			lTitleUserInfo.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lTitleUserInfo);

	        lMainPhotoUserInfo.setBounds(w/4,6*h/60,w/2,h/6);
	        lMainPhotoUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        card.add(lMainPhotoUserInfo);

	        for(int i=0;i<4;i++) {
	        	lSubPhotoUserInfo[i] = new JLabel();
	        	lSubPhotoUserInfo[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
	            lSubPhotoUserInfo[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	            card.add(lSubPhotoUserInfo[i]);
	        }

	        for(int i=0;i<row;i++) {
	        	tTableUserInfo.setValueAt(items[i], i, 0);
	        }

	        for(int i=0;i<row;i++) {
	        	tTableUserInfo.setValueAt("a", i, 1);
	        }
	        // スクロールバー
	        JScrollPane sp = new JScrollPane(tTableUserInfo);
			sp.setBounds(w/4,27*h/65,w/2,h/4);
			card.add(sp);

			// アカウント削除
			/*JLabel lDeleteUserInfo = new JLabel("アカウント削除");
			lDeleteUserInfo.setBounds(w/10,40*h/60,w/2,h/15);
			lDeleteUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
			lDeleteUserInfo.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lDeleteUserInfo);

	        tbDeleteUserInfo.setBounds(6*w/10,41*h/60,w/7,h/20);
	        tbDeleteUserInfo.addChangeListener(this);
	        tbDeleteUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/35));
			card.add(tbDeleteUserInfo);*/

			JButton bDecideUserInfo=new JButton("BAN");
			bDecideUserInfo.setBounds(w/4,24*h/30,w/2,h/15);
			bDecideUserInfo.addActionListener(this);
			bDecideUserInfo.setActionCommand("BAN");
			bDecideUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
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

			if(cmd=="検索") {
				String studentNum=tfStudentNumberSearch.getText();
				BufferedReader br = null;
		        FileReader fr = null;
		        String line;

				try {
					//ファイルを読み込み
					File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
					fr = new FileReader(file);
					br = new BufferedReader(fr);

					//該当行を検索
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
					System.err.print("ユーザ検索に関する処理でエラーが発生しました：" + e);

				}
			}
			else if(cmd=="BAN") {
				deleteUser(tfStudentNumberSearch.getText());
				cardLayout.show(cardPanel,"search");
			}
			else if(cmd=="戻る") {
				cardLayout.show(cardPanel,"search");
			}

		}
	}

}
