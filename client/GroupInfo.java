import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import javax.imageio.ImageIO;

public class GroupInfo implements Serializable{
	int MAX=100;	//イイネ数をやグループ数の保持する限界数。変えてもいい
	int state=0;		//グル作成なら0,プロフ更新なら1

	UUID groupNumber;	//このグルの識別番号。クライアントがグルを作ったのち、サーバが割り振る
	String name="グループ名";		//グループ名
	String relation="友達";		//グルのメンバの関係性
	UUID[] sendGood=new UUID[MAX];		//このグルがイイネを送った相手のグルの識別番号
	UUID[] receiveGood=new UUID[MAX];		//このグルにイイネを送った相手のグルの識別番号
	UUID[] matchedGroup=new UUID[MAX];		//このグルとマッチングした相手のグルの識別番号
	transient BufferedImage mainPhoto;		//グルの写真
	int hostUser=0;		//グルのホストユーザ(=グルを作った人)
	int[] nonhostUser=new int[4];		//グルのホストでないユーザ。グルに招待されている時点で追加する
	int purpose=0;			//グルの目的。Client.javaの配列Purposeで文字列に変える
	String comment="仲良くしてね！";		//グルの一言コメント
	int numberOfMember=1;	//グルの人数。グループから抜ける機能はなくなったので、招待した時点で固定。
	boolean isGathered=false;	//グルが全員集まったならtrueに変換し、検索に引っかかるようになる。
	//グルから抜ける機能がない以上、誰かが招待を断った時点でグルは削除

	int[] bufMainPhoto;
	int widthOfMainPhoto;
	int heightOfMainPhoto;

	static final long serialVersionUID=1L;

	GroupInfo(){
		try {
			setMainPhoto(ImageIO.read(new File("./img/初期アイコン.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int [] getArrayByImage(BufferedImage img,int w, int h){
		BufferedImage subImg = img.getSubimage(0, 0, w, h);
		WritableRaster raster = subImg.getRaster();
		int size = raster.getNumBands() * w * h;
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

	public void setReceiveGood(UUID[] rg) {
		receiveGood=rg;
	}

	public void setReceiveGood(UUID rg,int n) {
		receiveGood[n]=rg;

	}

	public UUID[] getReceiveGood() {
		return receiveGood;
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
		widthOfMainPhoto=mp.getWidth();
		heightOfMainPhoto=mp.getHeight();
		bufMainPhoto = getArrayByImage(mp, widthOfMainPhoto,heightOfMainPhoto);
		mainPhoto=mp;
	}

	public BufferedImage getMainPhoto() {
		mainPhoto=getImageByArray(bufMainPhoto, widthOfMainPhoto,heightOfMainPhoto);
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

	public void setIsGathered(boolean a) {
		isGathered=a;
	}

	public boolean getIsGathered() {
		return isGathered;
	}

}
