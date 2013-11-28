package taAllocation;
import java.util.LinkedList;

public class Instructor extends Entity {
	private LinkedList<Pair<Course,Lecture>> lectures;
	
	public LinkedList<Pair<Course,Lecture>> getLectures() {
		return lectures;
	}
	public Instructor(String name){
		super(name);
		lectures = new LinkedList<Pair<Course,Lecture>>();
	}
	public int addLecture(Pair<Course,Lecture> p){
		if (lectures.contains(p)){
			return -1;
		}
		lectures.add(p);
		return 0;
	}
	
}
