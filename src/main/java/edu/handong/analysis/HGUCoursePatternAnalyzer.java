package edu.handong.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	private HashMap<String, Integer> totalStu;
	private HashMap<String, Integer> Stu;
	private String dataPath; // csv file to be analyzed
	private String resultPath; // the file path where the results are saved.
	private int analOption;
	private String courseCode;
	private int startYear;
	private int endYear;
	private boolean help;
	private String courseName;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		Options options = createOptions();
		
		parseOptions(options, args);
		if (help){
			printHelp(options);
			return;
		}
		
		ArrayList<String> lines = Utils.getLines(dataPath, true, startYear, endYear);
		ArrayList<String> linesToBeSaved;
		students = loadStudentCourseRecords(lines);
		
		if(analOption == 1) {
			// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
			Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
			// Generate result lines to be saved.
			linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		}else {
			totalStu = loadTotalStu(students);
			Stu = loadStu(lines);
			
			Map<String, Integer> sortedTotalStu = new TreeMap<String,Integer>(totalStu); 
			Map<String, Integer> sortedStu = new TreeMap<String,Integer>(Stu);
			
			linesToBeSaved = countNumberOfStudentTakeCourse(sortedTotalStu,sortedStu,courseCode);
		}
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath, analOption);
	}
	private ArrayList<String> countNumberOfStudentTakeCourse(Map<String, Integer> sortedTotalStu, Map<String, Integer> sortedStu,String code){
		ArrayList<String> list = new ArrayList<String>();
		String rate;
		for(int i = startYear;i <= endYear;i ++) {
			for(int j = 1;j <=4 ; j++) {
				String key = i + "-" + j;
				if(sortedTotalStu.containsKey(key)) {
					if(sortedStu.containsKey(key)) {
						rate = String.format("%.1f%%",((float)sortedStu.get(key) / (float)sortedTotalStu.get(key)) * 100);
						list.add(i + "," + j + "," + courseCode + "," + courseName + "," + sortedTotalStu.get(key) + "," + sortedStu.get(key) + "," + rate);
					}else {
						list.add(i + "," + j + "," + courseCode + "," + courseName + "," + sortedTotalStu.get(key) + "," + 0 + "," + 0 + "%");
					}
				}
			}
		}
		
		return list;
	}
	
	private HashMap<String,Integer> loadTotalStu(HashMap<String,Student> students){ // key: 2018-2 ,value : totalstu
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		int total = 0;
		for(int i = startYear;i <= endYear;i ++) {
			for(int j = 1;j <=4 ; j++) {
				total = 0;
				String key2 = i + "-" + j;
				for(String key:students.keySet()) {
					if(students.get(key).getSemestersByYearAndSemester().containsKey(key2)) {
						total ++;
					}
				}
				map.put(key2,total);
			}
		}
		
		return map;
	}
	
	private HashMap<String,Integer> loadStu(ArrayList<String> lines){
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		int n = 1;
		for(String line:lines) {
			String key = line.split(",")[7].trim() + "-" + line.split(",")[8].trim();
			if(!courseCode.equals(line.split(",")[4].trim()))
				continue;
			else if(n == 1) {
				courseName = line.split(",")[5].trim();
				n = 0;
			}
			if(map.containsKey(key)) {
				int a = map.get(key) + 1;
				map.put(key,a);
			}else {
				map.put(key,1);
			}
		}
		return map;
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		
		// TODO: Implement this method
		HashMap<String,Student> map = new HashMap<String,Student>();
		String pkey = lines.get(0).split(",")[0];
		Student stu;
		Student pstu = new Student(lines.get(0).split(",")[0]);
		for(String line:lines) {
			String key = line.split(",")[0];
			Course course = new Course(line);
			if(pkey.equals(key)) {
				pstu.addCourse(course);
			}else {
				map.put(pkey,pstu);
				stu = new Student(key);	
				stu.addCourse(course);
				pstu = stu;
				pkey = key;
			}
		}
		return map; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		// TODO: Implement this method
		ArrayList<String> list = new ArrayList<String>();
		Set<Map.Entry<String, Student>> entries = sortedStudents.entrySet();
		for(Map.Entry<String, Student> tempEntry: entries){
            int totalNumberOfSemestersRegistered = tempEntry.getValue().getSemestersByYearAndSemester().size();
            
            for(int i = 1;i <= totalNumberOfSemestersRegistered;i ++)
            	list.add(tempEntry.getKey() + "," + totalNumberOfSemestersRegistered + "," + i + "," +tempEntry.getValue().getNumCourseInNthSementer(i));
        }
		return list; // do not forget to return a proper variable.
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			dataPath = cmd.getOptionValue("i");
			resultPath = cmd.getOptionValue("o");
			help = cmd.hasOption("h");
			analOption = Integer.parseInt(cmd.getOptionValue("a"));
			if(analOption == 2) {
				courseCode = cmd.getOptionValue("c");
			}
			startYear = Integer.parseInt(cmd.getOptionValue("s"));
			endYear = Integer.parseInt(cmd.getOptionValue("e"));
			
		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}
	
	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());

		// add options by using OptionBuilder
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()     // this option is intended not to have an option value but just an option
				.argName("Output path")
				.required() // this is an optional option. So disabled required().
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()     // this option is intended not to have an option value but just an option
				.argName("Analysis option")
				.required() // this is an optional option. So disabled required().
				.build());
				
		// add options by using OptionBuilder
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()     // this option is intended not to have an option value but just an option
				.argName("course code")
				//.required() // this is an optional option. So disabled required().
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()     // this option is intended not to have an option value but just an option
				.argName("Start year for analysis")
				.required() // this is an optional option. So disabled required().
				.build());
		
		//add options by using OptionBuilder
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());
				
		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help")
				.argName("Help")
		        .desc("Show a Help page")
		        .build());

		return options;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}
}
