package taAllocation;

public class Lab extends Entity {
	private Timeslot time;
	
	public Lab(String name){
		super(name);
	}
	
	public int setTime(Timeslot x){
		if(time!=null){
			return -1;
		}
		time = x;
		return 0;
	}
	
	public Timeslot getTime(){
		return time;
	}
}
