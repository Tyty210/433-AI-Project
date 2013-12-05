package taAllocation;

import java.util.LinkedList;

public class Timeslot extends Entity {
	public Timeslot(String name) {
		super(name);
		conflicts = new LinkedList<Timeslot>();
	}
	private LinkedList<Timeslot> conflicts;
	
	public boolean checkConflict(Timeslot t){
		if(t.equals(this))
			return true;
		for(Timeslot to:conflicts){
			if(to.getName().equals(t.getName()))
				return true;
		}
		return false;
	}
	public void addConflict(Timeslot t){
		conflicts.add(t);
	}
}
