package taAllocation;

import java.util.LinkedList;

public class Course extends Entity {
	private int type;
	private LinkedList<Lab> labs;
	private LinkedList<Lecture> lectures;
	private LinkedList<Instructor> instructors;
	
	public Course(String name, int t){
		super(name);
		type = t;
		labs = new LinkedList<Lab>();
		lectures = new LinkedList<Lecture>();
		instructors = new LinkedList<Instructor>();
	}
	
	public void addLab(Lecture lec, String x){
		Lab newLab = new Lab(x);
		lec.addLab(newLab);
		labs.add(newLab);
	}
	
	public int addLabTime(String x, Timeslot y){
		Lab tempLab = new Lab(x);
		for(int i = 0; i<labs.size();i++){
			if(labs.get(i).compareTo(tempLab)==0){
				return(labs.get(i).setTime(y));
			}
				
		}
		return -2; //Lab doesn't exist
	}
	
	public int addLectureTime(String x, Timeslot y){
		Lecture tempLec = new Lecture(x);
		for(int i = 0; i<labs.size();i++){
			if(labs.get(i).compareTo(tempLec)==0){
				return(labs.get(i).setTime(y));
			}
				
		}
		return -2; //Lab doesn't exist
	}
	public LinkedList<Lab> getLabs(){
		return labs;
	}
	public LinkedList<Instructor> getInstructors() {
		return instructors;
	}

	public LinkedList<Lecture> getLectures(){
		return lectures;
	}
	public int getType(){
		return type;
	}
	public int addLecture(String lec){
		if(e_lecture(lec)){
			return -1;
		}
		Lecture newLec = new Lecture(lec);
		lectures.add(newLec);
		return 0;
	}
	
	public boolean e_lecture(String lec){
		Lecture tempLec = new Lecture(lec);
		for(Lecture checkLec: lectures){
			if(checkLec.compareTo(tempLec)==0)
				return true;
		}
		return false;
	}
	
	public boolean e_lab(String la){
		Lab tempLab = new Lab(la);
		for(Lab checkLab: labs){
			if(checkLab.compareTo(tempLab)==0)
				return true;
		}
		return false;
	}
	
	public int addInstructor(Instructor i){
		if(instructors.contains(i)){
			return -1;
		}
		instructors.add(i);
		return 0;
	}
	public void setLevel(int n){
		type = n;
	}
	
}
