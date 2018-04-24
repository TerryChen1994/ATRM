package atrm.entity;

public class HeaderLine {
	public String name;
	public String value;
	public String line;
	public HeaderLine(){
		name = null;
		value = null;
		line = null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	
}
