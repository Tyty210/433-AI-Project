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


	public TA getTa() {
		return ta;
	}

	public void setTa(TA ta) {
		this.ta = ta;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}

	public LinkedList<Pair<Integer, TA>> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<Pair<Integer, TA>> children) {
		this.children = children;
	}

	public int getLocalScore() {
		return LocalScore;
	}

	public void setLocalScore(int localScore) {
		LocalScore = localScore;
	}
	
	
}
