////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Name:    Program 3 - Linear Regression and Correlation
// Author:  Watchanan Chantapakul
// Date:    13/02/2017
// Description: Calculating regression and correlation of a set of pairs (x, y) data
// Usage: Input a set of x and a set of y into the program
// 	- Inputs: The number of pairs (x, y) data, set of x and set of y
//	- Outputs: Beta0, Beta1, r, r^2 and yk are the output involved with linear regression and correlation
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

import static java.lang.Math.sqrt; /*ADDED*/

public class Main {

    public static double Sum(LinkedList<Double> dataList) { /*ADDED*/
        ListIterator<Double> listIterator = dataList.listIterator(); /*MODIFIED*/
        double sumData = 0; /*MODIFIED*/
        while (listIterator.hasNext()) { /*MODIFIED*/
            sumData += listIterator.next(); /*MODIFIED*/
        } /*MODIFIED*/
        return sumData; /*ADDED*/
    } /*ADDED*/

    public static double Mean(LinkedList<Double> dataList) { /*ADDED*/
        if (dataList.size() == 0) { /*ADDED*/
            System.out.println("[Mean calculating] can't be divided by ZERO!"); /*ADDED*/
            System.exit(0); /*ADDED*/
        } /*ADDED*/
        return Sum(dataList) / dataList.size(); /*ADDED*/
    } /*ADDED*/

    public static double SumOfProductList(LinkedList<Double> firstList, LinkedList<Double> secondList) { /*ADDED*/
        if (firstList.size() != secondList.size()) { /*ADDED*/
            System.exit(0); /*ADDED*/
        } /*ADDED*/
        ListIterator<Double> firstListIterator = firstList.listIterator(); /*ADDED*/
        ListIterator<Double> secondListIterator = secondList.listIterator(); /*ADDED*/
        double sumData = 0; /*ADDED*/
        while (firstListIterator.hasNext() && secondListIterator.hasNext()) { /*ADDED*/
            sumData += (firstListIterator.next() * secondListIterator.next()); /*ADDED*/
        } /*ADDED*/
        return sumData; /*ADDED*/
    } /*ADDED*/

    public static double Beta1(int n, double xy, double xMean, double yMean, double xSquare) { /*ADDED*/
        double dividend = ((xy) - (n * xMean * yMean)); /*ADDED*/
        double divisor = ((xSquare) - (n * xMean * xMean)); /*ADDED*/
        if (divisor == 0) { /*ADDED*/
            System.out.println("[Beta1 calculating] can't be divided by ZERO!"); /*ADDED*/
            System.exit(0); /*ADDED*/
        } /*ADDED*/
        return dividend / divisor; /*ADDED*/
    } /*ADDED*/

    public static double Beta0(double yMean, double beta1, double xMean) { /*ADDED*/
        return yMean - beta1 * xMean; /*ADDED*/
    } /*ADDED*/

    public static double Rxy(int n, double xy, double xSum, double ySum, double xSquare, double ySquare) { /*ADDED*/
        double dividend = (n * xy) - (xSum * ySum); /*ADDED*/
        double divisor = sqrt((n * xSquare - xSum * xSum) * (n * ySquare - ySum * ySum)); /*ADDED*/
        if (divisor == 0) { /*ADDED*/
            System.out.println("[Rxy calculating] can't be divided by ZERO!"); /*ADDED*/
            System.exit(0); /*ADDED*/
        } /*ADDED*/
        return dividend / divisor; /*ADDED*/
    } /*ADDED*/

    public static double Yk(double beta0, double beta1, double xK) { /*ADDED*/
        return beta0 + beta1 * xK; /*ADDED*/
    } /*ADDED*/

    public static void main(String[] args) {
        LinkedList<Double> xList = new LinkedList<>(); /*MODIFIED*/
        LinkedList<Double> yList = new LinkedList<>(); /*ADDED*/
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the number of data: ");
        int count = keyboard.nextInt();
        if (count < 2) {
            System.out.println("[ERROR] Number of input data must be more than 1");
            System.exit(0);
        }
        System.out.println("Enter " + count + " input:");
        double inputX, inputY; /*MODIFIED*/
        for (int i = 0; i < count; i++) {
            System.out.print((i + 1) + ") ");
            inputX = keyboard.nextDouble(); /*MODIFIED*/
            inputY = keyboard.nextDouble(); /*ADDED*/
            // Add input data x and y into each linked list
            xList.add(inputX); /*MODIFIED*/
            yList.add(inputY); /*ADDED*/
        }
        System.out.println("Enter xk: "); /*ADDED*/
        double xK = keyboard.nextDouble(); /*ADDED*/

        // Detect the number of data of x and y are not equal
        if (xList.size() != yList.size()) { /*ADDED*/
            System.exit(0); /*ADDED*/
        } /*ADDED*/
        double xSum = Sum(xList); /*ADDED*/
        double ySum = Sum(yList); /*ADDED*/
        double xMean = Mean(xList); /*ADDED*/
        double yMean = Mean(yList); /*ADDED*/
        double xSquareSum = SumOfProductList(xList, xList); /*ADDED*/
        double ySquareSum = SumOfProductList(yList, yList); /*ADDED*/
        double xySum = SumOfProductList(xList, yList); /*ADDED*/
        double beta1 = Beta1(count, xySum, xMean, yMean, xSquareSum); /*ADDED*/
        double beta0 = Beta0(yMean, beta1, xMean); /*ADDED*/
        double rXY = Rxy(count, xySum, xSum, ySum, xSquareSum, ySquareSum); /*ADDED*/
        double rSquare = rXY * rXY; /*ADDED*/
        double yK = Yk(beta0, beta1, 386); /*ADDED*/

        System.out.format("%8s%10f\n", "Beta0 = ", beta0); /*ADDED*/
        System.out.format("%8s%10f\n", "Beta1 = ", beta1); /*ADDED*/
        System.out.format("%8s%10f\n", "r = ", rXY); /*ADDED*/
        System.out.format("%8s%10f\n", "r^2 = ", rSquare); /*ADDED*/
        System.out.format("%8s%10f\n", "yk = ", yK); /*ADDED*/

//        double mean = sumData / count; /*DELETED*/
//        listIterator = dataList.listIterator(); /*DELETED*/
//        double sumIntermediate = 0; /*DELETED*/
//        while (listIterator.hasNext()) { /*DELETED*/
//            sumIntermediate += pow(listIterator.next() - mean, 2); /*DELETED*/
//        } /*DELETED*/
//        double std = Math.sqrt(sumIntermediate / (count - 1)); /*DELETED*/
//        System.out.println("Mean = " + mean); /*DELETED*/
//        System.out.println("Standard Deviation = " + std); /*DELETED*/
    }
}
