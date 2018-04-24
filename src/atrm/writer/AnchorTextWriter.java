package atrm.writer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AnchorTextWriter {

	public AnchorTextWriter() {
	}

	public void writeAnchorText(String Uri, BufferedOutputStream bos, ByteArrayOutputStream baos) throws IOException {
		
		bos.write(Uri.getBytes());
		bos.write(13);
		bos.write(baos.toByteArray());
		bos.write(13);
	}
}
