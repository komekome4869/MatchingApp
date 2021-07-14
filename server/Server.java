import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import javax.imageio.ImageIO;
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

	int w = 400;
	int h = 650;

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

		try {
			ss = new ServerSocket(50);
		} catch (IOException e) {
			System.err.println("エラーが発生しました: " + e);
		}
	}

	public static void main(String args[]) {
		Server server = new Server("MS_Server");
		server.acceptClient();
	}

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

	// データ受信用スレッド(内部クラス)
	static class Receiver extends Thread {
		private InputStreamReader sisr; //受信データ用文字ストリーム
		private BufferedReader br; //文字ストリーム用のバッファ
		private ObjectInputStream ois;
		private PrintWriter out_buf; //送信先を記録
		private Receiver receiver_buf; //受信元を記録

		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket){
			try{
				out_buf = out;
				receiver_buf = receiver;
				ois = new ObjectInputStream(socket.getInputStream());
				sisr = new InputStreamReader(socket.getInputStream());
			} catch (IOException e) {
					System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}

		//メッセージの処理
		public void receiveMessage(String inputLine) {
				if (inputLine != null){ //データを受信したら
					String act[] = inputLine.split(","); //カンマの前後で文字列を分割

					switch(act[0]){
					case "lg": //新規登録する
						if(checkPassword(act[1],act[2] /*(学籍番号,パスワード)*/) == true) {
							out_buf.print("1");
							out_buf.flush();
						}
						else {
							out_buf.print("0");
							out_buf.flush();
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

						break;

					}
				}
		}

		// 内部クラス Receiverのメソッド
		public void run(){
			try {
				while(true) {
					try {
						//UserInfo型なら
						if(ois.readObject() instanceof UserInfo) {
							UserInfo ui = new UserInfo();
							ui = (UserInfo)ois.readObject();
							//新規登録
							if(ui.state == 0) {
								signUp(ui);
							}
							//
						}

						//GroupInfo型なら
						else if(ois.readObject() instanceof GroupInfo) {
							GroupInfo gi = new GroupInfo();
							gi = (GroupInfo)ois.readObject();
							//グループ作成
							if(gi.state == 0) {
								makeGroup(gi);
							}
						}

						//その他ならreceiveMessage()
						else {
							br = new BufferedReader(sisr);
							String inputLine = br.readLine();//データを一行分読み込む
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
			ImageIO.write(ui.mainPhoto, "png", main_image);
			ImageIO.write(ui.subPhoto[0], "png", sub1_image);
			ImageIO.write(ui.subPhoto[1], "png", sub2_image);
			ImageIO.write(ui.subPhoto[2], "png", sub3_image);
			ImageIO.write(ui.subPhoto[3], "png", sub4_image);

		} catch (IOException e) {
			System.err.print("新規登録の際にエラーが発生しました：" + e);
			return;
		}
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

	//認証内部クラス
	class Authentificate extends JFrame implements ActionListener{

		public Authentificate() {
			super("新規会員認証");
			JPanel p1 = new JPanel();
			p1.setLayout(null);
			pack();

			JLabel lTitleAuthen = new JLabel("認証");
			lTitleAuthen.setBounds(w/4,h/15,w/2,h/15);
			lTitleAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/10));
			lTitleAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lTitleAuthen);

	        JLabel lStudentCardAuthen = new JLabel("");
	        lStudentCardAuthen.setBounds(w/10,h/3,9*w/10,h/5);
	        lStudentCardAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lStudentCardAuthen);

	        JLabel lNameAuthen = new JLabel("名前");
	        lNameAuthen.setBounds(w/10,7*h/15,w/5,h/15);
	        lNameAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lNameAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lNameAuthen);

	        JLabel lUserNameAuthen = new JLabel("");
	        lUserNameAuthen.setBounds(2*w/5,7*h/15,3*w/5,h/15);
	        lUserNameAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lUserNameAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lUserNameAuthen);

	        JLabel lNumberAuthen = new JLabel("学籍番号");
	        lNumberAuthen.setBounds(w/10,8*h/15,w/5,h/15);
	        lNumberAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lNumberAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lNumberAuthen);

	        JLabel lUserNumberAuthen = new JLabel("");
	        lUserNumberAuthen.setBounds(2*w/5,8*h/15,3*w/5,h/15);
	        lUserNumberAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lUserNumberAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lUserNumberAuthen);

	        JLabel lGenderAuthen = new JLabel("性別");
	        lGenderAuthen.setBounds(w/10,9*h/15,w/5,h/15);
	        lGenderAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lGenderAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lGenderAuthen);

	        JLabel lUserGenderAuthen = new JLabel("");
	        lUserGenderAuthen.setBounds(2*w/5,9*h/15,3*w/5,h/15);
	        lUserGenderAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lUserGenderAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lUserGenderAuthen);

	        JLabel lGradeAuthen = new JLabel("学年");
	        lGradeAuthen.setBounds(w/10,10*h/15,w/5,h/15);
	        lGradeAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lGradeAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lGradeAuthen);

	        JLabel lUserGradeAuthen = new JLabel("");
	        lUserGradeAuthen.setBounds(2*w/5,10*h/15,3*w/5,h/15);
	        lUserGradeAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        lUserGradeAuthen.setHorizontalAlignment(JLabel.CENTER);
	        p1.add(lUserGradeAuthen);

	        JButton bAcceptAuthen=new JButton("認証");
	        bAcceptAuthen.setBounds(w/5,5*h/6,w/4,h/15);
	        bAcceptAuthen.addActionListener(this);
	        bAcceptAuthen.setActionCommand("認証");
	        bAcceptAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        p1.add(bAcceptAuthen);

	        JButton bRejectAuthen=new JButton("却下");
	        bRejectAuthen.setBounds(11*w/20,5*h/6,w/4,h/15);
	        bRejectAuthen.addActionListener(this);
	        bRejectAuthen.setActionCommand("却下");
	        bRejectAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        p1.add(bRejectAuthen);

	        getContentPane().add(p1, null);
	        setSize(w,h);
		    setResizable(false);
		    setVisible(true);

		}

		public void actionPerformed(ActionEvent ae) {

		}
	}

	//会員検索内部クラス
	class searchUsers extends JFrame implements ActionListener,ChangeListener{
		JPanel cardPanel;
		CardLayout cardLayout;

		public searchUsers(){

			super("ユーザー検索");
			cardPanel = new JPanel();
		    cardLayout = new CardLayout();
		    cardPanel.setLayout(cardLayout);

		    search();
		    aboutUser();

		    cardLayout.show(cardPanel,"UserInfo");
		    pack();
		    getContentPane().add(cardPanel, BorderLayout.CENTER);
		    setSize(w,h);
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

	        JTextField tfStudentNumberSearch = new JTextField(20);
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


			// 項目
			String[] items = {"名前","性別","学年","学科","出身","趣味"};
			// 表の行数
			int row = items.length;

			JPanel card = new JPanel();
			card.setLayout(null);

			JButton bBackUserInfo = new JButton("←");
	        bBackUserInfo.setBounds(w/30,h/60,w/6,h/15);
	        bBackUserInfo.addActionListener(this);
	        bBackUserInfo.setActionCommand("ヘルプmenu");
	        bBackUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
	        card.add(bBackUserInfo);

	        JLabel lTitleUserInfo = new JLabel("ユーザ情報");
			lTitleUserInfo.setBounds(w/4,h/60,w/2,h/15);
			lTitleUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
			lTitleUserInfo.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lTitleUserInfo);

	        JButton bMainPhotoUserInfo = new JButton("");
	        bMainPhotoUserInfo.setBounds(w/4,6*h/60,w/2,h/6);
	        bMainPhotoUserInfo.addActionListener(this);
	        bMainPhotoUserInfo.setActionCommand("ヘルプmenu");
	        bMainPhotoUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        card.add(bMainPhotoUserInfo);

	        JButton[] bSubPhotoUserInfo = new JButton[4];
	        for(int i=0;i<4;i++) {
	        	bSubPhotoUserInfo[i] = new JButton();
	        	bSubPhotoUserInfo[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
	            bSubPhotoUserInfo[i].addActionListener(this);
	            bSubPhotoUserInfo[i].setActionCommand("ヘルプmenu");
	            bSubPhotoUserInfo[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	            card.add(bSubPhotoUserInfo[i]);
	        }

	        // プロフィールの表
	        JTable tTableUserInfo = new JTable(row,2);

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
			JLabel lDeleteUserInfo = new JLabel("アカウント削除");
			lDeleteUserInfo.setBounds(w/10,40*h/60,w/2,h/15);
			lDeleteUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
			lDeleteUserInfo.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lDeleteUserInfo);

	        JToggleButton tbDeleteUserInfo = new JToggleButton("OFF");
	        tbDeleteUserInfo.setBounds(6*w/10,41*h/60,w/7,h/20);
	        tbDeleteUserInfo.addChangeListener(this);
	        tbDeleteUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/35));
			card.add(tbDeleteUserInfo);

	        JLabel lBanUserInfo = new JLabel("BAN");
			lBanUserInfo.setBounds(w/10,44*h/60,w/2,h/15);
			lBanUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
			lBanUserInfo.setHorizontalAlignment(JLabel.CENTER);
	        card.add(lBanUserInfo);

	        JToggleButton tbBanUserInfo = new JToggleButton("OFF");
	        tbBanUserInfo.setBounds(6*w/10,45*h/60,w/7,h/20);
	        tbBanUserInfo.addChangeListener(this);
	        tbBanUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/35));
			card.add(tbBanUserInfo);

			JButton bDecideUserInfo=new JButton("確定");
			bDecideUserInfo.setBounds(w/4,24*h/30,w/2,h/15);
			bDecideUserInfo.addActionListener(this);
			bDecideUserInfo.setActionCommand("確定");
			bDecideUserInfo.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
	        card.add(bDecideUserInfo);

			cardPanel.add(card,"UserInfo");
		}

		public void stateChanged(ChangeEvent e) {
			JToggleButton btn = (JToggleButton)e.getSource();
			if (btn.isSelected()) {
				btn.setText("ON");
			} else {
				btn.setText("OFF");
			}
		}
		public void actionPerformed(ActionEvent ae) {

		}
	}

}
