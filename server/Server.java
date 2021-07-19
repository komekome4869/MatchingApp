import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.BufferedReader;
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
import java.util.Arrays;
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

	static UserInfo[] users = new UserInfo[1000];
	static HashMap<String, UserInfo> activeUsers =new HashMap<>();
	static int userFileNum = 0;	//ユーザファイル数

	static GroupInfo[] groups = new GroupInfo[1000];
	static HashMap<UUID, GroupInfo> activeGroups =new HashMap<>();
	static int groupFileNum = 0;	//ユーザファイル数

	//検索用
	static ArrayList<UserInfo> userlist;
	static ArrayList<UserInfo> user_buf = new ArrayList<UserInfo>();
	static ArrayList<GroupInfo> grouplist;
	static ArrayList<GroupInfo> group_buf = new ArrayList<GroupInfo>();

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
		readAllGroupFiles();

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
		users = new UserInfo[1000];
		activeUsers.clear();

		File dir = new File(System.getProperty("user.dir") + "\\ID");
		File image_dir = new File(System.getProperty("user.dir") + "\\ID\\images");

		if(!dir.exists()) dir.mkdir();
		if(!image_dir.exists()) image_dir.mkdir();

		File[] files = dir.listFiles();
		if( files == null ) {
			return;
		}else{
			for( File file : files ) {
				if( !file.exists() )
					continue;
				else if( file.isDirectory() )
					continue;
				else if( file.isFile() ) {
					readUserFile(file);
					userFileNum++;
				}
			}
			//ユーザ情報のリストを作成
			userlist = new ArrayList<UserInfo>(Arrays.asList(users));
			System.out.println(userlist);
			userFileNum--;
		}
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
			users[userFileNum] = new UserInfo();

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

						users[userFileNum].setStudentCard(card);
						users[userFileNum].setMainPhoto(main);
						users[userFileNum].setSubPhoto(sub1,0);
						users[userFileNum].setSubPhoto(sub2,1);
						users[userFileNum].setSubPhoto(sub3,2);
						users[userFileNum].setSubPhoto(sub4,3);
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
						if(line.length()>2) {
							System.out.println(line+"a");
							String sendStudents[] = line.split(" ");
							//System.out.println(line+"a");
							System.out.println(sendStudents[0]+"a");
							for(int i=0; i<sendStudents.length; i++) {
								users[userFileNum].sendGood[i] = Integer.parseInt(sendStudents[i]);
							}
						}
						break;

					case 11 :
						if(line.length()>2) {
							String receiveStudents[] = line.split(" ");
							for(int i=0; i<receiveStudents.length; i++) {
								users[userFileNum].receiveGood[i] = Integer.parseInt(receiveStudents[i]);
							}
						}
						break;

					case 12 :
						if(line.length()>2) {
							String matchingStudents[] = line.split(" ");
							for(int i=0; i<matchingStudents.length; i++) {
								users[userFileNum].matchedUser[i] = Integer.parseInt(matchingStudents[i]);
							}
						}
						break;

					case 13 :
						if(line.length()>2) {
							String joiningGroups[] = line.split(" ");
							for(int i=0; i<joiningGroups.length; i++) {
								users[userFileNum].joiningGroup[i] = UUID.fromString(joiningGroups[i]);
							}
						}
						break;

					case 14 :
						if(line.length()>2) {
							String invitedGroups[] = line.split(" ");
							for(int i=0; i<invitedGroups.length; i++) {
								users[userFileNum].invitedGroup[i] = UUID.fromString(invitedGroups[i]);
							}
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

				//userlist.add(users[userFileNum]);
				//System.out.println("a:"+users[userFileNum]+userFileNum);
				activeUsers.put(String.valueOf(users[userFileNum].studentNumber),users[userFileNum]);

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
				groupFileNum++;
			}
		}
		//グループのリストを作成
		if(groups!=null) {
			grouplist = new ArrayList<GroupInfo>(Arrays.asList(groups));
			System.out.println("grouplist"+grouplist);
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

							groups[groupFileNum].setMainPhoto(main);
									;
							break;

						case 2 :
							groups[groupFileNum].name = line;
							break;

						case 3 :
							groups[groupFileNum].relation = line;
							break;

						case 4 :
							if(line.length()>2) {
								String sendGood[] = line.split(" ");
								for(int i=0; i<sendGood.length; i++) {
									groups[groupFileNum].sendGood[i] = UUID.fromString(sendGood[i]);
								}
							}
							break;

						case 5 :
							if(line.length()>2) {
								String receiveGood[] = line.split(" ");
								for(int i=0; i<receiveGood.length; i++) {
									groups[groupFileNum].receiveGood[i] = UUID.fromString(receiveGood[i]);
								}
							}
							break;

						case 6 :
							if(line.length()>2) {
								String matchedGood[] = line.split(" ");
								for(int i=0; i<matchedGood.length; i++) {
									groups[groupFileNum].matchedGroup[i] = UUID.fromString(matchedGood[i]);
								}
							}
							break;

						case 7 :
							groups[groupFileNum].hostUser = Integer.parseInt(line);
							break;

						case 8 :
							if(line.length()>2) {
								String nonhostUser[] = line.split(" ");
								for(int i=0; i<nonhostUser.length; i++) {
									groups[groupFileNum].nonhostUser[i] = Integer.parseInt(nonhostUser[i]);
								}
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

					//grouplist.add(groups[groupFileNum]);
					activeGroups.put(groups[userFileNum].groupNumber, groups[groupFileNum]);

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
		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket){
			try{
				oos = new ObjectOutputStream(socket.getOutputStream()); //オブジェクトデータ送信用オブジェクトの用意
				ois = new ObjectInputStream(socket.getInputStream()); //オブジェクトデータ受信用オブジェクトの用意
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
								System.err.print("ユーザ情報送信時にエラーが発生しました：" + e);//TODO
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
							if(goodUser(act[1],act[2])) {
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
							if(joinGroup(act[1], act[2])){//久保田が書き換え
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

						case "ud": //ユーザ情報削除
							if(deleteUser(act[1])){
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						case "gd": //グループ情報削除
							if(deleteGroup(act[1])){
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						case "ur": //いいね拒否
							if(deleteReceivedGood(act[1], act[2])){
								oos.writeObject("1");
								oos.flush();
							}else {
								oos.writeObject("0");
								oos.flush();
							}
							break;

						case "gr": //グループいいね拒否
							if(deleteReceivedGroupGood(act[1], act[2])){
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
						System.out.println("データを受信しました");

						//UserInfo型なら
						if(inputObj instanceof UserInfo) {
							UserInfo ui = new UserInfo();
							ui = (UserInfo)inputObj;
							ui.setStudentCard(ui.getStudentCard());//TODO

							//新規登録
							if(ui.state == 0) {
								try{
									signUp(ui);
									oos.writeObject("1");
									oos.flush();
								}catch(IOException e) {
									oos.writeObject("0");
									oos.flush();
									System.err.print("サインアップでエラーが発生しました：" + e);
								}
							}
							//プロフ変更
							if(ui.state == 1) {
								try{
									changeUserInfo(ui);
									oos.writeObject("1");
									oos.flush();
								}catch(IOException e) {
									oos.writeObject("0");
									oos.flush();
									System.err.print("ユーザ情報変更時にエラーが発生しました：" + e);
								}
							}
						}

						//GroupInfo型なら
						else if(inputObj instanceof GroupInfo) {
							GroupInfo gi = new GroupInfo();
							gi = (GroupInfo)inputObj;
							//グループ作成
							if(gi.state == 0) {
								try{
									makeGroup(gi);
									oos.writeObject("1");
									oos.flush();
								}catch(IOException e) {
									oos.writeObject("0");
									oos.flush();
									System.err.print("グループ作成時にエラーが発生しました：" + e);
								}
							}
							//プロフ変更
							if(gi.state == 1) {
								try{
									changeGroupInfo(gi);
									oos.writeObject("1");
									oos.flush();
								}catch(IOException e) {
									oos.writeObject("0");
									oos.flush();
									System.err.print("グループ情報変更時にエラーが発生しました：" + e);
								}
							}
						}

						//その他ならreceiveMessage()
						else {
							String inputLine = inputObj.toString();
							System.out.println(inputLine);	//確認用
							receiveMessage(inputLine);
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

		System.out.println("userList："+userlist);

		for(int i = 0; i < userlist.size(); i++){
			UserInfo user = userlist.get(i);

			//完全一致
			if(user!=null) {
				if(user.gender == gender && user.grade == grade && user.faculty == faculty && user.birth == birth && user.circle == circle) {
					user_buf.add(user);
				}
			}

		}


			System.out.println("配列："+user_buf);


		//候補なしならnullを返す
		if(user_buf == null) return null;

		//候補がいる場合
		else {
			if(3*page - 2 <= user_buf.size()) res[0] = user_buf.get(3*page - 3);
			else res[0] = null;
			System.out.println(res[0]);
			if(3*page - 1 <= user_buf.size()) res[1] = user_buf.get(3*page - 2);
			else res[1] = null;
			System.out.println(res[1]);
			if(3*page <= user_buf.size()) res[2] = user_buf.get(3*page-1);
			else res[2] = null;
			System.out.println(res[2]);
		}

		return res;
	}

	//UserInfo送信
	public static UserInfo[] sendUserInfo(int page) {//久保田が書き換え
		UserInfo res[] = new UserInfo[3];

		//なしならnullを返す
		if(users == null) return null;

		//ユーザがいる場合
		else {
			if(users[3*page - 3] != null) res[0] = users[3*page - 3];
			else res[0] = null;

			if(users[3*page - 2] != null) res[1] = users[3*page - 2];
			else res[1] = null;

			if(users[3*page-1] != null) res[2] = users[3*page-1];
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
			if(group!=null) {
				if(group.purpose == purpose || group.numberOfMember == num) {
					group_buf.add(group);
				}
			}

		}

		//候補なしならnullを返す
		if(group_buf == null) return null;

		//候補がいる場合
		else {
			if(3*page - 2 <= group_buf.size()) res[0] = group_buf.get(3*page - 3);
			else res[0] = null;

			if(3*page - 1 <= group_buf.size()) res[1] = group_buf.get(3*page - 2);
			else res[1] = null;

			if(3*page <= group_buf.size()) res[2] = group_buf.get(3*page - 1);
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
				br.readLine();	//TODO
				String str = br.readLine();				//2行目読み込み
				br.close();
				System.out.println("str="+str);
				String res[] = str.split(" ");			//空白で分割

				//もしパスワードが一致していればtrue
				if(res[0].equals(pass)) return true;//TODO
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
		File image_user_dir = new File(path + "\\images\\" + ui.studentNumber);
		File studentCard = new File(path + "\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_card.png");
		File main_image = new File(path + "\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_main.png");
		File sub1_image = new File(path + "\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub1.png");
		File sub2_image = new File(path + "\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub2.png");
		File sub3_image = new File(path + "\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub3.png");
		File sub4_image = new File(path + "\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub4.png");

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
			ImageIO.write(ui.getStudentCard(), "png", studentCard);
			ImageIO.write(ui.getMainPhoto(), "png", main_image);
			ImageIO.write(ui.getSubPhoto()[0], "png", sub1_image);
			ImageIO.write(ui.getSubPhoto()[1], "png", sub2_image);
			ImageIO.write(ui.getSubPhoto()[2], "png", sub3_image);
			ImageIO.write(ui.getSubPhoto()[3], "png", sub4_image);

			//配列に追加
			readUserFile(LoginFile);

		} catch (IOException e) {
			System.err.print("新規登録の際にエラーが発生しました：" + e);
			return;
		}
	}

	//ユーザの情報変更
	public static void changeUserInfo(UserInfo ui) {

		BufferedReader br = null;
		FileReader fr = null;
		String line;
		int line_counter = 0;

		try {
			//ファイルを読み込み
			File file = new File(System.getProperty("user.dir") + "\\ID\\" + ui.studentNumber + ".txt");
			File main_image = new File(System.getProperty("user.dir") + "\\ID\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_main.png");
			File sub1_image = new File(System.getProperty("user.dir") + "\\ID\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub1.png");
			File sub2_image = new File(System.getProperty("user.dir") + "\\ID\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub2.png");
			File sub3_image = new File(System.getProperty("user.dir") + "\\ID\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub3.png");
			File sub4_image = new File(System.getProperty("user.dir") + "\\ID\\images\\" + ui.studentNumber + "\\" + ui.studentNumber + "_sub4.png");

			fr = new FileReader(file);
			br = new BufferedReader(fr);

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 10) break;
			}

			//いいね系列を保存
			String line10 = line;
			line = br.readLine();
			String line11 = line;
			line = br.readLine();
			String line12 = line;
			line = br.readLine();
			String line13 = line;
			line = br.readLine();
			String line14 = line;
			br.close();

			file.delete();
			file.createNewFile();

			//ユーザ情報ファイルを作成
			FileWriter fw = new FileWriter(file);
			fw.write(ui.studentNumber + "\n" +
					 ui.password + "\n" +
					 ui.name + "\n" +
					 ui.gender + "\n" +
					 ui.grade + "\n" +
					 ui.faculty + "\n" +
					 ui.birth + "\n" +
					 ui.circle + "\n" +
					 ui.hobby + "\n" +
					 line10 + "\n" +
					 line11 + "\n" +
					 line12 + "\n" +
					 line13 + "\n" +
					 line14 + "\n" +
					 ui.isAuthentificated +"\n"+
					 ui.lineId + "\n" +
					 ui.isPublic + "\n"
					 );
			fw.close();

			//画像を保存
			ImageIO.write(ui.getMainPhoto(), "png", main_image);
			ImageIO.write(ui.getSubPhoto()[0], "png", sub1_image);
			ImageIO.write(ui.getSubPhoto()[1], "png", sub2_image);
			ImageIO.write(ui.getSubPhoto()[2], "png", sub3_image);
			ImageIO.write(ui.getSubPhoto()[3], "png", sub4_image);

		}catch(IOException e) {
			System.err.print("ユーザ情報変更に関する処理でエラーが発生しました：" + e);
			return;
		}
		return;
	}

	//グループの情報変更
	public static void changeGroupInfo(GroupInfo gi) {

		BufferedReader br = null;
		FileReader fr = null;
		String line;
		int line_counter = 0;

		try {
			//ファイルを読み込み
			File file = new File(System.getProperty("user.dir") + "\\Group\\" + gi.groupNumber + ".txt");
			File main_image = new File(System.getProperty("user.dir") + "\\Group\\images\\" + gi.groupNumber + "_main.png");

			fr = new FileReader(file);
			br = new BufferedReader(fr);

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 4) break;
			}

			//いいね系列を保存
			String line4 = line;
			line = br.readLine();
			String line5 = line;
			line = br.readLine();
			String line6 = line;
			br.close();

			file.delete();
			file.createNewFile();

			String nonhost = gi.nonhostUser[0] + " " + gi.nonhostUser[1] + " " + gi.nonhostUser[2] + " " + gi.nonhostUser[3];
			nonhost.replace(" 0","");

			//ユーザ情報ファイルを作成
			FileWriter fw = new FileWriter(file);
			fw.write(gi.name + "\n" +
					 gi.relation + "\n" +
					 line4 + "\n" +
					 line5 + "\n" +
					 line6 + "\n" +
					 gi.hostUser + "\n" +
					 nonhost + "\n" +
					 gi.comment + "\n" +
					 gi.numberOfMember + "\n"
					 );
			fw.close();
			fw.close();

			//画像を保存
			ImageIO.write(gi.getMainPhoto(), "png", main_image);

		}catch(IOException e) {
			System.err.print("ユーザ情報変更に関する処理でエラーが発生しました：" + e);
			return;
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
			ImageIO.write(gi.getMainPhoto(), "png", main_image);

			//再度読み込み
			readAllUserFiles();
			readAllGroupFiles();

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
				fw.close();//nullpointer
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
		String line = "";
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

			if(line.length() < 3) { 	//今まで参加してなかった場合
				strbuf.append(uuid + "\n");
			}else {				//すでに参加したことがある場合
				strbuf.append(line + " " + uuid + "\n");
			}

			line = br.readLine(); //次の行

			//誘われているグループ(14行目)から削除
			line = line.replace(uuid, ""); //UUIDを削除
			line = line.replace("  "," "); //並んだ空白を削除
			//久保田が書き換え
			if(line.length() != 0) {
				if(Character.isWhitespace(line.charAt(0)))  line = line.substring(1, line.length()); //先頭の空白を削除
				if(Character.isWhitespace(line.charAt(line.length() - 1)))  line = line.substring(1, line.length()-1); //最後の空白を削除
			}

			strbuf.append(line + "\n");

			//最後まで読み込み
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			//参加したグループが全員集まったか確認
			judgeAllGathered(uuid);

		}catch(IOException e) {
			System.err.print("グループ参加に関する処理でエラーが発生しました：" + e);
			return false;

		}finally {
			try {//久保田が書き換え
				fr.close();
				br.close();
				fw.close();//nullpointer
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

        String line = "";
        String students[] = null;
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


			if(line.length() < 3) {
				students = line.split(" ");//TODO
			}
			strbuf.append(line + "\n");

			//非ホストユーザがグループに入っているか確認
			int i = 0;
			int count_true = 0;
			while(students[i] != null) {
				if(judgeJoinedGroup(students[i], uuid)) count_true++;
			}

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 11) break;
				strbuf.append(line + "\n");
			}

			//人数の行
			boolean judge = false;
			if(count_true + 1 == Integer.parseInt(line))
				judge = true;
			strbuf.append(line + "\n");

			//全員集まったかの行
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

	//グループの参加の有無をチェック
	public static boolean judgeJoinedGroup(String studentNum, String uuid) {
		BufferedReader br = null;
		FileReader fr = null;
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

			//参加しているグループにuuidがあるときtrue
			if(line.contains(uuid)) {
				return true;
			}else {
				return false;
			}

		}catch(IOException e) {
			System.err.print("judgeJoinedGroupでエラーが発生しました：" + e);
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
	public static boolean deleteUser(String studentNum) {
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
				for(int i=0; i<SentGoodStudents.length; i++) {
					deleteReceivedGood(SentGoodStudents[i], studentNum);
				}

				//次の行
				line = br.readLine();

				//空白で分割して保存、いいねをくれた人
				BeingSentGoodStudents = line.split(" ");
				//ここでいいね削除
				for(int i=0; i<BeingSentGoodStudents.length; i++) {
					deleteGood(BeingSentGoodStudents[i], studentNum);
				}

				//次の行
				line = br.readLine();

				//空白で分割して保存、マッチングした人
				MatchingStudents = line.split(" ");
				for(int i=0; i<MatchingStudents.length; i++) {
					deleteMatching(MatchingStudents[i], studentNum, false);
				}

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

				//削除
				file.delete();
				image_user_dir.delete();

				//再度読み込み
				readAllUserFiles();
				readAllGroupFiles();

			}catch(IOException e) {
				System.err.print("ユーザ削除に関する処理でエラーが発生しました：" + e);
				return false;
			}finally {
				try {
					fr.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return true;
	}

	//いいね
 	public static boolean goodUser(String my_num, String your_num) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\ID\\" + my_num + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 10) break;
				strbuf.append(line + "\n");
			}

			String students[] = null;
			String line10 = line;	//10行目いいねした

			//次の行
			line = br.readLine();

			String line11 = line;	//11行目いいねされた

			if(line11 != null) {
				students = line11.split(" ");
			}

			//一致してたらマッチ
			for(int i=0; i<students.length;) {
				if(students[i] == your_num) {
					deleteGood(your_num, my_num); //相手のいいねした欄から自分を消す
					deleteReceivedGood(my_num, your_num); //自分のいいねを受け取った欄から相手を消す
					return matchUsers(my_num, your_num, false);
				}
			}

			//自分のファイルの、いいねを送った人に相手を追加
			if(line10 == "") {
				strbuf.append(your_num + "\n");
			}else {
				strbuf.append(line10 + " " + your_num + "\n");
			}

			strbuf.append(line11 + "\n");

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
   			System.out.println(e);
   			return false;
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}

 		return true;
 	}

 	//ユーザマッチング
 	public static boolean matchUsers(String myId, String yourId, boolean preventLoop) {	//preventLoopがtrueなら再帰を行わない
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\ID\\" + myId + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 12) break;
				strbuf.append(line + "\n");
			}

			//自分のファイルの、いいねを送った人に相手を追加
			if(line == "") {
				strbuf.append(yourId + "\n");
			}else {
				strbuf.append(line + " " + yourId + "\n");
			}

			//最後まで読み込み
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}


			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			if(!preventLoop) {
				matchUsers(yourId, myId, true);
				readAllUserFiles();
				readAllGroupFiles();
			}

  		}catch(IOException e) {
   			System.out.println(e);
   			return false;
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}

 		return true;
 	}

 	//いいねを消す
 	public static boolean deleteGood(String myId, String yourId) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\ID\\" + myId + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 10) break;
				strbuf.append(line + "\n");
			}

			line = line.replace(yourId, ""); //numを削除
			line = line.replace("  "," "); //並んだ空白を削除
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
			strbuf.append(line + "\n");

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
   			System.out.println(e);
   			return false;
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}

 		return true;
 	}

 	//受け取ったいいねを消す
 	public static boolean deleteReceivedGood(String myId, String yourId) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\ID\\" + myId + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 11) break;
				strbuf.append(line + "\n");
			}

			line = line.replace(yourId, ""); //numを削除
			line = line.replace("  "," "); //並んだ空白を削除
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
			strbuf.append(line + "\n");

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
   			System.out.println(e);
   			return false;
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}

 		return true;
 	}

	//グループいいね
 	public static boolean goodGroup(String myuuid, String youruuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\Group\\" + myuuid + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 4) break;
				strbuf.append(line + "\n");
			}

			String groups[] = null;
			String line4 = line;	//4行目

			//次の行
			line = br.readLine();

			String line5 = line;	//5行目

			if(line5 != null) {
				groups = line5.split(" ");

				//一致してたらマッチ
				for(int i=0; i<groups.length;) {
					if(groups[i] == youruuid) {
						return matchGroups(myuuid, youruuid, false);
					}
				}
			}

			//自分のファイルの、いいねを送った人に相手を追加
			if(line4 == "") {
				strbuf.append(youruuid + "\n");
			}else {
				strbuf.append(line4 + " " + youruuid + "\n");
			}

			strbuf.append(line5 + "\n");

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
   			System.out.println(e);
   			return false;
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}

 		return true;
	}

 	//グループマッチング
 	public static boolean matchGroups(String myuuid, String youruuid, boolean preventLoop) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\ID\\" + myuuid + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 6) break;
				strbuf.append(line + "\n");
			}

			//自分のファイルの、いいねを送った人に相手を追加
			if(line == "") {
				strbuf.append(youruuid + "\n");
			}else {
				strbuf.append(line + " " + youruuid + "\n");
			}

			//最後まで読み込み
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			if(!preventLoop) {
				matchUsers(youruuid, myuuid, true);
				readAllUserFiles();
				readAllGroupFiles();
			}

  		}catch(IOException e) {
   			System.out.println(e);
   			return false;
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}

 		return true;
 	}

 	//グループいいねを消す
 	public static void deleteGroupGood(String myuuid, String youruuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\Group\\" + myuuid + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 4) break;
				strbuf.append(line + "\n");
			}

			line = line.replace(youruuid, ""); //uuidを削除
			line = line.replace("  "," "); //並んだ空白を削除
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
			strbuf.append(line + "\n");

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
   			System.out.println(e);
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}
 	}

 	//受け取ったグループいいねを消す
 	public static boolean deleteReceivedGroupGood(String myuuid, String youruuid) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\Group\\" + myuuid + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 5) break;
				strbuf.append(line + "\n");
			}

			line = line.replace(youruuid, ""); //uuidを削除
			line = line.replace("  "," "); //並んだ空白を削除
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
			strbuf.append(line + "\n");

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
   			System.out.println(e);
   			return false;
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}
 		return true;
 	}

 	//マッチング削除
 	public static void deleteMatching(String myId, String yourId, boolean preventLoop) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\ID\\" + myId + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 12) break;
				strbuf.append(line + "\n");
			}

			line = line.replace(yourId, ""); //numを削除
			line = line.replace("  "," "); //並んだ空白を削除
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
			strbuf.append(line + "\n");

			//最後まで読み込み
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			if(!preventLoop) {
				matchUsers(yourId, myId, true);
				readAllUserFiles();
				readAllGroupFiles();
			}

  		}catch(IOException e) {
   			System.out.println(e);
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
   		}

 	}

 	//グループマッチング削除
 	public static void deleteGroupMatching(String myuuid, String youruuid, boolean preventLoop) {
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		StringBuffer strbuf = new StringBuffer("");

 		try {
 			File file = new File(System.getProperty("user.dir") + "\\ID\\" + myuuid + ".txt");
 			fr = new FileReader(file);
 			br = new BufferedReader(fr);
 			String line;
			int line_counter = 0;

			//該当行を検索
			while((line = br.readLine()) != null) {
				line_counter++;
				if(line_counter == 6) break;
				strbuf.append(line + "\n");
			}

			line = line.replace(youruuid, ""); //numを削除
			line = line.replace("  "," "); //並んだ空白を削除
			if(line.charAt(0) == ' ')  line = line.substring(1, line.length()); //先頭の空白を削除
			if(line.charAt(line.length()) == ' ')  line = line.substring(1, line.length()-1); //最後の空白を削除
			strbuf.append(line + "\n");

			//最後まで読み込み
			while((line = br.readLine()) != null) {
				strbuf.append(line + "\n");
			}

			//書き込み
			fw = new FileWriter(file);
			fw.write(strbuf.toString());

			if(!preventLoop) {
				matchUsers(youruuid, myuuid, true);
				readAllUserFiles();
				readAllGroupFiles();
			}

  		}catch(IOException e) {
   			System.out.println(e);
   		}finally {
   			try {
				fw.close();
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
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
			/*finally {
            	try {
					fr.close();
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
            }*/

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

			if(cmd.equals("認証")) {
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
			else if(cmd.equals("却下")) {
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

	//画像のリサイズ
	public ImageIcon scaleImage(BufferedImage bi, int destWidth, int destHeight) throws IOException {
	        int width = bi.getWidth();    // オリジナル画像の幅
	        int height = bi.getHeight();  // オリジナル画像の高さ

	        // 縦横の比率から、scaleを決める
	        double widthScale = (double) destWidth / (double) width;
	        double heightScale = (double) destHeight / (double) height;
	        double scale = widthScale < heightScale ? widthScale : heightScale;

	        ImageFilter filter = new AreaAveragingScaleFilter(
	            (int) (bi.getWidth() * scale), (int) (bi.getHeight() * scale));
	        ImageProducer p = new FilteredImageSource(bi.getSource(), filter);
	        Image dstImage = Toolkit.getDefaultToolkit().createImage(p);
	        BufferedImage dst = new BufferedImage(
	            dstImage.getWidth(null), dstImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = dst.createGraphics();
	        g.drawImage(dstImage, 0, 0, null);
	        g.dispose();
	        return new ImageIcon(dst);
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
	        lMainPhotoUserInfo.setHorizontalAlignment(JLabel.CENTER);
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
			System.out.println(cmd);

			if(cmd=="検索") {
				System.out.println("検索実行");
				String studentNum=tfStudentNumberSearch.getText();
				BufferedReader br = null;
		        FileReader fr = null;
		        String line;

				try {
					//ファイルを読み込み
					File file = new File(System.getProperty("user.dir") + "\\ID\\" + studentNum + ".txt");
					File image_dir = new File(System.getProperty("user.dir") + "\\ID\\images\\"+studentNum+"\\"+studentNum+"_main.png");

					lMainPhotoUserInfo.setIcon(scaleImage(ImageIO.read(image_dir),w/2,h/6));

					for(int i=0;i<4;i++) {
						switch(i) {
						case 1:
							image_dir = new File(System.getProperty("user.dir") + "\\ID\\images\\"+studentNum+"\\"+studentNum+"_sub2.png");
							break;
						case 2:
							image_dir = new File(System.getProperty("user.dir") + "\\ID\\images\\"+studentNum+"\\"+studentNum+"_sub3.png");
							break;
						case 3:
							image_dir = new File(System.getProperty("user.dir") + "\\ID\\images\\"+studentNum+"\\"+studentNum+"_sub4.png");
							break;
						case 0:
							image_dir = new File(System.getProperty("user.dir") + "\\ID\\images\\"+studentNum+"\\"+studentNum+"_sub1.png");
							break;
						}
						lSubPhotoUserInfo[i].setIcon(scaleImage(ImageIO.read(image_dir),w/6,h/10));
					}

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
					cardLayout.show(cardPanel,"UserInfo");
				}
				catch(IOException e) {
					System.err.print("ユーザ検索に関する処理でエラーが発生しました：" + e);
				}
			}
			else if(cmd.equals("BAN")) {
				deleteUser(tfStudentNumberSearch.getText());
				cardLayout.show(cardPanel,"search");
			}
			else if(cmd.equals("戻る")) {
				cardLayout.show(cardPanel,"search");
			}

		}
	}

}
