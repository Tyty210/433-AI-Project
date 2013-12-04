package taAllocation;

import java.io.*;
import java.util.LinkedList;

public class TAallocation extends PredicateReader implements TAallocationPredicates {

	private long maxLabs;
	private long minLabs;
	public LinkedList<TA> TAs;
	public LinkedList<Instructor> instructors;
	public LinkedList<Course> courses;
	public long getMaxLabs() {
		return maxLabs;
	}

	public long getMinLabs() {
		return minLabs;
	}

	public LinkedList<Timeslot> timeslots;
	
	public TAallocation(PredicateReader p) {
		super(p);
		TAs = new LinkedList<TA>();
		courses = new LinkedList<Course>();
		instructors = new LinkedList<Instructor>();
		timeslots = new LinkedList<Timeslot>();
	}

	public static void main(String[] args) {
		PredicateReader workDammit = new PredicateReader("asdf");
		TAallocation myAllocation = new TAallocation(workDammit);
		int okay = myAllocation.fromFile(args[0]);
		String outName = args[0]+ ".out" ;
		File output = new File(outName);
		try{
			if(output.exists())
				output.delete();
			output.createNewFile();
			FileWriter fw = new FileWriter(output);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("minlabs("+myAllocation.getMinLabs()+")");
			bw.newLine();
			bw.write("maxlabs("+myAllocation.getMaxLabs()+")");
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.write("//Timeslots");
			bw.newLine();
			for(Timeslot t:myAllocation.getTimeslots()){
				bw.write("timeslot("+t.getName()+")");
				bw.newLine();
			}
			LinkedList<Pair<Timeslot,Timeslot>> conflictList = new LinkedList<Pair<Timeslot,Timeslot>>();
			for(Timeslot t:myAllocation.getTimeslots()){
				for(Timeslot t2:myAllocation.getTimeslots()){
					if(t.checkConflict(t2)){
						boolean exists = false;
						Pair<Timeslot,Timeslot> newCon = new Pair<Timeslot,Timeslot>(t,t2);
						for(Pair<Timeslot,Timeslot> p:conflictList){
							if(p.getKey().equals(t2) & p.getValue().equals(t)){ //Conflicts are symmetrical
								exists = true;
							}
						}
						if(!exists){
							conflictList.add(newCon);
						}
					}
				}
			}
			for(Pair<Timeslot,Timeslot> con:conflictList){
				bw.write("conflicts("+con.getKey().getName()+", "+con.getValue().getName()+")");
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.write("//Courses");
			bw.newLine();
			for(Course c: myAllocation.getCourses()){
				if(c.getType()==0)
					bw.write("course("+c.getName()+")");
				if(c.getType()==1)
					bw.write("senior-course("+c.getName()+")");
				if(c.getType()==2)
					bw.write("grad-course("+c.getName()+")");
				bw.newLine();
				for(Lecture l: c.getLectures()){
					bw.write("lecture("+c.getName()+", "+l.getName()+")");
					bw.newLine();
					bw.write("at("+c.getName()+", "+l.getName()+", "+l.getTime().getName()+")");
					bw.newLine();
					for(Lab la: l.getLabs()){
						bw.write("lab("+c.getName()+", "+l.getName()+", "+la.getName()+")");
						bw.newLine();
						bw.write("at("+c.getName()+", "+la.getName()+", "+la.getTime().getName()+")");
						bw.newLine();
					}
				}
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			bw.write("//Instructors");
			bw.newLine();
			for(Instructor in:myAllocation.getInstructors()){
				bw.write("instructor("+in.getName()+")");
				bw.newLine();
				for(Pair<Course,Lecture> p:in.getLectures()){
					bw.write("instructs("+in.getName()+", "+p.getKey().getName()+", "+p.getValue().getName()+")");
					bw.newLine();
				}
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			bw.write("//TAs");
			bw.newLine();
			for(TA t: myAllocation.getTAs()){
				bw.write("TA("+t.getName()+")");
				bw.newLine();
				if(!t.getPreference(0).equals(new Course("null", 0))){
					bw.write("prefers1("+t.getName()+", "+t.getPreference(0).getName()+")");
					bw.newLine();
				}
				if(!t.getPreference(1).equals(new Course("null", 0))){
					bw.write("prefers2("+t.getName()+", "+t.getPreference(1).getName()+")");
					bw.newLine();
				}
				if(!t.getPreference(2).equals(new Course("null", 0))){
					bw.write("prefers3("+t.getName()+", "+t.getPreference(2).getName()+")");
					bw.newLine();
				}
				for(Course c: t.getKnows()){
					bw.write("knows("+t.getName()+", "+c.getName()+")");
					bw.newLine();
				}
				bw.newLine();
				for(Pair<Course, Lecture> pe:t.getClasses()){
					bw.write("taking("+t.getName()+", "+pe.getKey().getName()+", "+pe.getValue().getName()+")");
					bw.newLine();
				}
				bw.newLine();
				for(Pair<Course,Lab> p:t.getInstructing()){
					bw.write("instructing("+t.getName()+", "+p.getKey().getName()+", "+p.getValue().getName()+")");
					bw.newLine();
				}
			}
			bw.newLine();
			bw.newLine();
			bw.write("//Instructor Preferences");
			bw.newLine();
			for(Instructor in: myAllocation.getInstructors()){
				for(Pair<Course, Lecture> pe:in.getLectures()){
					for(TA ta:pe.getValue().getPreference()){
						bw.write("prefers("+in.getName()+", "+ta.getName()+", "+pe.getKey().getName()+")");
						bw.newLine();
					}
				}
			}
			bw.close();
			fw.close();
		}
		catch(IOException e){
			System.out.println("File writing failed." + e);
		}
	}

	public LinkedList<TA> getTAs() {
		return TAs;
	}

	public LinkedList<Instructor> getInstructors() {
		return instructors;
	}

	public LinkedList<Course> getCourses() {
		return courses;
	}

	public LinkedList<Timeslot> getTimeslots() {
		return timeslots;
	}

	@Override
	public void a_maxlabs(Long p) {
		maxLabs=p;
	}

	@Override
	public void a_minlabs(Long p) {
		minLabs=p;
	}

	@Override
	public void a_TA(String p) {
		if(e_TA(p)){
			System.out.println("Warning: TA already exists @"+getLineNumber());
		}
		else{
		TA newTA = new TA(p);
			TAs.add(newTA);
		}
	}

	@Override
	public boolean e_TA(String p) {
		TA tempTA = new TA(p);
		for(TA checkTA: TAs){
			if(checkTA.compareTo(tempTA)==0)
				return true;
		}
		return false;
	}

	@Override
	public void a_instructor(String p) {
		if(e_instructor(p)){
			System.out.println("Instructor already exists");
		}
		else{
		Instructor newInstructor = new Instructor(p);
			instructors.add(newInstructor);
		}
	}

	@Override
	public boolean e_instructor(String p) {
		Instructor tempInstructor = new Instructor(p);
		for(Instructor checkInstructor: instructors){
			if(checkInstructor.compareTo(tempInstructor)==0)
				return true;
		}
		return false;
	}

	@Override
	public void a_course(String p) {
		if(e_course(p)){
			System.out.println("Warning: Course already exists @"+getLineNumber());
		}
		else{
		Course newCourse = new Course(p,0);
			courses.add(newCourse);
		}
	}

	@Override
	public boolean e_course(String p) {
		Course tempCourse = new Course(p, 0);
		for(Course checkCourse: courses){
			if(checkCourse.compareTo(tempCourse)==0)
				return true;
		}
		return false;
	}

	@Override
	public void a_senior_course(String p) {
		for(Course co: courses){
			if(co.getName().equals(p)){
				co.setLevel(1);
				return;
			}
		}
		Course newCourse = new Course(p,1);
		courses.add(newCourse);
	}

	@Override
	public boolean e_senior_course(String p) {
		Course tempCourse = new Course(p, 1);
		for(Course checkCourse: courses){
			if(checkCourse.compareTo(tempCourse)==0)
				if(checkCourse.getType()==1)
					return true;
		}
		return false;
	}

	@Override
	public void a_grad_course(String p) {
		for(Course co: courses){
			if(co.getName().equals(p)){
				co.setLevel(2);
				return;
			}
		}
		Course newCourse = new Course(p,2);
		courses.add(newCourse);
	}

	@Override
	public boolean e_grad_course(String p) {
		Course tempCourse = new Course(p, 2);
		for(Course checkCourse: courses){
			if(checkCourse.compareTo(tempCourse)==0)
				if(checkCourse.getType()==2)
					return true;
		}
		return false;
	}

	@Override
	public void a_timeslot(String p) {
		if(e_timeslot(p)){
			System.out.println("Warning: Timeslot already exists @"+getLineNumber());
		}
		else{
		Timeslot newTimeslot = new Timeslot(p);
			timeslots.add(newTimeslot);
		}
	}

	@Override
	public boolean e_timeslot(String p) {
		if(timeslots.size() == 0)
			return false;
		Timeslot tempTimeslot = new Timeslot(p);
		for(Timeslot checkTimeslot: timeslots){
			if(checkTimeslot.compareTo(tempTimeslot)==0)
				return true;
		}
		return false;
	}

	@Override
	public void a_lecture(String c, String lec) {
		if(e_course(c)){
			Course tempCourse = new Course(c,0);
			for(Course checkCourse: courses){
				if(checkCourse.compareTo(tempCourse)==0){
					int check = checkCourse.addLecture(lec);
					if(check == -1)
						System.out.println("Warning: Lecture already exists @"+getLineNumber());
					break;
				}
			}
		}
		else{
			errorWithPredicate("Course doesn't exist");
		}
	}

	@Override
	public boolean e_lecture(String c, String lec) {
		if(e_course(c)){
			Course tempCourse = new Course(c,0);
			for(Course checkCourse: courses){
				if(checkCourse.compareTo(tempCourse)==0){
					if(checkCourse.e_lecture(lec))
						return true;
				}
			}
		}
		else{
			errorWithPredicate("Course doesn't exist");
			return false;
		}
		return false;
	}

	@Override
	public void a_lab(String c, String lec, String lab) {
		if(e_course(c)){
			Course tempCourse = new Course(c,0);
			for(Course checkCourse: courses){
				if(checkCourse.compareTo(tempCourse)==0){
					if(checkCourse.e_lab(lab)){
						System.out.println("Warning: Lab already exists @"+getLineNumber());
					}
					else if(checkCourse.e_lecture(lec)){
						LinkedList<Lecture> lectures = checkCourse.getLectures();
						for(Lecture checkLec: lectures){
							if(checkLec.getName().equals(lec)){
								checkCourse.addLab(checkLec, lab);
							}
						}
					}
					else{
						errorWithPredicate("Lecture doesn't exist");
						return;
					}
				}
			}
		}
		else{
			errorWithPredicate("Course doesn't exist");
			return;
		}
	}

	@Override
	public boolean e_lab(String c, String lec, String lab) {
		for(Course co:courses){
			if(co.getName().equals(c)){
				LinkedList<Lecture> lectures = co.getLectures();
				for(Lecture le:lectures){
					if(le.getName().equals(lec)){
						LinkedList<Lab> labs = le.getLabs();
						for(Lab la:labs){
							if(la.getName().equals(lab)){
								return true;
							}
						}
						return false;
					}
				}
				errorWithPredicate("Lecture Doesn't Exist");
				return false;
			}
		}
		errorWithPredicate("Course doesn't exist");
		return false;
	}

	@Override
	public void a_instructs(String p, String c, String l) {
		for(Instructor in:instructors){
			if(in.getName().equals(p)){
				for(Course co:courses){
					if(co.getName().equals(c)){
						LinkedList<Lecture> lectures = co.getLectures();
						for(Lecture le: lectures){
							if(le.getName().equals(l)){
								for(Pair<Course,Lecture> pair: in.getLectures()){
									Lecture le2 = pair.getValue();
									if(co.equals(pair.getKey())&&le2.equals(le)){
										System.out.println("Warning: Instructor already teaching that lecture @"+getLineNumber());
										return;
									}
								}
								in.addLecture(new Pair<Course,Lecture>(co,le));
								return;
							}
						}
						errorWithPredicate("Lecture doesn't exist");
						return;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		for(TA t: TAs){
			for(Course co:courses){
				if(co.getName().equals(c)){
					LinkedList<Lab> labs = co.getLabs();
					for(Lab la: labs){
						if(la.getName().equals(l)){
							int addlab = t.addLab(new Pair<Course,Lab>(co,la));
							if(addlab==-1)
								System.out.println("Warning: TA already teaching that lab @"+getLineNumber());
							return;
						}
					}
					errorWithPredicate("Lab doesn't exist");
					return;
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		errorWithPredicate("Person doesn't exist");
		return;
	}

	@Override
	public boolean e_instructs(String p, String c, String l) {
		for(Instructor in:instructors){
			if(in.getName().equals(p)){
				for(Course co:courses){
					if(co.getName().equals(c)){
						LinkedList<Lecture> lectures = co.getLectures();
						for(Lecture le: lectures){
							if(le.getName().equals(l)){
								if(in.getLectures().contains(le))
									return true;
							}
						}
						errorWithPredicate("Lecture doesn't exist");
						return false;
					}
					errorWithPredicate("Course doesn't exist");
					return false;
				}
			}
		}
		for(TA t: TAs){
			for(Course co:courses){
				if(co.getName().equals(c)){
					LinkedList<Lab> labs = co.getLabs();
					for(Lab la: labs){
						if(la.getName().equals(l)){
							if(t.getInstructing().contains(la))
								return true;
							return false;
						}
					}
					errorWithPredicate("Lab doesn't exist");
					return false;
				}
				errorWithPredicate("Course doesn't exist");
				return false;
			}
		}
		errorWithPredicate("Person doesn't exist");
		return false;
	}

	@Override
	public void a_at(String c, String l, String t) {
		for(Course co: courses){
			if(co.getName().equals(c)){
				for(Lecture le:co.getLectures()){
					if(le.getName().equals(l)){
						if(le.getTime()==null){
							for(Timeslot ti:timeslots){
								if(ti.getName().equals(t)){
									le.setTime(ti);
									return;
								}
							}
							errorWithPredicate("Timeslot doesn't exist");
							return;
						}
						else{
							errorWithPredicate("Lecture already has a time");
							return;
						}
					}
				}
				for(Lab la:co.getLabs()){
					if(la.getName().equals(l)){
						if(la.getTime()==null){
							for(Timeslot ti:timeslots){
								if(ti.getName().equals(t)){
									la.setTime(ti);
									return;
								}
							}
							errorWithPredicate("Timeslot doesn't exist");
							return;
						}
						else{
							errorWithPredicate("Lab already has a time");
							return;
						}
					}
				}
				errorWithPredicate("Lecture/Lab doesn't exist");
				return;
			}
		}
		errorWithPredicate("Course doesn't exist");
		return;
	}

	@Override
	public boolean e_at(String c, String l, String t) {
		for(Course co:courses){
			if(co.getName().equals(c)){
				for(Lecture le:co.getLectures()){
					if(le.getName().equals(l)){
						for(Timeslot ti:timeslots){
							if(ti.getName().equals(t)){
								if(le.getTime().equals(ti))
									return true;
								return false;
							}
						}
						errorWithPredicate("Timeslot doesn't exist");
						return false;
					}
				}
				for(Lab la:co.getLabs()){
					if(la.getName().equals(l)){
						for(Timeslot ti:timeslots){
							if(ti.getName().equals(t)){
								if(la.getTime().equals(ti))
									return true;
								return false;
							}
						}
						errorWithPredicate("Timeslot doesn't exist");
						return false;
					}
				}
				errorWithPredicate("Lecture/Lab doesn't exist");
				return false;
			}
		}
		errorWithPredicate("Course Doesn't Exist");
		return false;
	}

	@Override
	public void a_knows(String ta, String c) {
		for(TA t: TAs){
			if(t.getName().equals(ta)){
				for(Course co:courses){
					if(co.getName().equals(c)){
						int knowsErr = t.addKnows(co);
						if(knowsErr == -1){
							System.out.println("Warning: TA already knows that course @"+getLineNumber());
						}
						return;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return;
	}

	@Override
	public boolean e_knows(String ta, String c) {
		for(TA t: TAs){
			if(t.getName().equals(ta)){
				for(Course co:courses){
					if(co.getName().equals(c)){
						if(t.getKnows().contains(co)){
							return true;
						}
						return false;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return false;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return false;
	}

	@Override
	public void a_prefers(String instructor, String ta, String c) {
		for(Instructor in:instructors){
			if(in.getName().equals(instructor)){
				for(Course co:courses){
					if(co.getName().equals(c)){
						for(TA t:TAs){
							if(t.getName().equals(ta)){
								for(Pair<Course,Lecture> p:in.getLectures()){
									Lecture le = p.getValue();
									for(Lecture le2:co.getLectures()){
										if(le.equals(le2)){
											int taAdd = le.addPreference(t);
											if(taAdd==-1){
												System.out.println("Warning: TA already in that instructor's preference list @"+getLineNumber());
												return;
											}
											return;
										}
									}
								}
								errorWithPredicate("Instructor does not teach that lecture");
								return;
							}
						}
						errorWithPredicate("TA doesn't exist");
						return;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		errorWithPredicate("Instructor doesn't exist");
		return;
	}

	@Override
	public boolean e_prefers(String instructor, String ta, String c) {
		for(Instructor in:instructors){
			if(in.getName().equals(instructor)){
				for(Course co:courses){
					if(co.getName().equals(c)){
						if(!co.getInstructors().contains(in)){
							errorWithPredicate("Instructor does not teach that course.");
							return false;
						}
						for(TA t:TAs){
							if(t.getName().equals(ta)){
								for(Pair<Course,Lecture> p:in.getLectures()){
									Lecture le = p.getValue();
									if(le.getPreference().contains(t))
											return true;
									
								}
								return false;
							}
						}
						errorWithPredicate("TA doesn't exist");
						return false;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return false;
			}
		}
		errorWithPredicate("Instructor doesn't exist");
		return false;
	}

	@Override
	public void a_prefers1(String ta, String c) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						if(!t.getPreference(0).equals(new Course("null", 0))){
							System.out.println("Warning: Preference 1 already set @"+getLineNumber());
							return;
						}
						t.setPreference(co, 0);
						return;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return;
	}

	@Override
	public boolean e_prefers1(String ta, String c) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						if(t.getPreference(0)!=co){
							return false;
						}
						return true;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return false;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return false;
	}

	@Override
	public void a_prefers2(String ta, String c) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						if(!t.getPreference(1).equals(new Course("null", 0))){
							System.out.println("Warning: Preference 2 already set @"+getLineNumber());
							return;
						}
						t.setPreference(co, 1);
						return;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return;
		
	}

	@Override
	public boolean e_prefers2(String ta, String c) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						if(t.getPreference(1)!=co){
							return false;
						}
						return true;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return false;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return false;
	}

	@Override
	public void a_prefers3(String ta, String c) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						if(!t.getPreference(2).equals(new Course("null", 0))){
							System.out.println("Warning: Preference 3 already set @"+getLineNumber());
							return;
						}
						t.setPreference(co, 2);
						return;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return;
		
	}

	@Override
	public boolean e_prefers3(String ta, String c) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						if(t.getPreference(2)!=co){
							return false;
						}
						return true;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return false;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return false;
	}

	@Override
	public void a_taking(String ta, String c, String l) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						for(Lecture le:co.getLectures()){
							if(le.getName().equals(l)){
								int takeError = t.addClass(co,le);
								if(takeError==-1)
									System.out.println("Warning: TA already taking that course @"+getLineNumber());
								return;
							}
						}
						errorWithPredicate("Lecture doesn't exist");
						return;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return;
		
	}

	@Override
	public boolean e_taking(String ta, String c, String l) {
		for(TA t:TAs){
			if(t.getName().equals(ta)){
				for(Course co: courses){
					if(co.getName().equals(c)){
						for(Lecture le:co.getLectures()){
							if(le.getName().equals(l)){
								if(t.getClasses().contains(le)){
									return true;
								}
								return false;
							}
						}
						errorWithPredicate("Lecture doesn't exist");
						return false;
					}
				}
				errorWithPredicate("Course doesn't exist");
				return false;
			}
		}
		errorWithPredicate("TA doesn't exist");
		return false;
	}

	@Override
	public void a_conflicts(String t1, String t2) {
		if(!e_timeslot(t1))
			a_timeslot(t1);
		if(!e_timeslot(t2))
			a_timeslot(t2);
		for(Timeslot tone:timeslots){
			if(tone.getName().equals(t1)){
				for(Timeslot ttwo:timeslots){
					if(ttwo.getName().equals(t2)){
						if(tone.checkConflict(ttwo)){
							System.out.println("Warning: Conflict already exists @"+getLineNumber());
							return;
						}
						tone.addConflict(ttwo);
						ttwo.addConflict(tone);
					}
				}

			}
		}
	}

	@Override
	public boolean e_conflicts(String t1, String t2) {
		for(Timeslot tone:timeslots){
			if(tone.getName().equals(t1)){
				for(Timeslot ttwo:timeslots){
					if(ttwo.getName().equals(t2)){
						if(tone.checkConflict(ttwo)){
							return true;
						}
						return false;
					}
				}
				errorWithPredicate("Timeslot 2 doesn't exist");
				return false;
			}
		}
		errorWithPredicate("Timeslot 1 doesn't exist");
		return false;
	}

}
