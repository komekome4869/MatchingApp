import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class MyBufferedImage implements Serializable{

	transient int [] buf; // この変数は、ローカル変数にした方がよい。改良すべき点です。

	transient BufferedImage img; // 直列化不可フィールドの指定

	static int [] getArrayByImage(BufferedImage img){
		int w = img.getWidth();
		int h = img.getHeight();
		int []buf = new int[ w * h ];
		img.getRGB(0, 0, w, h, buf, 0, w);
		return buf;
	}

	static BufferedImage getImageByArray(int [] buf, int width){
		int height = buf.length / width;
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
		img.setRGB(0, 0, width, height, buf, 0, width);
		return img;
	}

	/*private void writeObject(ObjectOutputStream stream) throws IOException {
		//stream.putFields();
		//stream.writeFields();//親クラスの情報出力
		//上記この2行の代わりに、
		stream.defaultWriteObject();//を使った。

		int width = this.img.getWidth();
		stream.writeInt(width);

		this.buf = getArrayByImage(this.img);
		stream.writeInt(this.buf.length);
		for(int i=0; i < this.buf.使ったlength;  i++){
			stream.writeInt(this.buf[i]);
		}
	}*/

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		//stream.readFields();
		//　上記代わりに、
		stream.defaultReadObject();//を使った。

		int width = stream.readInt();
		this.buf = new int[stream.readInt()];
		for(int i=0; i < this.buf.length;  i++){
			this.buf[i] = stream.readInt();
		}
		this.img = getImageByArray(this.buf, width);
	}
}