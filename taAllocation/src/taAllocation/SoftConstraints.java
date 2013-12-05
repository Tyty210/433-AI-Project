package taAllocation;


import java.util.LinkedList;

public class SoftConstraints {
		//HI I NEED TO BE CODED - I loled
	public int IncremSoft(TA ta, Course course){
		int tmpscore = 40;
		LinkedList<Pair<Course, Lab>> instruct = ta.getInstructing();
		LinkedList<Course> knows = ta.getKnows();
		String firstcourse = course.getName();
		String secondcourse = null;
		int numsenior = 0;
		String seniorcourse= null;
		boolean coursenum = true;
		for (int i = 0; i < instruct.size(); i++){
			if (instruct.get(i).getKey().getType() == 1) {
				if (seniorcourse == null){
					seniorcourse = instruct.get(i).getKey().getName();
				}
				else if (instruct.get(i).getKey().getName().equals(seniorcourse) == false){
					numsenior++;
				}
			}
			if (instruct.get(i).getKey().getName().equals(firstcourse) == false){
				if (secondcourse == null) {
					secondcourse = instruct.get(i).getKey().getName();
				}
				else if (instruct.get(i).getKey().getName().equals(secondcourse) == false) {
					coursenum = false;
				}
			} 
		}
		System.out.println(tmpscore);
		if (course.getType() == 1){
			if (seniorcourse != null){
				if (course.getName().equals(seniorcourse) == false && numsenior == 1){
					tmpscore += 10;
				}
			}
		}
		System.out.println(tmpscore);
		for (Course k:ta.getKnows()) {
			if (course.equals(k)) {tmpscore -= 30;}
		}
		System.out.println(tmpscore);
		if (course.getName().equals(firstcourse) == false){
			if (secondcourse == null) {
				tmpscore += 20;
				secondcourse = course.getName();
			}
			else if (course.equals(secondcourse) == false) {
				if (coursenum == true) 
					tmpscore += 35;
			}
		} 
		System.out.println(tmpscore);
		for (int m = 0; m < course.getLectures().size(); m++) {
			for (int p = 0; p < course.getLectures().get(m).getPreference().size(); p++) {
				if (course.getLectures().get(m).getPreference().get(p).getName().equals(ta.getName())) {
					tmpscore -= 10;
				}				
			}
		}
		System.out.println(tmpscore);
		return tmpscore;
	}
	
	public int ClosingSoft(LinkedList<TA> TAs, Stack<Node> tree){
		int closscore = 0;
		int tmpscore = 0;
		int unfunded = 0;
		int min = TAs.get(0).getNumLabs();
		int max=0;
		int tmp;
		
		for(TA ta: TAs){
			if(ta.getNumLabs()==0){
				unfunded++;
			}
		}
		for(TA ta: TAs){
			tmp = ta.getNumLabs();
			if (tmp < min) {min = tmp;}
			if (tmp > max) {max = tmp;}
			
			closscore += tmpscore;
			tmpscore = 0;
		}
		for(TA ta: TAs){
			
		}
		
		if (Math.abs(min-max) > 1) {closscore += 25;}
		if (Math.abs(min-max) > 0) {closscore += 5;}
		if (unfunded > 0) {closscore += 50*unfunded;}
		System.out.println(closscore);
		return closscore;
	}
	
}
