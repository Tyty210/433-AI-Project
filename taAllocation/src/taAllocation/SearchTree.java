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
	
	public Pair<Pair<Integer,Integer>, Stack<Node>> doSearch(long intime){
		long start = System.currentTimeMillis();
		long limit = start+intime;
		max = environment.getMaxLabs();
		min = environment.getMinLabs();
		tree = new Stack<Node>();
		for(TA ta: environment.TAs){ //Fill the "tree" with forced assignments. These should not be gone over.
			for(Pair<Course,Lab> in:ta.getInstructing()){
				int tempscore = 0;
				if(tree.size()!=0){
					tempscore = tree.peek().getLocalScore();
				}
				current = new Node(ta, in.getKey(), in.getValue(), sc.IncremSoft(ta, in.getKey(), tree)+tempscore,null);
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
					boolean alExists = false;
					for(Node n:tree){ //Make sure we don't put pre-assigned labs in our labOrder
						if(n.getCourse().equals(co) && n.getLab().equals(la)){
							alExists = true;
						}
					}
					if(la.getTime().equals(to) && !alExists){
						newLab = new Pair<Integer,Pair<Course,Lab>>(curVail, new Pair<Course, Lab>(co,la));
						labOrder.add(newLab);
					}
				}
			}
		}
		Collections.sort(labOrder, new MyComparator());
		Pair<Pair<Integer,Integer>, Stack<Node>> bestSet = null;
		int index = 0;
		Node root = new Node(null,null,null,CurrentScore,null);
		tree.push(root);
		while(index >= 0){
			long curtime = System.currentTimeMillis();
			if((tree.peek().children==null) && index < labOrder.size()){ //If we're not at the bottom, and have no children, expand
				tree.peek().children = new LinkedList<Pair<Integer, TA>>(); //Create new list
				if(bestSet!=null){ //If we have a best set, make sure children are at least better than it
					for(TA t:environment.TAs){
						int localScore =tree.peek().getLocalScore() + sc.IncremSoft(t, labOrder.get(index).getValue().getKey(),tree);
						if(localScore <= bestSet.getKey().getKey()/((labOrder.size()-index))/2){
							int instructing = t.getNumLabs();
							if(instructing < max || max == 0){
								boolean conflicts = false;
								for(Pair<Course,Lecture> p: t.getClasses()){
									if(p.getValue().getTime().checkConflict(labOrder.get(index).getValue().getValue().getTime())){
										conflicts = true;
										break;
									}
								}
								for(Node n: tree){
									if(n.getTa()!=null && n.getTa().equals(t)){
										if(n.getLab().getTime().checkConflict(labOrder.get(index).getValue().getValue().getTime())){
											conflicts = true;
											break;
										}
									}
								}
								if(!conflicts){
									tree.peek().children.add(new Pair<Integer,TA>(localScore,t));
								}
								
							}
						}
					}
				}
				else{ //Otherwise just generate children
					for(TA t:environment.TAs){
						int localScore = sc.IncremSoft(t, labOrder.get(index).getValue().getKey(),tree);
						localScore = localScore + tree.peek().getLocalScore();
						int instructing = t.getNumLabs();
						if(instructing < max || max==0){
							boolean conflicts = false;
							for(Pair<Course,Lecture> p: t.getClasses()){
								if(p.getValue().getTime().checkConflict(labOrder.get(index).getValue().getValue().getTime())){
									conflicts = true;
									break;
								}
							}
							for(Node n: tree){
								if(n.getTa()!=null&&n.getTa().equals(t)){
									if(n.getLab().getTime().checkConflict(labOrder.get(index).getValue().getValue().getTime())){
										conflicts = true;
										break;
									}
								}
							}
							if(!conflicts){
								tree.peek().children.add(new Pair<Integer,TA>(localScore,t));
							}
							
						}
					}
				}
			}
			else if(tree.peek().children==null && index == labOrder.size()){ //If we're at the bottom of the tree
				boolean isOverMin = true;
				for(TA t: environment.TAs){
					int numLabs = t.getNumLabs();
					if(numLabs < min && numLabs != 0){
						isOverMin = false;
						break;
					}
				}
					if(isOverMin){
						int finalScore = tree.peek().getLocalScore()+sc.ClosingSoft(environment.TAs,tree);
						if(bestSet!=null){
							if(finalScore<bestSet.getKey().getValue()){
								Stack<Node> bestStack = new Stack<Node>();
								for(Node n:tree){
									if(n.getTa()!=null){
										bestStack.push(n);
									}
								}
								bestSet = new Pair<Pair<Integer,Integer>,Stack<Node>>(new Pair<Integer,Integer>(tree.peek().getLocalScore(),finalScore),bestStack);
							}
						}
						else{
							Stack<Node> bestStack = new Stack<Node>();
							for(Node n:tree){
								if(n.getTa()!=null){
									bestStack.push(n);
								}
							}
							bestSet = new Pair<Pair<Integer,Integer>,Stack<Node>>(new Pair<Integer,Integer>(tree.peek().getLocalScore(),finalScore),bestStack);
						}
				}
				tree.peek().getTa().decLabs();
				Node temp = tree.pop();
				int localPos = 0;
				for(Pair<Integer,TA> p: tree.peek().children){
					if(p.getValue().equals(temp.getTa()))
						break;
					localPos++;
				}
				tree.peek().children.remove(localPos);
				if(bestSet!=null && bestSet.getKey().getValue() == 0)
					index = -1;
				index--;
			}
			else if(tree.peek().children.size()==0){ //If all children have been expanded, step back
				if(index != 0){
					tree.peek().getTa().decLabs();
					Node temp = tree.pop();
					int localPos = 0;
					for(Pair<Integer,TA> p: tree.peek().children){
						if(p.getValue().equals(temp.getTa()))
							break;
						localPos++;
					}
					tree.peek().children.remove(localPos);
				}
				index--;
			}
			else{
				if(bestSet!=null){
					int trimmer = ((labOrder.size()-index-1)/2);
					if(trimmer == 0)
						trimmer++;
					for(int i = 0; i<tree.peek().children.size();){
						if(tree.peek().children.get(i).getKey()>bestSet.getKey().getKey()/(trimmer)){
							tree.peek().children.remove(i);
						}
						else{
							i++;
						}
					}
				}
				if(tree.peek().children.size()!=0){
				Pair<Integer,TA> toExpand = tree.peek().children.get(0);
				for(Pair<Integer,TA> p:tree.peek().children){
					if(toExpand.getKey()<p.getKey()){
						toExpand =p;
					}
				}
				toExpand.getValue().incLabs();
				tree.push(new Node(toExpand.getValue(), labOrder.get(index).getValue().getKey(),labOrder.get(index).getValue().getValue(), toExpand.getKey(),null));
				index++;
				}
			}
			if(curtime>limit && intime!=0){
				return bestSet;
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
	

