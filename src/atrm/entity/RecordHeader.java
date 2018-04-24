package atrm.entity;

public class RecordHeader {
	private String warcVersion;
	private String warcType;
	private String warcDate;
	private String warcTrecId;
	private String warcTargetUri;
	private String warcPayloadDigest;
	private String warcIpAddress;
	private String warcRecordId;
	private String contentType;
	private String contentLength;

	public RecordHeader() {
		warcVersion = null;
		warcType = null;
		warcDate = null;
		warcTrecId = null;
		warcTargetUri = null;
		warcPayloadDigest = null;
		warcIpAddress = null;
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

	public String getWarcTrecId() {
		return warcTrecId;
	}

	public void setWarcTrecId(String warcTrecId) {
		this.warcTrecId = warcTrecId;
	}

	public String getWarcTargetUri() {
		return warcTargetUri;
	}

	public void setWarcTargetUri(String warcTargetUri) {
		this.warcTargetUri = warcTargetUri;
	}

	public String getWarcPayloadDigest() {
		return warcPayloadDigest;
	}

	public void setWarcPayloadDigest(String warcPayloadDigest) {
		this.warcPayloadDigest = warcPayloadDigest;
	}

	public String getWarcIpAddress() {
		return warcIpAddress;
	}

	public void setWarcIpAddress(String warcIpAddress) {
		this.warcIpAddress = warcIpAddress;
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
