package edu.handong.analysis.utils;

import java.io.*;
import java.util.ArrayList;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class Utils {
	public static ArrayList<String> getLines(String file,boolean removeHeader,int start,int end){
		ArrayList<String> list = new ArrayList<String>();
		try {
			Reader in = new FileReader("file");
			Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("StudentID","YearMonthGraduated","FistMajor","SecondMajor","CourseCode","CourseName","CourseCredit","YearTaken","SemesterTaken").parse(in);
			for (CSVRecord record : records) {
	
			    String studentId = record.get("StudentID");
			    String yearMonthGraduated = record.get("YearMonthGraduated");
			    String firstMajor = record.get("FistMajor");
			    String secondMajor = record.get("SecondMajor");
			    String courseCode = record.get("CourseCode");
			    String courseName = record.get("CourseName");
			    String courseCredit = record.get("CourseCredit");
			    int yearTaken =Integer.parseInt(record.get("YearTaken"));
			    int semesterCourseTaken = Integer.parseInt(record.get("SemesterTaken"));
			    if(start > yearTaken || end < yearTaken)
					continue;
			    list.add(studentId + "," + yearMonthGraduated + "," + firstMajor + "," + secondMajor + "," + courseCode + "," + courseName + "," + courseCredit + "," + yearTaken + "," + semesterCourseTaken );
			}
			
		}catch(IOException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		return list;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName, int option) {
		try {
			File file = new File(targetFileName);
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
			if(option == 1)
				bf.write("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
			else
				bf.write("Year,Semester,CouseCode, CourseName,TotalStudents,StudentsTaken,Rate");
			bf.newLine();
			for(String line : lines) {
				bf.write(line);
				bf.newLine();
			}
			bf.close();
		}catch(FileNotFoundException e) {
			try {
				File tmp = new File(targetFileName);
				tmp.getParentFile().mkdirs();
				tmp.createNewFile();
			}catch(IOException err) {
				System.out.println(err);
			}
		}catch(IOException e) {
			System.out.println(e);
		}
	}
}
