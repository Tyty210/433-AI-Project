package taAllocation;

import java.util.LinkedList;

public class Lecture extends Entity {
	private Timeslot time;
	private LinkedList<Lab> labs;
	private LinkedList<TA> preference;
	
	public Lecture(String name){
		super(name);
		labs = new LinkedList<Lab>();
		preference = new LinkedList<TA>();
	}
	
	public int setTime(Timeslot x){
		if(time!=null){
			return -1;
		}
		time = x;
		return 0;
	}
	
	public LinkedList<Lab> getLabs() {
		return labs;
	}

	public LinkedList<TA> getPreference() {
		return preference;
	}

	public Timeslot getTime(){
		return time;
	}
	
	public int addPreference(TA t){
		if (preference.contains(t)){
			return -1;
		}
		preference.add(t);
		return 0;
	}
	public int addLab(Lab l){
		if(labs.contains(l)){
			return -1;
		}
		labs.add(l);
		return 0;
	}
}
