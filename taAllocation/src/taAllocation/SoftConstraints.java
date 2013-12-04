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
		if (course.getType() == 1){
			if (seniorcourse != null){
				if (course.getName().equals(seniorcourse) == false && numsenior == 1){
					tmpscore += 10;
				}
			}
		}
		for (int j = 0; j < knows.size(); j++) {
			if (course.getName().equals(knows.get(j))) {tmpscore -= 30;}
		}
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

		for (int m = 0; m < course.getLectures().size(); m++) {
			for (int p = 0; p < course.getLectures().get(m).getPreference().size(); p++) {
				if (course.getLectures().get(m).getPreference().get(p).getName().equals(ta.getName())) {
					tmpscore -= 10;
				}				
			}
		}
		
		return tmpscore;
	}
	
	public int ClosingSoft(LinkedList<TA> TAs){
		int closscore = 0;
		int tmpscore = 0;
		int unfunded = 0;
		int min;
		int max;
		int tmp;
		
		min = max = TAs.get(0).getInstructing().size();
		if (min == 0) {unfunded += 1;}
		
		for(int i = 1; i < TAs.size(); i++){
			tmp = TAs.get(i).getInstructing().size();
			if (tmp < min) {min = tmp;}
			if (tmp > max) {max = tmp;}
			
			for (int j = 0; j < TAs.get(i).getInstructing().size(); j++){
				if (TAs.get(i).getInstructing().get(j).getKey().getName().equals(TAs.get(i).getPreference(0).getName())){
					tmpscore = 25;
				}
				if (tmpscore < 20){
					if (TAs.get(i).getInstructing().get(j).getKey().getName().equals(TAs.get(i).getPreference(1).getName())) tmpscore = 20;
				}
				if (tmpscore < 10){
					if (TAs.get(i).getInstructing().get(j).getKey().getName().equals(TAs.get(i).getPreference(2).getName())) tmpscore = 10;
				}
			}
			closscore += tmpscore;
			tmpscore = 0;
		}
		
		if (Math.abs(min-max) > 1) {closscore += 25;}
		if (Math.abs(min-max) > 0) {closscore += 5;}
		if (unfunded > 0) {closscore += 50*unfunded;}
		return closscore;
	}
	
}
