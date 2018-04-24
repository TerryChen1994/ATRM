package atrm.entity;

public class AnchorText {
	private String href;
	private String text;
	public AnchorText(){
		href = null;
		text = null;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
