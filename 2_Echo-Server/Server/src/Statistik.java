import java.io.File;

public class Statistik {
    private int shortest;
    private int longest;
    private int count;
    private long aggregateSize;

    public Statistik() {
	this.shortest = 0;
	this.longest = 0;
	this.count = 0;
	this.aggregateSize = 0;
    }

    public void addMessage(String message) {
	this.aggregateSize += message.length();
	if (message.length() < this.shortest || this.shortest == 0) {
	    this.shortest = message.length();
	}
	if (message.length() > this.longest) {
	    this.longest = message.length();
	}
	this.count++;
    }

    public String toString() {
	return "Shortest message: " + shortest + "\nLongest message: " + longest +
	    "\nMessage count: " + count + "\nAggregate Size: " + aggregateSize;
    }
}
