package taAllocation;

import java.util.LinkedList;

public class Node {
	private Pair<TA,Pair<Course,Lab>> assignment;
	public LinkedList<Node> children;
	
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
