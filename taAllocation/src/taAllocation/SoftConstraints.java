package taAllocation;


import java.util.LinkedList;
import java.util.Stack;

public class SoftConstraints {
	public int IncremSoft(TA ta, Course course, Stack<Node> tree){
		int tmpscore = 0;
		int distinctCourse = 0;
		int distinctSnr = 0;
		boolean knows = false;
		for(Node n: tree){
			if(n.getTa()!=null){
				if(n.getTa().equals(ta)){
					if(!n.getCourse().equals(course)){
						distinctCourse++;
						if(n.getCourse().getType()==1)
							distinctSnr++;
					}
				}
			}
		}
		if(distinctCourse>2){
			tmpscore=+35;
		}
		else if(distinctCourse==1){
			tmpscore=+20;
		}
		if(distinctSnr>0){
			tmpscore=+10;
		}
		for(Course k:ta.getKnows()){
			if(k.equals(course))
				knows = true;
		}
		if(!knows)
			tmpscore=+30;
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
			boolean first = false;
			boolean second = false;
			boolean third = false;
			tmpscore = 0;
			for(Node n:tree){
				if(n.getTa()!=null && n.getTa().equals(ta)){
					if(ta.getPreference(0) == null || n.getCourse().equals(ta.getPreference(0))){
						first = true;
						break;
					}
					else if(ta.getPreference(1) == null || n.getCourse().equals(ta.getPreference(1))){
						second = true;
					}
					else if(ta.getPreference(2) == null || n.getCourse().equals(ta.getPreference(2))){
						third = true;
					}
				}
			}
			if(!first){
				tmpscore += 5;
			}
			if(!first && !second){
				tmpscore += 10;
			}
			else if(!first && !second && !third){
				tmpscore += 10;
			}
			closscore+=tmpscore;
		}
		
		if (Math.abs(min-max) > 1) {closscore += 25;}
		if (Math.abs(min-max) > 0) {closscore += 5;}
		if (unfunded > 0) {closscore += unfunded*50;}
		return closscore;
	}
	
}
