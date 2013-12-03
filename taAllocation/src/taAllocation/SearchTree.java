package taAllocation;

import java.util.Stack;

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
					tree.peek().children.add(new Pair<Integer, Node>(2,setInstruct));
				}
				tree.push(setInstruct);
			}
		}
		//Find our lab with the lowest amount of free spaces.
		Node firstBranch = new Node(/*Lowest possible assignment here*/);
		if(!tree.peek().equals(null)){
			tree.peek().children.add(new Pair<Integer, Node>(2,firstBranch));
		}
		tree.push(firstBranch);
		
		while(/*There exist children not expanded/marked no*/){
			/*Do search. Watch for hard constraints*/
		}
	}
}
