package atrm.common;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class ExtendedGZIPInputStream extends GZIPInputStream {
	public int readBuf;

	public ExtendedGZIPInputStream(InputStream in) throws IOException {
		super(in);
		// TODO Auto-generated constructor stub
	}

	public void resize(int length) {
		this.buf = new byte[length];
	}

	@Override
	public int read(byte[] buf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		try {
			this.readBuf = super.read(buf, off, len);
		} catch (EOFException e) {
			return this.readBuf = -1;
		} catch (ZipException zipe) {
			return this.readBuf = -1;
		}
		return this.readBuf;
	}

}
