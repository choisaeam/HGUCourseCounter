package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken; // 학생이 들은 수업 목록
	private HashMap<String,Integer> semestersByYearAndSemester; 
	                                                         // key: Year-Semester
	                                                         // e.g., 2003-1, 
	public Student(String studentId) { // constructor
		this.studentId = studentId;
	}
	public void addCourse(Course newRecord) {
		System.out.println(newRecord.getSemesterCourseTaken());
		coursesTaken.add(newRecord);
	}
	public HashMap<String,Integer> getSemestersByYearAndSemester(){
		int count = 0;
		String pkey = "";
		for(int i = 0;i < coursesTaken.size();i ++) {
			String key = coursesTaken.get(i).getYearTaken() + "-" + coursesTaken.get(i).getSemesterCourseTaken();
			if(!pkey.equals(key)) {
				count ++;
				semestersByYearAndSemester.put(key, count);
				pkey = key;
			}
		}
		return semestersByYearAndSemester;
		
	}
	public int getNumCourseInNthSementer(int semester) {
		int count = 0;
		
		Set<Entry<String, Integer>> set =semestersByYearAndSemester.entrySet();
		Iterator<Entry<String, Integer>> iterator = set.iterator();
		String key = "";
		while(iterator.hasNext()){

		  Map.Entry entry = (Map.Entry)iterator.next();
		  key = (String)entry.getKey();
		  if(semestersByYearAndSemester.get(key) == semester)
			  break;
		}
		
		for(int i = 0;i < coursesTaken.size();i ++) {
			String skey = coursesTaken.get(i).getYearTaken() + "-" + coursesTaken.get(i).getSemesterCourseTaken();
			if(key.equals(skey)) {
				count ++;
			}
		}
		
		return count;
	}
	/* field에 대한 getter setter 필요에 따라 추가 */

}