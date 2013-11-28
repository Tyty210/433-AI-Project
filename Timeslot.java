package taAllocation;

import java.util.LinkedList;

public class Timeslot extends Entity {
	public Timeslot(String name) {
		super(name);
		conflicts = new LinkedList<Timeslot>();
	}
	private LinkedList<Timeslot> conflicts;
	
	public boolean checkConflict(Timeslot t){
		if (conflicts.contains(t)){
			return true;
		}
		return false;
	}
	public void addConflict(Timeslot t){
		conflicts.add(t);
	}
}
