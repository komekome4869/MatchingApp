import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Client extends JFrame implements ActionListener,ChangeListener{

	int w=400;
	int h=650;
	UserInfo myUserInfo=new UserInfo();

	//プロフィールの検索で選ぶやつ
	String[] Sex = {"男性", "女性", "その他"};
	String[] Grade = {"1", "2", "3", "4"};
	String[] Faculty = {"経営", "経済", "教育", "理工", "都市科学"};
	String[] Birthplace = {"北海道・東北", "関東", "中部", "近畿", "中国", "四国", "九州", "海外"};
	String[] Circle = {"テニス", "運動", "文化"};
	String[] Purpose = {"男子と仲良くなりたい","女子と仲良くなりたい"};


	JPanel cardPanel;
	CardLayout layout;

	public Client(){
		super("TITLE");
		cardPanel = new JPanel();
	    layout = new CardLayout();
	    cardPanel.setLayout(layout);

	    //自分が作る画面のメソッド名をここに書く
	    login();
	    matching();
	    new_regis();
	    wait1();
	    gathering();
	    menu();
	    myProfile();
	    howToUse();
	    setup();
	    invite();
	    home();
	    change();
	    searchUser();
	    viewGroup();
	    myGroupProfile();
	    makeGroup();
	    finishAuthen();
	    judging();
	    reply();
	    good();
	    

	    //"login"のところを違う画面の名前に変えれば、それが一番最初の画面になる。
	    layout.show(cardPanel,"reply");
	    pack();
	    getContentPane().add(cardPanel, BorderLayout.CENTER);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(w,h);
	    setVisible(true);
	}

	public void login() {
		//↓2行はコピペでOK
		JPanel card=new JPanel();
		card.setLayout(null);

		/*変数名は「変数の種類+内容+どの画面で使うか」。
		 * 「ログイン画面で使う タイトルを表示する JLabel」なら「lTitleLogin」。
		 * 単語の頭だけ大文字。ただし、最初の文字は小文字。
		 * lowerCammelCaseでググれば出てくる。
		 *
		 * 文字のフォントは、とりあえずMS明朝にしてるけど、アプリの雰囲気に合わせて後で変えよう
		 */

		JLabel lTitleLogin = new JLabel("TITLE");
		lTitleLogin.setBounds(w/4,h/10,w/2,h/10);
		lTitleLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/20));
		lTitleLogin.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleLogin);

        JLabel lIdLogin = new JLabel("学籍番号");
        lIdLogin.setBounds(w/6,h/3,w/5,h/15);
        lIdLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lIdLogin.setHorizontalAlignment(JLabel.CENTER);
        card.add(lIdLogin);

        JTextField tfIdLogin = new JTextField(20);
        tfIdLogin.setBounds(2*w/5,h/3,2*w/5,h/15);
        tfIdLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfIdLogin);

        JLabel lPasswordLogin = new JLabel("パスワード");
        lPasswordLogin.setBounds(w/6,7*h/15,w/5,h/15);
        lPasswordLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lPasswordLogin.setHorizontalAlignment(JLabel.CENTER);
        card.add(lPasswordLogin);

        JTextField tfPasswordLogin = new JTextField(20);
        tfPasswordLogin.setBounds(2*w/5,7*h/15,2*w/5,h/15);
        tfPasswordLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfPasswordLogin);

        JButton bLoginLogin=new JButton("ログイン");
        bLoginLogin.setBounds(w/4,19*h/30,w/2,h/15);
        bLoginLogin.addActionListener(this);
        bLoginLogin.setActionCommand("login");//ボタンにラベル付け、ここのルールも決めたほうがいい
        bLoginLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bLoginLogin);

        JButton bNewAccountLogin=new JButton("新規作成");
        bNewAccountLogin.setBounds(w/4,22*h/30,w/2,h/15);
        bNewAccountLogin.addActionListener(this);
        bNewAccountLogin.setActionCommand("アカウント作成");
        bNewAccountLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bNewAccountLogin);

        JLabel lMessageLogin = new JLabel("学籍番号もしくはパスワードが正しくありません");
        lMessageLogin.setBounds(w/10,26*h/30,4*w/5,h/30);
        lMessageLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lMessageLogin.setHorizontalAlignment(JLabel.CENTER);
        lMessageLogin.setForeground(Color.RED);
        lMessageLogin.setVisible(false);
        card.add(lMessageLogin);


        //自分が作る画面に名前付け。メソッド名と同じじゃなくても大丈夫だけど、同じのほうが分かりやすいかも。
		cardPanel.add(card,"login");
	}

	public void matching() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleMatching = new JLabel("マッチング");
		lTitleMatching.setBounds(w/4,h/50,w/2,h/10);
		lTitleMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		lTitleMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMatching);

        ImageIcon icon = new ImageIcon("test.png");
        JLabel lIconMatching = new JLabel(icon);
        lIconMatching.setBounds(w/5,h/7,w/2,h/5);
        lIconMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lIconMatching);

        JLabel lNameMatching = new JLabel("○○さんとマッチしました！");
        lNameMatching.setBounds(0,11*h/20,w,h/15);
        lNameMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        lNameMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMatching);

        JLabel lIdMatching = new JLabel("LINEID:aaaaaaaaa");
        lIdMatching.setBounds(0,13*h/20,w,h/15);
        lIdMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        lIdMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lIdMatching);

        JButton bProfMatching=new JButton("プロフィールを確認する");
        bProfMatching.setBounds(w/4,8*h/20,w/2,h/15);
        bProfMatching.addActionListener(this);
        bProfMatching.setActionCommand("label");
        bProfMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bProfMatching);

		cardPanel.add(card,"マッチング");
	}

	public void new_regis() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleNew_r = new JLabel("新規登録");
		lTitleNew_r.setBounds(w/4,h/10,w/2,h/10);
		lTitleNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 2*w/20));
		lTitleNew_r.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleNew_r);

        JLabel lIdNew_r = new JLabel("学籍番号");
        lIdNew_r.setBounds(w/5,h/3,w/5,h/15);
        lIdNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(lIdNew_r);

        JTextField tfIdNew_r = new JTextField(20);
        tfIdNew_r.setBounds(2*w/5,h/3,2*w/5,h/15);
        tfIdNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfIdNew_r);

        JLabel lPasswordNew_r = new JLabel("パスワード");
        lPasswordNew_r.setBounds(w/5,7*h/15,w/5,h/15);
        lPasswordNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(lPasswordNew_r);

        JTextField tfPasswordNew_r = new JTextField(20);
        tfPasswordNew_r.setBounds(2*w/5,7*h/15,2*w/5,h/15);
        tfPasswordNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfPasswordNew_r);

        JLabel lPasswordconfNew_r = new JLabel("パスワード（確認用）");
        lPasswordconfNew_r.setBounds(w/10,9*h/15,w/2,h/15);
        lPasswordconfNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(lPasswordconfNew_r);

        JTextField tfPasswordconfNew_r = new JTextField(20);
        tfPasswordconfNew_r.setBounds(2*w/5,9*h/15,2*w/5,h/15);
        tfPasswordconfNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfPasswordconfNew_r);

        JButton bNewAccountNew_r=new JButton("登録");
        bNewAccountNew_r.setBounds(w/4,23*h/30,w/2,h/15);
        bNewAccountNew_r.addActionListener(this);
        bNewAccountNew_r.setActionCommand("アカウント作成");
        bNewAccountNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bNewAccountNew_r);

		cardPanel.add(card,"new_regis");
	}

	public void wait1() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel waitpl = new JLabel("本人確認が完了するまでお待ちください\n"
				+ "このままアプリを閉じても大丈夫です");
		waitpl.setBounds(w/4,h/10,w/2,h/10);
		waitpl.setHorizontalAlignment(JLabel.CENTER);
		card.add(waitpl);

		TextArea manual = new TextArea();
		manual.setText("Step1　自分のプロフィールを作りましょう");
		manual.setText("Step2　気になるお相手を探して「いいね」を押しましょう");
		manual.setText("Step3　両想いでマッチング成立！LINEをしましょう");
		manual.setText("Step4　個人だけでなくグループでもマッチングできます");
		card.add(manual);

		cardPanel.add(card,"wait1");


	}

	public void gathering() {

		JPanel card=new JPanel();
		card.setLayout(null);

		// タイトル
		JLabel lTitleMatching = new JLabel("グループ作成");
		lTitleMatching.setBounds(10*w/40,h/15,20*w/40,h/10);
		lTitleMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lTitleMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMatching);

        //学籍番号表示
        JLabel lNumberGather = new JLabel("学籍番号");
        lNumberGather.setBounds(3*w/40,14*h/65,w/5,5*h/65);
        lNumberGather.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(lNumberGather);

      //入力欄
        JTextField tfNumberGather1 = new JTextField("");
        tfNumberGather1.setBounds(15*w/40,15*h/65,20*w/40,4*h/65);
        tfNumberGather1.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfNumberGather1);

      //入力欄
        JTextField tfNumberGather2 = new JTextField("");
        tfNumberGather2.setBounds(15*w/40,19*h/65,20*w/40,4*h/65);
        tfNumberGather2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfNumberGather2);

      //入力欄
        JTextField tfNumberGather3 = new JTextField("");
        tfNumberGather3.setBounds(15*w/40,23*h/65,20*w/40,4*h/65);
        tfNumberGather3.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfNumberGather3);

      //入力欄
        JTextField tfNumberGather4 = new JTextField("");
        tfNumberGather4.setBounds(15*w/40,27*h/65,20*w/40,4*h/65);
        tfNumberGather4.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfNumberGather4);

      //入力欄
        JTextField tfNumberGather5 = new JTextField("");
        tfNumberGather5.setBounds(15*w/40,31*h/65,20*w/40,4*h/65);
        tfNumberGather5.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfNumberGather5);

        /* //確定ボタン
        JButton bAddGather = new JButton("入力欄を追加する");
        bAddGather.setBounds(15*w/40,add_height*h/65,20*w/40,3*h/65);
        bAddGather.addActionListener(this);
        bAddGather.setActionCommand("追加");
        bAddGather.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bAddGather);
        */

        //確定ボタン
        JButton bConfGather = new JButton("確定");
        bConfGather.setBounds(15*w/40,42*h/65,w/5,h/15);
        bConfGather.addActionListener(this);
        bConfGather.setActionCommand("確定");
        bConfGather.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bConfGather);



		cardPanel.add(card,"gathering");
	}

	public void menu() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleMenu = new JLabel("Menu");
		lTitleMenu.setBounds(w/4,2*h/30,w/2,3*h/30);
		lTitleMenu.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 2*w/20));
		lTitleMenu.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMenu);

        JButton bProfileMenu=new JButton("Myプロフィール");
        bProfileMenu.setBounds(w/4,8*h/30,w/2,3*h/30);
        bProfileMenu.addActionListener(this);
        bProfileMenu.setActionCommand("Myプロフィールmenu");
        bProfileMenu.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bProfileMenu);

        JButton bChangeAccountMenu=new JButton("アカウント切り替え");
        bChangeAccountMenu.setBounds(w/4,12*h/30,w/2,3*h/30);
        bChangeAccountMenu.addActionListener(this);
        bChangeAccountMenu.setActionCommand("アカウント切り替えmenu");
        bChangeAccountMenu.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bChangeAccountMenu);

        JButton bConfigMenu=new JButton("設定");
        bConfigMenu.setBounds(w/4,16*h/30,w/2,3*h/30);
        bConfigMenu.addActionListener(this);
        bConfigMenu.setActionCommand("設定menu");
        bConfigMenu.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bConfigMenu);

        JButton bHelpMenu=new JButton("ヘルプ");
        bHelpMenu.setBounds(w/4,20*h/30,w/2,3*h/30);
        bHelpMenu.addActionListener(this);
        bHelpMenu.setActionCommand("ヘルプmenu");
        bHelpMenu.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bHelpMenu);

		cardPanel.add(card,"menu");
	}

	public void myProfile() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bBackMyProfile = new JButton("←");
        bBackMyProfile.setBounds(w/30,h/60,w/6,h/15);
        bBackMyProfile.addActionListener(this);
        bBackMyProfile.setActionCommand("ヘルプmenu");
        bBackMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bBackMyProfile);

        JLabel lTitleMyProfile = new JLabel("Myプロフィール");
		lTitleMyProfile.setBounds(w/4,h/60,w/2,h/15);
		lTitleMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMyProfile);

        JButton bMainPhotoMyProfile = new JButton("");
        bMainPhotoMyProfile.setBounds(w/4,6*h/60,w/2,h/6);
        bMainPhotoMyProfile.addActionListener(this);
        bMainPhotoMyProfile.setActionCommand("ヘルプmenu");
        bMainPhotoMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bMainPhotoMyProfile);

        JButton[] bSubPhotoMyProfile = new JButton[4];
        for(int i=0;i<4;i++) {
        	bSubPhotoMyProfile[i] = new JButton();
        	bSubPhotoMyProfile[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
            bSubPhotoMyProfile[i].addActionListener(this);
            bSubPhotoMyProfile[i].setActionCommand("ヘルプmenu");
            bSubPhotoMyProfile[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            card.add(bSubPhotoMyProfile[i]);
        }

        JLabel lNameMyProfile = new JLabel("名前");
		lNameMyProfile.setBounds(w/8,25*h/60,w/6,h/30);
		lNameMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lNameMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMyProfile);

        JTextField tfNameMyprofile = new JTextField("");
        tfNameMyprofile.setBounds(w/3,25*h/60,w/2,h/30);
        tfNameMyprofile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfNameMyprofile);

        JLabel lGenderMyProfile = new JLabel("性別");
		lGenderMyProfile.setBounds(w/8,28*h/60,w/6,h/30);
		lGenderMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGenderMyProfile);

        JComboBox<String> cbGenderMyProfile = new JComboBox<String>(Sex);
        cbGenderMyProfile.setBounds(w/3,28*h/60,w/2,h/30);
		cbGenderMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbGenderMyProfile);

        JLabel lGradeMyProfile = new JLabel("学年");
		lGradeMyProfile.setBounds(w/8,31*h/60,w/6,h/30);
		lGradeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGradeMyProfile);

        JComboBox<String> cbGradeMyProfile = new JComboBox<String>(Grade);
        cbGradeMyProfile.setBounds(w/3,31*h/60,w/2,h/30);
		cbGradeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbGradeMyProfile);

        JLabel lFacultyMyProfile = new JLabel("学部");
		lFacultyMyProfile.setBounds(w/8,34*h/60,w/6,h/30);
		lFacultyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lFacultyMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lFacultyMyProfile);

        JComboBox<String> cbFacultyMyProfile = new JComboBox<String>(Faculty);
        cbFacultyMyProfile.setBounds(w/3,34*h/60,w/2,h/30);
		cbFacultyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbFacultyMyProfile);

        JLabel lBirthMyProfile = new JLabel("出身");
		lBirthMyProfile.setBounds(w/8,37*h/60,w/6,h/30);
		lBirthMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lBirthMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lBirthMyProfile);

        JComboBox<String> cbBirthMyProfile = new JComboBox<String>(Birthplace);
        cbBirthMyProfile.setBounds(w/3,37*h/60,w/2,h/30);
        cbBirthMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbBirthMyProfile);

        JLabel lCircleMyProfile = new JLabel("サークル");
		lCircleMyProfile.setBounds(w/8,40*h/60,w/6,h/30);
		lCircleMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCircleMyProfile);

        JComboBox<String> cbCircleMyProfile = new JComboBox<String>(Circle);
        cbCircleMyProfile.setBounds(w/3,40*h/60,w/2,h/30);
        cbCircleMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbCircleMyProfile);

        JLabel lHobbyMyProfile = new JLabel("趣味");
		lHobbyMyProfile.setBounds(w/8,43*h/60,w/6,h/30);
		lHobbyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lHobbyMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lHobbyMyProfile);

        JTextField tfHobbyMyProfile = new JTextField("");
        tfHobbyMyProfile.setBounds(w/3,43*h/60,w/2,h/30);
        tfHobbyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfHobbyMyProfile);

        JLabel lLineIdMyProfile = new JLabel("LINEのID");
		lLineIdMyProfile.setBounds(w/8,46*h/60,w/6,h/30);
		lLineIdMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lLineIdMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lLineIdMyProfile);

        JTextField tfLineIdMyProfile = new JTextField("");
        tfLineIdMyProfile.setBounds(w/3,46*h/60,w/2,h/30);
        tfLineIdMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfLineIdMyProfile);

        JButton bChangeMyProfile = new JButton("変更確定");
        bChangeMyProfile.setBounds(w/4,49*h/60,w/2,h/20);
        bChangeMyProfile.addActionListener(this);
        bChangeMyProfile.setActionCommand("ヘルプmenu");
        bChangeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bChangeMyProfile);

		cardPanel.add(card,"MyProfile");
	}

	public void howToUse() {
		JPanel card=new JPanel();
		card.setLayout(null);

		//使い方
		String explain = "aaaaaaaaaaaa\naaaaaaaaa\naaaaaaa\naaa\na\na\n\na\naa\naa\naa\naa\naa\nb\na\nq\nww\nrr\nf";

		JLabel lTitleHtu = new JLabel("使い方");
		lTitleHtu.setBounds(w/4,h/15,w/2,h/10);
		lTitleHtu.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/20));
		lTitleHtu.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleHtu);

        JTextArea taexpHtu = new JTextArea(explain);
        taexpHtu.setEditable(false);
        //taexpHtu.setBounds(w/10+10,h/6+10,3*w/4,5*h/10);
        taexpHtu.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        // スクロールバー
        JScrollPane sp = new JScrollPane(taexpHtu);
		sp.setBounds(w/10+10,h/6+10,3*w/4,5*h/10);
		card.add(sp);
        //card.add(taexpHtu);

        Rect rect = new Rect();
        rect.setBounds(0,0,w,h);
        card.add(rect);


        cardPanel.add(card,"howToUse");
	}

	public void setup() {
		JPanel card=new JPanel();
		card.setLayout(null);


		JLabel lTitleSetup = new JLabel("設定");
		lTitleSetup.setBounds(w/4,h/15,w/2,h/10);
		lTitleSetup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/20));
		lTitleSetup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleSetup);

        JLabel lProfileSetup = new JLabel("個人プロフィールの公開・非公開");
        lProfileSetup.setBounds(w/10,2*h/10,w/2,h/10);
        lProfileSetup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lProfileSetup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lProfileSetup);

        JRadioButton rbProfileSetup = new JRadioButton("公開", true);
        rbProfileSetup.setBounds(7*w/10,13*h/65,w/5,h/10);
        rbProfileSetup.addChangeListener(this);
        rbProfileSetup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(rbProfileSetup);

        JLabel lDeleteAccountSetup = new JLabel("アカウント削除");
        lDeleteAccountSetup.setBounds(w/10,3*h/10,w/2,h/10);
        lDeleteAccountSetup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lDeleteAccountSetup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lDeleteAccountSetup);

        JButton bDeleteAccountSetup = new JButton("削除");
        bDeleteAccountSetup.setBounds(7*w/10,21*h/65,w/7,h/20);
        bDeleteAccountSetup.addActionListener(this);
        bDeleteAccountSetup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/35));
        card.add(bDeleteAccountSetup);

        cardPanel.add(card,"setup");
	}

	public void invite() {
		JPanel card=new JPanel();
		card.setLayout(null);

		//タイトル
		JLabel ltitleinvite = new JLabel("グループ招待");
		ltitleinvite.setBounds(w/4,h/50,w/2,h/10);
		ltitleinvite.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		ltitleinvite.setHorizontalAlignment(JLabel.CENTER);
        card.add(ltitleinvite);

        //画像貼り付け
        ImageIcon IIimageinvite = new ImageIcon("Black.jpg");
        JLabel liconinvite = new JLabel(IIimageinvite);
        liconinvite.setBounds(w/4,3*h/20,w/2,h/5);
        liconinvite.setHorizontalAlignment(JLabel.CENTER);
        card.add(liconinvite);

        //プロフィール確認ボタン
        JButton bprofileinvite = new JButton("プロフィールを確認する");
        bprofileinvite.setBounds(w/4,7*h/20,w/2,h/20);
        //bprofileinvite.addActionListener(this);
        card.add(bprofileinvite);

        //参加するボタン
        JButton bokinvite = new JButton("参加する！");
        bokinvite.setBounds(w/4,9*h/20,w/2,h/20);
        //bokinvite.addActionListener(this);
        card.add(bokinvite);

        //参加しないボタン
        JButton bnoinvite = new JButton("参加しない");
        bnoinvite.setBounds(w/4,11*h/20,w/2,h/20);
        //bnoinvite.addActionListener(this);
        card.add(bnoinvite);

        JLabel llabelinvite = new JLabel("〇〇さんに招待されました！");
        llabelinvite.setBounds(0,13*h/20,w,h/20);
        llabelinvite.setHorizontalAlignment(JLabel.CENTER);
        card.add(llabelinvite);

        getContentPane().add(card,null);
        //menu

        //通知


        cardPanel.add(card,"invite");
	}

	public void home() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel ltitlehome = new JLabel("HOME");
		ltitlehome.setBounds(w/4,h/50,w/2,h/10);
		ltitlehome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		ltitlehome.setHorizontalAlignment(JLabel.CENTER);
        card.add(ltitlehome);

        JButton bsearchhome = new JButton("Search");
        bsearchhome.setBounds(w/12,h/30,w/5,h/15);
        card.add(bsearchhome);

        JButton bmenuhome = new JButton("MENU");
        bmenuhome.setBounds(5*w/7,h/30,w/5,h/15);
        card.add(bmenuhome);

        ImageIcon IIimagehome1 = new ImageIcon("test.jpg");
        JButton biconhome1 = new JButton(" 〇〇 3年",IIimagehome1);
        biconhome1.setBounds(w/4,3*h/20,w/2,h/10);
        card.add(biconhome1);

        ImageIcon IIimagehome2 = new ImageIcon("test.jpg");
        JButton biconhome2 = new JButton(" 〇〇 4年",IIimagehome2);
        biconhome2.setBounds(w/4,7*h/20,w/2,h/10);
        card.add(biconhome2);

        ImageIcon IIimagehome3 = new ImageIcon("test.jpg");
        JButton biconhome3 = new JButton(" 〇〇 1年",IIimagehome3);
        biconhome3.setBounds(w/4,11*h/20,w/2,h/10);
        card.add(biconhome3);

        JLabel lbackhome = new JLabel("back");
        lbackhome.setBounds(w/6,15*h/20,w/2,h/20);
        card.add(lbackhome);

        ImageIcon IIlefthome = new ImageIcon("left.jpeg");
        JButton blefthome = new JButton(IIlefthome);
        blefthome.setBounds(w/4,15*h/20,w/11,h/20);
        card.add(blefthome);

        ImageIcon IIrighthome = new ImageIcon("right.jpeg");
        JButton brighthome = new JButton(IIrighthome);
        brighthome.setBounds(2*w/3,15*h/20,w/11,h/20);
        card.add(brighthome);

        JLabel lnexthome = new JLabel("next");
        lnexthome.setBounds(7*w/9,15*h/20,w/2,h/20);
        card.add(lnexthome);


        cardPanel.add(card,"home");
	}

	public void change() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel ltitlechange = new JLabel("アカウント切り替え");
		ltitlechange.setBounds(w/5,h/50,3*w/5,h/10);
		ltitlechange.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		ltitlechange.setHorizontalAlignment(JLabel.CENTER);
        card.add(ltitlechange);

        ImageIcon IIleftchange = new ImageIcon("left.jpeg");
        JButton bsearchchange = new JButton(IIleftchange);
        bsearchchange.setBounds(w/14,h/30,w/11,h/20);
        card.add(bsearchchange);


        ImageIcon IIimagechange1 = new ImageIcon("add.jpeg");
        JButton biconchange1 = new JButton("グループ作成",IIimagechange1);
        biconchange1.setBounds(w/4,3*h/20,w/2,h/10);
        card.add(biconchange1);

        ImageIcon IIimagechange2 = new ImageIcon("test.jpg");
        JButton biconchange2 = new JButton(" 〇〇 ",IIimagechange2);
        biconchange2.setBounds(w/4,7*h/20,w/2,h/10);
        card.add(biconchange2);

        ImageIcon IIimagechange3 = new ImageIcon("test.jpg");
        JButton biconchange3 = new JButton(" 〇〇 ",IIimagechange3);
        biconchange3.setBounds(w/4,11*h/20,w/2,h/10);
        card.add(biconchange3);

        JLabel lbackchange = new JLabel("back");
        lbackchange.setBounds(w/6,15*h/20,w/2,h/20);
        card.add(lbackchange);

        JButton bleftchange = new JButton(IIleftchange);
        bleftchange.setBounds(w/4,15*h/20,w/11,h/20);
        card.add(bleftchange);

        ImageIcon IIrightchange = new ImageIcon("right.jpeg");
        JButton brightchange = new JButton(IIrightchange);
        brightchange.setBounds(2*w/3,15*h/20,w/11,h/20);
        card.add(brightchange);

        JLabel lnextchange = new JLabel("next");
        lnextchange.setBounds(7*w/9,15*h/20,w/2,h/20);
        card.add(lnextchange);


        cardPanel.add(card,"change");
	}

	public void searchUser() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bBackSearchUser = new JButton("←");
        bBackSearchUser.setBounds(w/30,h/20,w/6,h/15);
        bBackSearchUser.addActionListener(this);
        bBackSearchUser.setActionCommand("ヘルプmenu");
        bBackSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bBackSearchUser);

		JLabel lTitleSearchUser = new JLabel("検索");
		lTitleSearchUser.setBounds(w/4,h/60,w/2,2*h/15);
		lTitleSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/10));
		lTitleSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleSearchUser);

        JLabel lGenderSearchUser = new JLabel("性別");
		lGenderSearchUser.setBounds(w/8,5*h/25,w/6,2*h/25);
		lGenderSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lGenderSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGenderSearchUser);

        JComboBox<String> cbGenderSearchUser = new JComboBox<String>(Sex);
        cbGenderSearchUser.setBounds(w/3,5*h/25,w/2,2*h/25);
		cbGenderSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbGenderSearchUser);

        JLabel lGradeSearchUser = new JLabel("学年");
		lGradeSearchUser.setBounds(w/8,8*h/25,w/6,2*h/25);
		lGradeSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lGradeSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGradeSearchUser);

        JComboBox<String> cbGradeSearchUser = new JComboBox<String>(Grade);
        cbGradeSearchUser.setBounds(w/3,8*h/25,w/2,2*h/25);
		cbGradeSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbGradeSearchUser);

        JLabel lFacultySearchUser = new JLabel("学部");
		lFacultySearchUser.setBounds(w/8,11*h/25,w/6,2*h/25);
		lFacultySearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lFacultySearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lFacultySearchUser);

        JComboBox<String> cbFacultySearchUser = new JComboBox<String>(Faculty);
        cbFacultySearchUser.setBounds(w/3,11*h/25,w/2,2*h/25);
		cbFacultySearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbFacultySearchUser);

        JLabel lBirthSearchUser = new JLabel("出身");
		lBirthSearchUser.setBounds(w/8,14*h/25,w/6,2*h/25);
		lBirthSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lBirthSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lBirthSearchUser);

        JComboBox<String> cbBirthSearchUser = new JComboBox<String>(Birthplace);
        cbBirthSearchUser.setBounds(w/3,14*h/25,w/2,2*h/25);
        cbBirthSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbBirthSearchUser);

        JLabel lCircleSearchUser = new JLabel("サークル");
		lCircleSearchUser.setBounds(w/8,17*h/25,w/6,2*h/25);
		lCircleSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCircleSearchUser);

        JComboBox<String> cbCircleSearchUser = new JComboBox<String>(Circle);
        cbCircleSearchUser.setBounds(w/3,17*h/25,w/2,2*h/25);
        cbCircleSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbCircleSearchUser);

        JButton bSearchSeachUser = new JButton("検索");
        bSearchSeachUser.setBounds(3*w/10,20*h/25,2*w/5,h/10);
        bSearchSeachUser.addActionListener(this);
        bSearchSeachUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bSearchSeachUser);

        cardPanel.add(card,"SearchUser");
	}

	public void viewGroup() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bBackViewGroup = new JButton("←");
        bBackViewGroup.setBounds(w/30,h/30,w/6,h/15);
        bBackViewGroup.addActionListener(this);
        bBackViewGroup.setActionCommand("ヘルプmenu");
        bBackViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bBackViewGroup);

		JLabel lGroupNameViewGroup = new JLabel("グループ名");
		lGroupNameViewGroup.setBounds(w/4,h/60,w/2,h/20);
		lGroupNameViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		lGroupNameViewGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupNameViewGroup);

        JLabel lGroupPhotoViewGroup = new JLabel("グル写真");
        lGroupPhotoViewGroup.setBounds(2*w/5,5*h/60,w/5,h/10);
        lGroupPhotoViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        lGroupPhotoViewGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupPhotoViewGroup);

        JLabel lGroupProfileViewGroup=new JLabel("プロフィール");
        lGroupProfileViewGroup.setBounds(3*w/5,7*h/60,w/3,h/15);
        lGroupProfileViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lGroupPhotoViewGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupProfileViewGroup);

        JLabel[] lMemberPhotoViewGroup = new JLabel[5];
        JButton[] bMemberProfileViewGroup = new JButton[5];
        for(int i=0;i<5;i++) {
        	lMemberPhotoViewGroup[i] = new JLabel("メンバ写真");
            lMemberPhotoViewGroup[i].setBounds(w/6,(11+7*i)*h/60,w/6,h/12);
            lMemberPhotoViewGroup[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            lMemberPhotoViewGroup[i].setHorizontalAlignment(JLabel.CENTER);
            card.add(lMemberPhotoViewGroup[i]);

            bMemberProfileViewGroup[i]=new JButton("プロフィール");
            bMemberProfileViewGroup[i].setBounds(w/3,(11+7*i)*h/60,2*w/5,h/12);
            bMemberProfileViewGroup[i].addActionListener(this);
            bMemberProfileViewGroup[i].setActionCommand("label");
            bMemberProfileViewGroup[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
            card.add(bMemberProfileViewGroup[i]);
        }

        JButton bGoodViewGroup=new JButton("いいね");
        bGoodViewGroup.setBounds(2*w/5,45*h/60,w/5,h/15);
        bGoodViewGroup.addActionListener(this);
        bGoodViewGroup.setActionCommand("label");
        bGoodViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/27));
        card.add(bGoodViewGroup);

        JButton bHomeViewGroup=new JButton("HOME");
        bHomeViewGroup.setBounds(w/5,51*h/60,w/5,h/15);
        bHomeViewGroup.addActionListener(this);
        bHomeViewGroup.setActionCommand("label");
        bHomeViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHomeViewGroup);

        JButton bInformViewGroup=new JButton("通知");
        bInformViewGroup.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInformViewGroup.addActionListener(this);
        bInformViewGroup.setActionCommand("label");
        bInformViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInformViewGroup);

		cardPanel.add(card,"viewGroup");
	}

	public void myGroupProfile() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bBackMyGroupProfile = new JButton("←");
        bBackMyGroupProfile.setBounds(w/30,h/60,w/6,h/15);
        bBackMyGroupProfile.addActionListener(this);
        bBackMyGroupProfile.setActionCommand("ヘルプmenu");
        bBackMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bBackMyGroupProfile);

        JLabel lTitleMyGroupProfile = new JLabel("グループプロフィール");
		lTitleMyGroupProfile.setBounds(w/5,h/60,3*w/5,h/15);
		lTitleMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMyGroupProfile);

        JButton bPhotoMyGroupProfile = new JButton("写真");
        bPhotoMyGroupProfile.setBounds(w/4,6*h/60,w/2,h/6);
        bPhotoMyGroupProfile.addActionListener(this);
        bPhotoMyGroupProfile.setActionCommand("ヘルプmenu");
        bPhotoMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bPhotoMyGroupProfile);

        JLabel lNameMyGroupProfile = new JLabel("グループ名");
		lNameMyGroupProfile.setBounds(w/9,18*h/60,w/5,h/20);
		lNameMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lNameMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMyGroupProfile);

        JTextField tfNameMyGroupProfile = new JTextField("");
        tfNameMyGroupProfile.setBounds(w/3,18*h/60,w/2,h/20);
        tfNameMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfNameMyGroupProfile);

        JLabel lRelationMyGroupProfile = new JLabel("関係性");
		lRelationMyGroupProfile.setBounds(w/8,22*h/60,w/6,h/20);
		lRelationMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lRelationMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lRelationMyGroupProfile);

        JTextField tfRelationMyGroupProfile = new JTextField("");
        tfRelationMyGroupProfile.setBounds(w/3,22*h/60,w/2,h/20);
        tfRelationMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfRelationMyGroupProfile);

        JLabel lPurposeMyGroupProfile = new JLabel("目的");
		lPurposeMyGroupProfile.setBounds(w/8,26*h/60,w/6,h/20);
		lPurposeMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lPurposeMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lPurposeMyGroupProfile);

        JComboBox<String> cbPurposeMyGroupProfile = new JComboBox<String>(Purpose);
        cbPurposeMyGroupProfile.setBounds(w/3,26*h/60,w/2,h/20);
        cbPurposeMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbPurposeMyGroupProfile);


        JLabel lCommentMyGroupProfile = new JLabel("ひとこと");
		lCommentMyGroupProfile.setBounds(w/8,30*h/60,w/6,h/20);
		lCommentMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCommentMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCommentMyGroupProfile);

        JTextArea taCommentMyGroupProfile = new JTextArea("",15,3);
        taCommentMyGroupProfile.setBounds(w/3,30*h/60,w/2,h/5);
        taCommentMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(taCommentMyGroupProfile);

        JButton bQuitMyGroupProfile = new JButton("解散");
        bQuitMyGroupProfile.setBounds(3*w/11,44*h/60,5*w/22,h/20);
        bQuitMyGroupProfile.addActionListener(this);
        bQuitMyGroupProfile.setActionCommand("ヘルプmenu");
        bQuitMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bQuitMyGroupProfile);

        JButton bChangeMyProfile = new JButton("変更確定");
        bChangeMyProfile.setBounds(w/2,44*h/60,5*w/22,h/20);
        bChangeMyProfile.addActionListener(this);
        bChangeMyProfile.setActionCommand("ヘルプmenu");
        bChangeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bChangeMyProfile);

        JButton bHomeViewGroup=new JButton("HOME");
        bHomeViewGroup.setBounds(w/5,51*h/60,w/5,h/15);
        bHomeViewGroup.addActionListener(this);
        bHomeViewGroup.setActionCommand("label");
        bHomeViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHomeViewGroup);

        JButton bInformViewGroup=new JButton("通知");
        bInformViewGroup.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInformViewGroup.addActionListener(this);
        bInformViewGroup.setActionCommand("label");
        bInformViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInformViewGroup);

		cardPanel.add(card,"MyGroupProfile");
	}

	public void makeGroup() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bBackMyGroupProfile = new JButton("←");
        bBackMyGroupProfile.setBounds(w/30,h/60,w/6,h/15);
        bBackMyGroupProfile.addActionListener(this);
        bBackMyGroupProfile.setActionCommand("ヘルプmenu");
        bBackMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bBackMyGroupProfile);

        JLabel lTitleMyGroupProfile = new JLabel("グループプロフィール");
		lTitleMyGroupProfile.setBounds(w/5,h/60,3*w/5,h/15);
		lTitleMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMyGroupProfile);

        JButton bPhotoMyGroupProfile = new JButton("プロフィール写真を選択");
        bPhotoMyGroupProfile.setBounds(w/4,6*h/60,w/2,h/6);
        bPhotoMyGroupProfile.addActionListener(this);
        bPhotoMyGroupProfile.setActionCommand("ヘルプmenu");
        bPhotoMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bPhotoMyGroupProfile);

        JLabel lNameMyGroupProfile = new JLabel("グループ名");
		lNameMyGroupProfile.setBounds(w/9,18*h/60,w/5,h/20);
		lNameMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lNameMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMyGroupProfile);

        JTextField tfNameMyGroupProfile = new JTextField("");
        tfNameMyGroupProfile.setBounds(w/3,18*h/60,w/2,h/20);
        tfNameMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfNameMyGroupProfile);

        JLabel lRelationMyGroupProfile = new JLabel("関係性");
		lRelationMyGroupProfile.setBounds(w/8,22*h/60,w/6,h/20);
		lRelationMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lRelationMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lRelationMyGroupProfile);

        JTextField tfRelationMyGroupProfile = new JTextField("");
        tfRelationMyGroupProfile.setBounds(w/3,22*h/60,w/2,h/20);
        tfRelationMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfRelationMyGroupProfile);

        JLabel lPurposeMyGroupProfile = new JLabel("目的");
		lPurposeMyGroupProfile.setBounds(w/8,26*h/60,w/6,h/20);
		lPurposeMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lPurposeMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lPurposeMyGroupProfile);

        JComboBox<String> cbPurposeMyGroupProfile = new JComboBox<String>(Purpose);
        cbPurposeMyGroupProfile.setBounds(w/3,26*h/60,w/2,h/20);
        cbPurposeMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbPurposeMyGroupProfile);


        JLabel lCommentMyGroupProfile = new JLabel("ひとこと");
		lCommentMyGroupProfile.setBounds(w/8,30*h/60,w/6,h/20);
		lCommentMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCommentMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCommentMyGroupProfile);

        JTextArea taCommentMyGroupProfile = new JTextArea("",15,3);
        taCommentMyGroupProfile.setBounds(w/3,30*h/60,w/2,h/5);
        taCommentMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(taCommentMyGroupProfile);

        JButton bQuitMyGroupProfile = new JButton("メンバーを選択");
        bQuitMyGroupProfile.setBounds(w/3,44*h/60,w/3,h/15);
        bQuitMyGroupProfile.addActionListener(this);
        bQuitMyGroupProfile.setActionCommand("ヘルプmenu");
        bQuitMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bQuitMyGroupProfile);

        JButton bHomeViewGroup=new JButton("HOME");
        bHomeViewGroup.setBounds(w/5,51*h/60,w/5,h/15);
        bHomeViewGroup.addActionListener(this);
        bHomeViewGroup.setActionCommand("label");
        bHomeViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHomeViewGroup);

        JButton bInformViewGroup=new JButton("通知");
        bInformViewGroup.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInformViewGroup.addActionListener(this);
        bInformViewGroup.setActionCommand("label");
        bInformViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInformViewGroup);

		cardPanel.add(card,"MakeGroup");
	}

	public void finishAuthen(){
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lMessageFinishAuthen = new JLabel("本人確認が完了しました");
		lMessageFinishAuthen.setBounds(0,h/3,w,h/5);
		lMessageFinishAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/15));
		lMessageFinishAuthen.setHorizontalAlignment(JLabel.CENTER);
        card.add(lMessageFinishAuthen);

        JButton bnextFinishAuthen=new JButton("すすむ");
        bnextFinishAuthen.setBounds(w/4,3*h/4,w/2,h/10);
        bnextFinishAuthen.addActionListener(this);
        bnextFinishAuthen.setActionCommand("label");
        bnextFinishAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bnextFinishAuthen);

		cardPanel.add(card,"finishAuthen");
	}

	public void judging() {

		JPanel card=new JPanel();

		card.setLayout(null);

		JLabel lTitleJudge = new JLabel("本人確認");
		lTitleJudge.setBounds(w/4,h/20,w/2,h/10);
		lTitleJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/10));
		lTitleJudge.setHorizontalAlignment(JLabel.CENTER);
		card.add(lTitleJudge);

		JLabel lIdJudge = new JLabel("氏名");
		lIdJudge.setBounds(w/5,h/5,w/5,h/15);
		lIdJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		card.add(lIdJudge);

		JTextField tfIdJudge = new JTextField(20);
		tfIdJudge.setBounds(2*w/5,h/5,2*w/5,h/15);
		tfIdJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		card.add(tfIdJudge);

		JLabel lNumberJudge = new JLabel("学籍番号");
		lNumberJudge.setBounds(w/5,h/3,w/5,h/15);
		lNumberJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		card.add(lNumberJudge);

		JTextField tfPasswordJudge= new JTextField(20);
		tfPasswordJudge.setBounds(2*w/5,h/3,2*w/5,h/15);
		tfPasswordJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		card.add(tfPasswordJudge);

		JLabel lPicJudge = new JLabel("学生証");
		lPicJudge.setBounds(w/5,h/2,w/5,h/15);
		lPicJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		card.add(lPicJudge);

		JButton bChoiceJudge=new JButton("選択");
		bChoiceJudge.setBounds(2*w/5,h/2,w/8,h/15);
		bChoiceJudge.addActionListener(this);
		bChoiceJudge.setActionCommand("choice");
		bChoiceJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/50));
		card.add(bChoiceJudge);

		JTextField tfPicJudge= new JTextField(20);
		tfPicJudge.setBounds(7*w/13,h/2,w/3,h/15);
		tfPicJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		card.add(tfPicJudge);

		JLabel lErrorJudge = new JLabel("未入力の箇所があります");
		lErrorJudge.setBounds(0,13*h/20,w,h/15);
		lErrorJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lErrorJudge.setHorizontalAlignment(JLabel.CENTER);
		card.add(lErrorJudge);

		JButton bSendJudge=new JButton("送信");
		bSendJudge.setBounds(w/4,23*h/30,w/2,h/15);
		bSendJudge.addActionListener(this);
		bSendJudge.setActionCommand("送信");
		bSendJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		card.add(bSendJudge);

		cardPanel.add(card,"judge");
		}

	public void reply() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bBackReply = new JButton("←");
        bBackReply.setBounds(w/30,h/60,w/6,h/15);
        bBackReply.addActionListener(this);
        bBackReply.setActionCommand("ヘルプmenu");
        bBackReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bBackReply);

        JLabel lTitleReply = new JLabel("○○");
		lTitleReply.setBounds(w/4,h/60,w/2,h/15);
		lTitleReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleReply.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleReply);

        JButton bMainPhotoReply = new JButton("");
        bMainPhotoReply.setBounds(w/4,6*h/60,w/2,h/6);
        bMainPhotoReply.addActionListener(this);
        bMainPhotoReply.setActionCommand("ヘルプmenu");
        bMainPhotoReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bMainPhotoReply);

        JButton[] bSubPhotoReply = new JButton[4];
        for(int i=0;i<4;i++) {
        	bSubPhotoReply[i] = new JButton();
        	bSubPhotoReply[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
            bSubPhotoReply[i].addActionListener(this);
            bSubPhotoReply[i].setActionCommand("ヘルプmenu");
            bSubPhotoReply[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            card.add(bSubPhotoReply[i]);
        }

        JLabel lNameReply = new JLabel("名前");
		lNameReply.setBounds(0,25*h/60,w/3,h/30);
		lNameReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lNameReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lNameReply);

        JLabel lNameReply2 = new JLabel("○○");
        lNameReply2.setBounds(w/2,25*h/60,w/3,h/30);
        lNameReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lNameReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lNameReply2);

        JLabel lGenderReply = new JLabel("性別");
		lGenderReply.setBounds(0,28*h/60,w/3,h/30);
		lGenderReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGenderReply);

        JLabel lGenderReply2 = new JLabel("○○");
        lGenderReply2.setBounds(w/2,28*h/60,w/3,h/30);
		lGenderReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGenderReply2);

        JLabel lGradeReply = new JLabel("学年");
		lGradeReply.setBounds(0,31*h/60,w/3,h/30);
		lGradeReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGradeReply);

        JLabel lGradeReply2 = new JLabel("○○");
        lGradeReply2.setBounds(w/2,31*h/60,w/3,h/30);
		lGradeReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGradeReply2);

        JLabel lFacultyReply = new JLabel("学部");
		lFacultyReply.setBounds(0,34*h/60,w/3,h/30);
		lFacultyReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lFacultyReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lFacultyReply);

        JLabel lFacultyReply2 = new JLabel("○○");
        lFacultyReply2.setBounds(w/2,34*h/60,w/3,h/30);
        lFacultyReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lFacultyReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lFacultyReply2);

        JLabel lBirthReply = new JLabel("出身");
		lBirthReply.setBounds(0,37*h/60,w/3,h/30);
		lBirthReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lBirthReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lBirthReply);

        JLabel lBirthReply2 = new JLabel("○○");
        lBirthReply2.setBounds(w/2,37*h/60,w/3,h/30);
        lBirthReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lBirthReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lBirthReply2);

        JLabel lCircleReply = new JLabel("サークル");
		lCircleReply.setBounds(0,40*h/60,w/3,h/30);
		lCircleReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lCircleReply);

        JLabel lCircleReply2 = new JLabel("○○");
        lCircleReply2.setBounds(w/2,40*h/60,w/3,h/30);
        lCircleReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lCircleReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lCircleReply2);

        JLabel lHobbyReply = new JLabel("趣味");
		lHobbyReply.setBounds(0,43*h/60,w/3,h/30);
		lHobbyReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lHobbyReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lHobbyReply);

        JLabel lHobbyReply2 = new JLabel("○○");
        lHobbyReply2.setBounds(w/2,43*h/60,w/3,h/30);
        lHobbyReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lHobbyReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lHobbyReply2);

        JButton bGoodReply = new JButton("いいね");
        bGoodReply.setBounds(w/4,49*h/60,w/4,h/20);
        bGoodReply.addActionListener(this);
        bGoodReply.setActionCommand("ヘルプmenu");
        bGoodReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bGoodReply);
        
        JButton bBadReply = new JButton("断る");
        bBadReply.setBounds(w/2,49*h/60,w/4,h/20);
        bBadReply.addActionListener(this);
        bBadReply.setActionCommand("ヘルプmenu");
        bBadReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bBadReply);

		cardPanel.add(card,"reply");
	}
	
	public void good() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bBackGood = new JButton("←");
        bBackGood.setBounds(w/30,h/60,w/6,h/15);
        bBackGood.addActionListener(this);
        bBackGood.setActionCommand("ヘルプmenu");
        bBackGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bBackGood);

        JLabel lTitleGood = new JLabel("○○");
		lTitleGood.setBounds(w/4,h/60,w/2,h/15);
		lTitleGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleGood.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleGood);

        JButton bMainPhotoGood = new JButton("");
        bMainPhotoGood.setBounds(w/4,6*h/60,w/2,h/6);
        bMainPhotoGood.addActionListener(this);
        bMainPhotoGood.setActionCommand("ヘルプmenu");
        bMainPhotoGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bMainPhotoGood);

        JButton[] bSubPhotoGood = new JButton[4];
        for(int i=0;i<4;i++) {
        	bSubPhotoGood[i] = new JButton();
        	bSubPhotoGood[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
            bSubPhotoGood[i].addActionListener(this);
            bSubPhotoGood[i].setActionCommand("ヘルプmenu");
            bSubPhotoGood[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            card.add(bSubPhotoGood[i]);
        }

        JLabel lNameGood = new JLabel("名前");
		lNameGood.setBounds(0,25*h/60,w/3,h/30);
		lNameGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lNameGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lNameGood);

        JLabel lNameGood2 = new JLabel("○○");
        lNameGood2.setBounds(w/2,25*h/60,w/3,h/30);
        lNameGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lNameGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lNameGood2);

        JLabel lGenderGood = new JLabel("性別");
		lGenderGood.setBounds(0,28*h/60,w/3,h/30);
		lGenderGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGenderGood);

        JLabel lGenderGood2 = new JLabel("○○");
        lGenderGood2.setBounds(w/2,28*h/60,w/3,h/30);
		lGenderGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGenderGood2);

        JLabel lGradeGood = new JLabel("学年");
		lGradeGood.setBounds(0,31*h/60,w/3,h/30);
		lGradeGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGradeGood);

        JLabel lGradeGood2 = new JLabel("○○");
        lGradeGood2.setBounds(w/2,31*h/60,w/3,h/30);
		lGradeGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGradeGood2);

        JLabel lFacultyGood = new JLabel("学部");
		lFacultyGood.setBounds(0,34*h/60,w/3,h/30);
		lFacultyGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lFacultyGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lFacultyGood);

        JLabel lFacultyGood2 = new JLabel("○○");
        lFacultyGood2.setBounds(w/2,34*h/60,w/3,h/30);
        lFacultyGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lFacultyGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lFacultyGood2);

        JLabel lBirthGood = new JLabel("出身");
		lBirthGood.setBounds(0,37*h/60,w/3,h/30);
		lBirthGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lBirthGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lBirthGood);

        JLabel lBirthGood2 = new JLabel("○○");
        lBirthGood2.setBounds(w/2,37*h/60,w/3,h/30);
        lBirthGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lBirthGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lBirthGood2);

        JLabel lCircleGood = new JLabel("サークル");
		lCircleGood.setBounds(0,40*h/60,w/3,h/30);
		lCircleGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lCircleGood);

        JLabel lCircleGood2 = new JLabel("○○");
        lCircleGood2.setBounds(w/2,40*h/60,w/3,h/30);
        lCircleGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lCircleGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lCircleGood2);

        JLabel lHobbyGood = new JLabel("趣味");
		lHobbyGood.setBounds(0,43*h/60,w/3,h/30);
		lHobbyGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lHobbyGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lHobbyGood);

        JLabel lHobbyGood2 = new JLabel("○○");
        lHobbyGood2.setBounds(w/2,43*h/60,w/3,h/30);
        lHobbyGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lHobbyGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lHobbyGood2);

        /*JButton bGoodGood = new JButton("いいね"); //いいねをしたかどうかで条件分岐(bGoodGood or lGoodGood2)
        bGoodGood.setBounds(w/4,49*h/60,w/2,h/20);
        bGoodGood.addActionListener(this);
        bGoodGood.setActionCommand("ヘルプmenu");
        bGoodGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bGoodGood);*/

        JLabel lGoodGood2 = new JLabel("既にいいねしました");
        lGoodGood2.setBounds(0,49*h/60,w,h/20);
        lGoodGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lGoodGood2.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGoodGood2);
        
		cardPanel.add(card,"good");
	}
	
	public void actionPerformed(ActionEvent ae) {
	}

	public void stateChanged(ChangeEvent e) {
		JRadioButton cb = (JRadioButton)e.getSource();
		String message = cb.getText();
		if(message=="公開" || message=="非公開") {
			if (cb.isSelected()) {
				cb.setText("公開");
			} else {
				cb.setText("非公開");
			}
		}
	}

    public static void main(String[] args) {
    	new Client();
    }


	//通知ウィンドウ内部クラスですよ
    public class Notification extends JFrame implements ActionListener{

    	public Notification() {
    		//閉じるボタン・リサイズ
			JButton end = new JButton("閉じる");
			end.setPreferredSize(new Dimension(100, 50));
			end.addActionListener(this);

			//backボタン nextボタン
			JButton next = new JButton("next");
			JButton back = new JButton("back");
			next.setPreferredSize(new Dimension(100, 50));
			next.addActionListener(this);
			back.setPreferredSize(new Dimension(100, 50));
			back.addActionListener(this);

			//パネル
		    JPanel p1 = new JPanel();
		    JPanel p2 = new JPanel();
		    FlowLayout layout = new FlowLayout();
		    layout.setAlignment(FlowLayout.RIGHT);
		    p1.setLayout(layout);
		    p2.setLayout(null);

		    p1.add(end);
		    p2.add(next);
		    p2.add(back);
		    getContentPane().add(p1, BorderLayout.PAGE_END);
		    getContentPane().add(p2, null);

		    //タイトルなど
    		setTitle("通知");
    		setSize(w, h);
    		setVisible(true);
    		setResizable(false);
    	}

		public void actionPerformed(ActionEvent e) {
			//閉じるボタンでウィンドウを閉じる
			Component c = (Component)e.getSource();
			Window w = SwingUtilities.getWindowAncestor(c);
			w.dispose();
		}

    }
    /*
    //検索ウィンドウ内部クラス
    public class search extends JFrame implements ActionListener{
    	public search() {
    		JLabel menu = new JLabel("検索");
    		JComboBox<String> sex = new JComboBox<String>(Sex);
    		JLabel sexL = new JLabel("性別");
    		JComboBox<String> grade = new JComboBox<String>(Grade);
    		JLabel gradeL = new JLabel("学年");
    		JComboBox<String> faculty = new JComboBox<String>(Faculty);
    		JLabel facultyL = new JLabel("学部");
    		JComboBox<String> birthplace = new JComboBox<String>(Birthplace);
    		JLabel birthplaceL = new JLabel("出身");
    		JComboBox<String> circle = new JComboBox<String>(Circle);
    		JLabel circleL = new JLabel("サークル");

    		JPanel p = new JPanel();
    		p.add(menu);
    		p.add(sexL);
    		p.add(sex);
    		p.add(gradeL);
    		p.add(grade);
    		p.add(facultyL);
    		p.add(faculty);
    		p.add(birthplaceL);
    		p.add(birthplace);
    		p.add(circleL);
    		p.add(circle);

    		getContentPane().add(p, BorderLayout.CENTER);

    		setSize(w, h);
    		setVisible(true);
    		setResizable(false);

    	}

		public void actionPerformed(ActionEvent e) {
			//閉じるボタンでウィンドウを閉じる
			Component c = (Component)e.getSource();
			Window w = SwingUtilities.getWindowAncestor(c);
			w.dispose();
		}
    }
    //検索ウィンドウ内部クラスここまで
     *
     */

    //四角形描画クラス
    public class Rect extends Canvas{
    	public void paint(Graphics g) {
    		super.paint(g);
    		Graphics2D g2 = (Graphics2D)this.getGraphics();
    		Rectangle rect = new Rectangle();
    		rect.setRect(w/10,h/10,8*w/10,6*h/10);	//(x座標、y座標、幅、高さ)
    		g2.setColor(Color.GREEN);
    	    g2.setStroke(new BasicStroke(5.0f));
    	    g2.draw(rect);
    	}
    }
}
