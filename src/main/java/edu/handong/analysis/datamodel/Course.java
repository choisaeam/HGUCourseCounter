package edu.handong.analysis.datamodel;

public class Course {
	private String studentId;
	private String yearMonthGraduated;
	private String firstMajor;
	private String secondMajor;
	private String courseCode;
	private String courseName;
	private String courseCredit;
	private int yearTaken;
	private int semesterCourseTaken;
	
	public Course(String line) { // constructor에서 line을 받아 split해서 field초기화
		String[] info = line.split(",");
		studentId = info[0].trim();
		yearMonthGraduated = info[1].trim();
		firstMajor = info[2].trim();
		secondMajor = info[3].trim();
		courseCode = info[4].trim();
		courseName = info[5].trim();
		courseCredit = info[6].trim();
		yearTaken =Integer.parseInt(info[7].trim());
		semesterCourseTaken = Integer.parseInt(info[8].trim());
		
	}
	public String getCourseName() {
		return courseName;
	}
	public int getYearTaken() {
		return yearTaken;
	}
	public void setYearTaken(int yearTaken) {
		this.yearTaken = yearTaken;
	}
	public int getSemesterCourseTaken() {
		return semesterCourseTaken;
	}
	public void setSemesterCourseTaken(int semesterCourseTaken) {
		this.semesterCourseTaken = semesterCourseTaken;
	}
	

}
