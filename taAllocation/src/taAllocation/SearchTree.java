package taAllocation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Collections;

public class SearchTree {
	SoftConstraints sc = new SoftConstraints();
	private TAallocation environment;
	private Stack<Node> tree;
	public SearchTree(TAallocation env){
		environment = env;
	}
	public Pair<Integer,LinkedList<Node>> doSearch(){
		tree = new Stack<Node>();
		for(TA ta: environment.TAs){ //Fill the "tree" with forced assignments. These should not be gone over.
			for(Pair<Course,Lab> in:ta.getInstructing()){
				int checkAssin;
				if(tree.peek().equals(null)){
					checkAssin = 0;
				}
				else{
					checkAssin = tree.peek().curScore;
				}
				Node setInstruct = new Node(new Pair<TA,Pair<Course,Lab>>(ta,in),checkAssin,sc.IncremSoft(ta, in.getKey()));
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
						labOrder.add(newLab);
					}
				}
			}
		}
		Collections.sort(labOrder, new MyComparator());
		int done = tree.size();
		Node root = new Node(null,0,0);
		Node current = root;
		tree.push(root);
		Pair<Integer, LinkedList<Node>> bestSet = null;
		while(tree.size()!=done){
			if(labOrder.get(tree.size()).equals(null)){ //If we hit the bottom of the tree
				boolean noHCVio = true;
				for(TA ta: environment.TAs){
					if(ta.getInstructing().size()!=0 && ta.getInstructing().size()<environment.getMinLabs()){ //HC violation. Do not consider.
						noHCVio = false;
					}
				}
				if(noHCVio){
					LinkedList<Node> curBest = new LinkedList<Node>();
						for(Node n: tree){
							if(!n.getAssignment().equals(null))
								curBest.add(n);
						}
					int highScore = current.curScore+sc.ClosingSoft(environment.TAs);
					bestSet = new Pair<Integer,LinkedList<Node>>(highScore,curBest);
				}
			}
			else{ //Otherwise expand as normal
				for(TA ta: environment.TAs){
					if(current.children.size()==0){
						if(bestSet!=null){
							if(current.curScore+sc.IncremSoft(ta,labOrder.get(tree.size()).getValue().getKey())<bestSet.getKey()){
								if(ta.getInstructing().size()<environment.getMaxLabs()){
									boolean isFree = true;
									for(Pair<Course,Lecture> pt: ta.getClasses()){
										if(labOrder.get(tree.size()).getValue().getValue().getTime().checkConflict(pt.getValue().getTime()))
											isFree = false;
									}
									if(isFree = true){
										current.children.add(new Node(new Pair<TA, Pair<Course, Lab>>(ta,labOrder.get(tree.size() - 1 - done).getValue()),current.curScore,sc.IncremSoft(ta,labOrder.get(tree.size()).getValue().getKey())));
									}
								}
							}
						}
						else{
							if(ta.getInstructing().size()<environment.getMaxLabs()){
								boolean isFree = true;
								for(Pair<Course,Lecture> pt: ta.getClasses()){
									if(labOrder.get(tree.size()).getValue().getValue().getTime().checkConflict(pt.getValue().getTime()))
										isFree = false;
								}
								if(isFree = true){
									current.children.add(new Node(new Pair<TA, Pair<Course, Lab>>(ta,labOrder.get(tree.size() - 1 - done).getValue()),current.curScore,sc.IncremSoft(ta,labOrder.get(tree.size()).getValue().getKey())));
									System.out.println(current.children.get(0).getAssignment().getValue().getKey().getName());
								}
							}
						}
					}
				}
			}
			boolean isFullyExpanded = true;
			for(Node n:current.children){
				if(!current.Expanded.contains(n)){
					isFullyExpanded = false;
				}
			}
			if(current.children==null || isFullyExpanded){
				current.getAssignment().getKey().remLab(current.getAssignment().getValue());
				tree.pop();
			}  
			else{
				Node toExpand = null;
				for(Node n:current.children){
					if(toExpand==null)
						toExpand = n;
					if(n.curScore<=toExpand.curScore && !current.Expanded.contains(n)){
						toExpand = n;
					}
				}
				current.Expanded.add(toExpand);
				current = toExpand;
				current.getAssignment().getKey().addLab(current.getAssignment().getValue());
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
