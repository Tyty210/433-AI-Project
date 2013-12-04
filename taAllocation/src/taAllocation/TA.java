package taAllocation;

import java.util.LinkedList;

public class TA extends Entity {
	
	public LinkedList<Pair<Course, Lab>> getInstructing() {
		return instructing;
	}

	private Course[] preferences;
	private LinkedList<Pair<Course, Lecture>> classes;
	private LinkedList<Course> knows;
	private LinkedList<Pair<Course, Lab>>	instructing;
	
	public TA (String name){
		super(name);
		preferences = new Course[3];
		classes = new LinkedList<Pair<Course, Lecture>>();
		knows = new LinkedList<Course>();
		instructing = new LinkedList<Pair<Course,Lab>>();
		preferences[0] = new Course("null",0);
		preferences[1] = new Course("null",0);
		preferences[2] = new Course("null",0);
	}
	
	public Course getPreference(int n){
		return preferences[n];
	}
	
	public int setPreference(Course c, int n){
		preferences[n]=c;
		return 0;
	}
	
	
	public int addClass(Course c, Lecture l){
		if (classes.contains(c)){
			return -1;
		}
		classes.add(new Pair<Course, Lecture>(c, l));
		return 0;
	}
	
	public int addKnows(Course c){
		if(knows.contains(c)){
			return -1;
		}
		knows.add(c);
		return 0;
	}
	public Course[] getPreferences() {
		return preferences;
	}
	public LinkedList<Pair<Course, Lecture>> getClasses() {
		return classes;
	}
	public LinkedList<Course> getKnows() {
		return knows;
	}

	public int addLab(Pair<Course,Lab> la) {
		if (instructing.contains(la)){
			return -1;
		}
		instructing.add(la);
		return 0;
	}
	public void remLab(Pair<Course,Lab> la) {
		instructing.remove(la);
	}
	

}
