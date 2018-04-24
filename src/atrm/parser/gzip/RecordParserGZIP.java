package atrm.parser.gzip;

import java.io.IOException;

import atrm.common.ExtendedBufferedInputStream;
import atrm.common.ExtendedGZIPInputStream;
import atrm.entity.HeaderLine;
import atrm.entity.Record;
import atrm.entity.RecordHeader;
import atrm.reader.gzip.HeaderReaderGZIP;

public class RecordParserGZIP {
	protected boolean validVersion;
	protected HeaderReaderGZIP headerReader;
	protected Record record;
	protected RecordHeader recordHeader;
//	protected HttpHeader httpHeader;
	protected ExtendedBufferedInputStream content;
	
	public RecordParserGZIP(){
		init();
	}
	public void init(){
		validVersion = false;
		headerReader = new HeaderReaderGZIP();
		record = new Record();
		recordHeader = new RecordHeader();
//		httpHeader = new HttpHeader();
		content = null;
	}
	public Record getNextRecord(ExtendedGZIPInputStream egzis) throws IOException{
		init();
		if(parseRecordHeader(egzis)){
			record.setRecordHeader(recordHeader);
//			record.setHttpHeader(httpHeader);
			record.setContent(content);
		}
		else{
			return null;
		}
		return record;
	}
	public boolean parseRecordHeader(ExtendedGZIPInputStream egzis) throws IOException{
		if (parseRecordVersion(egzis)) {
			boolean loop = true;
			while (loop) {
				HeaderLine headerLine = headerReader.readLine(egzis, 2);
				if (!headerReader.isEnd()) {
					if (headerLine.line.length() <= 0 || (headerLine.getName() == null)
							|| (headerLine.getValue() == null) || headerLine.getValue().equals("")
							|| headerLine.getName().equals("")) {
						if (recordHeader.getContentLength() != null && !recordHeader.getContentLength().equals(""))
//								skipToNextRecord(egzis);
								addRecordContent(egzis);
						loop = false;
					} else {
						addRecordHeader(headerLine);
					}
				} else {
					loop = false;
				}
			}
			
			
		}
		return validVersion;
	}
	public boolean parseRecordVersion(ExtendedGZIPInputStream egzis) throws IOException {
		boolean loop = true;
		while (loop) {
			HeaderLine headerLine = headerReader.readLine(egzis, 1);
			if (!headerReader.isEnd()) {
				if ((headerLine.getLine() != null) && headerLine.getLine().toUpperCase().startsWith("WARC/")) {
					this.validVersion = true;
					String version = headerLine.getLine().substring("WARC/".length());
					recordHeader.setWarcVersion(version);
					loop = false;
				}
			} else {
				loop = false;
			}
		}
		return validVersion;
	}
	public void addRecordHeader(HeaderLine headerLine){
		String name = headerLine.getName().toUpperCase();
		String value = headerLine.getValue();
		switch (name) {
		case "WARC-TYPE":
			recordHeader.setWarcType(value);
			break;
		case "WARC-DATE":
			recordHeader.setWarcDate(value);
			break;
		case "WARC-TREC-ID":
			recordHeader.setWarcTrecId(value);
			break;
		case "WARC-TARGET-URI":
			recordHeader.setWarcTargetUri(value);
			break;
		case "WARC-PAYLOAD-DIGEST":
			recordHeader.setWarcPayloadDigest(value);
			break;
		case "WARC-IP-ADDRESS":
			recordHeader.setWarcIpAddress(value);
			break;
		case "WARC-RECORD-ID":
			recordHeader.setWarcRecordId(value);
			break;
		case "CONTENT-TYPE":
			recordHeader.setContentType(value);
			break;
		case "CONTENT-LENGTH":
			recordHeader.setContentLength(value);
			break;
		}
	}
	public void addRecordContent(ExtendedGZIPInputStream egzis) throws IOException{
		int length = Integer.parseInt(recordHeader.getContentLength());
		egzis.resize(length);
		content = new ExtendedBufferedInputStream(egzis, length);
		content.fill();
	}
	public void skipToNextRecord(ExtendedGZIPInputStream egzis) throws IOException{
		long skip = Long.parseLong(recordHeader.getContentLength());
		egzis.skip(skip);
	}
}
