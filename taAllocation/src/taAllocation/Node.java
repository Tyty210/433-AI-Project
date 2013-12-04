package taAllocation;

import java.util.LinkedList;

public class Node {
	private Pair<TA,Pair<Course,Lab>> assignment;
	public LinkedList<Node> children;
	public LinkedList<Node> Expanded; 
	public int curScore;
	
	public Node(Pair<TA,Pair<Course,Lab>> ass, int oldScore, int newScore){
		setAssignment(ass);
		curScore = oldScore + newScore;
	}

	public Pair<TA,Pair<Course,Lab>> getAssignment() {
		return assignment;
	}

	public void setAssignment(Pair<TA,Pair<Course,Lab>> assignment) {
		this.assignment = assignment;
	}
}
