import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	int MAX=100;
	UserInfo myUserInfo=new UserInfo();
	GroupInfo myGroupInfo= new GroupInfo();
	UserInfo[] nowShowingUsers=new UserInfo[3];//ホーム画面や通知画面で使う
	GroupInfo[] nowShowingGroups=new GroupInfo[3];
	UserInfo nowShowingUser=new UserInfo();//イイネ画面とかで使う
	GroupInfo nowShowingGroup=new GroupInfo();
	int nowPage=1;
	boolean isNowUsingGroupAccount=false;
	String previousPage="HOME";

	//プロフィールの検索で選ぶやつ
	String[] Sex = {"男性", "女性", "その他"};
	String[] Grade = {"1", "2", "3", "4"};
	String[] Faculty = {"経営", "経済", "教育", "理工", "都市科学"};
	String[] Birthplace = {"北海道・東北", "関東", "中部", "近畿", "中国", "四国", "九州", "海外"};
	String[] Circle = {"テニス", "運動", "文化"};
	String[] Purpose = {"男子と仲良くなりたい","女子と仲良くなりたい"};
	String[] HowMany = {"2","3","4","5"};
	String[] yesNo= {"はい","いいえ"};

	//画像
	ImageIcon iRight=new ImageIcon("./img/right.jpeg");
	ImageIcon iLeft=new ImageIcon("./img/left.jpeg");
	ImageIcon iAdd=new ImageIcon("./img/Add.jpeg");
	ImageIcon iTest=new ImageIcon("./img/test.jpg");

	//アクションリスナーでいじるために一部の変数を外部変数に
	JTextField tfIdLogin = new JTextField(20);
	JTextField tfPasswordLogin = new JTextField(20);
	JLabel lMessageLogin = new JLabel("学籍番号もしくはパスワードが正しくありません");

	JTextField tfIdNew_r = new JTextField(20);
	JTextField tfPasswordNew_r = new JTextField(20);
	JTextField tfPasswordConfNew_r = new JTextField(20);
	JLabel lMessageNew_r = new JLabel("パスワードが一致していません");

	JTextField tfNameJudge = new JTextField(20);
	JTextField tfNumberJudge= new JTextField(20);
	JLabel lPicOutputJudge= new JLabel("<html><body>本人確認に<br />学生証を使用します<br />選択ボタンを押して<br />学生証の写真を<br />送信してください</body></html>");
	JLabel lErrorJudge = new JLabel("未入力の箇所があります");

	JButton bIconHome[]=new JButton[3];

	JLabel lNameReply = new JLabel("○○");
    JLabel lMainPhotoReply = new JLabel("");
    JLabel[] lSubPhotoReply = new JLabel[4];
    JLabel lGenderReply2 = new JLabel("○○");
    JLabel lGradeReply2 = new JLabel("○○");
    JLabel lFacultyReply2 = new JLabel("○○");
    JLabel lBirthReply2 = new JLabel("○○");
    JLabel lCircleReply2 = new JLabel("○○");
    JLabel lHobbyReply2 = new JLabel("○○");

    JLabel lGroupNameReplyGroup = new JLabel("グループ名");
    JLabel lGroupPhotoReplyGroup = new JLabel("グル写真");
    JLabel lGroupProfileReplyGroup=new JLabel("プロフィール");
    JButton[] bMemberProfileReplyGroup = new JButton[5];

    JLabel lNameGood = new JLabel("○○");
    JLabel lMainPhotoGood = new JLabel("");
    JLabel[] lSubPhotoGood = new JLabel[4];
    JLabel lGenderGood2 = new JLabel("○○");
    JLabel lGradeGood2 = new JLabel("○○");
    JLabel lFacultyGood2 = new JLabel("○○");
    JLabel lBirthGood2 = new JLabel("○○");
    JLabel lCircleGood2 = new JLabel("○○");
    JLabel lHobbyGood2 = new JLabel("○○");
    JButton bGoodGood = new JButton("いいね");
    JLabel lGoodGood = new JLabel("既にいいねしました");

    JLabel lIconMatching = new JLabel(iTest);
    JLabel lNameMatching = new JLabel("○○さんとマッチしました！");
    JLabel lIdMatching = new JLabel("LINEID:aaaaaaaaa");

    JComboBox<String> cbGenderSearchUser = new JComboBox<String>(Sex);
    JComboBox<String> cbGradeSearchUser = new JComboBox<String>(Grade);
    JComboBox<String> cbFacultySearchUser = new JComboBox<String>(Faculty);
    JComboBox<String> cbBirthSearchUser = new JComboBox<String>(Birthplace);
    JComboBox<String> cbCircleSearchUser = new JComboBox<String>(Circle);

    JComboBox<String> cbPurposeSearchGroup = new JComboBox<String>(Purpose);
    JComboBox<String> cbHowManySearchGroup = new JComboBox<String>(HowMany);

    JButton bMainPhotoMyProfile = new JButton("");
    JButton[] bSubPhotoMyProfile = new JButton[4];
    JTextField tfNameMyProfile = new JTextField("");
    JComboBox<String> cbGenderMyProfile = new JComboBox<String>(Sex);
    JComboBox<String> cbGradeMyProfile = new JComboBox<String>(Grade);
    JComboBox<String> cbFacultyMyProfile = new JComboBox<String>(Faculty);
    JComboBox<String> cbBirthMyProfile = new JComboBox<String>(Birthplace);
    JComboBox<String> cbCircleMyProfile = new JComboBox<String>(Circle);
    JTextField tfHobbyMyProfile = new JTextField("");
    JTextField tfLineIdMyProfile = new JTextField("");

    JButton bPersonalChange = new JButton("個人アカウント",iAdd);
    JButton[] bIconChange=new JButton[3];

    JButton bPhotoMakeGroup = new JButton("プロフィール写真を選択");
    JTextField tfNameMakeGroup = new JTextField("");
    JTextField tfRelationMakeGroup = new JTextField("");
    JComboBox<String> cbPurposeMakeGroup = new JComboBox<String>(Purpose);
    JTextArea taCommentMakeGroup = new JTextArea("",15,3);

    JTextField tfNumberGather[]=new JTextField[5];

    JLabel lIconInvite = new JLabel(iTest);
    JLabel lHostInvite = new JLabel("〇〇さんに招待されました！");

    JLabel lGroupNameViewGroup = new JLabel("グループ名");
    JLabel lGroupPhotoViewGroup = new JLabel("グル写真");
    JLabel lGroupProfileViewGroup=new JLabel("プロフィール");
    JButton[] bMemberProfileViewGroup = new JButton[5];
    JButton bGoodViewGroup=new JButton("いいね");
    JLabel lGoodViewGroup=new JLabel("すでに いいね しています");

    JButton bPhotoMyGroupProfile = new JButton("写真");
    JTextField tfNameMyGroupProfile = new JTextField("");
    JTextField tfRelationMyGroupProfile = new JTextField("");
    JComboBox<String> cbPurposeMyGroupProfile = new JComboBox<String>(Purpose);
    JTextArea taCommentMyGroupProfile = new JTextArea("",15,3);
    JButton bQuitMyGroupProfile = new JButton("解散");

    JRadioButton rbProfileSetup = new JRadioButton("公開", true);

    JButton[] bIconInviteInform=new JButton[3];

    JButton[] bIconGoodInform=new JButton[3];

    JButton[] bIconMatchingInform=new JButton[3];


	JPanel cardPanel;
	CardLayout layout;

	public Client(){
		super("TITLE");
		cardPanel = new JPanel();
	    layout = new CardLayout();
	    cardPanel.setLayout(layout);

	    //自分が作る画面のメソッド名をここに書く
	    login();
	    new_regis();
	    judging();
	    pleaseWait();
	    finishAuthen();
	    home();
	    reply();
	    replyGroup();
	    good();
	    matching();
	    searchUser();
	    searchGroup();
	    menu();
	    myProfile();
	    change();
	    makeGroup();
	    gathering();
	    invite();
	    viewGroup();
	    myGroupProfile();
	    setup();
	    howToUse();
	    inform();
	    inviteInform();
	    goodInform();
	    matchingInform();

	    //"login"のところを違う画面の名前に変えれば、それが一番最初の画面になる。
	    layout.show(cardPanel,"howToUse");
	    pack();
	    getContentPane().add(cardPanel, BorderLayout.CENTER);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(w,h);
	    setVisible(true);

	    for(int i=0;i<3;i++) {
	    	nowShowingUsers[i]=new UserInfo();
	    	nowShowingGroups[i]=new GroupInfo();
	    }

	    //goHome();
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

        tfIdLogin.setBounds(2*w/5,h/3,2*w/5,h/15);
        tfIdLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfIdLogin);

        JLabel lPasswordLogin = new JLabel("パスワード");
        lPasswordLogin.setBounds(w/6,7*h/15,w/5,h/15);
        lPasswordLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lPasswordLogin.setHorizontalAlignment(JLabel.CENTER);
        card.add(lPasswordLogin);

        tfPasswordLogin.setBounds(2*w/5,7*h/15,2*w/5,h/15);
        tfPasswordLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfPasswordLogin);

        JButton bLoginLogin=new JButton("ログイン");
        bLoginLogin.setBounds(w/4,19*h/30,w/2,h/15);
        bLoginLogin.addActionListener(this);
        bLoginLogin.setActionCommand("ログインlogin");//ボタンにラベル付け、ここのルールも決めたほうがいい
        bLoginLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bLoginLogin);

        JButton bNewAccountLogin=new JButton("新規作成");
        bNewAccountLogin.setBounds(w/4,22*h/30,w/2,h/15);
        bNewAccountLogin.addActionListener(this);
        bNewAccountLogin.setActionCommand("アカウント作成login");
        bNewAccountLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bNewAccountLogin);

        lMessageLogin.setBounds(w/10,26*h/30,4*w/5,h/30);
        lMessageLogin.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lMessageLogin.setHorizontalAlignment(JLabel.CENTER);
        lMessageLogin.setForeground(Color.RED);
        lMessageLogin.setVisible(false);
        card.add(lMessageLogin);


        //自分が作る画面に名前付け。メソッド名と同じじゃなくても大丈夫だけど、同じのほうが分かりやすいかも。
		cardPanel.add(card,"login");
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

        tfIdNew_r.setBounds(2*w/5,h/3,2*w/5,h/15);
        tfIdNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfIdNew_r);

        JLabel lPasswordNew_r = new JLabel("パスワード");
        lPasswordNew_r.setBounds(w/5,7*h/15,w/5,h/15);
        lPasswordNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(lPasswordNew_r);

        tfPasswordNew_r.setBounds(2*w/5,7*h/15,2*w/5,h/15);
        tfPasswordNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfPasswordNew_r);

        JLabel lPasswordconfNew_r = new JLabel("パスワード(確認用)");
        lPasswordconfNew_r.setBounds(w/10,9*h/15,w/2,h/15);
        lPasswordconfNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(lPasswordconfNew_r);

        tfPasswordConfNew_r.setBounds(2*w/5,9*h/15,2*w/5,h/15);
        tfPasswordConfNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(tfPasswordConfNew_r);

        JButton bNewAccountNew_r=new JButton("登録");
        bNewAccountNew_r.setBounds(w/4,23*h/30,w/2,h/15);
        bNewAccountNew_r.addActionListener(this);
        bNewAccountNew_r.setActionCommand("登録new_regis");
        bNewAccountNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bNewAccountNew_r);

        lMessageNew_r.setBounds(w/10,26*h/30,4*w/5,h/30);
        lMessageNew_r.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lMessageNew_r.setHorizontalAlignment(JLabel.CENTER);
        lMessageNew_r.setForeground(Color.RED);
        lMessageNew_r.setVisible(false);
        card.add(lMessageNew_r);

		cardPanel.add(card,"new_regis");
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
		lIdJudge.setHorizontalAlignment(JLabel.CENTER);
		card.add(lIdJudge);

		tfNameJudge.setBounds(2*w/5,h/5,2*w/5,h/15);
		tfNameJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		card.add(tfNameJudge);

		JLabel lNumberJudge = new JLabel("学籍番号");
		lNumberJudge.setBounds(w/5,h/3,w/5,h/15);
		lNumberJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lNumberJudge.setHorizontalAlignment(JLabel.CENTER);
		card.add(lNumberJudge);

		tfNumberJudge.setBounds(2*w/5,h/3,2*w/5,h/15);
		tfNumberJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		card.add(tfNumberJudge);

		JLabel lPicJudge = new JLabel("学生証");
		lPicJudge.setBounds(w/5,h/2,w/5,h/15);
		lPicJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lPicJudge.setHorizontalAlignment(JLabel.CENTER);
		card.add(lPicJudge);

		JButton bChoiceJudge=new JButton("選択");
		bChoiceJudge.setBounds(9*w/40,9*h/15,3*w/20,h/15);
		bChoiceJudge.addActionListener(this);
		bChoiceJudge.setActionCommand("選択judge");
		bChoiceJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/50));
		card.add(bChoiceJudge);

		lPicOutputJudge.setBounds(2*w/5,h/2,2*w/5,h/6);
		lPicOutputJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/23));
		lPicOutputJudge.setHorizontalAlignment(JLabel.CENTER);
		card.add(lPicOutputJudge);

		JButton bSendJudge=new JButton("送信");
		bSendJudge.setBounds(w/4,23*h/30,w/2,h/15);
		bSendJudge.addActionListener(this);
		bSendJudge.setActionCommand("送信judge");
		bSendJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		card.add(bSendJudge);

		lErrorJudge.setBounds(0,26*h/30,w,h/15);
		lErrorJudge.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lErrorJudge.setForeground(Color.RED);
		lErrorJudge.setHorizontalAlignment(JLabel.CENTER);
		lErrorJudge.setVisible(false);
		card.add(lErrorJudge);

		cardPanel.add(card,"judge");
		}

	public void pleaseWait() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel waitpl = new JLabel("<html><body>本人確認が完了するまでお待ちください<br />"
				+ "このままアプリを閉じても大丈夫です</body></html>");
		waitpl.setBounds(0,h/10,w,h/5);
		waitpl.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		waitpl.setHorizontalAlignment(JLabel.CENTER);
		card.add(waitpl);

		JLabel manual = new JLabel("<html><body>"
				+ "Step1 自分のプロフィールを<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作りましょう<br /><br />"
				+ "step2 気になるお相手を探して<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;「いいね」を押しましょう<br /><br />"
				+ "Step3 両想いでマッチング成立！<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LINEをしましょう<br /><br />"
				+ "Step4 個人だけでなくグループでも<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;マッチングできます</body></html>");
		manual.setBounds(w/8,h/5,3*w/4,5*h/10);
		manual.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		manual.setHorizontalAlignment(JLabel.CENTER);
		card.add(manual);

		cardPanel.add(card,"pleaseWait");
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
        bnextFinishAuthen.setActionCommand("すすむfinishAuthen");
        bnextFinishAuthen.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bnextFinishAuthen);

		cardPanel.add(card,"finishAuthen");
	}

	public void home() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleHome = new JLabel("HOME");
		lTitleHome.setBounds(w/4,h/50,w/2,h/10);
		lTitleHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		lTitleHome.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleHome);

        JButton bSearchHome = new JButton("Search");
        bSearchHome.setBounds(w/12,h/30,w/5,h/15);
        bSearchHome.addActionListener(this);
        bSearchHome.setActionCommand("検索home");
        card.add(bSearchHome);

        JButton bMenuHome = new JButton("MENU");
        bMenuHome.setBounds(5*w/7,h/30,w/5,h/15);
        bMenuHome.addActionListener(this);
        bMenuHome.setActionCommand("メニューhome");
        card.add(bMenuHome);


        for(int i=0;i<3;i++) {
        	bIconHome[i]=new JButton("プロフィール",iRight);
        	bIconHome[i].setBounds(w/4,(3+4*i)*h/20,w/2,h/10);
        	bIconHome[i].addActionListener(this);
        	bIconHome[i].setActionCommand("プロフィール"+String.valueOf(i)+"home");
        	bIconHome[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        	card.add(bIconHome[i]);
        }

        JLabel lbackhome = new JLabel("back");
        lbackhome.setBounds(w/6,15*h/20,w/2,h/20);
        card.add(lbackhome);

        JButton bLeftHome = new JButton(iLeft);
        bLeftHome.setBounds(w/4,15*h/20,w/11,h/20);
        bLeftHome.addActionListener(this);
    	bLeftHome.setActionCommand("前のページhome");
        card.add(bLeftHome);

        JButton bRightHome = new JButton(iRight);
        bRightHome.setBounds(2*w/3,15*h/20,w/11,h/20);
        bRightHome.addActionListener(this);
    	bRightHome.setActionCommand("次のページhome");
        card.add(bRightHome);

        JLabel lnexthome = new JLabel("next");
        lnexthome.setBounds(7*w/9,15*h/20,w/2,h/20);
        card.add(lnexthome);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"home");

	}

	public void reply() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("いいね通知へ");
        card.add(bPrePage);

		lNameReply.setBounds(w/4,h/60,w/2,h/15);
		lNameReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lNameReply.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameReply);

        lMainPhotoReply.setBounds(w/4,6*h/60,w/2,h/6);
        lMainPhotoReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(lMainPhotoReply);

        for(int i=0;i<4;i++) {
        	lSubPhotoReply[i] = new JLabel();
        	lSubPhotoReply[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
            lSubPhotoReply[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            card.add(lSubPhotoReply[i]);
        }

        JLabel lGenderReply = new JLabel("性別");
		lGenderReply.setBounds(0,25*h/60,w/3,h/30);
		lGenderReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGenderReply);

        lGenderReply2.setBounds(w/2,25*h/60,w/3,h/30);
		lGenderReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGenderReply2);

        JLabel lGradeReply = new JLabel("学年");
		lGradeReply.setBounds(0,28*h/60,w/3,h/30);
		lGradeReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGradeReply);

        lGradeReply2.setBounds(w/2,28*h/60,w/3,h/30);
		lGradeReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGradeReply2);

        JLabel lFacultyReply = new JLabel("学部");
		lFacultyReply.setBounds(0,31*h/60,w/3,h/30);
		lFacultyReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lFacultyReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lFacultyReply);

        lFacultyReply2.setBounds(w/2,31*h/60,w/3,h/30);
        lFacultyReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lFacultyReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lFacultyReply2);

        JLabel lBirthReply = new JLabel("出身");
		lBirthReply.setBounds(0,34*h/60,w/3,h/30);
		lBirthReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lBirthReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lBirthReply);

        lBirthReply2.setBounds(w/2,34*h/60,w/3,h/30);
        lBirthReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lBirthReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lBirthReply2);

        JLabel lCircleReply = new JLabel("サークル");
		lCircleReply.setBounds(0,37*h/60,w/3,h/30);
		lCircleReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lCircleReply);

        lCircleReply2.setBounds(w/2,37*h/60,w/3,h/30);
        lCircleReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lCircleReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lCircleReply2);

        JLabel lHobbyReply = new JLabel("趣味");
		lHobbyReply.setBounds(0,40*h/60,w/3,h/30);
		lHobbyReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lHobbyReply.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lHobbyReply);

        lHobbyReply2.setBounds(w/2,40*h/60,w/3,h/30);
        lHobbyReply2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lHobbyReply2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lHobbyReply2);

        JButton bGoodReply = new JButton("いいね");
        bGoodReply.setBounds(w/4,46*h/60,w/4,h/20);
        bGoodReply.addActionListener(this);
        bGoodReply.setActionCommand("いいねreply");
        bGoodReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bGoodReply);

        JButton bBadReply = new JButton("断る");
        bBadReply.setBounds(w/2,46*h/60,w/4,h/20);
        bBadReply.addActionListener(this);
        bBadReply.setActionCommand("断るreply");
        bBadReply.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bBadReply);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"reply");
	}

	public void replyGroup() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("いいね通知へ");
        card.add(bPrePage);

		lGroupNameReplyGroup.setBounds(w/4,h/60,w/2,h/20);
		lGroupNameReplyGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		lGroupNameReplyGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupNameReplyGroup);

        lGroupPhotoReplyGroup.setBounds(w/5,5*h/60,w/5,h/10);
        lGroupPhotoReplyGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        lGroupPhotoReplyGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupPhotoReplyGroup);

        lGroupProfileReplyGroup.setBounds(2*w/5,7*h/60,w/2,h/15);
        lGroupProfileReplyGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lGroupPhotoReplyGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupProfileReplyGroup);

        for(int i=0;i<5;i++) {
            bMemberProfileReplyGroup[i]=new JButton("プロフィール");
            bMemberProfileReplyGroup[i].setBounds(w/6,(11+7*i)*h/60,2*w/3,h/12);
            bMemberProfileReplyGroup[i].addActionListener(this);
            bMemberProfileReplyGroup[i].setActionCommand("メンバ"+String.valueOf(i)+"replyGroup");
            bMemberProfileReplyGroup[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
            card.add(bMemberProfileReplyGroup[i]);
        }

        JButton bGoodReplyGroup=new JButton("いいね");
        bGoodReplyGroup.setBounds(w/4,45*h/60,w/4,h/15);
        bGoodReplyGroup.addActionListener(this);
        bGoodReplyGroup.setActionCommand("いいねreplyGroup");
        bGoodReplyGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/27));
        bGoodReplyGroup.setVisible(false);
        card.add(bGoodReplyGroup);

        JButton bBadReplyGroup=new JButton("断る");
        bBadReplyGroup.setBounds(w/2,45*h/60,w/4,h/15);
        bBadReplyGroup.addActionListener(this);
        bBadReplyGroup.setActionCommand("断るreplyGroup");
        bBadReplyGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/27));
        bBadReplyGroup.setVisible(false);
        card.add(bBadReplyGroup);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);


		cardPanel.add(card,"replyGroup");
	}

	public void good() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("戻る");
        card.add(bPrePage);

		lNameGood.setBounds(w/4,h/60,w/2,h/15);
		lNameGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lNameGood.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameGood);

        lMainPhotoGood.setBounds(w/4,6*h/60,w/2,h/6);
        lMainPhotoGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(lMainPhotoGood);

        for(int i=0;i<4;i++) {
        	lSubPhotoGood[i] = new JLabel();
        	lSubPhotoGood[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
            lSubPhotoGood[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            card.add(lSubPhotoGood[i]);
        }

        JLabel lGenderGood = new JLabel("性別");
		lGenderGood.setBounds(0,25*h/60,w/3,h/30);
		lGenderGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGenderGood);

        lGenderGood2.setBounds(w/2,25*h/60,w/3,h/30);
		lGenderGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGenderGood2);

        JLabel lGradeGood = new JLabel("学年");
		lGradeGood.setBounds(0,28*h/60,w/3,h/30);
		lGradeGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lGradeGood);

        lGradeGood2.setBounds(w/2,28*h/60,w/3,h/30);
		lGradeGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lGradeGood2);

        JLabel lFacultyGood = new JLabel("学部");
		lFacultyGood.setBounds(0,31*h/60,w/3,h/30);
		lFacultyGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lFacultyGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lFacultyGood);

        lFacultyGood2.setBounds(w/2,31*h/60,w/3,h/30);
        lFacultyGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lFacultyGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lFacultyGood2);

        JLabel lBirthGood = new JLabel("出身");
		lBirthGood.setBounds(0,34*h/60,w/3,h/30);
		lBirthGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lBirthGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lBirthGood);

        lBirthGood2.setBounds(w/2,34*h/60,w/3,h/30);
        lBirthGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lBirthGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lBirthGood2);

        JLabel lCircleGood = new JLabel("サークル");
		lCircleGood.setBounds(0,37*h/60,w/3,h/30);
		lCircleGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lCircleGood);

        lCircleGood2.setBounds(w/2,37*h/60,w/3,h/30);
        lCircleGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lCircleGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lCircleGood2);

        JLabel lHobbyGood = new JLabel("趣味");
		lHobbyGood.setBounds(0,40*h/60,w/3,h/30);
		lHobbyGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lHobbyGood.setHorizontalAlignment(JLabel.RIGHT);
        card.add(lHobbyGood);

        lHobbyGood2.setBounds(w/2,40*h/60,w/3,h/30);
        lHobbyGood2.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lHobbyGood2.setHorizontalAlignment(JLabel.LEFT);
        card.add(lHobbyGood2);

        bGoodGood.setBounds(w/4,46*h/60,w/2,h/20);
        bGoodGood.addActionListener(this);
        bGoodGood.setActionCommand("いいねgood");
        bGoodGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bGoodGood);

        lGoodGood.setBounds(0,46*h/60,w,h/20);
        lGoodGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        lGoodGood.setHorizontalAlignment(JLabel.CENTER);
        lGoodGood.setVisible(false);
        card.add(lGoodGood);

        JButton bHomeGood=new JButton("HOME");
        bHomeGood.setBounds(w/5,51*h/60,w/5,h/15);
        bHomeGood.addActionListener(this);
        bHomeGood.setActionCommand("HOME");
        bHomeGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHomeGood);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"good");
	}

	public void matching() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleMatching = new JLabel("マッチング");
		lTitleMatching.setBounds(w/4,h/50,w/2,h/10);
		lTitleMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		lTitleMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMatching);

        JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("マッチング通知へ");
        card.add(bPrePage);

        lIconMatching.setBounds(w/5,h/7,w/2,h/5);
        lIconMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lIconMatching);

        lNameMatching.setBounds(0,11*h/20,w,h/15);
        lNameMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        lNameMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMatching);

        lIdMatching.setBounds(0,13*h/20,w,h/15);
        lIdMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        lIdMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lIdMatching);

        JButton bProfMatching=new JButton("プロフィールを確認する");
        bProfMatching.setBounds(w/4,8*h/20,w/2,h/15);
        bProfMatching.addActionListener(this);
        bProfMatching.setActionCommand("確認matching");
        bProfMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bProfMatching);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"matching");
	}

	public void searchUser() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("ホームへ");
        card.add(bPrePage);

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

        cbGenderSearchUser.setBounds(w/3,5*h/25,w/2,2*h/25);
		cbGenderSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbGenderSearchUser);

        JLabel lGradeSearchUser = new JLabel("学年");
		lGradeSearchUser.setBounds(w/8,8*h/25,w/6,2*h/25);
		lGradeSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lGradeSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGradeSearchUser);

        cbGradeSearchUser.setBounds(w/3,8*h/25,w/2,2*h/25);
		cbGradeSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbGradeSearchUser);

        JLabel lFacultySearchUser = new JLabel("学部");
		lFacultySearchUser.setBounds(w/8,11*h/25,w/6,2*h/25);
		lFacultySearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lFacultySearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lFacultySearchUser);

        cbFacultySearchUser.setBounds(w/3,11*h/25,w/2,2*h/25);
		cbFacultySearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbFacultySearchUser);

        JLabel lBirthSearchUser = new JLabel("出身");
		lBirthSearchUser.setBounds(w/8,14*h/25,w/6,2*h/25);
		lBirthSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lBirthSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lBirthSearchUser);

        cbBirthSearchUser.setBounds(w/3,14*h/25,w/2,2*h/25);
        cbBirthSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbBirthSearchUser);

        JLabel lCircleSearchUser = new JLabel("サークル");
		lCircleSearchUser.setBounds(w/8,17*h/25,w/6,2*h/25);
		lCircleSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleSearchUser.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCircleSearchUser);

        cbCircleSearchUser.setBounds(w/3,17*h/25,w/2,2*h/25);
        cbCircleSearchUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbCircleSearchUser);

        JButton bSearchSeachUser = new JButton("検索");
        bSearchSeachUser.setBounds(3*w/10,20*h/25,2*w/5,h/10);
        bSearchSeachUser.addActionListener(this);
        bSearchSeachUser.setActionCommand("検索searchUser");
        bSearchSeachUser.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bSearchSeachUser);

        cardPanel.add(card,"searchUser");
	}

	public void searchGroup() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("ホームへ");
        card.add(bPrePage);

		JLabel lTitleSearchGroup = new JLabel("検索");
		lTitleSearchGroup.setBounds(w/4,h/60,w/2,2*h/15);
		lTitleSearchGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/10));
		lTitleSearchGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleSearchGroup);

        JLabel lPurposeSearchGroup = new JLabel("目的");
		lPurposeSearchGroup.setBounds(w/8,5*h/20,w/6,2*h/20);
		lPurposeSearchGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lPurposeSearchGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lPurposeSearchGroup);

        cbPurposeSearchGroup.setBounds(w/3,5*h/20,w/2,2*h/20);
		cbPurposeSearchGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbPurposeSearchGroup);

        JLabel lHowManySearchGroup = new JLabel("人数");
		lHowManySearchGroup.setBounds(w/8,10*h/20,w/6,2*h/20);
		lHowManySearchGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
		lHowManySearchGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lHowManySearchGroup);

        cbHowManySearchGroup.setBounds(w/3,10*h/20,w/2,2*h/20);
        cbHowManySearchGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(cbHowManySearchGroup);

        JButton bSearchSeachGroup = new JButton("検索");
        bSearchSeachGroup.setBounds(3*w/10,15*h/20,2*w/5,h/8);
        bSearchSeachGroup.addActionListener(this);
        bSearchSeachGroup.setActionCommand("検索searchGroup");
        bSearchSeachGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bSearchSeachGroup);

        cardPanel.add(card,"searchGroup");
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

        JButton bHomeGood=new JButton("HOME");
        bHomeGood.setBounds(w/5,51*h/60,w/5,h/15);
        bHomeGood.addActionListener(this);
        bHomeGood.setActionCommand("HOME");
        bHomeGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHomeGood);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"menu");
	}

	public void myProfile() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("メニューへ");
        card.add(bPrePage);

        JLabel lTitleMyProfile = new JLabel("Myプロフィール");
		lTitleMyProfile.setBounds(w/4,h/60,w/2,h/15);
		lTitleMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMyProfile);

        bMainPhotoMyProfile.setBounds(w/4,6*h/60,w/2,h/6);
        bMainPhotoMyProfile.addActionListener(this);
        bMainPhotoMyProfile.setActionCommand("メインmyProfile");
        bMainPhotoMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bMainPhotoMyProfile);

        for(int i=0;i<4;i++) {
        	bSubPhotoMyProfile[i] = new JButton();
        	bSubPhotoMyProfile[i].setBounds(w/15+w*i*7/30,17*h/60,w/6,h/10);
            bSubPhotoMyProfile[i].addActionListener(this);
            bSubPhotoMyProfile[i].setActionCommand("サブ"+String.valueOf(i)+"myProfile");
            bSubPhotoMyProfile[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            card.add(bSubPhotoMyProfile[i]);
        }

        JLabel lNameMyProfile = new JLabel("名前");
		lNameMyProfile.setBounds(w/8,25*h/60,w/6,h/30);
		lNameMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lNameMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMyProfile);

        tfNameMyProfile.setBounds(w/3,25*h/60,w/2,h/30);
        tfNameMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfNameMyProfile);

        JLabel lGenderMyProfile = new JLabel("性別");
		lGenderMyProfile.setBounds(w/8,55*h/120,w/6,h/30);
		lGenderMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGenderMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGenderMyProfile);

        cbGenderMyProfile.setBounds(w/3,55*h/120,w/2,h/30);
		cbGenderMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbGenderMyProfile);

        JLabel lGradeMyProfile = new JLabel("学年");
		lGradeMyProfile.setBounds(w/8,60*h/120,w/6,h/30);
		lGradeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lGradeMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGradeMyProfile);

        cbGradeMyProfile.setBounds(w/3,60*h/120,w/2,h/30);
		cbGradeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbGradeMyProfile);

        JLabel lFacultyMyProfile = new JLabel("学部");
		lFacultyMyProfile.setBounds(w/8,65*h/120,w/6,h/30);
		lFacultyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lFacultyMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lFacultyMyProfile);

        cbFacultyMyProfile.setBounds(w/3,65*h/120,w/2,h/30);
		cbFacultyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbFacultyMyProfile);

        JLabel lBirthMyProfile = new JLabel("出身");
		lBirthMyProfile.setBounds(w/8,70*h/120,w/6,h/30);
		lBirthMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lBirthMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lBirthMyProfile);

        cbBirthMyProfile.setBounds(w/3,70*h/120,w/2,h/30);
        cbBirthMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbBirthMyProfile);

        JLabel lCircleMyProfile = new JLabel("サークル");
		lCircleMyProfile.setBounds(w/8,75*h/120,w/6,h/30);
		lCircleMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCircleMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCircleMyProfile);

        cbCircleMyProfile.setBounds(w/3,75*h/120,w/2,h/30);
        cbCircleMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbCircleMyProfile);

        JLabel lHobbyMyProfile = new JLabel("趣味");
		lHobbyMyProfile.setBounds(w/8,80*h/120,w/6,h/30);
		lHobbyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lHobbyMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lHobbyMyProfile);

        tfHobbyMyProfile.setBounds(w/3,80*h/120,w/2,h/30);
        tfHobbyMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfHobbyMyProfile);

        JLabel lLineIdMyProfile = new JLabel("LINEのID");
		lLineIdMyProfile.setBounds(w/8,85*h/120,w/6,h/30);
		lLineIdMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lLineIdMyProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lLineIdMyProfile);

        tfLineIdMyProfile.setBounds(w/3,85*h/120,w/2,h/30);
        tfLineIdMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfLineIdMyProfile);

        JButton bChangeMyProfile = new JButton("変更確定");
        bChangeMyProfile.setBounds(w/4,92*h/120,w/2,h/20);
        bChangeMyProfile.addActionListener(this);
        bChangeMyProfile.setActionCommand("確定myProfile");
        bChangeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bChangeMyProfile);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"myProfile");
	}

	public void change() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel ltitlechange = new JLabel("アカウント切り替え");
		ltitlechange.setBounds(w/5,h/50,3*w/5,h/10);
		ltitlechange.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		ltitlechange.setHorizontalAlignment(JLabel.CENTER);
        card.add(ltitlechange);

        JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("メニューへ");
        card.add(bPrePage);

        JButton bMakeGroupChange = new JButton("グループ作成",iAdd);
        bMakeGroupChange.setBounds(w/4,9*h/60,w/2,h/10);
        bMakeGroupChange.addActionListener(this);
        bMakeGroupChange.setActionCommand("グループ作成change");
        card.add(bMakeGroupChange);

        bPersonalChange.setBounds(w/4,16*h/60,w/2,h/10);
        bPersonalChange.addActionListener(this);
        bPersonalChange.setActionCommand("個人アカウントchange");
        card.add(bPersonalChange);

        for(int i=0;i<3;i++) {
        	bIconChange[i]=new JButton();
        	bIconChange[i].setBounds(w/4,(24+7*i)*h/60,w/2,h/10);
        	bIconChange[i].addActionListener(this);
        	bIconChange[i].setActionCommand("グループ"+String.valueOf(i)+"change");
        	card.add(bIconChange[i]);
        }

        JLabel lbackchange = new JLabel("back");
        lbackchange.setBounds(w/6,46*h/60,w/2,h/20);
        card.add(lbackchange);

        JButton bleftchange = new JButton(iLeft);
        bleftchange.setBounds(w/4,46*h/60,w/11,h/20);
        bleftchange.addActionListener(this);
    	bleftchange.setActionCommand("前のページchange");
        card.add(bleftchange);

        JButton brightchange = new JButton(iRight);
        brightchange.setBounds(2*w/3,46*h/60,w/11,h/20);
        brightchange.addActionListener(this);
    	brightchange.setActionCommand("次のページchange");
        card.add(brightchange);

        JLabel lnextchange = new JLabel("next");
        lnextchange.setBounds(7*w/9,46*h/60,w/2,h/20);
        card.add(lnextchange);

        JButton bHomeGood=new JButton("HOME");
        bHomeGood.setBounds(w/5,51*h/60,w/5,h/15);
        bHomeGood.addActionListener(this);
        bHomeGood.setActionCommand("HOME");
        bHomeGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHomeGood);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"change");
	}

	public void makeGroup() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("切り替えへ");
        card.add(bPrePage);

        JLabel lTitleMakeGroup = new JLabel("グループプロフィール");
		lTitleMakeGroup.setBounds(w/5,h/60,3*w/5,h/15);
		lTitleMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleMakeGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMakeGroup);

        bPhotoMakeGroup.setBounds(w/4,6*h/60,w/2,h/6);
        bPhotoMakeGroup.addActionListener(this);
        bPhotoMakeGroup.setActionCommand("メインmakeGroup");
        bPhotoMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bPhotoMakeGroup);

        JLabel lNameMakeGroup = new JLabel("グループ名");
		lNameMakeGroup.setBounds(w/9,18*h/60,w/5,h/20);
		lNameMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lNameMakeGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMakeGroup);

        tfNameMakeGroup.setBounds(w/3,18*h/60,w/2,h/20);
        tfNameMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfNameMakeGroup);

        JLabel lRelationMakeGroup = new JLabel("関係性");
		lRelationMakeGroup.setBounds(w/8,22*h/60,w/6,h/20);
		lRelationMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lRelationMakeGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lRelationMakeGroup);

        tfRelationMakeGroup.setBounds(w/3,22*h/60,w/2,h/20);
        tfRelationMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfRelationMakeGroup);

        JLabel lPurposeMakeGroup = new JLabel("目的");
		lPurposeMakeGroup.setBounds(w/8,26*h/60,w/6,h/20);
		lPurposeMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lPurposeMakeGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lPurposeMakeGroup);

        cbPurposeMakeGroup.setBounds(w/3,26*h/60,w/2,h/20);
        cbPurposeMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbPurposeMakeGroup);


        JLabel lCommentMakeGroup = new JLabel("ひとこと");
		lCommentMakeGroup.setBounds(w/8,30*h/60,w/6,h/20);
		lCommentMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCommentMakeGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCommentMakeGroup);

        taCommentMakeGroup.setBounds(w/3,30*h/60,w/2,h/5);
        taCommentMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(taCommentMakeGroup);

        JButton bGatherMakeGroup = new JButton("メンバーを選択");
        bGatherMakeGroup.setBounds(w/3,44*h/60,w/3,h/15);
        bGatherMakeGroup.addActionListener(this);
        bGatherMakeGroup.setActionCommand("選択makeGroup");
        bGatherMakeGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bGatherMakeGroup);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"makeGroup");
	}

	public void gathering() {

		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("グル作成へ");
        card.add(bPrePage);

		// タイトル
		JLabel lTitleMatching = new JLabel("グループ作成");
		lTitleMatching.setBounds(10*w/40,h/120,20*w/40,h/10);
		lTitleMatching.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/15));
		lTitleMatching.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMatching);

        //学籍番号表示
        JLabel lNumberGather = new JLabel("学籍番号");
        lNumberGather.setBounds(3*w/40,14*h/65,w/5,5*h/65);
        lNumberGather.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(lNumberGather);

        for(int i=0;i<4;i++) {
        	tfNumberGather[i] = new JTextField("");
        	tfNumberGather[i].setBounds(15*w/40,(15+4*i)*h/65,20*w/40,4*h/65);
            tfNumberGather[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
            card.add(tfNumberGather[i]);
        }

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
        bConfGather.setActionCommand("確定gathering");
        bConfGather.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bConfGather);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"gathering");
	}

	public void invite() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("招待通知へ");
        card.add(bPrePage);

		//タイトル
		JLabel ltitleinvite = new JLabel("グループ招待");
		ltitleinvite.setBounds(w/4,h/50,w/2,h/10);
		ltitleinvite.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		ltitleinvite.setHorizontalAlignment(JLabel.CENTER);
        card.add(ltitleinvite);

        //画像貼り付け
        lIconInvite.setBounds(w/4,3*h/20,w/2,h/5);
        lIconInvite.setHorizontalAlignment(JLabel.CENTER);
        card.add(lIconInvite);

        //プロフィール確認ボタン
        JButton bprofileinvite = new JButton("プロフィールを確認する");
        bprofileinvite.setBounds(w/4,7*h/20,w/2,h/20);
        bprofileinvite.addActionListener(this);
        bprofileinvite.setActionCommand("確認invite");
        card.add(bprofileinvite);

        //参加するボタン
        JButton bokinvite = new JButton("参加する！");
        bokinvite.setBounds(w/4,9*h/20,w/2,h/20);
        bokinvite.addActionListener(this);
        bprofileinvite.setActionCommand("参加invite");
        card.add(bokinvite);

        //参加しないボタン
        JButton bnoinvite = new JButton("参加しない");
        bnoinvite.setBounds(w/4,11*h/20,w/2,h/20);
        bnoinvite.addActionListener(this);
        bnoinvite.setActionCommand("断るinvite");
        card.add(bnoinvite);

        lHostInvite.setBounds(0,13*h/20,w,h/20);
        lHostInvite.setHorizontalAlignment(JLabel.CENTER);
        card.add(lHostInvite);


        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"invite");
	}

	public void viewGroup() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("戻る");
        card.add(bPrePage);

		lGroupNameViewGroup.setBounds(w/4,h/60,w/2,h/20);
		lGroupNameViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 3*w/50));
		lGroupNameViewGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupNameViewGroup);

        lGroupPhotoViewGroup.setBounds(w/5,5*h/60,w/5,h/10);
        lGroupPhotoViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        lGroupPhotoViewGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupPhotoViewGroup);

        lGroupProfileViewGroup.setBounds(2*w/5,7*h/60,w/2,h/15);
        lGroupProfileViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lGroupPhotoViewGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGroupProfileViewGroup);

        for(int i=0;i<5;i++) {
            bMemberProfileViewGroup[i]=new JButton("プロフィール");
            bMemberProfileViewGroup[i].setBounds(w/6,(11+7*i)*h/60,2*w/3,h/12);
            bMemberProfileViewGroup[i].addActionListener(this);
            bMemberProfileViewGroup[i].setActionCommand("メンバ"+String.valueOf(i)+"viewGroup");
            bMemberProfileViewGroup[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
            card.add(bMemberProfileViewGroup[i]);
        }

        bGoodViewGroup.setBounds(2*w/5,45*h/60,w/5,h/15);
        bGoodViewGroup.addActionListener(this);
        bGoodViewGroup.setActionCommand("いいねviewGroup");
        bGoodViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/27));
        bGoodViewGroup.setVisible(false);
        card.add(bGoodViewGroup);

        lGoodViewGroup.setBounds(w/5,45*h/60,3*w/5,h/15);
        lGoodViewGroup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        lGoodViewGroup.setHorizontalAlignment(JLabel.CENTER);
        card.add(lGoodViewGroup);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);


		cardPanel.add(card,"viewGroup");
	}

	public void myGroupProfile() {
		JPanel card = new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("メニューへ");
        card.add(bPrePage);

        JLabel lTitleMyGroupProfile = new JLabel("グループプロフィール");
		lTitleMyGroupProfile.setBounds(w/5,h/60,3*w/5,h/15);
		lTitleMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lTitleMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMyGroupProfile);

        bPhotoMyGroupProfile.setBounds(w/4,6*h/60,w/2,h/6);
        bPhotoMyGroupProfile.addActionListener(this);
        bPhotoMyGroupProfile.setActionCommand("メインmyGroupProfile");
        bPhotoMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bPhotoMyGroupProfile);

        JLabel lNameMyGroupProfile = new JLabel("グループ名");
		lNameMyGroupProfile.setBounds(w/9,18*h/60,w/5,h/20);
		lNameMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
		lNameMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lNameMyGroupProfile);

        tfNameMyGroupProfile.setBounds(w/3,18*h/60,w/2,h/20);
        tfNameMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfNameMyGroupProfile);

        JLabel lRelationMyGroupProfile = new JLabel("関係性");
		lRelationMyGroupProfile.setBounds(w/8,22*h/60,w/6,h/20);
		lRelationMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lRelationMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lRelationMyGroupProfile);

        tfRelationMyGroupProfile.setBounds(w/3,22*h/60,w/2,h/20);
        tfRelationMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(tfRelationMyGroupProfile);

        JLabel lPurposeMyGroupProfile = new JLabel("目的");
		lPurposeMyGroupProfile.setBounds(w/8,26*h/60,w/6,h/20);
		lPurposeMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lPurposeMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lPurposeMyGroupProfile);

        cbPurposeMyGroupProfile.setBounds(w/3,26*h/60,w/2,h/20);
        cbPurposeMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(cbPurposeMyGroupProfile);


        JLabel lCommentMyGroupProfile = new JLabel("ひとこと");
		lCommentMyGroupProfile.setBounds(w/8,30*h/60,w/6,h/20);
		lCommentMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
		lCommentMyGroupProfile.setHorizontalAlignment(JLabel.CENTER);
        card.add(lCommentMyGroupProfile);

        taCommentMyGroupProfile.setBounds(w/3,30*h/60,w/2,h/5);
        taCommentMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(taCommentMyGroupProfile);

        bQuitMyGroupProfile.setBounds(3*w/11,44*h/60,5*w/22,h/20);
        bQuitMyGroupProfile.addActionListener(this);
        bQuitMyGroupProfile.setActionCommand("削除myGroupProfile");
        bQuitMyGroupProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bQuitMyGroupProfile);

        JButton bChangeMyProfile = new JButton("変更確定");
        bChangeMyProfile.setBounds(w/2,44*h/60,5*w/22,h/20);
        bChangeMyProfile.addActionListener(this);
        bChangeMyProfile.setActionCommand("確定myGroupProfile");
        bChangeMyProfile.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/30));
        card.add(bChangeMyProfile);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"myGroupProfile");
	}

	public void setup() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("メニューへ");
        card.add(bPrePage);

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
        bDeleteAccountSetup.setActionCommand("削除setup");
        bDeleteAccountSetup.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/35));
        card.add(bDeleteAccountSetup);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"setup");
	}

	public void howToUse() {
		JPanel card=new JPanel();
		card.setLayout(null);

		//使い方
		String explain = "aaaaaaaaaaaa\naaaaaaaaa\naaaaaaa\naaa\na\na\n\na\naa\naa\naa\naa\naa\nb\na\nq\nww\nrr\nf";

		JButton bPrePage = new JButton(iLeft);
        bPrePage.setBounds(w/14,h/30,w/11,h/20);
        bPrePage.addActionListener(this);
        bPrePage.setActionCommand("メニューへ");
        card.add(bPrePage);

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

        /*Rect rect = new Rect();
        rect.setBounds(0,0,w,h);
        card.add(rect);*/

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"howToUse");
	}

	public void inform() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleInform = new JLabel("通知");
		lTitleInform.setBounds(w/4,2*h/30,w/2,3*h/30);
		lTitleInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 2*w/20));
		lTitleInform.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleInform);

        JButton bInviteInform=new JButton("グループへの招待");
        bInviteInform.setBounds(w/4,8*h/30,w/2,4*h/30);
        bInviteInform.addActionListener(this);
        bInviteInform.setActionCommand("招待inform");
        bInviteInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bInviteInform);

        JButton bGoodInform=new JButton("いいねを押した人");
        bGoodInform.setBounds(w/4,13*h/30,w/2,4*h/30);
        bGoodInform.addActionListener(this);
        bGoodInform.setActionCommand("いいねinform");
        bGoodInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bGoodInform);

        JButton bMatchedInform=new JButton("マッチングした人");
        bMatchedInform.setBounds(w/4,18*h/30,w/2,4*h/30);
        bMatchedInform.addActionListener(this);
        bMatchedInform.setActionCommand("マッチングinform");
        bMatchedInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        card.add(bMatchedInform);

        JButton bHomeGood=new JButton("HOME");
        bHomeGood.setBounds(w/5,51*h/60,w/5,h/15);
        bHomeGood.addActionListener(this);
        bHomeGood.setActionCommand("HOME");
        bHomeGood.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHomeGood);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

		cardPanel.add(card,"inform");
	}

	public void inviteInform() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleInviteInform = new JLabel("グループに招待されました");
		lTitleInviteInform.setBounds(w/4,h/50,w/2,h/10);
		lTitleInviteInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 2*w/50));
		lTitleInviteInform.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleInviteInform);

        for(int i=0;i<3;i++) {
        	bIconInviteInform[i]=new JButton("プロフィール",iRight);
        	bIconInviteInform[i].setBounds(w/4,(3+4*i)*h/20,w/2,h/10);
        	bIconInviteInform[i].addActionListener(this);
        	bIconInviteInform[i].setActionCommand("プロフィール"+String.valueOf(i)+"inviteInform");
        	bIconInviteInform[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        	card.add(bIconInviteInform[i]);
        }

        JLabel lbackInviteInform = new JLabel("back");
        lbackInviteInform.setBounds(w/6,15*h/20,w/2,h/20);
        card.add(lbackInviteInform);

        JButton bLeftInviteInform = new JButton(iLeft);
        bLeftInviteInform.setBounds(w/4,15*h/20,w/11,h/20);
        bLeftInviteInform.addActionListener(this);
    	bLeftInviteInform.setActionCommand("前のページinviteInform");
        card.add(bLeftInviteInform);

        JButton bRightInviteInform = new JButton(iRight);
        bRightInviteInform.setBounds(2*w/3,15*h/20,w/11,h/20);
        bRightInviteInform.addActionListener(this);
    	bRightInviteInform.setActionCommand("次のページinviteInform");
        card.add(bRightInviteInform);

        JLabel lnextInviteInform = new JLabel("next");
        lnextInviteInform.setBounds(7*w/9,15*h/20,w/2,h/20);
        card.add(lnextInviteInform);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"inviteInform");
	}

	public void goodInform() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleGoodInform = new JLabel("いいねが送られました");
		lTitleGoodInform.setBounds(w/4,h/50,w/2,h/10);
		lTitleGoodInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 2*w/50));
		lTitleGoodInform.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleGoodInform);

        for(int i=0;i<3;i++) {
        	bIconGoodInform[i]=new JButton("プロフィール",iRight);
        	bIconGoodInform[i].setBounds(w/4,(3+4*i)*h/20,w/2,h/10);
        	bIconGoodInform[i].addActionListener(this);
        	bIconGoodInform[i].setActionCommand("プロフィール"+String.valueOf(i)+"goodInform");
        	bIconGoodInform[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        	card.add(bIconGoodInform[i]);
        }

        JLabel lbackGoodInform = new JLabel("back");
        lbackGoodInform.setBounds(w/6,15*h/20,w/2,h/20);
        card.add(lbackGoodInform);

        JButton bLeftGoodInform = new JButton(iLeft);
        bLeftGoodInform.setBounds(w/4,15*h/20,w/11,h/20);
        bLeftGoodInform.addActionListener(this);
    	bLeftGoodInform.setActionCommand("前のページgoodInform");
        card.add(bLeftGoodInform);

        JButton bRightGoodInform = new JButton(iRight);
        bRightGoodInform.setBounds(2*w/3,15*h/20,w/11,h/20);
        bRightGoodInform.addActionListener(this);
    	bRightGoodInform.setActionCommand("次のページGoodInform");
        card.add(bRightGoodInform);

        JLabel lnextGoodInform = new JLabel("next");
        lnextGoodInform.setBounds(7*w/9,15*h/20,w/2,h/20);
        card.add(lnextGoodInform);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"goodInform");
	}

	public void matchingInform() {
		JPanel card=new JPanel();
		card.setLayout(null);

		JLabel lTitleMatchingInform = new JLabel("マッチングしました");
		lTitleMatchingInform.setBounds(w/4,h/50,w/2,h/10);
		lTitleMatchingInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, 2*w/50));
		lTitleMatchingInform.setHorizontalAlignment(JLabel.CENTER);
        card.add(lTitleMatchingInform);

        for(int i=0;i<3;i++) {
        	bIconMatchingInform[i]=new JButton("プロフィール",iRight);
        	bIconMatchingInform[i].setBounds(w/4,(3+4*i)*h/20,w/2,h/10);
        	bIconMatchingInform[i].addActionListener(this);
        	bIconMatchingInform[i].setActionCommand("プロフィール"+String.valueOf(i)+"matchingInform");
        	bIconMatchingInform[i].setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/20));
        	card.add(bIconMatchingInform[i]);
        }

        JLabel lbackMatchingInform = new JLabel("back");
        lbackMatchingInform.setBounds(w/6,15*h/20,w/2,h/20);
        card.add(lbackMatchingInform);

        JButton bLeftMatchingInform = new JButton(iLeft);
        bLeftMatchingInform.setBounds(w/4,15*h/20,w/11,h/20);
        bLeftMatchingInform.addActionListener(this);
    	bLeftMatchingInform.setActionCommand("前のページmatchingInform");
        card.add(bLeftMatchingInform);

        JButton bRightMatchingInform = new JButton(iRight);
        bRightMatchingInform.setBounds(2*w/3,15*h/20,w/11,h/20);
        bRightMatchingInform.addActionListener(this);
    	bRightMatchingInform.setActionCommand("次のページmatchingInform");
        card.add(bRightMatchingInform);

        JLabel lnextMatchingInform = new JLabel("next");
        lnextMatchingInform.setBounds(7*w/9,15*h/20,w/2,h/20);
        card.add(lnextMatchingInform);

        JButton bHome=new JButton("HOME");
        bHome.setBounds(w/5,51*h/60,w/5,h/15);
        bHome.addActionListener(this);
        bHome.setActionCommand("HOME");
        bHome.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bHome);

        JButton bInform=new JButton("通知");
        bInform.setBounds(3*w/5,51*h/60,w/5,h/15);
        bInform.addActionListener(this);
        bInform.setActionCommand("通知");
        bInform.setFont(new Font("ＭＳ 明朝", Font.PLAIN, w/25));
        card.add(bInform);

        cardPanel.add(card,"matchingInform");
	}


	public void goHome() {
		if(isNowUsingGroupAccount) {
			//nowShowingGroups=nページ目グルの情報取得
			for(int i=0;i<3;i++) {
				if(nowShowingGroups[i]==null) {
					bIconHome[i].setVisible(false);
				}
				else {
					try {
						bIconHome[i].setIcon(scaleImage(nowShowingGroups[i].getMainPhoto(),w/6,h/15));
					} catch (IOException e) {
						e.printStackTrace();
					}
				    bIconHome[i].setText(nowShowingGroups[i].getName());
				    bIconHome[i].setVisible(true);
				}
			}
		}
		else {
			for(int i=0;i<3;i++) {
				//nowShowingUsers[i]=nページ目のユーザ情報取得
				if(nowShowingUsers[i]==null) {
					bIconHome[i].setVisible(false);
				}
				else {
				    try {
						bIconHome[i].setIcon(scaleImage(myUserInfo.getMainPhoto(),w/6,h/15));
					} catch (IOException e) {
						e.printStackTrace();
					}
				    bIconHome[i].setText(nowShowingUsers[i].getName());
				    bIconHome[i].setVisible(true);
				}
			}
		}

		layout.show(cardPanel,"home");
	}

	public void goViewGroup() {
		lGroupNameViewGroup.setText(nowShowingGroup.getName());
		try {
			lGroupPhotoViewGroup.setIcon(scaleImage(nowShowingGroup.getMainPhoto(),w/5,h/10));
			lGroupProfileViewGroup.setText("<html><body>"+nowShowingGroup.getPurpose()+"<br />"+nowShowingGroup.getComment()+"</body></html>");
			//TODO 一言は何文字まで？

			//UserInfoの取得(nowShowingGroup.getHostUser())
			//bMemberProfileViewGroup[0].setIcon(scaleImage(,w/3,h/12));
			//bMemberProfileViewGroup[0].setText(ホストの名前)

			/*for(int i=1;i<5;i++) {
				//UserInfoの取得(nowShowingGroup.getNonHostUser()[i-1])
				if(nonhostUserの情報!=null) {
					bMemberProfileViewGroup[i].setIcon(scaleImage(,w/3,h/12));
					bMemberProfileViewGroup[i].setText(ノンホストの名前)
				}
			}*/

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		boolean flag=false;
		for(int i=0;i<myGroupInfo.getSendGood().length;i++) {
			if(myGroupInfo.getSendGood()[i]==nowShowingGroup.getStudentNumber()) {
				flag=true;
			}
		}
		if(flag) {
			lGoodViewGroup.setVisible(true);
			bGoodViewGroup.setVisible(false);
		}
		else {
			lGoodViewGroup.setVisible(false);
			bGoodViewGroup.setVisible(true);
		}

		layout.show(cardPanel, "viewGroup");
	}

	public void goGood() {
		lNameGood.setText(nowShowingUser.getName());
		lGenderGood2.setText(Sex[nowShowingUser.getGender()]);
		lGradeGood2.setText(String.valueOf(nowShowingUser.getGrade()));
		lFacultyGood2.setText(Faculty[nowShowingUser.getFaculty()]);
		lBirthGood2.setText(Birthplace[nowShowingUser.getBirth()]);
		lCircleGood2.setText(Circle[nowShowingUser.getCircle()]);
		lHobbyGood2.setText(nowShowingUser.getHobby());

		try {
			lMainPhotoGood.setIcon(scaleImage(nowShowingUser.getMainPhoto(),w/2,h/6));
			for(int i=0;i<4;i++) {
				lSubPhotoGood[i].setIcon(scaleImage(nowShowingUser.getSubPhoto()[i],w/6,h/10));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		boolean flag=false;
		for(int i=0;i<myUserInfo.getSendGood().length;i++) {
			if(myUserInfo.getSendGood()[i]==nowShowingUser.getStudentNumber()) {
				flag=true;
			}
		}
		if(flag) {
			lGoodGood.setVisible(true);
			bGoodGood.setVisible(false);
		}
		else {
			lGoodGood.setVisible(false);
			bGoodGood.setVisible(true);
		}
		layout.show(cardPanel,"good");
	}

	public void goInviteInform() {

		for(int i=0;i<3;i++) {
			//nowShowingGroups[i]=グル情報取得(myUserInfo.getInvitedGroup()[3*(nowPage-1)+i]);
			if(nowShowingGroups[i]==null) {
				bIconInviteInform[i].setVisible(false);
			}
			else {
			    try {
			    	bIconInviteInform[i].setIcon(scaleImage(nowShowingGroups[i].getMainPhoto(),w/4,h/20));
				} catch (IOException e) {
					e.printStackTrace();
				}
			    bIconInviteInform[i].setText(nowShowingGroups[i].getName());
			    bIconInviteInform[i].setVisible(true);
			}
		}
		layout.show(cardPanel, "inviteInform");
	}

	public void goGoodInform() {
		if(isNowUsingGroupAccount) {
			for(int i=0;i<3;i++) {
				//nowShowingGroups[i]=グル情報取得(myGroupInfo.getRecieveGood()[3*(nowPage-1)+i]);
				if(nowShowingGroups[i]==null) {
					bIconGoodInform[i].setVisible(false);
				}
				else {
				    try {
				    	bIconGoodInform[i].setIcon(scaleImage(nowShowingGroups[i].getMainPhoto(),w/4,h/20));
					} catch (IOException e) {
						e.printStackTrace();
					}
				    bIconInviteInform[i].setText(nowShowingGroups[i].getName());
				    bIconGoodInform[i].setVisible(true);
				}
			}
		}
		else {
			for(int i=0;i<3;i++) {
				//nowShowingUsers[i]=ユーザ情報取得(myUserInfo.getRecieveGood()[3*(nowPage-1)+i]);
				if(nowShowingUsers[i]==null) {
					bIconGoodInform[i].setVisible(false);
				}
				else {
				    try {
				    	bIconGoodInform[i].setIcon(scaleImage(nowShowingUsers[i].getMainPhoto(),w/4,h/20));
					} catch (IOException e) {
						e.printStackTrace();
					}
				    bIconGoodInform[i].setText(nowShowingUsers[i].getName());
				    bIconGoodInform[i].setVisible(true);
				}
			}
		}
		layout.show(cardPanel, "goodInform");
	}

	public void goMatchingInform() {
		if(isNowUsingGroupAccount) {
			for(int i=0;i<3;i++) {
				//nowShowingGroups[i]=グル情報取得(myGroupInfo.getMatchedGroup()[3*(nowPage-1)+i]);
				if(nowShowingGroups[i]==null) {
					bIconMatchingInform[i].setVisible(false);
				}
				else {
				    try {
				    	bIconMatchingInform[i].setIcon(scaleImage(nowShowingGroups[i].getMainPhoto(),w/4,h/20));
					} catch (IOException e) {
						e.printStackTrace();
					}
				    bIconMatchingInform[i].setText(nowShowingGroups[i].getName());
				}
			}
		}
		else {
			for(int i=0;i<3;i++) {
				//nowShowingUsers[i]=グル情報取得(myUserInfo.getRecieveGood()[3*(nowPage-1)+i]);
				if(nowShowingUsers[i]==null) {
					bIconMatchingInform[i].setVisible(false);
				}
				else {
				    try {
				    	bIconMatchingInform[i].setIcon(scaleImage(nowShowingUsers[i].getMainPhoto(),w/4,h/20));
					} catch (IOException e) {
						e.printStackTrace();
					}
				    bIconMatchingInform[i].setText(nowShowingUsers[i].getName());
				}
			}
		}
		layout.show(cardPanel, "matchingInform");
	}

	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		System.out.println(cmd);//TODO debug用
		int temp=0;
		boolean flag=false;
		FileDialog fd ;
		BufferedImage bi = null;
		File f;

		switch(cmd) {

		case "ログインlogin":
			if(tfIdLogin.getText().length()!=0&&tfPasswordLogin.getText().length()!=0) {
				try {
					int loginId=Integer.valueOf(tfIdLogin.getText());
				}
				catch(NumberFormatException e) {
					lMessageLogin.setVisible(true);
				}
				String loginPassword=tfPasswordLogin.getText();
			}
			else {
				lMessageLogin.setVisible(true);
			}

			boolean canLogin=false;
			//canLogin=ログイン確認のメソッド

			if(canLogin) {
				//myUserInfoの設定
				temp=myUserInfo.getIsAuthentificated();
				if(temp==0) {
					layout.show(cardPanel,"pleaseWait");
				}
				else if(temp==1) {
					goHome();
				}
				else {
					layout.show(cardPanel,"finishAuthen");
				}
			}
			else {
				lMessageLogin.setVisible(true);
			}

			break;


		case "アカウント作成login":
			layout.show(cardPanel,"new_regis");

			break;


		case "登録new_regis":
			if(tfIdNew_r.getText().length()!=0&&tfPasswordNew_r.getText().length()!=0) {
				try {
					myUserInfo.setStudentNumber(Integer.valueOf(tfIdNew_r.getText()));

					if(tfPasswordNew_r.getText().equals(tfPasswordConfNew_r.getText())) {
						myUserInfo.setPassword(tfPasswordNew_r.getText());
						layout.show(cardPanel,"judge");
					}
					else {
						lMessageNew_r.setVisible(true);
					}
				}
				catch(NumberFormatException e) {
					lMessageNew_r.setVisible(true);
				}
			}
			else {
				lMessageNew_r.setVisible(true);
			}
			break;


		case "選択judge":
			fd = new FileDialog(this,"Open File",FileDialog.LOAD);
			fd.setVisible(true);
			bi = null;

			try {
				f = new File(fd.getDirectory()+"/"+fd.getFile());
				bi=ImageIO.read(f);
				myUserInfo.setStudentCard(bi);
				lPicOutputJudge.setIcon(scaleImage(bi,2*w/5,h/6));
				lPicOutputJudge.setText("");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			break;


		case "送信judge":
			if(tfNameJudge.getText().length()==0||tfNumberJudge.getText().length()==0||lPicOutputJudge.getIcon()==null) {
				lErrorJudge.setVisible(true);
			}
			else {
				myUserInfo.setName(tfNameJudge.getText());
				myUserInfo.setStudentNumber(Integer.valueOf(tfNumberJudge.getText()));
				//TODO 学籍番号の登録が2回目？
				//新規登録メソッド
				layout.show(cardPanel,"pleaseWait");
			}
			break;


		case "すすむfinishAuthen":
			myUserInfo.setIsAuthentificated(1);
			//新プロフの送信
			goHome();

			break;


		case "HOME":
			nowPage=1;
			goHome();
			break;


		case "通知":
			layout.show(cardPanel,"inform");
			break;


		case "いいね通知へ":
			layout.show(cardPanel,"goodInform");
			break;


		case "ホームへ":
			layout.show(cardPanel,"home");
			break;


		case "メニューへ":
			layout.show(cardPanel,"menu");
			break;


		case "切り替えへ":
			layout.show(cardPanel,"change");
			break;


		case "グル作成へ":
			layout.show(cardPanel,"makeGroup");
			break;


		case "招待通知へ":
			layout.show(cardPanel,"inviteInform");
			break;


		case "検索home":
			previousPage="HOME";
			if(isNowUsingGroupAccount) {
				layout.show(cardPanel, "searchGroup");
			}
			else {
				layout.show(cardPanel,"searchUser");
			}
			break;


		case "メニューhome":
			layout.show(cardPanel,"menu");
			break;


		case "プロフィール0home":
		case "プロフィール1home":
		case "プロフィール2home":
			if(isNowUsingGroupAccount) {
				for(int i=0;i<3;i++) {
					if(cmd=="プロフィール"+String.valueOf(i)+"home") {
						nowShowingGroup=nowShowingGroups[i];
					}
				}
				goViewGroup();
			}
			else {
				for(int i=0;i<3;i++) {
					if(cmd=="プロフィール"+String.valueOf(i)+"home") {
						nowShowingUser=nowShowingUsers[i];
					}
				}
				goGood();
			}
			previousPage="HOME";
			break;


		case"前のページhome":
			nowPage--;
			if(nowPage<1) {
				nowPage++;
			}
			else {
				if(isNowUsingGroupAccount) {
					//nowShowingGroups=条件検索
					goHome();
				}
				else {
					//nowShowingUsers=条件検索
					goHome();
				}
			}
			break;


		case"次のページhome":
			nowPage++;
			if(isNowUsingGroupAccount) {
				//nowShowingGroups=条件検索
				if(nowShowingGroups!=null) {
					goHome();
				}
				else {
					nowPage--;
				}
			}
			else {
				//nowShowingUserss=条件検索
				if(nowShowingUsers!=null) {
					goHome();
				}
				else {
					nowPage--;
				}
			}
			break;


		case"いいねreply":
			//いいねメソッド(nowShowingUser.getStudentNumber())
			//TODO メッセージ出したほうがいい?
			layout.show(cardPanel,"goodInform");
			break;


		case"断るreply":
			//TODO
			break;


		case"いいねreplyGroup":
			//いいねメソッド(nowShowingGroup.getStudentNumber())
			//TODO メッセージ出したほうがいい?
			layout.show(cardPanel, "goodInform");
			break;


		case"断るreplyGroup":
			//TODO
			break;


		case "いいねgood":
			//いいねメソッド(nowShowingUser.getStudentNumber())
			bGoodGood.setVisible(false);
			lGoodGood.setVisible(true);
			break;


		case"確認matching":
			if(isNowUsingGroupAccount) {
				goViewGroup();
			}
			else {
				goGood();
			}
			break;


		case "検索searchUser":

			//TODO
			goHome();
			break;


		case "検索searchGroup":
			//TODO
			goHome();
			break;


		case "Myプロフィールmenu":
			if(isNowUsingGroupAccount) {
				tfNameMyGroupProfile.setText(myGroupInfo.getName());
				tfRelationMyGroupProfile.setText(myGroupInfo.getRelation());
				cbPurposeMyGroupProfile.setSelectedIndex(myGroupInfo.getPurpose());
				taCommentMyGroupProfile.setText(myGroupInfo.getComment());
				try {
					bPhotoMyGroupProfile.setIcon(scaleImage(myGroupInfo.getMainPhoto(),w/2,h/6));
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				if(myGroupInfo.getHostUser()==myUserInfo.getStudentNumber()) {
					bQuitMyGroupProfile.setEnabled(true);
				}
				else {
					bQuitMyGroupProfile.setEnabled(false);
				}

				layout.show(cardPanel, "myGroupProfile");
			}
			else {
				//TODO 学年0
				tfNameMyProfile.setText(myUserInfo.getName());
				cbGenderMyProfile.setSelectedIndex(myUserInfo.getGender());
				cbGradeMyProfile.setSelectedIndex(myUserInfo.getGrade());
				cbFacultyMyProfile.setSelectedIndex(myUserInfo.getFaculty());
				cbBirthMyProfile.setSelectedIndex(myUserInfo.getBirth());
				cbCircleMyProfile.setSelectedIndex(myUserInfo.getCircle());
				tfHobbyMyProfile.setText(myUserInfo.getHobby());
				tfLineIdMyProfile.setText(myUserInfo.getLineId());

				try {
					bMainPhotoMyProfile.setIcon(scaleImage(myUserInfo.getMainPhoto(),w/2,h/6));
					for(int i=0;i<4;i++) {
						bSubPhotoMyProfile[i].setIcon(scaleImage(myUserInfo.getSubPhoto()[i],w/6,h/10));
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				layout.show(cardPanel,"myProfile");
			}

			break;


		case "アカウント切り替えmenu":
			nowPage=1;

			try {
				bPersonalChange.setIcon(scaleImage(myUserInfo.getMainPhoto(),w/4,h/10));
				bPersonalChange.setText(myUserInfo.getName());

				for(int i=0;i<3;i++) {
					if(myUserInfo.getJoiningGroup()[i]==null) {
						bIconChange[i].setVisible(false);
					}
					else {
						//nowShowingGroups[i]=グル情報の取得(myUserInfo.getJoiningGroup()[i]
						bIconChange[i].setIcon(scaleImage(nowShowingGroups[i].getMainPhoto(),w/4,h/10));
						bIconChange[i].setText(nowShowingGroups[i].getName());
						bIconChange[i].setEnabled(nowShowingGroups[i].getIsGathered());
					}
				}

			}
			catch (IOException e) {
				e.printStackTrace();
			}
			layout.show(cardPanel,"change");
			break;


		case "設定menu":
			if(myUserInfo.getIsPublic()) {
				rbProfileSetup.setSelected(true);
				rbProfileSetup.setText("公開");
			}
			else {
				rbProfileSetup.setSelected(false);
				rbProfileSetup.setText("非公開");
			}

			layout.show(cardPanel,"setup");
			break;


		case"ヘルプmenu":
			layout.show(cardPanel,"howToUse");
			break;


		case"メインmyProfile":
			fd = new FileDialog(this,"Open File",FileDialog.LOAD);
			fd.setVisible(true);
			bi = null;

			try {
				f = new File(fd.getDirectory()+"/"+fd.getFile());
				bi=ImageIO.read(f);
				bMainPhotoMyProfile.setIcon(scaleImage(bi,w/2,h/6));
				myUserInfo.setMainPhoto(bi);
				//新プロフの作成(myUsrInfo)
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			break;


		case "サブ0myProfile":
		case "サブ1myProfile":
		case "サブ2myProfile":
		case "サブ3myProfile":
		case "サブ4myProfile":
			for(int i=0;i<5;i++) {
				if(cmd=="サブ"+String.valueOf(i)+"Profile") {
					temp=i;
				}
			}

			fd = new FileDialog(this,"Open File",FileDialog.LOAD);
			fd.setVisible(true);
			bi = null;

			try {
				f = new File(fd.getDirectory()+"/"+fd.getFile());
				bi=ImageIO.read(f);
				bSubPhotoMyProfile[temp] .setIcon(scaleImage(bi,w/6,h/10));
				myUserInfo.setSubPhoto(bi,temp);
				//新プロフの作成(myUserInfo)
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			break;


		case "確定myProfile":
			myUserInfo.setName(tfNameMyProfile.getText());
			myUserInfo.setGender(cbGenderMyProfile.getSelectedIndex());
			myUserInfo.setGrade(cbGradeMyProfile.getSelectedIndex());
			myUserInfo.setFaculty(cbFacultyMyProfile.getSelectedIndex());
			myUserInfo.setBirth(cbBirthMyProfile.getSelectedIndex());
			myUserInfo.setCircle(cbCircleMyProfile.getSelectedIndex());
			myUserInfo.setHobby(tfHobbyMyProfile.getText());
			myUserInfo.setLineId(tfLineIdMyProfile.getText());
			//新プロフ(myUserInfo)
			layout.show(cardPanel,"menu");
			break;


		case "グループ作成change":
			nowShowingGroup=new GroupInfo();

			tfNameMakeGroup.setText(nowShowingGroup.getName());
			tfRelationMakeGroup.setText(nowShowingGroup.getRelation());
			cbPurposeMakeGroup.setSelectedIndex(0);
			taCommentMakeGroup.setText(nowShowingGroup.getComment());
			try {
				bPhotoMakeGroup.setIcon(scaleImage(nowShowingGroup.getMainPhoto(),w/2,h/6));
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			layout.show(cardPanel,"makeGroup");
			break;


		case "個人アカウントchange":
			isNowUsingGroupAccount=false;
			nowPage=1;
			goHome();
			break;


		case "グループ0change":
		case "グループ1change":
		case "グループ2change":
			for(int i=0;i<3;i++) {
				if(cmd=="グループ"+String.valueOf(i)+"change") {
					//TODO myGroupInfo=グル情報を取得(nowShowingGroups[i]);
				}
			}
			isNowUsingGroupAccount=true;
			nowPage=1;
			goHome();
			break;


		case"前のページchange":
			nowPage--;
			if(nowPage<1) {
				nowPage++;
			}
			else {
				try {
					bPersonalChange.setIcon(scaleImage(myUserInfo.getMainPhoto(),w/4,h/10));
					bPersonalChange.setText(myUserInfo.getName());

					for(int i=0;i<3;i++) {
						if(myUserInfo.getJoiningGroup()[3*(nowPage-1)+i]==null) {
							bIconChange[i].setVisible(false);
						}
						else {
							//nowShowingGroups[i]=グル情報の取得(myUserInfo.getJoiningGroup()[3*(nowPage-1)+i])
							bIconChange[i].setVisible(true);
							bIconChange[i].setIcon(scaleImage(nowShowingGroups[i].getMainPhoto(),w/4,h/10));
							bIconChange[i].setText(nowShowingGroups[i].getName());
							bIconChange[i].setEnabled(nowShowingGroups[i].getIsGathered());
						}
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;


		case "次のページchange":
			nowPage++;
			if(myUserInfo.getJoiningGroup()[3*(nowPage-1)]!=null){
				try {
					bPersonalChange.setIcon(scaleImage(myUserInfo.getMainPhoto(),w/4,h/10));
					bPersonalChange.setText(myUserInfo.getName());

					for(int i=0;i<3;i++) {
						if(myUserInfo.getJoiningGroup()[3*(nowPage-1)+i]==null) {
							bIconChange[i].setVisible(false);
						}
						else {
							//nowShowingGroups[i]=グル情報の取得(myUserInfo.getJoiningGroup()[3*(nowPage-1)+i])
							bIconChange[i].setIcon(scaleImage(nowShowingGroups[i].getMainPhoto(),w/4,h/10));
							bIconChange[i].setText(nowShowingGroups[i].getName());
							bIconChange[i].setEnabled(nowShowingGroups[i].getIsGathered());
							bIconChange[i].setVisible(true);
						}
					}

				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
		  		nowPage--;
			}
			break;


		case "メインmakeGroup":
			fd = new FileDialog(this,"Open File",FileDialog.LOAD);
			fd.setVisible(true);
			bi = null;

			try {
				f = new File(fd.getDirectory()+"/"+fd.getFile());
				bi=ImageIO.read(f);
				nowShowingGroup.setMainPhoto(bi);
				//TODO グル情報新プロフ
				bPhotoMakeGroup.setIcon(scaleImage(bi,w/2,h/6));
				bPhotoMakeGroup.setText("");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			break;


		case "選択makeGroup":
			//TODO 各値の最大文字数とかどうする？
			if(tfNameMakeGroup.getText().length()>0) {
				nowShowingGroup.setName(tfNameMakeGroup.getText());
			}

			if(tfRelationMakeGroup.getText().length()>0) {
				nowShowingGroup.setName(tfRelationMakeGroup.getText());
			}

			for(int i=0;i<Purpose.length;i++) {
				if(cbPurposeMakeGroup.getSelectedItem()==Purpose[i]) {
					nowShowingGroup.setPurpose(i);
				}
			}

			for(int i=0;i<Grade.length;i++) {
				if(cbGradeMyProfile.getSelectedItem()==Grade[i]) {
					myUserInfo.setGrade(i);
				}
			}

			if(taCommentMakeGroup.getText().length()>0) {
				nowShowingGroup.setName(taCommentMakeGroup.getText());
			}
			layout.show(cardPanel,"gathering");
			break;


		case "確定gathering":
			//TODO 赤文字出てる
			nowShowingGroup.setHostUser(myUserInfo.getStudentNumber());
			temp=0;
			for(int i=0;i<4;i++) {
				if(tfNumberGather[i].getText()!=null) {
					nowShowingGroup.setNonhostUser(Integer.valueOf(tfNumberGather[i].getText()),temp);
					temp++;
				}
			}
			nowShowingGroup.setNumberOfMember(temp+1);
			//グルの作成(nowShowingGroup)
			goHome();
			break;


		case "確認invite":
			goViewGroup();
			break;


		case "参加invite":
			//TODO 参加メソッド？

			/*int i=0;
			flag=false;
			while(i<MAX && flag==false) {
				if(myUserInfo.getJoiningGroup()[i]==null) {
					myUserInfo.setJoiningGroop(nowShowingGroup.getStudentNumber(),i);
					flag=true;
				}
				i++;
			}

			if(flag) {
				i=0;
				flag=false;
				while(i<MAX && flag==false) {
					if(myUserInfo.getInvitedGroup()[i]==nowShowingGroup.getStudentNumber()) {
						myUserInfo.setInvitedGroup(null,i);
						flag=true;
					}
					i++;
				}

				if(flag) {

				}
			}*/
			//戻る
			break;


		case "断るinvite":
			//TODO 断るメソッド？
			//戻る
			break;


		case "メンバ0viewGroup":
		case "メンバ1viewGroup":
		case "メンバ2viewGroup":
		case "メンバ3viewGroup":
		case "メンバ4viewGroup":
			if(cmd=="メンバ0viewGroup") {
				//nowShowigUser=情報を取得(nowShowingGroup.getHostUser())
			}
			else {
				for(int i=1;i<5;i++) {
					if(cmd=="メンバ"+String.valueOf(i)+"viewGroup") {
						//nowShowigUser=情報を取得(nowShowingGroup.getHostUser())
					}
				}
			}
			goGood();
			break;


		case "いいねviewGroup":
			//いいねメソッド(myGroupInfo.getStudentNumber())
			bGoodViewGroup.setVisible(false);
			lGoodViewGroup.setVisible(true);
			break;


		case"メインmyGroupProfile":
			fd = new FileDialog(this,"Open File",FileDialog.LOAD);
			fd.setVisible(true);
			bi = null;

			try {
				f = new File(fd.getDirectory()+"/"+fd.getFile());
				bi=ImageIO.read(f);
				bPhotoMyGroupProfile.setIcon(scaleImage(bi,w/2,h/6));
				myUserInfo.setMainPhoto(bi);
				//新プロフの作成
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			break;


		case "確定myGroupProfile":
			myGroupInfo.setName(tfNameMyGroupProfile.getText());
			myGroupInfo.setRelation(tfRelationMyGroupProfile.getText());
			myGroupInfo.setPurpose(cbPurposeMyGroupProfile.getSelectedIndex());
			myGroupInfo.setComment(taCommentMyGroupProfile.getText());
			//新プロフ(myUserInfo)
			layout.show(cardPanel,"menu");
			break;


		case "削除myGroupProfile":
			temp = JOptionPane.showOptionDialog(this,"本当に削除しますか？","最終確認",JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE,null, yesNo, yesNo[1]);

			if (temp == 0){
				//削除メソッド(myGroupProfile.getStudentNumber)
				isNowUsingGroupAccount=false;
				nowPage=1;
				goHome();
			}
			break;


		case "削除setup":
			temp = JOptionPane.showOptionDialog(this,"本当に削除しますか？","最終確認",JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE,null, yesNo, yesNo[1]);
			if (temp == 0){
				//削除メソッド(myGroupProfile.getStudentNumber)
				layout.show(cardPanel,"login");
			}
			break;

		//TODO 通知の配列がとびとびになっていないこと前提
		case "招待inform":
			nowPage=1;
			goInviteInform();
			break;


		case "いいねinform":
			nowPage=1;
			goGoodInform();
			break;



		case "マッチングinform":
			nowPage=1;
			goMatchingInform();
			break;


		case"前のページinviteInform":
			nowPage--;
			if(nowPage<1) {
				nowPage++;
			}
			else {
				goInviteInform();
			}
			break;


		case "次のページinviteInform":
			nowPage++;
			if(myUserInfo.getJoiningGroup()[3*(nowPage-1)]!=null){
				goInviteInform();
			}
			else{
		  		nowPage--;
			}
			break;


		case"前のページgoodInform":
			nowPage--;
			if(nowPage<1) {
				nowPage++;
			}
			else {
				goGoodInform();
			}
			break;


		case "次のページgoodInform":
			nowPage++;
			if(isNowUsingGroupAccount) {
				if(myGroupInfo.getRecieveGood()[3*(nowPage-1)]!=null){
					goInviteInform();
				}
				else{
			  		nowPage--;
				}
			}
			else {
				if(myUserInfo.getRecieveGood()[3*(nowPage-1)]!=0){
					goInviteInform();
				}
				else{
			  		nowPage--;
				}
			}
			break;


		case"前のページmatchingInform":
			nowPage--;
			if(nowPage<1) {
				nowPage++;
			}
			else {
				goMatchingInform();
			}
			break;


		case "次のページmatchingInform":
			nowPage++;
			if(isNowUsingGroupAccount) {
				if(myGroupInfo.getMatchedGroup()[3*(nowPage-1)]!=null){
					goInviteInform();
				}
				else{
			  		nowPage--;
				}
			}
			else {
				if(myUserInfo.getMatchedUser()[3*(nowPage-1)]!=0){
					goInviteInform();
				}
				else{
			  		nowPage--;
				}
			}
			break;


		case "プロフィール0inviteInform":
		case "プロフィール1inviteInform":
		case "プロフィール2inviteInform":
			for(int i=0;i<3;i++) {
				if(cmd=="プロフィール"+String.valueOf(i)+"inviteInform") {
					nowShowingGroup=nowShowingGroups[i];
				}
			}

			try {
				lIconInvite.setIcon(scaleImage(nowShowingGroup.getMainPhoto(),w/2,h/5));
			} catch (IOException e) {
				e.printStackTrace();
			}
			//lHostInvite.setText("<html><body>"+//ユーザ情報(nowShowingGroup.getHostUser).getName()
			//+"<br/>に招待されました</html></body>");
			layout.show(cardPanel,"invite");
			break;


		case "プロフィール0goodInform":
		case "プロフィール1goodInform":
		case "プロフィール2goodInform":
			if(isNowUsingGroupAccount) {
				for(int i=0;i<3;i++) {
					if(cmd=="プロフィール"+String.valueOf(i)+"goodInform") {
						nowShowingGroup=nowShowingGroups[i];
					}
				}
				lGroupNameReplyGroup.setText(nowShowingGroup.getName());
				try {
					lGroupPhotoReplyGroup.setIcon(scaleImage(nowShowingGroup.getMainPhoto(),w/5,h/10));
					lGroupProfileReplyGroup.setText("<html><body>"+nowShowingGroup.getPurpose()+"<br />"+nowShowingGroup.getComment()+"</body></html>");
					//TODO 一言は何文字まで？

					//UserInfoの取得(nowShowingGroup.getHostUser())
					//bMemberProfileReplyGroup[0].setIcon(scaleImage(,w/3,h/12));
					//bMemberProfileReplyGroup[0].setText(ホストの名前)

					/*for(int i=1;i<5;i++) {
						//UserInfoの取得(nowShowingGroup.getNonHostUser()[i-1])
						if(nonhostUserの情報!=null) {
							bMemberProfileReplyGroup[i].setIcon(scaleImage(,w/3,h/12));
							bMemberProfileReplyGroup[i].setText(ノンホストの名前)
						}
					}*/

				}
				catch (IOException e) {
					e.printStackTrace();
				}
				layout.show(cardPanel,"replyGroup");

			}
			else {
				for(int i=0;i<3;i++) {
					if(cmd=="プロフィール"+String.valueOf(i)+"goodInform") {
						nowShowingUser=nowShowingUsers[i];
					}
				}
				lNameReply.setText(nowShowingUser.getName());
				lGenderReply2.setText(Sex[nowShowingUser.getGender()]);
				lGradeReply2.setText(String.valueOf(nowShowingUser.getGrade()));
				lFacultyReply2.setText(Faculty[nowShowingUser.getFaculty()]);
				lBirthReply2.setText(Birthplace[nowShowingUser.getBirth()]);
				lCircleReply2.setText(Circle[nowShowingUser.getCircle()]);
				lHobbyReply2.setText(nowShowingUser.getHobby());

				try {
					lMainPhotoReply.setIcon(scaleImage(nowShowingUser.getMainPhoto(),w/2,h/6));
					for(int i=0;i<4;i++) {
						lSubPhotoReply[i].setIcon(scaleImage(nowShowingUser.getSubPhoto()[i],w/6,h/10));
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				layout.show(cardPanel,"reply");
			}
			break;


		case "プロフィール0matchingInform":
		case "プロフィール1matchingInform":
		case "プロフィール2matchingInform":
			if(isNowUsingGroupAccount) {
				for(int i=0;i<3;i++) {
					if(cmd=="プロフィール"+String.valueOf(i)+"matchingInform") {
						nowShowingGroup=nowShowingGroups[i];
					}
				}

				try {
					lIconMatching.setIcon(scaleImage(nowShowingGroup.getMainPhoto(),w/2,h/5));
					lNameMatching.setText("<html><body>"+nowShowingGroup.getName()+"<br+nowSh />とマッチングしました！</body></html>");
					//lIdMatching.setText("LINE ID:"+//ユーザ情報の取得(nowShowingGroup.getHost()).getLineId());
				}
				catch (IOException e) {
					e.printStackTrace();
				}

			}
			else {
				for(int i=0;i<3;i++) {
					if(cmd=="プロフィール"+String.valueOf(i)+"goodInform") {
						nowShowingUser=nowShowingUsers[i];
					}
				}
				try {
					lIconMatching.setIcon(scaleImage(nowShowingUser.getMainPhoto(),w/2,h/5));
					lNameMatching.setText("<html><body>"+nowShowingUser.getName()+"<br+nowSh />とマッチングしました！</body></html>");
					lIdMatching.setText("LINE ID:"+nowShowingUser.getLineId());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			layout.show(cardPanel,"matching");
			break;
		}

	}

	public void stateChanged(ChangeEvent e) {
		JRadioButton cb = (JRadioButton)e.getSource();
		String message = cb.getText();
		if(message=="公開" || message=="非公開") {
			if (cb.isSelected()) {
				cb.setText("公開");
				myUserInfo.setIsPublic(true);
				//新プロフ(myUserInfo.getStudentNumber())
			} else {
				cb.setText("非公開");
				myUserInfo.setIsPublic(false);
				//新プロフ(myUserInfo.getStudentNumber())
			}
		}
	}

    public static void main(String[] args) {
    	Client client=new Client();
    	//client.new Notification();
    }

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
	
	/***************サーバー関連のメソッド・クラス群************************************************************/
	/*//サーバーに送るメソッド
	public void connectServer(String ipAddress, int port){	// サーバに接続
		//Socket socket = null;
		try {
			socket = new Socket(ipAddress, port); //サーバ(ipAddress, port)に接続
			out = new ObjectOutputStream(socket.getOutputStream()); //データ送信用オブジェクトの用意

			receiver = new Receiver(socket); //受信用オブジェクトの準備
			receiver.start();//受信用オブジェクト(スレッド)起動
		} catch (UnknownHostException e) {
			System.err.println("ホストのIPアドレスが判定できません: " + e);
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("サーバ接続時にエラーが発生しました: " + e);
			//updateDisp(topScreenPanel,connectionRefusedPanel);
			System.exit(-1);
		}
	}

	//ソケットを閉じるメソッド
	public void closeSocket() {
		try {
			System.out.println("Socekt Close");//確認用
			socket.close();
		}catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}

	// サーバにオブジェクトを送信
	public void sendUserInfo(Object obj) {
		out.writeObject(obj);
	}

	// データ受信用スレッド(内部クラス)
	class Receiver extends Thread {

		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket){
			try{
				ois = new ObjectInputStream(socket.getInputStream()); //受信したバイトデータを文字ストリームに
				sisr = new InputStreamReader(socket.getInputStream());
			} catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
		// 内部クラス Receiverのメソッド
		public void run(){
			try{
				while(true) {
					try {
						//UserInfo型なら
						if(ois.readObject() instanceof UserInfo) {
							UserInfo ui = new UserInfo();
							ui = (UserInfo)ois.readObject();
						}

						//GroupInfo型なら
						else if(ois.readObject() instanceof GroupInfo) {
							GroupInfo gi = new GroupInfo();
							gi = (GroupInfo)ois.readObject();
						}

						//その他ならreceiveMessage()
						else {
							br = new BufferedReader(sisr);
							String inputLine = br.readLine();//データを一行分読み込む
							receiveMessage(inputLine);
						}
					}catch (ClassNotFoundException e) {
							System.err.print("オブジェクト受信時にエラーが発生しました：" + e);
					}
				}
			} catch (IOException e){
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
	}

	// メッセージの受信
	public void receiveMessage(Object msg){
		System.out.println("サーバからメッセージ " + msg + " を受信しました"); //テスト用標準出力
		//受け取ったメッセージによって異なる処理を記述

	}*/

	/***************ここまで***************/
}
