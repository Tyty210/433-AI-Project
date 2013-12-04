package taAllocation;

import java.util.LinkedList;

public class Node {
	private TA ta;
	private Course course;
	private Lab lab;	
	public LinkedList<Pair<Integer, TA>> children;
	private int LocalScore;
	
	public Node(TA Ta, Course Course, Lab Lab, int localscore, LinkedList<Pair<Integer, TA>> Children){
		ta = Ta;
		course = Course;
		lab = Lab;
		localscore = LocalScore;
		children = Children;
	}

	public void setTA(TA Ta){
		ta = Ta;
	}
	
	
}
