import java.util.UUID;
public class ClientDriver {

	public static void main(String[] args) {
		Client client=new Client();
		UserInfo user=new UserInfo();
		user.setName("山田太郎");
		GroupInfo group = new GroupInfo();
		group.setName("山田グループ");
		UUID uuid = UUID.randomUUID();
		group.setStudentNumber(uuid);
		System.out.println("Scheckメソッドでサーバに接続します");
		client.Scheck(0, "Scheck");
		System.out.println("Shomeメソッドでサーバに接続します");
		client.Shome(1);
		System.out.println("Susersearchメソッドでサーバに接続します");
		client.Susersearch(2, "Susersearch");
		System.out.println("Sgroupsearchメソッドでサーバに接続します");
		client.Sgroupsearch(3, "Sgroupsearch");
		System.out.println("sendUserInfoメソッドでサーバに接続します");
		client.sendUserInfo(user);
		System.out.println("Sgroup_homeメソッドでサーバに接続します");
		client.Sgroup_home(4);
		System.out.println("Sgoodメソッドでサーバに接続します");
		client.Sgood(5);
		System.out.println("Sgroup_goodメソッドでサーバに接続します");
		client.Sgroup_good(group.getStudentNumber());
		System.out.println("SchangeProfメソッドでサーバに接続します");
		client.SchangeProf(user);
		System.out.println("SchangeGroupProfメソッドでサーバに接続します");
		client.SchangeGroupProf(group);
		System.out.println("SmakeGroupメソッドでサーバに接続します");
		client.SmakeGroup(group);
		System.out.println("SgetmyUserprofメソッドでサーバに接続します");
		client.SgetmyUserprof(6);
		System.out.println("SgetyourUserprofメソッドでサーバに接続します");
		client.SgetyourUserprof(7);
		System.out.println("SgetmyGroupprofメソッドでサーバに接続します");
		client.SgetmyGroupprof(group.getStudentNumber());
		System.out.println("SgetyourGroupprofメソッドでサーバに接続します");
		client.SgetyourGroupprof(group.getStudentNumber());
		System.out.println("SdeleteUserメソッドでサーバに接続します");
		client.SdeleteUser(8);
		System.out.println("SdeleteGroupメソッドでサーバに接続します");
		client.SdeleteGroup(group.getStudentNumber());
		System.out.println("SjoinGroupメソッドでサーバに接続します");
		client.SjoinGroup(group.getStudentNumber());
		System.out.println("SrejectJoinGroupメソッドでサーバに接続します");
		client.SrejectJoinGroup(group.getStudentNumber());
		System.out.println("SrejectGoodメソッドでサーバに接続します");
		client.SrejectGood(9);
		System.out.println("SrejectGoodfromGroupメソッドでサーバに接続します");
		client.SrejectGoodfromGroup(group.getStudentNumber());
		
		
	}

}
