import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

public class GroupInfoDriver {
	public static void main(String[] args) {
		GroupInfo gi = new GroupInfo();
		int[] testInt= {1234561,1234562};
		UUID[] testUUID= {UUID.randomUUID(),UUID.randomUUID()};
		UUID testUUID2=UUID.randomUUID();
		BufferedImage testbi=null;
		try {
			testbi=ImageIO.read(new File("./テスト用画像/testImage.png"));
		} catch (IOException e) {
			System.out.println("テスト用画像の読み込みに失敗");
		}

		System.out.println("setStudentNumberで"+testUUID2+"を入力します");
		gi.setStudentNumber(testUUID2);
		System.out.println("getStudentNumber出力: " +gi.getStudentNumber());

		System.out.println("setNameで「グループA」を入力します");
		gi.setName("グループA");
		System.out.println("getName出力: " +gi.getName());

		System.out.println("setRelationで「関係性テスト」を入力します");
		gi.setName("関係性テスト");
		System.out.println("getRelation出力: " +gi.getRelation());

		System.out.println("setSendGoodで{"+testUUID[0]+","+testUUID[1]+"}を入力します");
		gi.setSendGood(testUUID);
		System.out.println("getSendGood出力\n第一要素：" +gi.getSendGood()[0]+"\n第2要素："+gi.getSendGood()[1]);

		System.out.println("setSendGoodで配列の第1要素に"+testUUID2+"を入力します");
		gi.setSendGood(testUUID2,0);
		System.out.println("getSendGood出力\n第一要素：" +gi.getSendGood()[0]+"\n第2要素："+gi.getSendGood()[1]);

		System.out.println("setReceiveGoodで{"+testUUID[0]+","+testUUID[1]+"}を入力します");
		gi.setReceiveGood(testUUID);
		System.out.println("getReceiveGood出力\n第一要素：" +gi.getReceiveGood()[0]+"\n第2要素："+gi.getReceiveGood()[1]);

		System.out.println("setReceiveGoodで配列の第1要素に"+testUUID2+"を入力します");
		gi.setReceiveGood(testUUID2,0);
		System.out.println("getReceiveGood出力\n第一要素：" +gi.getReceiveGood()[0]+"\n第2要素："+gi.getReceiveGood()[1]);

		System.out.println("setMatchedGroupで{"+testUUID[0]+","+testUUID[1]+"}を入力します");
		gi.setMatchedGroup(testUUID);
		System.out.println("getMatchedGroup出力\n第一要素：" +gi.getMatchedGroup()[0]+"\n第2要素："+gi.getMatchedGroup()[1]);

		System.out.println("setMatchedGroupで配列の第1要素に"+testUUID2+"を入力します");
		gi.setMatchedGroup(testUUID2,0);
		System.out.println("getMatchedGroup出力\n第一要素：" +gi.getMatchedGroup()[0]+"\n第2要素："+gi.getMatchedGroup()[1]);

		System.out.println("setHostUserで「1234567」を入力します");
		gi.setHostUser(1234567);
		System.out.println("getHostUser出力: " +gi.getHostUser());

		System.out.println("setNonhostUserで{1234561,1234562}を入力します");
		gi.setNonhostUser(testInt);
		System.out.println("getNonhostUser出力\n第一要素：" +gi.getNonhostUser()[0]+"\n第2要素："+gi.getNonhostUser()[1]);

		System.out.println("setNonhostUserで配列の第1要素に「2234561」を入力します");
		gi.setNonhostUser(2234561,0);
		System.out.println("getNonhostUser出力\n第一要素：" +gi.getNonhostUser()[0]+"\n第2要素："+gi.getNonhostUser()[1]);

		System.out.println("setPurposerで「1」を入力します");
		gi.setPurpose(1);
		System.out.println("getHostUser出力: " +gi.getPurpose());

		System.out.println("setCommentで「コメントテスト」を入力します");
		gi.setName("コメントテスト");
		System.out.println("getComment出力: " +gi.getComment());

		System.out.println("setNumberOfMemberで「5」を入力します");
		gi.setNumberOfMember(5);
		System.out.println("getsetNumberOfMember出力: " +gi.getNumberOfMember());

		System.out.println("setIsGatheredで「true」を入力します");
		gi.setIsGathered(true);
		System.out.println("getsetNumberOfMember出力: " +gi.getIsGathered());

		System.out.println("setMainPhotoでtestImageを入力します");
		gi.setMainPhoto(testbi);
		try {
			ImageIO.write(gi.getMainPhoto(), "png", new File("./テスト用画像/GroupMainPhotoTest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
