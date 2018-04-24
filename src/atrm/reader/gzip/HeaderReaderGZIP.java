package atrm.reader.gzip;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import atrm.common.ExtendedGZIPInputStream;
import atrm.entity.HeaderLine;

public class HeaderReaderGZIP {
	protected ByteArrayOutputStream baos;
	protected boolean is13;
	protected boolean is58;
	protected boolean isEnd;
	protected StringBuffer name;
	protected StringBuffer value;
	protected StringBuffer line;

	public HeaderReaderGZIP() {
		init();
	}
	public void init(){
		name = new StringBuffer();
		value = new StringBuffer();
		line = new StringBuffer();
		is13 = false;
		isEnd = false;
		is58 = false;
		baos = new ByteArrayOutputStream();
	}

	public HeaderLine readLine(ExtendedGZIPInputStream egzis, int state) throws IOException {
		HeaderLine headerLine = new HeaderLine();
		boolean loop = true;
		baos = new ByteArrayOutputStream();
		while (loop) {
			int c = egzis.read();
			if (c != -1) {
				baos.write(c);
			}
			switch (state) {
			case 1:{
				switch (c) {
				case -1:
					loop = false;
					break;
				case 13:
					this.is13 = true;
					break;
				case 10:
					if (this.is13 == true) {
						loop = false;
						is13 = false;
						headerLine.setLine(line.toString());
						line.setLength(0);
					}
					break;
				default:
					line.append((char) c);
					break;
				}
				break;
			}
			case 2:{
				switch (c) {
				case -1:
					loop = false;
					break;
				case 13:
					this.is13 = true;
					break;
				case 10:
					if (this.is13 == true) {
						headerLine.setLine(line.toString());
						line.setLength(0);
						loop = false;
						is13 = false;
					}
					break;
				case 32:
					if(is58)
						state = 3;
					break;
				case 58:
					is58 = true;
					break;
				default:
					name.append((char)c);
					line.append((char)c);
					break;
				}
				break;
			}
			case 3:{
				switch (c) {
				case -1:
					loop = false;
					break;
				case 13:
					this.is13 = true;
					break;
				case 10:
					if (this.is13 == true) {
						headerLine.setLine(line.toString());
						headerLine.setName(name.toString());
						headerLine.setValue(value.toString());
						line.setLength(0);
						name.setLength(0);
						value.setLength(0);
						loop = false;
						is13 = false;
						is58 = false;
					}
					break;
				default:
					value.append((char)c);
					line.append((char)c);
					break;
				}
			}
			break;
			}

		}

		this.isEnd = (baos.toByteArray().length == 0);
		return headerLine;
	}

	public boolean isEnd() {
		return this.isEnd;
	}
}
