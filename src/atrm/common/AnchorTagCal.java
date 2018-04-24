package atrm.common;

public class AnchorTagCal {
	private int matched;
	private int startATag;
	private int endATag;
	private int startATagErr;
	private int endATagErr;
	private int matchedStartTag;
	private int matchedEndTag;
	public AnchorTagCal(){
		matched = 0;
		startATag = 0;
		endATag = 0;
		startATagErr = 0;
		endATagErr = 0;
		matchedStartTag = 0;
		matchedEndTag = 0;;
	}
	public int getStartATag() {
		return startATag;
	}
	public void setStartATag(int startATag) {
		this.startATag = startATag;
	}
	public int getEndATag() {
		return endATag;
	}
	public void setEndATag(int endATag) {
		this.endATag = endATag;
	}
	public int getStartATagErr() {
		return startATagErr;
	}
	public void setStartATagErr(int startATagErr) {
		this.startATagErr = startATagErr;
	}
	public int getEndATagErr() {
		return endATagErr;
	}
	public void setEndATagErr(int endATagErr) {
		this.endATagErr = endATagErr;
	}
	public int getMatched() {
		return matched;
	}
	public void setMatched(int matched) {
		this.matched = matched;
	}
	public int getMatchedStartTag() {
		return matchedStartTag;
	}
	public void setMatchedStartTag(int matchedStartTag) {
		this.matchedStartTag = matchedStartTag;
	}
	public int getMatchedEndTag() {
		return matchedEndTag;
	}
	public void setMatchedEndTag(int matchedEndTag) {
		this.matchedEndTag = matchedEndTag;
	}
	
}
