import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import javax.imageio.ImageIO;

public class ServerDriver{

	static UserInfo ui = new UserInfo();
	static GroupInfo gi = new GroupInfo();

	public static void main(String [] args) throws Exception{
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in), 1);
		ServerSocket ss = new ServerSocket(100);
		Socket sock = ss.accept();

		System.out.println("サーバが起動しました");

		// データ受信用オブジェクト
		Server.Receiver receiver = new Server.Receiver(sock);

		Server server = new Server("test");

		receiver.start();

		while(true){
			try {
				System.out.println("UserInfoを送信するなら0,GroupInfoを送信するなら1,メッセージを送信するなら2");
				String msg = r.readLine();
				if(Integer.parseInt(msg) == 0) {
					System.out.println("デフォルト情報を入力");

					System.out.println("state?");
					msg = r.readLine();
					ui.state = Integer.parseInt(msg);

					System.out.println("username?");
					msg = r.readLine();
					readExampleUserFile(msg);

					receiver.executeUserInfo(ui);

				}
				else if(Integer.parseInt(msg) == 1) {
					System.out.println("デフォルト情報を入力");

					System.out.println("state?");
					msg = r.readLine();
					gi.state = Integer.parseInt(msg);

					System.out.println("groupname?");
					msg = r.readLine();
					readExampleGroupFile(msg);

					receiver.executeGroupInfo(gi);
				}
				else if(Integer.parseInt(msg) == 2){
					System.out.println("受信用メッセージを入力してください");
					msg = r.readLine();
					receiver.receiveMessage(msg);
					System.out.println("テストメッセージ「" + msg + "」を受信しました");
				}
				else {
					System.out.println("正しい値を入力してください");
				}
				System.out.println("テスト操作を行った後、受信用テストメッセージを入力してください");

			}catch(IOException e) {
				System.err.println(e);
			}finally {
				ss.close();
			}
		}
	}

	//選択したファイルを読み込み
	public static void readExampleUserFile(String username) {
        FileReader fr;
        BufferedReader br;
		try {
			fr = new FileReader(System.getProperty("user.dir") + "\\ID\\" + username + ".txt");
	        br = new BufferedReader(fr);
	        String line;
			int line_counter = 0;
			ui = new UserInfo();

			while((line = br.readLine()) != null) {
				line_counter++;
				switch(line_counter) {

					case 1 :
						ui.studentNumber = Integer.parseInt(line);

						//画像の読み込み
						File studentCard = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_card.png");
						File main_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_main.png");
						File sub1_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub1.png");
						File sub2_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub2.png");
						File sub3_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub3.png");
						File sub4_image = new File(System.getProperty("user.dir") + "\\ID\\images" + "\\" + line + "\\" + line + "_sub4.png");
						BufferedImage card = ImageIO.read(studentCard);
						BufferedImage main = ImageIO.read(main_image);
						BufferedImage sub1 = ImageIO.read(sub1_image);
						BufferedImage sub2 = ImageIO.read(sub2_image);
						BufferedImage sub3 = ImageIO.read(sub3_image);
						BufferedImage sub4 = ImageIO.read(sub4_image);

						ui.setStudentCard(card);
						ui.setMainPhoto(main);
						ui.setSubPhoto(sub1,0);
						ui.setSubPhoto(sub2,1);
						ui.setSubPhoto(sub3,2);
						ui.setSubPhoto(sub4,3);
						break;

					case 2 :
						ui.password = line;
						break;

					case 3 :
						ui.name = line;
						break;

					case 4 :
						ui.gender = Integer.parseInt(line);
						break;

					case 5 :
						ui.grade = Integer.parseInt(line);
						break;

					case 6 :
						ui.faculty = Integer.parseInt(line);
						break;

					case 7 :
						ui.birth = Integer.parseInt(line);
						break;

					case 8 :
						ui.circle = Integer.parseInt(line);
						break;

					case 9 :
						ui.hobby = line;
						break;

					case 10 :
						if(line.length()>2) {
							System.out.println(line+"a");
							String sendStudents[] = line.split(" ");
							//System.out.println(line+"a");
							System.out.println(sendStudents[0]+"a");
							for(int i=0; i<sendStudents.length; i++) {
								if(sendStudents[i]!="")
									ui.sendGood[i] = Integer.parseInt(sendStudents[i]);
							}
						}
						break;

					case 11 :
						if(line.length()>2) {
							String receiveStudents[] = line.split(" ");
							for(int i=0; i<receiveStudents.length; i++) {
								if(receiveStudents[i]!="")
									ui.receiveGood[i] = Integer.parseInt(receiveStudents[i]);
							}
						}
						break;

					case 12 :
						if(line.length()>2) {
							String matchingStudents[] = line.split(" ");
							for(int i=0; i<matchingStudents.length; i++) {
								ui.matchedUser[i] = Integer.parseInt(matchingStudents[i]);
							}
						}
						break;

					case 13 :
						if(line.length()>2) {
							String joiningGroups[] = line.split(" ");
							for(int i=0; i<joiningGroups.length; i++) {
								ui.joiningGroup[i] = UUID.fromString(joiningGroups[i]);
							}
						}
						break;

					case 14 :
						if(line.length()>2) {
							String invitedGroups[] = line.split(" ");
							for(int i=0; i<invitedGroups.length; i++) {
								ui.invitedGroup[i] = UUID.fromString(invitedGroups[i]);
							}
						}
						break;

					case 15 :
						if(line.length()>0)
						ui.isAuthentificated = Integer.parseInt(line);
						break;

					case 16 :
						ui.lineId = line;
						break;

					case 17 :
						ui.isPublic = Boolean.parseBoolean(line);
						break;

				}
			}
		} catch (IOException e) {
			System.out.println("ユーザファイル読み込みがでエラー発生");
		}
	}

	//選択したファイルを読み込み
	public static void readExampleGroupFile(String groupname) {
		FileReader fr;
		BufferedReader br;
		try {
				fr = new FileReader(System.getProperty("user.dir") + "\\Group\\" + groupname + ".txt");
		        br = new BufferedReader(fr);
		        String line;
				int line_counter = 0;
				gi = new GroupInfo();

				while((line = br.readLine()) != null) {
					line_counter++;
					switch(line_counter) {
						case 1 :
							gi.groupNumber = UUID.fromString(line);

							//画像の読み込み
							File main_image = new File(System.getProperty("user.dir") + "\\Group\\images\\" + line + "_main.png");
							BufferedImage main = ImageIO.read(main_image);

							gi.setMainPhoto(main);
									;
							break;

						case 2 :
							gi.name = line;
							break;

						case 3 :
							gi.relation = line;
							break;

						case 4 :
							if(line.length()>2) {
								try {
									String sendGood[] = line.split(" ");
									for(int i=0; i<sendGood.length; i++) {
										gi.sendGood[i] = UUID.fromString(sendGood[i]);
									}
								 } catch (IllegalArgumentException e) {

							        }
							}
							break;

						case 5 :
							if(line.length()>2) {
								String receiveGood[] = line.split(" ");
								for(int i=0; i<receiveGood.length; i++) {
									gi.receiveGood[i] = UUID.fromString(receiveGood[i]);
								}
							}
							break;

						case 6 :
							if(line.length()>2) {
								String matchedGood[] = line.split(" ");
								for(int i=0; i<matchedGood.length; i++) {
									gi.matchedGroup[i] = UUID.fromString(matchedGood[i]);
								}
							}
							break;

						case 7 :
							gi.hostUser = Integer.parseInt(line);
							break;

						case 8 :
							if(line.length()>2) {
								String nonhostUser[] = line.split(" ");
								for(int i=0; i<nonhostUser.length; i++) {
									gi.nonhostUser[i] = Integer.parseInt(nonhostUser[i]);
								}
							}
							break;

						case 9 :
							gi.purpose = Integer.parseInt(line);
							break;

						case 10 :
							gi.comment = line;
							break;

						case 11 :
							gi.numberOfMember = Integer.parseInt(line);
							break;

						case 12 :
							gi.isGathered = Boolean.parseBoolean(line);
							break;

					}
				}
			} catch (IOException e) {
				System.out.println("グループファイル読み込みでエラー発生");
			}
	}
}

