// Waad Turki Alharbi
// 2006198 
// IAR 
// CPCS203

import java.io.*;

import java.util.*;

public class Project_1_Waad_Alharbi_2006198 {

    public static void main(String[] args) throws Exception {

        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        if (!inputFile.exists()) {
            System.out.println("File " + inputFile.getName() + " does not exists!");
            System.exit(0);
        }
        Scanner input = new Scanner(inputFile);
        PrintWriter output = new PrintWriter(outputFile);

        output.println("");
        output.println("    ================================================================================================");
        output.println("    ||                                Saudi Arabia Water Resource Usage                           ||");
        output.println("    ================================================================================================\n");

        String[] region = new String[input.nextInt()];
        output.println("- Number of Regions: " + region.length + "\n");

        String[][] city = new String[region.length][];
        for (int i = 0; i < region.length; i++) {
            city[i] = new String[input.nextInt()];
        }

        String[][][] year = new String[region.length][][];
        for (int i = 0; i < region.length; i++) {
            year[i] = new String[city[i].length][];
        }
        int[][][] populations = new int[region.length][][];
        for (int i = 0; i < region.length; i++) {
            populations[i] = new int[city[i].length][];
        }
        long[][][] water = new long[region.length][][];
        for (int i = 0; i < region.length; i++) {
            water[i] = new long[city[i].length][];
        }

        double[][][] capitaDailyUsage = new double[region.length][][];
        for (int j = 0; j < region.length; j++) {
            capitaDailyUsage[j] = new double[city[j].length][];
        }
        double[][][] change = new double[region.length][][];
        for (int j = 0; j < region.length; j++) {
            change[j] = new double[city[j].length][];
        }

        String command;
        while (true) {
            command = input.next();
            if (command.matches("add_regions")) {

                addRegions(region, input, output);

            } else if (command.equals("add_cities")) {

                addCities(region, city, input, output);

            } else if (command.equals("add_populations")) {

                addPopulations(input, output, year, populations, water, region, city);

            } else if (command.equals("print_result")) {

                printResult(input, output, year, populations, water, region, city, capitaDailyUsage, change);

            } else if (command.equals("find_lowest_usage")) {

                findLowestUsage(output, year, populations, water, region, city, capitaDailyUsage);

            } else if (command.equals("find_higest_usage")) {

                findHigestUsage(output, year, populations, water, region, city, capitaDailyUsage);

            } else if (command.equals("about_developer")) {

                aboutDeveloper(output);

            } else if (command.equals("exit")) {
               
                exit(output);

                break;
            }

        } 

        output.flush();
        input.close();
        output.close();
    }

    public static void addRegions(String[] region, Scanner input, PrintWriter output) {

        output.println("[Command] add_regions");

        for (int i = 0; i < region.length; i++) {
            region[i] = input.next();
            output.print("	+ " + region[i]);

        }

        output.println("");
        output.println("");
    }

    public static void addCities(String[] region, String[][] city, Scanner input, PrintWriter output) {

        output.println("[Command] add_cities");

        int index = indexRegionSearch(input, region);
        output.println("	-> City: " + region[index]);

        for (int i = 0; i < city[index].length; i++) {
            city[index][i] = input.next();
            output.print("	+ " + city[index][i]);
        }

        output.println("");
        output.println("");
    }

    public static void addPopulations(Scanner input, PrintWriter output, String[][][] year, int[][][] populations, long[][][] water, String[] region, String[][] city) {

        output.println("[Command] add_population");

        int rIndex = indexRegionSearch(input, region);
        int cIndex = indexCitySearch(input, city, rIndex);
        output.println("	-> Region: " + region[rIndex]);
        output.println("	-> City: " + city[rIndex][cIndex]);

        year[rIndex][cIndex] = new String[input.nextInt()];
        populations[rIndex][cIndex] = new int[year[rIndex][cIndex].length];
        water[rIndex][cIndex] = new long[year[rIndex][cIndex].length];

        output.println("    ------------------------------------------------------------------------");
        output.println("	Year      	Population		Annual Water Usage (cb.m)");

        for (int i = 0; i < year[rIndex][cIndex].length; i++) {
            year[rIndex][cIndex][i] = input.next();
            populations[rIndex][cIndex][i] = input.nextInt();
            water[rIndex][cIndex][i] = input.nextInt();

            output.printf("        %-15s %-,23d %-,25d %n", year[rIndex][cIndex][i], populations[rIndex][cIndex][i], water[rIndex][cIndex][i]);
        }

        output.println("    ------------------------------------------------------------------------");
        output.println("");
    }

    public static void printResult(Scanner input, PrintWriter output, String[][][] year, int[][][] populations, long[][][] water, String[] region, String[][] city, double[][][] capitaDailyUsage, double[][][] change) {

        output.println("[Command] print_result");

        int rIndex = indexRegionSearch(input, region);
        int cIndex = indexCitySearch(input, city, rIndex);
        output.println("	-> Region: " + region[rIndex]);
        output.println("	-> City: " + city[rIndex][cIndex]);

        output.println("    ---------------------------------------------------------------------------------------------------------------");
        output.println("	Year      	Population	Annual Water Usage (cb.m)	Per capita daily usage (l)	Change ");
        output.println("    ---------------------------------------------------------------------------------------------------------------");

        capitaDailyUsage[rIndex][cIndex] = new double[year[rIndex][cIndex].length];
        change[rIndex][cIndex] = new double[year[rIndex][cIndex].length];

        for (int i = 0; i < year[rIndex][cIndex].length; i++) {
            capitaDailyUsage[rIndex][cIndex][i] = (water[rIndex][cIndex][i] * 1000) / (populations[rIndex][cIndex][i] * 365);

            if (capitaDailyUsage[rIndex][cIndex][i] == capitaDailyUsage[rIndex][cIndex][0]) {
                output.printf("        %-15s %-,15d %-,31d %-,31d %s %n", year[rIndex][cIndex][i], populations[rIndex][cIndex][i], water[rIndex][cIndex][i],
                        (int) capitaDailyUsage[rIndex][cIndex][i], "NA");
            } else {
                change[rIndex][cIndex][i]
                        = ((capitaDailyUsage[rIndex][cIndex][i] - capitaDailyUsage[rIndex][cIndex][i - 1]) / capitaDailyUsage[rIndex][cIndex][i - 1]) * 100;
                output.printf("        %-15s %-,15d %-,31d %-,31d %.2f%% %n", year[rIndex][cIndex][i], populations[rIndex][cIndex][i], water[rIndex][cIndex][i],
                        (int) capitaDailyUsage[rIndex][cIndex][i], change[rIndex][cIndex][i]);
            }

        }

        output.println("    ---------------------------------------------------------------------------------------------------------------");
        output.println("");
    }

    public static void findLowestUsage(PrintWriter output, String[][][] year, int[][][] populations, long[][][] water, String[] region, String[][] city, double[][][] capitaDailyUsage) {
        output.println("[Command] find_lowest_usage");
        output.println("	* The city with lowest per capita water usage in all regions");
        output.println("	------------------------------------------------------------------------------------------");
        output.println("	Year      	Population	Annual Water Usage (cb.m)	Per capita daily usage (l)	");
        output.println("	------------------------------------------------------------------------------------------");

        double smallestElement = capitaDailyUsage[0][0][0];
        int rIndex = 0;
        int cIndex = 0;
        int index = 0;

        for (int i = 0; i < capitaDailyUsage.length; i++) {
            for (int j = 0; j < capitaDailyUsage[i].length; j++) {
                for (int k = 0; k < capitaDailyUsage[i][j].length; k++) {
                    if (capitaDailyUsage[i][j][k] < smallestElement) {
                        smallestElement = capitaDailyUsage[i][j][k];
                        rIndex = i;
                        cIndex = j;
                        index = k;
                    }
                }
            }
        }

        output.printf("        %-15s %-,15d %-,31d %d %n", year[rIndex][cIndex][index], populations[rIndex][cIndex][index], water[rIndex][cIndex][index], (int) capitaDailyUsage[rIndex][cIndex][index]);
        output.println("	------------------------------------------------------------------------------------------");
        output.println("	In Region: " + region[rIndex] + ", City: " + city[rIndex][cIndex]);
        output.println("");
    }

    public static void findHigestUsage(PrintWriter output, String[][][] year, int[][][] populations, long[][][] water, String[] region, String[][] city, double[][][] capitaDailyUsage) {
        output.println("[Command] find_higest_usage");
        output.println("	* The city with highest per capita water usage in all regions");
        output.println("	------------------------------------------------------------------------------------------");
        output.println("	Year      	Population	Annual Water Usage (cb.m)	Per capita daily usage (l)	");
        output.println("	------------------------------------------------------------------------------------------");

        double largestElement = capitaDailyUsage[0][0][0];
        int rIndex = 0;
        int cIndex = 0;
        int index = 0;

        for (int i = 0; i < capitaDailyUsage.length; i++) {
            for (int j = 0; j < capitaDailyUsage[i].length; j++) {
                for (int k = 0; k < capitaDailyUsage[i][j].length; k++) {
                    if (capitaDailyUsage[i][j][k] > largestElement) {
                        largestElement = capitaDailyUsage[i][j][k];
                        rIndex = i;
                        cIndex = j;
                        index = k;
                    }
                }
            }
        }

        output.printf("        %-15s %-,15d %-,31d %d %n", year[rIndex][cIndex][index], populations[rIndex][cIndex][index], water[rIndex][cIndex][index], (int) capitaDailyUsage[rIndex][cIndex][index]);
        output.println("	------------------------------------------------------------------------------------------");
        output.println("	In Region: " + region[rIndex] + ", City: " + city[rIndex][cIndex]);
        output.println("");
    }

    public static void aboutDeveloper(PrintWriter output) {
        output.println("[Command] about_developer");
        output.println("	-> Developed By: Waad Turki Alharbi");
        output.println("	-> University ID: 2006198 ");
        output.println("	-> Section: IAR");
        output.println("");

    }

    public static void exit(PrintWriter output) {
        Date d = new Date();
        output.println("Thank You! :)");
        output.print("Report generated on " + d.toString());
    }

    public static int indexRegionSearch(Scanner input, String[] region) {

        String target = input.next();
        int index = -1;
        for (int i = 0; i < region.length; i++) {
            if (region[i].equalsIgnoreCase(target)) {
                return i;
            }
        }
        return index;
    }

    public static int indexCitySearch(Scanner input, String[][] city, int rIndex) {

        String target = input.next();
        int index = -1;

        for (int j = 0; j < city[rIndex].length; j++) {
            if (city[rIndex][j].equalsIgnoreCase(target)) {
                return j;
            }
        }
        return index;
    }

}
