package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken = new ArrayList<Course>(); // 학생이 들은 수업 목록
	private HashMap<String,Integer> semestersByYearAndSemester = new HashMap<String,Integer>(); 
	                                                         // key: Year-Semester
	                                                         // e.g., 2003-1, 
	public Student(String studentId) { // constructor
		this.studentId = studentId;
	}
	
	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
	}
	
	public HashMap<String,Integer> getSemestersByYearAndSemester(){
		int count = 1;
		String key = "";
		String pkey = coursesTaken.get(0).getYearTaken() + "-" + coursesTaken.get(0).getSemesterCourseTaken();
		//System.out.println(coursesTaken.size());
		for(int i = 0;i < coursesTaken.size();i ++) {
			key = coursesTaken.get(i).getYearTaken() + "-" + coursesTaken.get(i).getSemesterCourseTaken();
			if(!pkey.equals(key)) {
				semestersByYearAndSemester.put(pkey, count);
				count ++;
				pkey = key;
			}
//				else {
//				semestersByYearAndSemester.put(key,count);
//				if(semestersByYearAndSemester.containsKey(key)) System.out.println("1");
//				else System.out.println("2");
//				System.out.println(coursesTaken.get(0).getYearTaken() + "-" + coursesTaken.get(0).getSemesterCourseTaken());
//				count = 1;
//				pkey = key;
//			}
		}
		semestersByYearAndSemester.put(pkey, count);
		//System.out.println(pkey);
		//System.out.println(semestersByYearAndSemester.get(key));
		return semestersByYearAndSemester;
	}
	
	public int getNumCourseInNthSementer(int semester) {
		int count = 0;
		HashMap<String,Integer> map = getSemestersByYearAndSemester();
		for(Course course:coursesTaken) {
			String key = course.getYearTaken() + "-" + course.getSemesterCourseTaken();
			//System.out.println(key);
			//if(map.containsKey(key)) 
			//System.out.println(map);
			if(semester == map.get(key)) {
				count ++;
			}
		}
		
		return count;
	}
	/* field에 대한 getter setter 필요에 따라 추가 */

	public ArrayList<Course> getCoursesTaken() {
		return coursesTaken;
	}

	public void setCoursesTaken(ArrayList<Course> coursesTaken) {
		this.coursesTaken = coursesTaken;
	}

}