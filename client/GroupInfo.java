import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import javax.imageio.ImageIO;

public class GroupInfo implements Serializable{
	static int MAX=100;	//イイネ数をやグループ数の保持する限界数。変えてもいい

	static UUID groupNumber;	//このグルの識別番号。クライアントがグルを作ったのち、サーバが割り振る
	static String name="";		//グループ名
	static String relation="";		//グルのメンバの関係性
	static UUID[] sendGood=new UUID[MAX];		//このグルがイイネを送った相手のグルの識別番号
	static UUID[] recieveGood=new UUID[MAX];		//このグルにイイネを送った相手のグルの識別番号
	static UUID[] matchedGroup=new UUID[MAX];		//このグルとマッチングした相手のグルの識別番号
	static BufferedImage mainPhoto;		//グルの写真
	static int hostUser=0;		//グルのホストユーザ(=グルを作った人)
	static int[] nonhostUser=new int[4];		//グルのホストでないユーザ。グルに招待されている時点で追加する
	static int purpose=0;			//グルの目的。Client.javaの配列Purposeで文字列に変える
	static String comment="";		//グルの一言コメント
	static int numberOfMember=1;	//グルの人数。グループから抜ける機能はなくなったので、招待した時点で固定。
	static boolean isGathered=false;	//グルが全員集まったならtrueに変換し、検索に引っかかるようになる。

	//グルから抜ける機能がない以上、誰かが招待を断った時点でグルは削除


	GroupInfo(){
		try {
			mainPhoto=ImageIO.read(new File("./img/test.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setStudentNumber(UUID gn) {
		groupNumber=gn;
	}

	public UUID getStudentNumber() {
		return groupNumber;
	}

	public void setName(String n) {
		name=n;
	}

	public String getName() {
		return name;
	}

	public void setRelation(String r) {
		relation=r;
	}

	public String getRelation() {
		return relation;
	}

	public void setSendGood(UUID[] sg) {
		sendGood=sg;
	}
	
	public void setSendGood(UUID sg,int n) {
		sendGood[n]=sg;
	}

	public UUID[] getSendGood() {
		return sendGood;
	}

	public void setRecieveGood(UUID[] rg) {
		recieveGood=rg;
	}
	
	public void setRecieveGood(UUID rg,int n) {
		recieveGood[n]=rg;
		
	}

	public UUID[] getRecieveGood() {
		return recieveGood;
	}

	public void setMatchedGroup(UUID[] mg) {
		matchedGroup=mg;
	}
	
	public void setMatchedGroup(UUID mg,int n) {
		matchedGroup[n]=mg;
	}

	public UUID[] getMatchedGroup() {
		return matchedGroup;
	}

	public void setMainPhoto(BufferedImage mp) {
		mainPhoto=mp;
	}

	public BufferedImage getMainPhoto() {
		return mainPhoto;
	}

	public void setHostUser(int hu) {
		hostUser=hu;
	}

	public int getHostUser() {
		return hostUser;
	}

	public void setNonhostUser(int[] nu) {
		nonhostUser=nu;
	}
	
	public void setNonhostUser(int nu,int n) {
		nonhostUser[n]=nu;
	}

	public int[] getNonhostUser() {
		return nonhostUser;
	}

	public void setPurpose(int p) {
		purpose=p;
	}

	public int getPurpose() {
		return purpose;
	}

	public void setComment(String c) {
		comment=c;
	}

	public String getComment() {
		return comment;
	}

	public void setNumberOfMember(int nm) {
		numberOfMember=nm;
	}

	public int getNumberOfMember() {
		return numberOfMember;
	}

	public void setIsGatered(boolean a) {
		isGathered=a;
	}

	public boolean getIsGathered() {
		return isGathered;
	}

}
