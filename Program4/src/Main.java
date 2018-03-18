////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Name:    Program 4 - Graph Distribution
// Author:  Watchanan Chantapakul
// Date:    22/02/2017
// Description: Computing standard deviations of input data that conform to the normal distribution
// Usage: Input a data file by passing argument
// 	- Inputs: A set data that can be one or two column data
//	- Outputs: Areas under portions of a normal distribution
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Main {

    public static double Sum(LinkedList<Double> dataList) {
        ListIterator<Double> listIterator = dataList.listIterator();
        double sumData = 0;
        while (listIterator.hasNext()) {
            sumData += listIterator.next();
        }
        return sumData;
    }

    public static double Mean(LinkedList<Double> dataList) {
        if (dataList.size() == 0) {
            System.out.println("[Mean calculating] can't be divided by ZERO!");
            System.exit(0);
        }
        return Sum(dataList) / dataList.size();
    }

    public static double Variance(LinkedList<Double> dataList) {
        double sum = 0;
        double average = Mean(dataList);
        for (double data : dataList) {
            sum += Math.pow((data - average), 2);
        }
        return sum / (dataList.size() - 1);
    }

    public static double StdDeviation(LinkedList<Double> dataList) {
        double variance = Variance(dataList);
        return Math.sqrt(variance);
    }

    public static void main(String[] args) {
        LinkedList<Double> data = new LinkedList<>();
        try {
            // Read data from file
            List<String> lines = Files.readAllLines(Paths.get(args[0]));
            for (String line : lines) {
                String[] columns = line.split("\\s+");
                if (columns.length == 1) {
                    data.add(Math.log(Double.parseDouble(columns[0])));
                }
                else if (columns.length == 2) {
                    if (columns[1] == "0") {
                        System.out.println("[INPUT ERROR] the 2nd column data can't be zero");
                        System.exit(0);
                    }
                    data.add(Math.log(Double.parseDouble(columns[0]) / Double.parseDouble(columns[1])));
                }
                else {
                    System.err.println("[INPUT ERROR] the data must be input only 1-2 columns");
                    System.exit(0);
                }
            }

            // Computing
            double average = Mean(data);
            double stdDeviation = StdDeviation(data);
            double rangeVS = average - (2 * stdDeviation);
            double rangeS = average - stdDeviation;
            double rangeM = average;
            double rangeL = average + stdDeviation;
            double rangeVL = average + (2 * stdDeviation);
            double VS = Math.exp(rangeVS);
            double S = Math.exp(rangeS);
            double M = Math.exp(rangeM);
            double L = Math.exp(rangeL);
            double VL = Math.exp(rangeVL);

            // Print out the result
            System.out.format("%10s%10s%10s%10s%10s\n", "VS", "S", "M", "L", "VL");
            System.out.format("%10.4f%10.4f%10.4f%10.4f%10.4f\n", VS, S, M, L, VL);
        } catch (IOException e) { // Exception on file reading
            e.printStackTrace();
        }
    }
}
