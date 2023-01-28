import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class

public class Assignment1 {
    
    public static int N = 100000000;
    // public static int N = 482496;
    // public static int N = 2242496;
    // public static int N = 10000000;
    // public static int N = 2667536;
    // public static int N = 17777776;

    public static boolean[] sieve = new boolean[N+1];

    public static int numThreads = 8;
    public static int segmentSize = N/numThreads;

    public static int totalPrimeCount = 0;
    public static long sumOfPrimes = 0;
    public static int[] top10LargestPrimes = new int[10];
    public static int numberPrimesInTop10 = 0;

    public static int[] leftBounds = new int[numThreads];
    public static int[] rightBounds = new int[numThreads];

    public static boolean showIndividualThreadTimes = false;


    // Used to ceates Sieve that is used by all threads. 
    public static void createSieve() {
        // initializes sieve to true
        for(int i = 2; i <= N; i++) {
            sieve[i] = true;
        }

        // marks each prime with false if it is a composite of another number. 
        for(int i = 2; i*i <= N; i++) {
            if(sieve[i] == true) {
                for(int j = i*i; j <= N; j+=i) {
                    sieve[j] = false;
                }
            }
        }
    }

    public static void initializeBounds() {
        // initializes the bounds for each segent size. 
        for(int i = 0; i < numThreads; i++) {
            leftBounds[i] = segmentSize * (i) + 1;
            rightBounds[i] = segmentSize * (i + 1);
        }
    }

    public static synchronized void incrementPrimeSum(int newPrime) {
        sumOfPrimes += newPrime;
    }

    public static synchronized void addToTotalPrimeCount(int count) {
        totalPrimeCount += count;
    }

    public static synchronized void addToTop10LargestPrimes(int newPrime) {
        int minValue = top10LargestPrimes[0]; 
        int minIndex = 0;

        for(int i = 1; i < 10; i++) {
            if(top10LargestPrimes[i] < minValue) {
                minValue = top10LargestPrimes[i];
                minIndex = i;
            }
        }

        if(newPrime > minValue) {
            top10LargestPrimes[minIndex] = newPrime;
        }
    }

    public static ArrayList<Integer> generatePrimes(int N) {
        // Gets list of primes up to (including) N
        ArrayList<Integer> primes = new ArrayList<Integer>();

        for(int i = 2; i <= N; i++) {

            if(sieve[i] == true) {
                primes.add(i);
            }
        }

        return primes;
    }

    static class FancySieve extends Thread {

        public int sequenceNumber;

        public FancySieve(int sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }


        public void run() {
            long start = 0;
            if(showIndividualThreadTimes == true) {
                start = (System.nanoTime());
            }

            int L = leftBounds[sequenceNumber]; 
            int R = rightBounds[sequenceNumber];

            ArrayList<Integer> primes = generatePrimes((int)Math.sqrt(R));    

            int[] dummy = new int[R-L+1];
            for(int i = 0; i < R-L+1; i++) {
                dummy[i] = 1;
            }

            for(Integer pr : primes) {

                int firstMultiple = (L/pr) * pr;
                
                if(firstMultiple < L) {
                    firstMultiple += pr;
                }

                for(int j = Math.max(firstMultiple, pr*pr); j <= R; j += pr) {
                    dummy[j-L] = 0;
                }
            }

            int count = 0;
            for(int i = L; i <= R; i++) {
                if(dummy[i-L] == 1 && i != 1) {    
                    count++;
                    incrementPrimeSum(i);
                    addToTop10LargestPrimes(i);
                }

            }

            addToTotalPrimeCount(count);

            if(showIndividualThreadTimes == true) {
                long durration = (System.nanoTime() - start) / 1000000;
                System.out.println("thread " + (sequenceNumber + 1) + " took " + durration + " ms to complete");
            }

        }
    }

    public static void main(String[] args) {

        for (String s: args) {
            if(s.equalsIgnoreCase("showThreadTimes")) {
                showIndividualThreadTimes = true;
            }
        }
        long start = System.nanoTime();

        createSieve();
        initializeBounds();

        FancySieve t1 = new FancySieve(0);
        FancySieve t2 = new FancySieve(1);
        FancySieve t3 = new FancySieve(2);
        FancySieve t4 = new FancySieve(3);
        FancySieve t5 = new FancySieve(4);
        FancySieve t6 = new FancySieve(5);
        FancySieve t7 = new FancySieve(6);
        FancySieve t8 = new FancySieve(7);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        long durration = (System.nanoTime() - start) / 1000000;
        System.out.print("total runtime = " + durration + "ms" + " number of primes = " + totalPrimeCount + " sum of all primes = " + sumOfPrimes + " ");
        System.out.print("Top 10 maximum primes = ");
        Arrays.sort(top10LargestPrimes);
        for(int i = 0; i < 10; i++) {
            System.out.print(top10LargestPrimes[i] + " ");
        }

        try {
            FileWriter myWriter = new FileWriter("primes.txt");
            myWriter.write(durration + "ms " + totalPrimeCount + " " + sumOfPrimes + " ");
            // myWriter.write("Top 10 maximum primes = ");
            Arrays.sort(top10LargestPrimes);
            for(int i = 0; i < 10; i++) {
                myWriter.write(top10LargestPrimes[i] + " ");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

    }

}
