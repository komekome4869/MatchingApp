import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

public class UserInfoDriver {

	public static void main(String[] args) {
		UserInfo ui = new UserInfo();
		int[] testInt= {1234561,1234562};
		UUID[] testUUID= {UUID.randomUUID(),UUID.randomUUID()};
		UUID testUUID2=UUID.randomUUID();
		BufferedImage[] testbi=new BufferedImage[4];
		for(int i=0;i<4;i++) {
			testbi[i]=null;
		}
		try {
			testbi[0]=ImageIO.read(new File("./テスト用画像/testImage.png"));
			testbi[1]=ImageIO.read(new File("./テスト用画像/testImage2.png"));
			testbi[2]=ImageIO.read(new File("./テスト用画像/testImage3.png"));
			testbi[3]=ImageIO.read(new File("./テスト用画像/testImage4.png"));
		} catch (IOException e) {
			System.out.println("テスト用画像の読み込みに失敗");
		}

		System.out.println("setStudentNumberで「1234567」を入力します");
		ui.setStudentNumber(1234567);
		System.out.println("getStudentNumber出力: " +ui.getStudentNumber());

		System.out.println("setPasswordで「password」を入力します");
		ui.setPassword("password");
		System.out.println("getPassword出力: " +ui.getPassword());

		System.out.println("setNameで「山田太郎」を入力します");
		ui.setName("山田太郎");
		System.out.println("getName出力: " +ui.getName());

		System.out.println("setGenderで「1」を入力します");
		ui.setGender(1);
		System.out.println("getGender出力: " +ui.getGender());

		System.out.println("setGradeで「1」を入力します");
		ui.setGrade(1);
		System.out.println("getGrade出力: " +ui.getGrade());

		System.out.println("setFacultyで「1」を入力します");
		ui.setFaculty(1);
		System.out.println("getFuculty出力: " +ui.getFaculty());

		System.out.println("setBirthで「1」を入力します");
		ui.setBirth(1);
		System.out.println("getBirth出力: " +ui.getBirth());

		System.out.println("setCircleで「1」を入力します");
		ui.setCircle(1);
		System.out.println("getCircle出力: " +ui.getCircle());

		System.out.println("setHobbyで「読書」を入力します");
		ui.setHobby("読書");
		System.out.println("getHobby出力: " +ui.getHobby());

		System.out.println("setSendGoodで{1234561,1234562}を入力します");
		ui.setSendGood(testInt);
		System.out.println("getSendGood出力\n第一要素：" +ui.getSendGood()[0]+"\n第2要素："+ui.getSendGood()[1]);

		System.out.println("setSendGoodで配列の第1要素に「2234561」を入力します");
		ui.setSendGood(2234561,0);
		System.out.println("getSendGood出力\n第一要素：" +ui.getSendGood()[0]+"\n第2要素："+ui.getSendGood()[1]);

		System.out.println("setReceiveGoodで{1234561,1234562}を入力します");
		ui.setReceiveGood(testInt);
		System.out.println("getReceiveGood出力\n第一要素：" +ui.getReceiveGood()[0]+"\n第2要素："+ui.getReceiveGood()[1]);

		System.out.println("setReceiveGoodで配列の第1要素に「2234561」を入力します");
		ui.setReceiveGood(2234561,0);
		System.out.println("getReceiveGood出力\n第一要素：" +ui.getReceiveGood()[0]+"\n第2要素："+ui.getReceiveGood()[1]);

		System.out.println("setMatchedUserで{1234561,1234562}を入力します");
		ui.setMatchedUser(testInt);
		System.out.println("getMatchedUser出力\n第一要素：" +ui.getMatchedUser()[0]+"\n第2要素："+ui.getMatchedUser()[1]);

		System.out.println("setMatchedUserで配列の第1要素に「2234561」を入力します");
		ui.setMatchedUser(2234561,0);
		System.out.println("getMatchedUser出力\n第一要素：" +ui.getMatchedUser()[0]+"\n第2要素："+ui.getMatchedUser()[1]);

		System.out.println("setJoiningGroupで{"+testUUID[0]+","+testUUID[1]+"}を入力します");
		ui.setJoiningGroup(testUUID);
		System.out.println("getJoiningGroup出力\n第一要素：" +ui.getJoiningGroup()[0]+"\n第2要素："+ui.getJoiningGroup()[1]);

		System.out.println("setJoiningGroupで配列の第1要素に"+testUUID2+"を入力します");
		ui.setJoiningGroup(testUUID2,0);
		System.out.println("getJoiningGroup出力\n第一要素：" +ui.getJoiningGroup()[0]+"\n第2要素："+ui.getJoiningGroup()[1]);

		System.out.println("setInvitedGroupで{"+testUUID[0]+","+testUUID[1]+"}を入力します");
		ui.setInvitedGroup(testUUID);
		System.out.println("getInvitedGroup出力\n第一要素：" +ui.getInvitedGroup()[0]+"\n第2要素："+ui.getInvitedGroup()[1]);

		System.out.println("setInvitedGroupで配列の第1要素に"+testUUID2+"を入力します");
		ui.setInvitedGroup(testUUID2,0);
		System.out.println("getInvitedGroup出力\n第一要素：" +ui.getInvitedGroup()[0]+"\n第2要素："+ui.getInvitedGroup()[1]);

		System.out.println("setIsAuthentificatedで「1」を入力します");
		ui.setIsAuthentificated(1);
		System.out.println("getIsAuthentificated出力: " +ui.getIsAuthentificated());

		System.out.println("setLineIdで「lineid」を入力します");
		ui.setLineId("lineid");
		System.out.println("getLineId出力: " +ui.getLineId());

		System.out.println("setIsPublicで「false」を入力します");
		ui.setIsPublic(false);
		System.out.println("getIsPublic出力: " +ui.getIsPublic());

		System.out.println("setStudentCardでtestImageを入力します");
		ui.setStudentCard(testbi[0]);
		try {
			ImageIO.write(ui.getStudentCard(), "png", new File("./テスト用画像/StudentCardTest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("setMainPhotoでtestImage2を入力します");
		ui.setMainPhoto(testbi[1]);
		try {
			ImageIO.write(ui.getMainPhoto(), "png", new File("./テスト用画像/MainPhotoTest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("setSubPhotoでtestImage,testImage2,testImage3,testImage4を入力します");
		ui.setSubPhoto(testbi);
		try {
			ImageIO.write(ui.getSubPhoto()[0], "png", new File("./テスト用画像/SubPhotoTest1.png"));
			ImageIO.write(ui.getSubPhoto()[1], "png", new File("./テスト用画像/SubPhotoTest2.png"));
			ImageIO.write(ui.getSubPhoto()[2], "png", new File("./テスト用画像/SubPhotoTest3.png"));
			ImageIO.write(ui.getSubPhoto()[3], "png", new File("./テスト用画像/SubPhotoTest4.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("setSubPhotoで配列の第1要素にtestImage4を入力します");
		ui.setSubPhoto(testbi[3],0);
		try {
			ImageIO.write(ui.getSubPhoto()[0], "png", new File("./テスト用画像/SubPhoto単体Test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
