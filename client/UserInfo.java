import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import javax.imageio.ImageIO;

public class UserInfo implements Serializable{
	static int MAX=100;	//イイネ数をやグループ数の保持する限界数。変えてもいい

	static int studentNumber=0;	//学籍番号
	static String password="";
	static String name="国大太郎";		//ユーザ名。認証時は氏名として使う。
	//性別～サークルはClient.java内の配列で文字に変換する
	static int gender=2;	//性別
	static int grade=0;	//学年
	static int faculty=0;	//学部
	static int birth=0;	//出身
	static int circle=0;	//サークル
	static String hobby="";	//趣味
	static int[] sendGood=new int[MAX];	//自分がイイネを送った相手の学籍番号
	static int[] recieveGood=new int[MAX];	//自分にイイネを送った相手の学籍番号
	static int[] matchedUser=new int[MAX];	//マッチングした相手の学籍番号
	static UUID[] joiningGroup=new UUID[MAX];		//参加しているグルの識別番号
	static UUID[] invitedGroup=new UUID[MAX];		//誘われているグルの識別番号
	static BufferedImage studentCard;		//学生証の写真
	static int isAuthentificated=0;		//0:認証されていない,1:認証された,2:認証されたが「認証されました画面」を見ていない
	static BufferedImage mainPhoto;	//メインの写真
	static BufferedImage[] subPhoto=new BufferedImage[4];		//サブの写真
	static String lineId="";	//ラインID
	static boolean isPublic=true;		//プロフの非公開を希望するユーザはfalseに。falseだと検索に引っかからなくなる。

	UserInfo(){
		try {
			mainPhoto=ImageIO.read(new File("./img/test.jpg"));
			for(int i=0;i<4;i++) {
				subPhoto[i]=ImageIO.read(new File("./img/test.jpg"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStudentNumber(int sn) {
		studentNumber=sn;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setPassword(String pw) {
		password=pw;
	}

	public String getPassword() {
		return password;
	}

	public void setName(String n) {
		name=n;
	}

	public String getName() {
		return name;
	}

	public void setGender(int g) {
		gender=g;
	}

	public int getGender() {
		return gender;
	}

	public void setGrade(int g) {
		grade=g;
	}

	public int getGrade() {
		return grade;
	}

	public void setFaculty(int f) {
		faculty=f;
	}

	public int getFaculty() {
		return faculty;
	}

	public void setBirth(int b) {
		gender=b;
	}

	public int getBirth() {
		return birth;
	}

	public void setCircle(int c) {
		gender=c;
	}

	public int getCircle() {
		return circle;
	}

	public void setHobby(String h) {
		hobby=h;
	}

	public String getHobby() {
		return hobby;
	}

	public void setSendGood(int[] sg) {
		sendGood=sg;
	}

	public int[] getSendGood() {
		return sendGood;
	}

	public void setRecieveGood(int[] rg) {
		recieveGood=rg;
	}

	public int[] getRecieveGood() {
		return recieveGood;
	}

	public void setMatchedUser(int[] mu) {
		matchedUser=mu;
	}

	public int[] getMatchedUser() {
		return matchedUser;
	}

	public void setJoiningGroop(UUID[] jg) {
		joiningGroup=jg;
	}

	public UUID[] getJoiningGroup() {
		return joiningGroup;
	}

	public void setInvitedGroop(UUID[] ig) {
		invitedGroup=ig;
	}

	public UUID[] getInvitedGroup() {
		return invitedGroup;
	}

	public void setStudentCard(BufferedImage sc) {
		studentCard=sc;
	}

	public BufferedImage getStudentCard() {
		return studentCard;
	}

	public void setIsAuthentificated(int ia) {
		isAuthentificated=ia;
	}

	public int getIsAuthentificated() {
		return isAuthentificated;
	}

	public void setMainPhoto(BufferedImage mp) {
		mainPhoto=mp;
	}

	public BufferedImage getMainPhoto() {
		return mainPhoto;
	}

	public void setSubPhoto(BufferedImage[] sp) {
		subPhoto=sp;
	}

	public BufferedImage[] getSubPhoto() {
		return subPhoto;
	}

	public void setLineId(String li) {
		lineId=li;
	}

	public String getLineId() {
		return lineId;
	}

	public void setIsPublic(boolean a) {
		isPublic=a;
	}

	public boolean getIsPublic() {
		return isPublic;
	}
}
