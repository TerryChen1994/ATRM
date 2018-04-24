package atrm.entity;

public class FileHeader {
	private String warcVersion;
	private String warcType;
	private String warcDate;
	private String warcFileName;
	private String warcNumberOfDocuments;
	private String warcFileLength;
	private String warcDataType;
	private String warcRecordId;
	private String contentType;
	private String contentLength;

	public FileHeader() {
		warcVersion = null;
		warcType = null;
		warcDate = null;
		warcFileName = null;
		warcNumberOfDocuments = null;
		warcFileLength = null;
		warcDataType = null;
		warcRecordId = null;
		contentType = null;
		contentLength = null;
	}

	public String getWarcVersion() {
		return warcVersion;
	}

	public void setWarcVersion(String warcVersion) {
		this.warcVersion = warcVersion;
	}

	public String getWarcType() {
		return warcType;
	}

	public void setWarcType(String warcType) {
		this.warcType = warcType;
	}

	public String getWarcDate() {
		return warcDate;
	}

	public void setWarcDate(String warcDate) {
		this.warcDate = warcDate;
	}

	public String getWarcFileName() {
		return warcFileName;
	}

	public void setWarcFileName(String warcFileName) {
		this.warcFileName = warcFileName;
	}

	public String getWarcNumberOfDocuments() {
		return warcNumberOfDocuments;
	}

	public void setWarcNumberOfDocuments(String warcNumberOfDocuments) {
		this.warcNumberOfDocuments = warcNumberOfDocuments;
	}

	public String getWarcFileLength() {
		return warcFileLength;
	}

	public void setWarcFileLength(String warcFileLength) {
		this.warcFileLength = warcFileLength;
	}

	public String getWarcDataType() {
		return warcDataType;
	}

	public void setWarcDataType(String warcDataType) {
		this.warcDataType = warcDataType;
	}

	public String getWarcRecordId() {
		return warcRecordId;
	}

	public void setWarcRecordId(String warcRecordId) {
		this.warcRecordId = warcRecordId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentLength() {
		return contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

}
