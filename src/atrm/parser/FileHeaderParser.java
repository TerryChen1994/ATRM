package atrm.parser;

import java.io.IOException;
import java.io.PushbackInputStream;

import atrm.entity.FileHeader;
import atrm.entity.HeaderLine;
import atrm.reader.HeaderReader;

public class FileHeaderParser {
	protected boolean validVersion;
	protected HeaderReader headerReader;
	protected FileHeader fileHeader;

	public FileHeaderParser() {
		this.validVersion = false;
		this.headerReader = new HeaderReader();
		this.fileHeader = new FileHeader();
	}

	public FileHeader parseFileHeader(PushbackInputStream pbin) throws IOException {
		headerReader = new HeaderReader();
		if (parseVersion(pbin)) {
			boolean loop = true;
			while (loop) {
				HeaderLine headerLine = headerReader.readLine(pbin, 2);
				if (!headerReader.isEnd()) {
					if (headerLine.line.length() <= 0 || (headerLine.getName() == null)
							|| (headerLine.getValue() == null) || headerLine.getValue().equals("")
							|| headerLine.getName().equals("")) {
						if (fileHeader.getContentLength() != null && !fileHeader.getContentLength().equals(""))
								skipToRecord(pbin);
						loop = false;
					} else {
						addHeader(headerLine);
					}
				} else {
					loop = false;
				}
			}
			
			
		}
		return fileHeader;
	}

	public boolean parseVersion(PushbackInputStream pbin) throws IOException {
		headerReader = new HeaderReader();
		boolean loop = true;
		while (loop) {
			HeaderLine headerLine = headerReader.readLine(pbin, 1);
			if (!headerReader.isEnd()) {

				if ((headerLine.getLine() != null) && headerLine.getLine().toUpperCase().startsWith("WARC/")) {
					this.validVersion = true;
					String version = headerLine.getLine().substring("WARC/".length());
					fileHeader.setWarcVersion(version);
					loop = false;
				}
			} else {
				loop = false;
			}
		}
		return validVersion;
	}

	public void addHeader(HeaderLine headerLine) {
		String name = headerLine.getName().toUpperCase();
		String value = headerLine.getValue();
		switch (name) {
		case "WARC-TYPE":
			fileHeader.setWarcType(value);
			break;
		case "WARC-DATE":
			fileHeader.setWarcDate(value);
			break;
		case "WARC-FILENAME":
			fileHeader.setWarcFileName(value);
			break;
		case "WARC-NUMBER-OF-DOCUMENTS":
			fileHeader.setWarcNumberOfDocuments(value);
			break;
		case "WARC-FILE-LENGTH":
			fileHeader.setWarcFileLength(value);
			break;
		case "WARC-DATA-TYPE":
			fileHeader.setWarcDataType(value);
			break;
		case "WARC-RECORD-ID":
			fileHeader.setWarcRecordId(value);
			break;
		case "CONTENT-TYPE":
			fileHeader.setContentType(value);
			break;
		case "CONTENT-LENGTH":
			fileHeader.setContentLength(value);
			break;
		}
	}
	public void skipToRecord(PushbackInputStream pbin) throws IOException{
		long skip = Long.parseLong(fileHeader.getContentLength());
		pbin.skip(skip);
	}
}
