package util;

public class FieldToken {
	private String name;
	private String children;
	
	public FieldToken(String fullname) {
		int delim = fullname.indexOf(46);
		if (delim > -1) {
			this.name = fullname.substring(0, delim);
			this.children = fullname.substring(delim + 1);
		} else {
			this.name = fullname;
			this.children = null;
		}
		delim = this.name.indexOf(91);
		if (delim > -1) {
			this.name = this.name.substring(0, delim);
		}
	}
	
	public boolean hasNext() {
		return (this.children != null);
	}
	
	public FieldToken next() {
		return new FieldToken(this.children);
	}

	public String getName() {
		return name;
	}

	public String getChildren() {
		return children;
	}
}
