import java.io.Serializable;

public class Author implements Serializable {
    private String forename;
    private String surename;

    public Author(String forename, String surename) {
	this.forename = forename;
	this.surename = surename;
    }

    /**For deserialization purposes */
    public Author() {

    }

    public String getForename() {
	return forename;
    }

    public String getSurename() {
	return surename;
    }
}
