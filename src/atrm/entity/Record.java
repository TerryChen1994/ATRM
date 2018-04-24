package atrm.entity;


import atrm.common.ExtendedBufferedInputStream;

public class Record {
	private HttpHeader httpHeader;
	private RecordHeader recordHeader;
	private ExtendedBufferedInputStream content;
	
	public Record(){
		httpHeader = null;
		recordHeader = null;
		content = null;
	}
	public HttpHeader getHttpHeader() {
		return httpHeader;
	}
	public void setHttpHeader(HttpHeader httpHeader) {
		this.httpHeader = httpHeader;
	}
	public RecordHeader getRecordHeader() {
		return recordHeader;
	}
	public void setRecordHeader(RecordHeader recordHeader) {
		this.recordHeader = recordHeader;
	}
	public ExtendedBufferedInputStream getContent() {
		return content;
	}
	public void setContent(ExtendedBufferedInputStream content) {
		this.content = content;
	}
	
}
