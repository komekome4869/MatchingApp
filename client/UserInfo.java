import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import javax.imageio.ImageIO;

public class UserInfo implements Serializable{
	static int MAX=100;	//イイネ数をやグループ数の保持する限界数。変えてもいい
	int state;		//新規登録なら0,プロフ更新なら1

	int studentNumber=1;	//学籍番号
	String password="";
	String name="国大太郎";		//ユーザ名。認証時は氏名として使う。
	//性別～サークルはClient.java内の配列で文字に変換する
	int gender=2;	//性別
	int grade=0;	//学年
	int faculty=0;	//学部
	int birth=0;	//出身
	int circle=0;	//サークル
	int[] bufStudentCard;		//学生証バフ
	int[] bufMainPhoto;
	int[] bufSubPhoto1;
	int[] bufSubPhoto2;
	int[] bufSubPhoto3;
	int[] bufSubPhoto4;
	String hobby="";	//趣味
	int[] sendGood=new int[MAX];	//自分がイイネを送った相手の学籍番号
	int[] receiveGood=new int[MAX];	//自分にイイネを送った相手の学籍番号
	int[] matchedUser=new int[MAX];	//マッチングした相手の学籍番号
	UUID[] joiningGroup=new UUID[MAX];		//参加しているグルの識別番号
	UUID[] invitedGroup=new UUID[MAX];		//誘われているグルの識別番号
	transient BufferedImage studentCard;		//学生証の写真
	int isAuthentificated=0;		//0:認証されていない,1:認証された,2:認証されたが「認証されました画面」を見ていない
	transient BufferedImage mainPhoto;	//メインの写真
	transient BufferedImage[] subPhoto=new BufferedImage[4];		//サブの写真
	String lineId="未登録";	//ラインID
	boolean isPublic=true;		//プロフの非公開を希望するユーザはfalseに。falseだと検索に引っかからなくなる。


	int widthOfStudentCard;
	int heightOfStudentCard;
	int widthOfMainPhoto;
	int heightOfMainPhoto;
	int[] widthOfSubPhoto=new int[4];
	int[] heightOfSubPhoto=new int[4];
	;
	static final long serialVersionUID=1000;

	UserInfo(){
		try {
			setMainPhoto(ImageIO.read(new File("./img/初期アイコン.png")));
			for(int i=0;i<4;i++) {
				setSubPhoto(ImageIO.read(new File("./img/初期アイコン.png")),i);
			}
		} catch (IOException e) {
			System.out.println("初期アイコンの取得に失敗");
		}
	}

	public static int [] getArrayByImage(BufferedImage img,int w, int h){
		BufferedImage subImg = img.getSubimage(0, 0, w, h);
		WritableRaster raster = subImg.getRaster();
		int size = raster.getNumBands() * w * h;
//		System.out.println("getNumBands:" + size);
		int [] buf = new int[ size ];
		raster.getPixels(0, 0, w, h, buf);
		return buf;
	}

	public  BufferedImage getImageByArray(int [] buf,int w, int h){
		BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
		WritableRaster raster = img.getRaster();
		raster.setPixels(0, 0, w, h, buf);
		return img;
	}

	public int getState() {
		return state;
	}

	public void setState(int s) {
		state=s;
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

	public void setSendGood(int sg,int n) {
		sendGood[n]=sg;
	}

	public int[] getSendGood() {
		return sendGood;
	}

	public void setReceiveGood(int[] rg) {
		receiveGood=rg;
	}

	public void setReceiveGood(int rg,int n) {
		receiveGood[n]=rg;
	}

	public int[] getReceiveGood() {
		return receiveGood;
	}

	public void setMatchedUser(int[] mu) {
		matchedUser=mu;
	}

	public void setMatchedUser(int mu,int n) {
		matchedUser[n]=mu;
	}

	public int[] getMatchedUser() {
		return matchedUser;
	}

	public void setJoiningGroup(UUID[] jg) {
		joiningGroup=jg;
	}

	public void setJoiningGroup(UUID jg,int n) {
		joiningGroup[n]=jg;
	}

	public UUID[] getJoiningGroup() {
		return joiningGroup;
	}

	public void setInvitedGroup(UUID[] ig) {
		invitedGroup=ig;
	}

	public void setInvitedGroup(UUID ig,int n) {
		invitedGroup[n]=ig;
	}

	public UUID[] getInvitedGroup() {
		return invitedGroup;
	}


	public void setStudentCard(BufferedImage sc) {
		widthOfStudentCard=sc.getWidth();
		heightOfStudentCard=sc.getHeight();
		bufStudentCard = getArrayByImage(sc, widthOfStudentCard,heightOfStudentCard);
		studentCard=sc;
	}

	public BufferedImage getStudentCard() {
		studentCard = getImageByArray(bufStudentCard,widthOfStudentCard,heightOfStudentCard);
		return studentCard;
	}

	public void setIsAuthentificated(int ia) {
		isAuthentificated=ia;
	}

	public int getIsAuthentificated() {
		return isAuthentificated;
	}

	public void setMainPhoto(BufferedImage mp) {
		widthOfMainPhoto=mp.getWidth();
		heightOfMainPhoto=mp.getHeight();
		bufMainPhoto = getArrayByImage(mp, widthOfMainPhoto,heightOfMainPhoto);
		mainPhoto=mp;
	}

	public BufferedImage getMainPhoto() {
		mainPhoto=getImageByArray(bufMainPhoto, widthOfMainPhoto,heightOfMainPhoto);
		return mainPhoto;
	}

	public void setSubPhoto(BufferedImage[] sp) {
		int[] temp;
		for(int i=0;i<4;i++) {
			if(sp[i]!=null) {
				widthOfSubPhoto[i]=sp[i].getWidth();
				heightOfSubPhoto[i]=sp[i].getHeight();
				temp = getArrayByImage(sp[i], widthOfSubPhoto[i],heightOfSubPhoto[i]);
				subPhoto[i]=sp[i];
			}
			else {
				temp=null;
				sp[i]=null;
			}
			switch (i) {
			case 0:
				bufSubPhoto1=temp;
				break;
			case 1:
				bufSubPhoto2=temp;
				break;
			case 2:
				bufSubPhoto3=temp;
				break;
			case 3:
				bufSubPhoto4=temp;
				break;
			}
		}

	}

	public void setSubPhoto(BufferedImage sp,int i) {
		int[] temp;
		widthOfSubPhoto[i]=sp.getWidth();
		heightOfSubPhoto[i]=sp.getHeight();
		temp = getArrayByImage(sp, widthOfSubPhoto[i],heightOfSubPhoto[i]);
		subPhoto[i]=sp;

		switch (i) {
		case 0:
			bufSubPhoto1=temp;
			break;
		case 1:
			bufSubPhoto2=temp;
			break;
		case 2:
			bufSubPhoto3=temp;
			break;
		case 3:
			bufSubPhoto4=temp;
			break;
		}
	}

	public BufferedImage[] getSubPhoto() {
		subPhoto=new BufferedImage[4];
		int[] temp;
		for(int i=0;i<4;i++) {
			switch (i) {
			case 0:
				temp=bufSubPhoto1;
				break;
			case 1:
				temp=bufSubPhoto2;
				break;
			case 2:
				temp=bufSubPhoto3;
				break;
			default:
				temp=bufSubPhoto4;
				break;
			}
			if(temp!=null) {
				//System.out.println(i+"の幅"+ widthOfSubPhoto[i]);
				//System.out.println(i+"の高さ"+heightOfSubPhoto[i]);
				subPhoto[i]=getImageByArray(temp, widthOfSubPhoto[i],heightOfSubPhoto[i]);
				//System.out.println("sabu"+i+"はnullじゃない");
			}
			else {
				subPhoto[i]=null;
				//System.out.println("sabu"+i+"はnull");
			}
		}
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
