package taAllocation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Collections;

public class SearchTree {
	SoftConstraints sc = new SoftConstraints();
	int CurrentScore = 0;
	private TAallocation environment;
	private Stack<Node> tree;
	private Node current;
	public SearchTree(TAallocation env){
		environment = env;
	}
	private long max;
	private long min;
	private LinkedList<Pair<Integer, TA>> children;
	
	public Pair<Integer, Stack<Node>> doSearch(){
		max = environment.getMaxLabs();
		min = environment.getMinLabs();
		tree = new Stack<Node>();
		for(TA ta: environment.TAs){ //Fill the "tree" with forced assignments. These should not be gone over.
			for(Pair<Course,Lab> in:ta.getInstructing()){
				current = new Node(ta, in.getKey(), in.getValue(), sc.IncremSoft(ta, in.getKey()) , null);
				CurrentScore += current.getLocalScore();
				tree.push(current);
				//Do something about this node in the tree
			}
		}
		LinkedList<Pair<Integer,Pair<Course,Lab>>> labOrder = new LinkedList<Pair<Integer, Pair<Course,Lab>>>();
		for(Timeslot to:environment.timeslots){
			int curVail = 0;
			boolean isAvail = true;
			Pair<Integer,Pair<Course,Lab>> newLab;
			for(TA ta:environment.TAs){
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
				if(isAvail){curVail++;}
			}
			for(Course co:environment.courses){
				for(Lab la:co.getLabs()){
					if(la.getTime().equals(to)){
						newLab = new Pair<Integer,Pair<Course,Lab>>(curVail, new Pair<Course, Lab>(co,la));
						labOrder.add(newLab);
					}
				}
			}
			labOrder.add(new Pair<Integer,Pair<Course,Lab>>(500000, new Pair<Course, Lab>(new Course(" ", 0), new Lab(" "))));
		}
		Collections.sort(labOrder, new MyComparator());
		Pair<Integer, Stack<Node>> bestSet = null;
		int index = 0;
		while(index >= 0){
			
			if (index >= labOrder.size()) {	
				for(TA ta: environment.TAs){
					int locsize = ta.getInstructing().size();
					if (locsize >= min || locsize == 0) {
						int FinalScore = CurrentScore + sc.ClosingSoft(environment.getTAs());
						if (FinalScore < bestSet.getKey()){
							bestSet = new Pair<Integer, Stack<Node>>(FinalScore, tree);
						}
					}
				}
			}
			
			children = new LinkedList<Pair<Integer, TA>>();
			boolean conflict;
			for(TA ta:environment.TAs){
				conflict = false;
				for(Pair<Course,Lecture> co:ta.getClasses()){
					if(co.getValue().getTime().checkConflict(labOrder.get(index).getValue().getValue().getTime())){
						conflict = true;
						break;
					}
				}
				for(Pair<Course,Lab> co:ta.getInstructing()){
					if(co.getValue().getTime().checkConflict(labOrder.get(index).getValue().getValue().getTime())){
						conflict = true;
						break;
					}
				}
				if (ta.getInstructing().size() <= max) {
					children.add(new Pair<Integer,TA>(sc.IncremSoft(ta, labOrder.get(index).getValue().getKey()), ta));
				}
			}
			if (children.size() > 0) {
				Pair<Integer,TA> locmin = children.get(0);
				for(Pair<Integer,TA> p:children){
					if (p.getKey() < locmin.getKey()) {locmin = p;}
				}
				int tempscr = locmin.getKey();
				CurrentScore += tempscr;
				TA tempta = locmin.getValue();
				children.remove(locmin);
				current = new Node(tempta, labOrder.get(index).getValue().getKey(), labOrder.get(index).getValue().getValue(), tempscr , children);
				tree.push(current);
				index++;
			} else {	

				for(TA ta: environment.TAs){
					int locsize = ta.getInstructing().size();
					if (locsize >= min || locsize == 0) {
						int FinalScore = CurrentScore + sc.ClosingSoft(environment.getTAs());
						if (FinalScore < bestSet.getKey()){
							bestSet = new Pair<Integer, Stack<Node>>(FinalScore, tree);
						}
					}
				}
				CurrentScore -= tree.peek().getLocalScore();
				tree.pop();
				current = tree.peek();
				index--;
			}
		}
		return bestSet;
	}
}

class MyComparator implements Comparator<Pair<Integer,Pair<Course,Lab>>>{

	@Override
	public int compare(Pair<Integer, Pair<Course, Lab>> arg0,
			Pair<Integer, Pair<Course, Lab>> arg1) {
			return(arg0.getKey()-arg1.getKey());
	}
}
	

