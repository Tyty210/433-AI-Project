package taAllocation;

import java.util.LinkedList;

public class Node {
	private Pair<TA,Pair<Course,Lab>> assignment;
	public LinkedList<Pair<Integer, Node>> children; //0 is "no", 1 is "yes", -1 is unexpanded, 2 is forced
	
	public Node(Pair<TA,Pair<Course,Lab>> ass){
		setAssignment(ass);
	}

	public Pair<TA,Pair<Course,Lab>> getAssignment() {
		return assignment;
	}

	public void setAssignment(Pair<TA,Pair<Course,Lab>> assignment) {
		this.assignment = assignment;
	}
}
