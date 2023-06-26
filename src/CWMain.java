import java.util.Arrays;
import java.util.Scanner;

public class CWMain {
    static Scanner scanner =  new Scanner(System.in);

    //to change the color of the console
    public static String ANSI_RESET = "\u001B[0m";
    public static String ANSI_RED = "\u001B[31m";
    public static String ANSI_GREEN = "\u001B[32m";

    //arrays to keep data ( since it's not mandatory to use matrix arrays
    // i use arrays to keep each information separately
    static String[] studentIds = new String[0];
    static String[] studentNames = new String[0];
    static int[] pfMarks = new int[0];
    static int[] dbmsMarks = new int[0];

    public static void main(String[] args) {
        while (true){
            displayMainMenu();
            System.out.print("Input  : ");
            int input;
            try{
                input = Integer.parseInt(scanner.nextLine());
                clearConsole();
                switch (input){
                    case 1:
                        addNewStudentWithoutMarks();
                        break;
                    case 2:
                        addNewStudentWithMarks();
                        break;
                    case 3:
                        addMarkToAStudent();
                        break;
                    case 4:
                        updateStudentDetails();
                        break;
                    case 5:
                        updateMarks();
                        break;
                    case 6:
                        deleteStudent();
                        break;
                    case 7:
                        printStudentDetails();
                        break;
                    case 8:
                        printStudentRanks();
                        break;
                    case 9:
                        printBestInProgrammingFund();
                        break;
                    case 10:
                        printBestInDBMS();
                        break;
                    default:
                        System.out.println(ANSI_RED+"Please enter a valid input"+ANSI_RESET);
                }
            }catch (NumberFormatException nfe){
                System.out.println("Please enter a valid input");
                clearConsole();
            }
    }
}

    public static void clearConsole(){
        try{
            String operatingSystem = System.getProperty("os.name");
            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        }catch(Exception e){
            System.out.println("An error occurred during the clearance");
        }
    }


    //this method contains the main menu
    public static void displayMainMenu(){
        System.out.printf("---------------------------------------------------%n");
        System.out.printf("Welcome to the GDSE Marks Management System \n Please enter the option number to proceed%n");

        System.out.printf("---------------------------------------------------%n");
        System.out.printf(" %-37s  %-37s %n", "", "");
        System.out.printf("| %-37s | %-37s |%n", "[1] Add new Student", "[2] Add new student with Marks");
        System.out.printf("| %-37s | %-37s |%n", "[3] Add marks", "[4] Update Student Details");
        System.out.printf("| %-37s | %-37s |%n", "[5] Update marks", "[6] Delete Student ");
        System.out.printf("| %-37s | %-37s |%n", "[7] Print student details", "[8] Print Student Ranks");
        System.out.printf("| %-37s | %-37s |%n", "[9] Best in Programming Fundamentals", "[10] Best in Database Management System ");
    }

    //adds new student data
    public static void addNewStudentWithoutMarks(){
        System.out.print("Enter Student Id : ");
        String studentId = scanner.next();
        if(isRegistered(studentId)){
            System.out.println(ANSI_RED+"Student already exists. Please re-enter a correct student id"+ANSI_RESET);
            addNewStudentWithoutMarks();
        }else {
            System.out.print("Enter student Name : ");
            String studentName = scanner.next();
            if(saveStudentData(studentId,studentName,-1,-1)){
                System.out.print(ANSI_GREEN+"New student added. Do you want to add a new student (Y/n) :"+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))addNewStudentWithoutMarks();
            }
        }
    }

    public static void addNewStudentWithMarks(){
        System.out.print("Enter Student Id : ");
        String studentId = scanner.next();
        if(isRegistered(studentId)){
            System.out.println(ANSI_RED+"Student already exists. Please re-enter a correct student id"+ANSI_RESET);
            addNewStudentWithMarks();
        }else {
            System.out.print("Enter student Name : ");
            String studentName = scanner.next();
            int prfm = getMark("Programming Fundamentals");
            int dbms = getMark("Database Management Systems");
            if(saveStudentData(studentId,studentName,prfm,dbms)){
                System.out.print(ANSI_GREEN+"New student added. Do you want to add a new student (Y/n) :"+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))addNewStudentWithMarks();
            }
        }
    }

    public static void addMarkToAStudent(){
        System.out.print("Enter Student Id : ");
        String id = scanner.next();
        if(isRegistered(id)){
            int index = getIndex(id);
            if(isMarksAvailable(index)){
                System.out.print(ANSI_GREEN+"Marks have been added to this student. Use option 4 to update marks. Do you want to add a new student (Y/n) :"+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))addMarkToAStudent();
            }else {
                int prfm = getMark("Programming Fundamentals");
                int dbms = getMark("Database Management Systems");
                if(updateStudentData(prfm,dbms,index)){
                    System.out.print(ANSI_GREEN+"Marks have been added. Do you want to add marks to another student (Y/n) :"+ANSI_RESET);
                    if (scanner.next().equalsIgnoreCase("y"))addMarkToAStudent();
                }
            }
        }else {
            System.out.println(ANSI_RED+"Invalid Student ID. Please re-enter a correct student id"+ANSI_RESET);
            addMarkToAStudent();
        }
    }

    public static void updateStudentDetails(){
        System.out.print("Enter Student Id : ");
        String id = scanner.next();
        if(isRegistered(id)){
            int index = getIndex(id);
            System.out.println(ANSI_GREEN+"Student Name : "+getNameByIndex(index)+ANSI_RESET);
            System.out.print(ANSI_GREEN+"Enter the new student Name : "+ANSI_RESET);
            String newName = scanner.next();
            if(updateStudentData(newName,index)){
                System.out.print(ANSI_GREEN+"Student Details Updated. Do you want to update another student (Y/n) :"+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))updateStudentDetails();
            }
        }else{
            System.out.println(ANSI_RED+"Invalid Student ID. Please re-enter a correct student id"+ANSI_RESET);
            updateStudentDetails();
        }
    }

    public static void updateMarks(){
        System.out.print("Enter Student Id : ");
        String id = scanner.next();
        if(isRegistered(id)){
            int index = getIndex(id);
            if(getPMFMarkByIndex(index)==-1){
                System.out.print(ANSI_GREEN+"Marks are yet to be added. Do you want to update another student's marks (Y/n) :"+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))updateMarks();
            }else {
                System.out.println(ANSI_GREEN+"Student Name : "+getNameByIndex(index)+ANSI_RESET);
                System.out.println(ANSI_GREEN+"Programming Fundamentals Marks : "+getPMFMarkByIndex(index)+ANSI_RESET);
                System.out.println(ANSI_GREEN+"Database Management Systems Marks : "+getDBMSMarkByIndex(index)+ANSI_RESET);
                int pfm = getMark("Programming Fundamentals");
                int dbms = getMark("Database Management Systems");
                if (updateStudentData(pfm,dbms,index)){
                    System.out.print(ANSI_GREEN+"Marks Updated. Do you want to update another student's marks (Y/n) :"+ANSI_RESET);
                    if (scanner.next().equalsIgnoreCase("y"))updateMarks();
                }
            }
        }else {
            System.out.println(ANSI_RED+"Invalid Student ID. Please re-enter a correct student id"+ANSI_RESET);
            updateMarks();
        }
    }

    //this method is called when a student need to be deleted
    public static void deleteStudent(){
        System.out.print("Enter Student Id : ");
        String id = scanner.next();
        if(isRegistered(id)){
            int index = getIndex(id);
            if (removeStudentData(index)){
                System.out.println(ANSI_RED+"Student Deleted."+ANSI_RESET);
                System.out.println(ANSI_GREEN+"Do you want to delete another student (Y/n) : "+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))updateMarks();
            }
        }else {
            System.out.println(ANSI_RED+"Invalid Student ID. Please re-enter a correct student id"+ANSI_RESET);
            deleteStudent();
        }
    }

    public static void printStudentDetails(){
        System.out.print("Enter Student Id : ");
        String id = scanner.next();
        if(isRegistered(id)){
            int index = getIndex(id);
            System.out.println(ANSI_GREEN+"Student Name : "+getNameByIndex(index)+ANSI_RESET);
            if(getPMFMarkByIndex(index)==-1){
                System.out.print(ANSI_GREEN+"Marks are yet to be added. Do you want to view another student's marks (Y/n) :"+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))printStudentDetails();
            }else {
                System.out.printf("----------------------------------------------------------");
                System.out.printf(" %-37s  %-15s %n", "", "");
                System.out.printf("| %-37s | %-15s |%n", "Programming Fundamentals Marks", getPMFMarkByIndex(index));
                System.out.printf("| %-37s | %-15s |%n", "Database Management Systems Marks", getDBMSMarkByIndex(index));
                System.out.printf("| %-37s | %-15s |%n", "Total Marks", getTotalMarksOfAStudent(index));
                System.out.printf("| %-37s | %-15s |%n", "Average Marks", getAverageMarksOfAStudent(index));
                rankStudents();
                System.out.printf("| %-37s | %-15s |%n", "Rank",getIndex(id)+1);
                System.out.printf("----------------------------------------------------------%n%n");

                    System.out.print(ANSI_GREEN+"Do you want to search another student (Y/n) :"+ANSI_RESET);
                if (scanner.next().equalsIgnoreCase("y"))printStudentDetails();

            }
        }else {
            System.out.println(ANSI_RED+"Invalid Student ID. Please re-enter a correct student id"+ANSI_RESET);
            printStudentDetails();
        }
    }

    public static void printStudentRanks(){
        System.out.printf("-------------------------------------------------------------%n");
        System.out.printf("|\t\t\t\t\tStudent Ranks \t\t\t\t\t\t\t|%n");
        System.out.printf("--------------------------------------------------------------%n");
        rankStudents();
        System.out.printf("| %-5s | %-6s | %-15s | %-12s| %-10s |%n", "Rank", "ID","Name","Total Marks","Avg. Marks");
        System.out.printf("--------------------------------------------------------------%n");
        for (int i = 0; i < studentIds.length; i++) {
            if (getTotalMarksOfAStudent(i)>=0){
                System.out.printf("| %-5s | %-6s | %-15s | %-12s| %-10s |%n", (i+1),getIDByIndex(i),getNameByIndex(i),getTotalMarksOfAStudent(i),getAverageMarksOfAStudent(i));
            }
        }
        System.out.printf("--------------------------------------------------------------%n%n");
        System.out.print(ANSI_GREEN+"Do you want to go back to the main menu (Y/n) :"+ANSI_RESET);
        if (scanner.next().equalsIgnoreCase("n"))printStudentRanks();
    }

    public static void printBestInProgrammingFund(){
        System.out.printf("----------------------------------------------------%n");
        System.out.printf("|\t\tBest In Programming Fundamentals \t\t\t|%n");
        System.out.printf("-----------------------------------------------------%n");
        rankStudentsByPF();
        System.out.printf("| %-6s | %-15s | %-10s| %-10s |%n",  "ID","Name","PF Marks","DBMS Marks");
        System.out.printf("-----------------------------------------------------%n");
        for (int i = 0; i < studentIds.length; i++) {
            if (getPMFMarkByIndex(i)>=0) {
                System.out.printf("| %-6s | %-15s | %-10s| %-10s |%n", getIDByIndex(i), getNameByIndex(i), getPMFMarkByIndex(i), getDBMSMarkByIndex(i));
            }
        }
        System.out.printf("-----------------------------------------------------%n%n");
        System.out.print(ANSI_GREEN+"Do you want to go back to the main menu (Y/n) :"+ANSI_RESET);
        if (scanner.next().equalsIgnoreCase("n"))printBestInProgrammingFund();
    }

    public static void printBestInDBMS(){
        System.out.printf("----------------------------------------------------%n");
        System.out.printf("|\t\tBest In DBMS \t\t\t|%n");
        System.out.printf("-----------------------------------------------------%n");
        rankStudentsByDBMS();
        System.out.printf("| %-6s | %-15s | %-10s| %-10s |%n",  "ID","Name","DBMS Marks","PF Marks");
        System.out.printf("-----------------------------------------------------%n");
        for (int i = 0; i < studentIds.length; i++) {
            if (getPMFMarkByIndex(i)>=0) {
                System.out.printf("| %-6s | %-15s | %-10s| %-10s |%n", getIDByIndex(i), getNameByIndex(i), getDBMSMarkByIndex(i), getPMFMarkByIndex(i));
            }
        }
        System.out.printf("-----------------------------------------------------%n%n");
        System.out.print(ANSI_GREEN+"Do you want to go back to the main menu (Y/n) :"+ANSI_RESET);
        if (scanner.next().equalsIgnoreCase("n"))printBestInDBMS();
    }

    // is called by the addnewstudent method and used to add data to arrays
    public static boolean saveStudentData(String id, String name, int mark1 , int mark2){
        String[] tempIds = new String[studentIds.length+1];
        String[] tempNames = new String[studentIds.length+1];
        int[] tempPFMarks = new int[studentIds.length+1];
        int[] tempDBMSMarks = new int[studentIds.length+1];
        for (int i = 0; i < studentIds.length; i++) {
            tempIds[i] = studentIds[i];
            tempNames[i] = studentNames[i];
            tempPFMarks[i] = pfMarks[i];
            tempDBMSMarks[i] = dbmsMarks[i];
        }
        tempIds[studentIds.length] = id;
        tempNames[studentIds.length] = name;
        tempPFMarks[studentIds.length] = mark1;
        tempDBMSMarks[studentIds.length] = mark2;

        studentIds = tempIds;
        studentNames = tempNames;
        pfMarks = tempPFMarks;
        dbmsMarks = tempDBMSMarks;
        return true;
    }

    //this method is called to delete a student
    public static boolean removeStudentData(int index){
        String[] tempIds = new String[studentIds.length-1];
        String[] tempNames = new String[studentIds.length-1];
        int[] tempPFMarks = new int[studentIds.length-1];
        int[] tempDBMSMarks = new int[studentIds.length-1];

        for (int i = 0; i < index; i++) {
            tempIds[i] = studentIds[i];
            tempNames[i] = studentNames[i];
            tempPFMarks[i] = pfMarks[i];
            tempDBMSMarks[i] = dbmsMarks[i];

        }
        for (int i = (index+1); i < studentIds.length; i++) {
            tempIds[i-1] = studentIds[i];
            tempNames[i-1] = studentNames[i];
            tempPFMarks[i-1] = pfMarks[i];
            tempDBMSMarks[i-1] = dbmsMarks[i];
        }
        studentIds = tempIds;
        studentNames = tempNames;
        pfMarks = tempPFMarks;
        dbmsMarks = tempDBMSMarks;
        return true;
    }

    public static boolean updateStudentData(int mark1 , int mark2,int index){
        pfMarks[index] = mark1;
        dbmsMarks[index] = mark2;
        return true;
    }

    public static boolean updateStudentData(String name,int index){
        studentNames[index] = name;
        return true;
    }


        public static boolean isRegistered(String id){
        boolean isRegistered = false;
        for (int i = 0; i < studentIds.length; i++) {
            if (studentIds[i].equals(id)) {
                isRegistered = true;
            }
        }
        return isRegistered;
    }
    public static boolean isMarksAvailable(int index){
        if(pfMarks[index]==-1)return false;
        else return true;
    }


    //used to get subject marks from the user and validate.
    public static int getMark(String subject){
        boolean isMarkValid = false;
        int mark=-1;
        while (!isMarkValid){
            System.out.print(String.format("Enter %s marks : ", subject));
            mark = scanner.nextInt();
            if (mark>=0 & mark<=100) isMarkValid=true;
        }
        return mark;
    }

    //used to get the index of a given student id
    public static int getIndex(String id){
        int index = -1;
        for (int i = 0; i < studentIds.length; i++) {
            if (studentIds[i].equals(id)) index = i;
        }
        return index;
    }

    //instead of directly accessing the data, i'm using couple of methods to do that
    public static String getNameByIndex(int index){
        return studentNames[index];
    }
    public static String getIDByIndex(int index){
        return studentIds[index];
    }
    public static int getPMFMarkByIndex(int index){
        return pfMarks[index];
    }
    public static int getDBMSMarkByIndex(int index){
        return dbmsMarks[index];
    }

    public static int getTotalMarksOfAStudent(int index){
        return pfMarks[index]+dbmsMarks[index];
    }
    public static double getAverageMarksOfAStudent(int index){
        return (double) getTotalMarksOfAStudent(index)/2;
    }

    public static void rankStudents(){
        int[] total = new int[pfMarks.length];
        for (int i = 0; i < pfMarks.length; i++) {
            total[i] = getTotalMarksOfAStudent(i);
        }
        //rank students - sort by descending
        int tempTotal;
        String tempId;
        String tempName;
        int tempPF;
        int tempDBMS;
        for (int i = 0; i < total.length; i++)
        {
            for (int j = i + 1; j < total.length; j++)
            {
                if (total[i] < total[j])
                {
                    tempTotal = total[i];
                    tempId = studentIds[i];
                    tempName = studentNames[i];
                    tempPF = pfMarks[i];
                    tempDBMS = dbmsMarks[i];

                    total[i] = total[j];
                    studentIds[i] = studentIds[j];
                    studentNames[i] = studentNames[j];
                    pfMarks[i] = pfMarks[j];
                    dbmsMarks[i] = dbmsMarks[j];

                    total[j] = tempTotal;
                    studentIds[j] = tempId;
                    studentNames[j] = tempName;
                    pfMarks[j] = tempPF;
                    dbmsMarks[j] = tempDBMS;
                }
            }
        }
    }

    public static void rankStudentsByPF(){
        //rank students - sort by descending
        String tempId;
        String tempName;
        int tempPF;
        int tempDBMS;
        for (int i = 0; i < pfMarks.length; i++)
        {
            for (int j = i + 1; j < pfMarks.length; j++)
            {
                if (pfMarks[i] < pfMarks[j])
                {
                    tempId = studentIds[i];
                    tempName = studentNames[i];
                    tempPF = pfMarks[i];
                    tempDBMS = dbmsMarks[i];

                    studentIds[i] = studentIds[j];
                    studentNames[i] = studentNames[j];
                    pfMarks[i] = pfMarks[j];
                    dbmsMarks[i] = dbmsMarks[j];

                    studentIds[j] = tempId;
                    studentNames[j] = tempName;
                    pfMarks[j] = tempPF;
                    dbmsMarks[j] = tempDBMS;
                }
            }
        }
    }
    public static void rankStudentsByDBMS(){
        //rank students - sort by descending
        String tempId;
        String tempName;
        int tempPF;
        int tempDBMS;
        for (int i = 0; i < dbmsMarks.length; i++)
        {
            for (int j = i + 1; j < dbmsMarks.length; j++)
            {
                if (dbmsMarks[i] < dbmsMarks[j])
                {
                    tempId = studentIds[i];
                    tempName = studentNames[i];
                    tempPF = pfMarks[i];
                    tempDBMS = dbmsMarks[i];

                    studentIds[i] = studentIds[j];
                    studentNames[i] = studentNames[j];
                    pfMarks[i] = pfMarks[j];
                    dbmsMarks[i] = dbmsMarks[j];

                    studentIds[j] = tempId;
                    studentNames[j] = tempName;
                    pfMarks[j] = tempPF;
                    dbmsMarks[j] = tempDBMS;
                }
            }
        }
    }

}
