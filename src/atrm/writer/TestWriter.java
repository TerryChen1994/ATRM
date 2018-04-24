package atrm.writer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestWriter {

	public TestWriter() {
	}

	public void writeAnchorText(String Uri, BufferedOutputStream bos, ByteArrayOutputStream baos) throws IOException {
//		byte metaPage[] = new byte[] { 60, 112, 97, 103, 101, 62};
//		bos.write(metaPage);
		bos.write(Uri.getBytes());
		bos.write(13);
//		bos.write(13);
		bos.write(baos.toByteArray());
		bos.write(13);
//		for (int i = 0; i < 10; i++) {
//			bos.write(95);
//		}
//		bos.write(13);
//		bos.write(13);
	}
}
