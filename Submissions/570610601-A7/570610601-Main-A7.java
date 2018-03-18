////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Name:    Program 7 - Prediction Interval
// Author:  Watchanan Chantapakul
// Date:    23/03/2017
// Description: Computing the prediction interval from two sets of numbers x and y
// Usage: Program will retrieve data from input file and display the output
// 	- Inputs: estimated proxy size (xk), and two sets of numbers x and y
//	- Outputs: r_xy, r^2, tail area, beta0, beta1, yk, range, UPI, and LPI
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    public static double SumOfProductList(LinkedList<Double> firstList, LinkedList<Double> secondList) {
        if (firstList.size() != secondList.size()) {
            System.exit(0);
        }
        ListIterator<Double> firstListIterator = firstList.listIterator();
        ListIterator<Double> secondListIterator = secondList.listIterator();
        double sumData = 0;
        while (firstListIterator.hasNext() && secondListIterator.hasNext()) {
            sumData += (firstListIterator.next() * secondListIterator.next());
        }
        return sumData;
    }

    public static double Beta1(int n, double xy, double xMean, double yMean, double xSquare) {
        double dividend = ((xy) - (n * xMean * yMean));
        double divisor = ((xSquare) - (n * xMean * xMean));
        if (divisor == 0) {
            System.out.println("[Beta1 calculating] can't be divided by ZERO!");
            System.exit(0);
        }
        return dividend / divisor;
    }

    public static double Beta0(double yMean, double beta1, double xMean) {
        return yMean - beta1 * xMean;
    }

    public static double Rxy(int n, double xy, double xSum, double ySum, double xSquare, double ySquare) {
        double dividend = (n * xy) - (xSum * ySum);
        double divisor = Math.sqrt((n * xSquare - xSum * xSum) * (n * ySquare - ySum * ySum));
        if (divisor == 0) {
            System.out.println("[Rxy calculating] can't be divided by ZERO!");
            System.exit(0);
        }
        return dividend / divisor;
    }

    public static double Yk(double beta0, double beta1, double xK) {
        return beta0 + beta1 * xK;
    }

    public static double XCorrelationSignificance(double rxy, double n) {
        return (Math.abs(rxy) * Math.sqrt(n - 2)) / Math.sqrt(1.0 - rxy * rxy);
    }

    public static double TailArea(double p) {
        return 1.0 - 2.0 * p;
    }

    public static double Gammar(double x) {
        if (x == 1) { // BASE CASE: x is an integer number
            return 1;
        } else if (x == 0.5) { // BASE CASE:  x is a real number
            return Math.sqrt(Math.PI);
        } else { // RECURSIVE CASE
            return (x - 1) * Gammar(x - 1);
        }
    }

    public static double T(double x, int dof) {
        double divisor = ((Math.pow(dof * Math.PI, 1.0 / 2.0)) * Gammar(dof / 2.0)); // Divisor of T calculation
        if (divisor == 0) { // ERROR can not be divided by 0
            System.err.println("[ERROR] divisor of T function can not be 0!");
        }
        return Gammar((dof + 1.0) / 2.0) * (Math.pow(1.0 + (x * x / dof), (-1.0) * (dof + 1.0) / 2.0)) / divisor;
    }

    public static double p(double x, int num_seg, double W, int dof) {
        double Fzero = T(0, dof); // F(0)
        double sumFourFiW = 0; // sum(4 * F(iW)), i = 1,3,5,...
        for (int i = 1; i <= (num_seg - 1); i += 2) {
            sumFourFiW += (4.0 * T(i * W, dof));
        }
        double sumTwoFiW = 0; // sum(2 * F(iW)), i = 2,4,6,...
        for (int i = 2; i <= (num_seg - 2); i += 2) {
            sumTwoFiW += (2.0 * T(i * W, dof));
        }
        double Fx = T(x, dof); // F(x)
        double p = (W / 3) * (Fzero + sumFourFiW + sumTwoFiW + Fx);
        return p;
    }

    public static boolean AcceptError(double p1, double p2, double E) {
        double difference = Math.abs(p1 - p2); // |p1 - p2|
        return difference < E; // The computed difference must be less than the acceptable error, then yields true
    }

    public static double ForwardIntegrate(double x, int dof, double E) {
        int num_seg = 10; // Number of segments (even number)
        List<Double> pList = new LinkedList<>(); // list of p value
        int idx = 0;
        for (; ; idx++, num_seg *= 2) {
            double W = x / num_seg; // Segment width
            pList.add(p(x, num_seg, W, dof)); // Computing p
            if (idx > 0) { // Check error with acceptable error
                if (AcceptError(pList.get(idx), pList.get(idx - 1), E)) {
                    break;
                }
            }
        }
        return pList.get(idx);
    }

    public static double ReverseIntegrate(double p, int dof, double E) {
        double d = 0.5;
        double x = 1.0;
        double pTry = 0.0;
        boolean previous = false, current = false;
        do {
            pTry = ForwardIntegrate(x, dof, E);
            if (pTry < p) {
                x += d;
                current = false;
            }
            else if (pTry > p) {
                x -= d;
                current = true;
            }
            if ((current && !previous) || !current && previous) d /= 2;
            previous = current;
        } while (!AcceptError(p, pTry, E));
        return x;
    }

    public static double TotalSumOfSquares(LinkedList<Double> list, double mean) {
        double sumData = 0;
        ListIterator<Double> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            sumData += Math.pow(listIterator.next() - mean, 2);
        }
        return sumData;
    }

    public static double StdDeviation(LinkedList<Double> x, LinkedList<Double> y, double beta0, double beta1) {
        double sum = 0;
        for (int i = 0 ; i != x.size(); ++i) {
            sum += Math.pow(y.get(i) - beta0 - beta1 * x.get(i), 2);
        }
        return Math.sqrt(sum / (x.size() - 2));
    }

    public static double Range(int dof, double n, double std, double xk, LinkedList<Double> x, double E) {
        double t = ReverseIntegrate(0.35, dof, E);
        double root = 1.0 + (1.0 / n);
        double xMean = Mean(x);
        double dividend = Math.pow(xk - xMean, 2);
        double divisor = TotalSumOfSquares(x, xMean);
        root += (dividend / divisor);
        root = Math.sqrt(root);
        return t * std * root;
    }

    public static double UPI(double yk, double range) {
        return yk + range;
    }

    public static double LPI(double yk, double range) {
        return yk - range;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("[USAGE]: program7.exe <path-to-file>"); // Wrap .jar into .exe, named "src-count.exe"
        } else {
            try {
                int n = -1;
                double xk = 0;
                LinkedList<Double> x = new LinkedList<>();
                LinkedList<Double> y = new LinkedList<>();
                List<String> lines = Files.readAllLines(Paths.get(args[0]));
                for (String line : lines) {
                    if (n == -1) {
                        xk = Double.parseDouble(line);
                    } else {
                        String[] lineArray = line.split("\\s+");
                        x.add(Double.parseDouble(lineArray[0]));
                        y.add(Double.parseDouble(lineArray[1]));
                    }
                    ++n;
                }
                double xMean = Mean(x);
                double yMean = Mean(y);
                double xSOP = SumOfProductList(x, x);
                double ySOP = SumOfProductList(y, y);
                double xySOP = SumOfProductList(x, y);
                double rxy = Rxy(n, xySOP, Sum(x), Sum(y), xSOP, ySOP);
                double rSquare = rxy * rxy;
                double xCorrelationSignificance = XCorrelationSignificance(rxy, n);
                int dof = n - 2;
                double E = 0.000000000000001;
                double p = ForwardIntegrate(xCorrelationSignificance, dof, E);
                double tailArea = TailArea(p);
                double beta1 = Beta1(n, xySOP, xMean, yMean, xSOP);
                double beta0 = Beta0(yMean, beta1, xMean);
                double yk = Yk(beta0, beta1, xk);
                double std = StdDeviation(x, y, beta0, beta1);
                double range = Range(dof, n, std, xk, x, E);
                double upi = UPI(yk, range);
                double lpi = LPI(yk, range);

                System.out.printf("r_xy = %.9f" + "\n", rxy);
                System.out.printf("r^2 = %.9f" + "\n", rSquare);
                System.out.printf("tail area = ");
                NumberFormat formatter = new DecimalFormat();
                formatter = new DecimalFormat("0.#####E0");
                System.out.println(formatter.format(tailArea));
                System.out.printf("Beta0 = %.9f" + "\n", beta0);
                System.out.printf("Beta1 = %.9f" + "\n", beta1);
                System.out.printf("yk = %.8f" + "\n", yk);
                System.out.printf("Range = %.8f" + "\n", range);
                System.out.printf("UPI (70%%)- = %.8f" + "\n", upi);
                System.out.printf("LPI (70%%) = %.8f" + "\n", lpi);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
