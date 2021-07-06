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

	//プロフィールの検索で選ぶやつ
	String[] Sex = {"男性", "女性", "その他"};
	String[] Grade = {"1", "2", "3", "4"};
	String[] Faculty = {"経営", "経済", "教育", "理工", "都市科学"};
	String[] Birthplace = {"北海道・東北", "関東", "中部", "近畿", "中国", "四国", "九州", "海外"};
	String[] Circle = {"テニス", "運動", "文化"};

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

	    //"login"のところを違う画面の名前に変えれば、それが一番最初の画面になる。
	    layout.show(cardPanel,"setup");
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

        JTextField tfNumberGather5 = new JTextField("");
        tfNumberGather5.setBounds(w/3,25*h/60,w/2,h/30);
        tfNumberGather5.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfNumberGather5);

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
        cbGradeMyProfile.addItem("1");
        cbGradeMyProfile.addItem("2");
        cbGradeMyProfile.addItem("3");
        cbGradeMyProfile.addItem("4");
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

        JLabel lHobbyMyProfile = new JLabel("趣味");
		lHobbyMyProfile.setBounds(w/8,40*h/60,w/6,h/30);
		lHobbyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lHobbyMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lHobbyMyProfile);

        JTextField tfHobbyMyProfile = new JTextField("");
        tfHobbyMyProfile.setBounds(w/3,40*h/60,w/2,h/30);
        tfHobbyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfHobbyMyProfile);

        JLabel lLineIdMyProfile = new JLabel("LINEのID");
		lLineIdMyProfile.setBounds(w/8,43*h/60,w/6,h/30);
		lLineIdMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lLineIdMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lLineIdMyProfile);

        JTextField tfLineIdMyProfile = new JTextField("");
        tfLineIdMyProfile.setBounds(w/3,43*h/60,w/2,h/30);
        tfLineIdMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfLineIdMyProfile);

        JButton bChangeMyProfile = new JButton("変更確定");
        bChangeMyProfile.setBounds(w/4,46*h/60,w/2,h/20);
        bChangeMyProfile.addActionListener(this);
        bChangeMyProfile.setActionCommand("ヘルプmenu");
        bChangeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bChangeMyProfile);

		cardPanel.add(card,"MyProfile");
	}

    public void actionPerformed(ActionEvent ae) {
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

        JButton rbDeleteAccountSetup = new JButton("削除");
        rbDeleteAccountSetup.setBounds(7*w/10,21*h/65,w/7,h/20);
        rbDeleteAccountSetup.addActionListener(this);
        rbDeleteAccountSetup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/35));
        card.add(rbDeleteAccountSetup);

        cardPanel.add(card,"setup");
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
    	Client client = new Client();
		client.new search(); //test(削除可)
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