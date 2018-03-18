////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Name:    Program 6 - Binary Search
// Author:  Watchanan Chantapakul
// Date:    16/03/2017
// Description: Computing the upper limit range (x) of numerically integral that applying Simpson's rule and t distribution function which match with expected p value
// Usage: Program will prompt user to input data and display the output
// 	- Inputs: integral value (p) and degree of freedom (dof) value
//	- Outputs: upper limit (x) that passes acceptable error
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static double Grammar(double x) {
        if (x == 1) { // BASE CASE: x is an integer number
            return 1;
        } else if (x == 0.5) { // BASE CASE:  x is a real number
            return Math.sqrt(Math.PI);
        } else { // RECURSIVE CASE
            return (x - 1) * Grammar(x - 1);
        }
    }

    public static double T(double x, int dof) {
        double divisor = ((Math.pow(dof * Math.PI, 1.0 / 2.0)) * Grammar(dof / 2.0)); // Divisor of T calculation
        if (divisor == 0) { // ERROR can not be divided by 0
            System.err.println("[ERROR] divisor of T function can not be 0!");
        }
        return Grammar((dof + 1.0) / 2.0) * (Math.pow(1.0 + (x * x / dof), (-1.0) * (dof + 1.0) / 2.0)) / divisor;
    }

    public static double p(double x, int num_seg, double W, int dof) {
        double Fzero = T(0, dof); // F(0)
        double sumFourFiW = 0; // sum(4 * F(iW)), i = 1,3,5,...
        for (int i = 1; i <= (num_seg - 1); i+=2) {
            sumFourFiW += (4.0 * T(i * W, dof));
        }
        double sumTwoFiW = 0; // sum(2 * F(iW)), i = 2,4,6,...
        for (int i = 2; i <= (num_seg - 2); i+=2) {
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
        List<Double> pList = new ArrayList<>(); // list of p value
        int idx = 0;
        for (; ; idx++, num_seg *= 2){
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

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        // prompt for x value
        System.out.print("p value: ");
        double p = keyboard.nextDouble();
        if (p <= 0) { // x value is out of range
            System.err.println("[ERROR] p value must be more than 0");
            System.exit(0);
        }
        // Prompt for degree of freedom
        System.out.print("degree of freedom: ");
        int dof = keyboard.nextInt();
        if (dof < 0) { // Degree of freedom is out of range
            System.err.println("[ERROR] degree of freedom must be more than 0");
            System.exit(0);
        }
        double E = 0.000000001; // Acceptable error
        double x = ReverseIntegrate(p, dof, E);
        System.out.format("x = %.5f", x);
    }
}
