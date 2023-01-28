# Assignment 1

## Running the Application

javac Assignment1.java

java Assignment1 

OR (to show the individual thread's time)

java Assignment1 showThreadTimes 

## Proof of Correctness

One attribute examined in proving corrrectness was examining the consistancy of the output. Initially, the programm was running into race conditions, however, those where resolved by making counter variables only accessible through synchronized methods. 

Correctness can be examined by analyzing the implemented algorithm. The application utilizes a segmented sieve, where the number 100,000,000 is split into eight separate segments of size 12,500,000. The reason 100,000,000 is split into eight segments is so that one segment can run on each thread in parallel. 

First the application calculates a sieve for all primes from 1 to sqrt(100,000,000).

Second, the eight threads are created and assigned to one of the eight segments. 

Thrid, each thread starts and for the numbers in their segment, they perform the following

  1) get all primes from <lower limmit of segment> to sqrt(<upper limmit of segment>)
  2) For each prime (p) from (1), starting at p^2, mark all multiples of p that are within the range of the segment. 
  3) Examine all unmarked values in the segment and use them to produce the desired output. 
  
Forth, all threads are joined, and their results are combined to form the final output. 

This process ensures correctness by utilizing all threads and performing the calculations in such a way that thead i does not rely on information from thread j.

## Proof of Efficiency

To prove efficiency, the implemented algorithm was run against larger inputs (input is directly assigned to the variable N). Then, each thread was started and the time for each thread to perform the calculation was printed to the console. In addition to this, the total time taken to complete the program was calculated and printed to the console. Efficiency can be determined by examining these times and noting that each thread takes similar time to complete the task, and that the total run time is slightly more than the individual thread’s time. 

Test case 1 calculates the desired output for primes from 1 to 482496

Test case 2 calculates the desired output for primes from 1 to 2242496

Test case 3 calculates the desired output for primes from 1 to 10000000

Test case 4 calculates the desired output for primes from 1 to 2667536

Test case 5 calculates the desired output for primes from 1 to 17777776


The output for each test case is specified below:


Test case 1:

thread 2 took 65 ms to complete

thread 7 took 66 ms to complete

thread 8 took 67 ms to complete

thread 4 took 64 ms to complete

thread 5 took 67 ms to complete

thread 3 took 56 ms to complete

thread 6 took 55 ms to complete

thread 1 took 63 ms to complete

total runtime = 199ms number of primes = 40198 sum of all primes = 9255854957 Top 10 maximum primes = 482387 482393 482399 482401 482407 482413 482423 482437 482441 482483 


Test case 2:

thread 8 took 116 ms to complete

thread 2 took 115 ms to complete

thread 5 took 113 ms to complete

thread 3 took 119 ms to complete

thread 1 took 114 ms to complete

thread 6 took 110 ms to complete

thread 4 took 118 ms to complete

thread 7 took 110 ms to complete

total runtime = 263ms number of primes = 165565 sum of all primes = 178188491928 Top 10 maximum primes = 2242319 2242337 2242343 2242363 2242369 2242379 2242381 2242433 2242441 2242469

Test case 3:

thread 4 took 155 ms to complete

thread 1 took 188 ms to complete

thread 8 took 213 ms to complete

thread 7 took 205 ms to complete

thread 2 took 205 ms to complete

thread 6 took 191 ms to complete

thread 5 took 194 ms to complete

thread 3 took 212 ms to complete

total runtime = 313ms number of primes = 664579 sum of all primes = 3203324994356 Top 10 maximum primes = 9999889 9999901 9999907 9999929 9999931 9999937 9999943 9999971 9999973 9999991


Test case 4:

thread 4 took 113 ms to complete

thread 2 took 114 ms to complete

thread 7 took 107 ms to complete

thread 8 took 113 ms to complete

thread 1 took 113 ms to complete

thread 5 took 109 ms to complete

thread 6 took 112 ms to complete

thread 3 took 112 ms to complete

total runtime = 267ms number of primes = 194453 sum of all primes = 249103836402 Top 10 maximum primes = 2667361 2667383 2667433 2667439 2667451 2667461 2667463 2667481 2667499 2667503


Test case 5:

thread 1 took 307 ms to complete

thread 4 took 355 ms to complete

thread 3 took 348 ms to complete

thread 8 took 351 ms to complete

thread 5 took 343 ms to complete


thread 6 took 312 ms to complete

thread 7 took 321 ms to complete

thread 2 took 347 ms to complete

total runtime = 586ms number of primes = 1137981 sum of all primes = 9767572640342 Top 10 maximum primes = 17777569 17777603 17777611 17777633 17777647 17777659 17777719 17777731 17777741 17777759

As shown for each test cases, the time it takes for each thread to complete is relatively close and indicates that work is being distributed evenly.


## Experimental Evaluation

Experimental evaluation was performed in part during the prof of efficiency, however, it was also performed by setting N to various multiple of eight (so there could be eight segments of numbers, 1 for each thread) and examining for consistency and correctness. 

One thing noted was that while each thread seems to be executing in relatively the same amount of time, the total execution time is still larger then the time it takes for a single thread to complete. After running multiple iterations, it seems that this additional overhead was caused by generating the initial state of the application in the main thread. 
