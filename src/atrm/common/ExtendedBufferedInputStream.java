package atrm.common;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExtendedBufferedInputStream extends BufferedInputStream {
	public ExtendedGZIPInputStream gzis;
	public int size;

	public ExtendedBufferedInputStream(InputStream in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	public ExtendedBufferedInputStream(InputStream in, int size) {
		super(in, size);
		// TODO Auto-generated constructor stub
	}

	public ExtendedBufferedInputStream(ExtendedGZIPInputStream gzis, int size) {
		super(gzis, size);
		this.gzis = gzis;
		this.size = size;
	}
	public byte[] getBufIfOpen() throws IOException {
        byte[] buffer = buf;
        if (buffer == null)
            throw new IOException("Stream closed");
        return buffer;
    }
	public void fill() throws IOException {
		System.out.println("Size is " + size);
		super.read();
		pos--;
//		判断是否已经读取size长度的内容到byte[]里，如果没有读取完整，循环读取直到读取完整
		while (gzis != null && count != size) {
			System.out.println("Count read " + count);
			System.out.println("gzis.Buf is " + gzis.readBuf);
			byte[] buffer = this.getBufIfOpen();
			count += gzis.read(buffer, count, size - count);
		}
		
	}

	public synchronized int read() throws IOException {
		if (pos >= count) {
			return -1;
		}
		return super.read();
	}

	public synchronized void markTag(int n) {
		markpos = pos - n;
	}

	public long getPos() {
		return this.pos;
	}

}
