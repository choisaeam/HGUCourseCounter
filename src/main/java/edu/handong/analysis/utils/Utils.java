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
			File f = new File(file);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String data = "";
			if(removeHeader) {
				data = br.readLine();
			}
			while((data = br.readLine()) != null) {
				if(start > Integer.parseInt(data.split(",")[7].trim()) || end < Integer.parseInt(data.split(",")[7].trim()))
					continue;
				list.add(data);
			}
			br.close();
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
