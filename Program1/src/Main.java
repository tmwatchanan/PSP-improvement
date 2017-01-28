import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LinkedList<Double> dataList = new LinkedList<>();
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the number of data: ");
        int count = keyboard.nextInt();
        if (count < 2) {
            System.out.println("[ERROR] Number of input data must be more than 1");
            System.exit(0);
        }
        System.out.println("Enter " + count + " input:");
        double inputNumber;
        for (int i = 0; i < count; i++) {
            System.out.print((i + 1) + ") ");
            inputNumber = keyboard.nextDouble();
            dataList.add(inputNumber);
        }
        ListIterator<Double> listIterator = dataList.listIterator();
        double sumData = 0;
        while (listIterator.hasNext()) {
            sumData += listIterator.next();
        }
        double mean = sumData / count;
        listIterator = dataList.listIterator();
        double sumIntermediate = 0;
        while (listIterator.hasNext()) {
            sumIntermediate += Math.pow(listIterator.next() - mean, 2);
        }
        double std = Math.sqrt(sumIntermediate / (count - 1));
        System.out.println("Mean = " + mean);
        System.out.println("Standard Deviation = " + std);
    }
}
