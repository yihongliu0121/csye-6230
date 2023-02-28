import java.util.Arrays;
import java.util.Random;
public class Main {

    public static void main(String[] args) {
        // Define the size of the array
        int arraySize = 50000000;

        // Generate the array of random values
        int[] values = new int[arraySize];
        Random rand = new Random();
        for (int i = 0; i < arraySize; i++) {
            values[i] = rand.nextInt(50000);
        }

        // Multiply each element of the array by a random value between 0.1 and 0.9
        double[] factors = new double[arraySize];
        for (int i = 0; i < arraySize; i++) {
            factors[i] = 0.1 + (0.9 - 0.1) * rand.nextDouble();
            values[i] *= factors[i];
        }

        // Sum all the values in the array in parallel and measure the time taken
        long startParallel = System.currentTimeMillis();
        double sumParallel = parallelSum(values);
        long endParallel = System.currentTimeMillis();

        // Sum all the values in the array in serial and measure the time taken
        long startSerial = System.currentTimeMillis();
        double sumSerial = serialSum(values);
        long endSerial = System.currentTimeMillis();

        // Print the results and time taken
        System.out.println("Parallel sum: " + sumParallel);
        System.out.println("Time taken for parallel sum: " + (endParallel - startParallel) + " milliseconds");

        System.out.println("Serial sum: " + sumSerial);
        System.out.println("Time taken for serial sum: " + (endSerial - startSerial) + " milliseconds");
    }

    // Define a function to sum all the numbers in an array in parallel
    public static double parallelSum(int[] values) {
        double sum = 0;
        int numThreads = 4;
        SumThread[] threads = new SumThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * values.length / numThreads;
            int endIndex = (i + 1) * values.length / numThreads;
            threads[i] = new SumThread(startIndex, endIndex, values);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                sum += threads[i].getSum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sum;
    }

    // Define a class to sum a portion of an array in parallel
    public static class SumThread extends Thread {
        private final int startIndex;
        private final int endIndex;
        private final int[] values;
        private double sum;

        public SumThread(int startIndex, int endIndex, int[] values) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.values = values;
        }

        public void run() {
            for (int i = startIndex; i < endIndex; i++) {
                sum += values[i];
            }
        }

        public double getSum() {
            return sum;
        }
    }

    // Define a function to sum all the numbers in an array in serial
    public static double serialSum(int[] values) {
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }

        return sum;
    }
}
