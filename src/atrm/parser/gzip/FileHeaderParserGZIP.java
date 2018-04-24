package atrm.parser.gzip;

import java.io.IOException;

import atrm.common.ExtendedGZIPInputStream;
import atrm.entity.FileHeader;
import atrm.entity.HeaderLine;
import atrm.reader.gzip.HeaderReaderGZIP;

public class FileHeaderParserGZIP {
	protected boolean validVersion;
	protected HeaderReaderGZIP headerReader;
	protected FileHeader fileHeader;

	public FileHeaderParserGZIP() {
		this.validVersion = false;
		this.headerReader = new HeaderReaderGZIP();
		this.fileHeader = new FileHeader();
	}

	public FileHeader parseFileHeader(ExtendedGZIPInputStream egzis) throws IOException {
		headerReader = new HeaderReaderGZIP();
		if (parseVersion(egzis)) {
			boolean loop = true;
			while (loop) {
				HeaderLine headerLine = headerReader.readLine(egzis, 2);
				if (!headerReader.isEnd()) {
					if (headerLine.line.length() <= 0 || (headerLine.getName() == null)
							|| (headerLine.getValue() == null) || headerLine.getValue().equals("")
							|| headerLine.getName().equals("")) {
						if (fileHeader.getContentLength() != null && !fileHeader.getContentLength().equals(""))
								skipToRecord(egzis);
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

	public boolean parseVersion(ExtendedGZIPInputStream egzis) throws IOException {
		headerReader = new HeaderReaderGZIP();
		boolean loop = true;
		while (loop) {
			HeaderLine headerLine = headerReader.readLine(egzis, 1);
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
	public void skipToRecord(ExtendedGZIPInputStream egzis) throws IOException{
		long skip = Long.parseLong(fileHeader.getContentLength());
		egzis.skip(skip);
	}
}
