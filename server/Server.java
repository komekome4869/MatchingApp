import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	}

	public static void main(String args[]) {
		Server Server = new Server("MS_Server");
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

