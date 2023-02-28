public class Main {

    public static void main(String[] args) {
	// write your code here
        Thread[] threads = new Thread[4];

        for (int i = 0; i < threads.length; i++) {
            final int threadNumber = i + 1;
            threads[i] = new Thread(() -> {
                for (int j = 1; j <= 10; j++) {
                    System.out.println(Thread.currentThread().getName() + ": " + j);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
