package taAllocation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Collections;

public class SearchTree {
	private TAallocation environment;
	private Stack<Node> tree;
	public SearchTree(TAallocation env){
		environment = env;
	}
	public void doSearch(){
		for(TA ta: environment.TAs){ //Fill the "tree" with forced assignments. These should not be gone over.
			for(Pair<Course,Lab> in:ta.getInstructing()){
				Node setInstruct = new Node(new Pair<TA,Pair<Course,Lab>>(ta,in));
				if(!tree.peek().equals(null)){
					tree.peek().children.add(setInstruct);
				}
				tree.push(setInstruct);
			}
		}
		LinkedList<Pair<Integer,Pair<Course,Lab>>> labOrder = new LinkedList<Pair<Integer, Pair<Course,Lab>>>();
		for(Timeslot to:environment.timeslots){
			int curVail = 0;
			for(TA ta:environment.TAs){
				boolean isAvail = true;
				for(Pair<Course,Lecture> pt:ta.getClasses()){
					if(to.checkConflict(pt.getValue().getTime())){
						isAvail = false;
						break;
					}
				}
				for(Pair<Course,Lab> pt:ta.getInstructing()){
					if(to.checkConflict(pt.getValue().getTime()) || !isAvail){
						isAvail = false;
						break;
					}
				}
				if(isAvail)
					curVail++;
			}
			for(Course co:environment.courses){
				for(Lab la:co.getLabs()){
					if(la.getTime().equals(to)){
						Pair<Integer,Pair<Course,Lab>> newLab = new Pair<Integer,Pair<Course,Lab>>(curVail,new Pair<Course, Lab>(co,la));
					}
				}
			}
		}
		Collections.sort(labOrder, new MyComparator());
		int done = tree.size();
		Node root = new Node(null);
		Node current = root;
		while(tree.size()!=done){
			for(TA ta: environment.TAs){ //Expand node. Remember to add check against current best if it exists, and check against hard constraints
				current.children.add(new Node(new Pair<TA, Pair<Course, Lab>>(ta,labOrder.get(tree.size() - 1 - done).getValue())));
			}
			//Look for best child, follow it and expand
			
			//Set current to the current expanded node
			
			//If bottom of tree (i.e. labOrder.size()=(tree.size()-1-done)), set a value, pop, mark parent yes. If best set best.
		}
	}
}

class MyComparator implements Comparator<Pair<Integer,Pair<Course,Lab>>>{

	@Override
	public int compare(Pair<Integer, Pair<Course, Lab>> arg0,
			Pair<Integer, Pair<Course, Lab>> arg1) {
			return(arg0.getKey()-arg1.getKey());
	}
	
}
