package edu.handong.analysis.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {
	public static ArrayList<String> getLines(String file,boolean removeHeader){
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
				list.add(data);
			}
			br.close();
		}catch(IOException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
		}
		return list;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		try {
			File file = new File(targetFileName);
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
			bf.write("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
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
