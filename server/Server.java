import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
	static int userFileNum = 0;	//ユーザファイル数
	static HashMap<String, UserInfo> activeUsers =new HashMap<>();

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

				activeUsers.put(String.valueOf(users[userFileNum].studentNumber),users[userFileNum]);
				userFileNum++;

			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	//データ受信用スレッド(内部クラス)
	class Receiver extends Thread {
		//private InputStreamReader sisr; //受信データ用文字ストリーム
		//private BufferedReader br; //文字ストリーム用のバッファ
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private PrintWriter out_buf; //送信先を記録
		private Receiver receiver_buf; //受信元を記録

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

						case "ug": //ユーザにいいねを送る

							break;

						case "gg": //グループにいいねを送る

							break;

						case "jg": //グループに参加
							joinGroup(act[1], act[2]);
							break;

						case "rg": //グループ参加拒否
							deleteGroup(act[2]);
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

		try {
			//グループ情報ファイルを作成
			FileWriter fw = new FileWriter(GroupFile);
			fw.write(gi.name + "\n" +
					 gi.relation + "\n" +
					 /*UUID*/ "\n" +
					 /*UUID*/ "\n" +
					 /*UUID*/ "\n" +
					 gi.hostUser + "\n" +
					 gi.nonhostUser[0] + " " + gi.nonhostUser[1] + " " + gi.nonhostUser[2] + " " + gi.nonhostUser[3] + "\n" +
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
				strbuf.append(line + "\n");
				if(line_counter == 13) break;
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
					strbuf.append(line + "\n");
					if(line_counter == 13) break;
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
