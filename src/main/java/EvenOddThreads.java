public class EvenOddThreads {
    public static void main(String[] args) {
        Thread evenThread = new EvenNumberThread();
        Thread oddThread = new Thread(new OddNumberRunnable());

        evenThread.start();
        oddThread.start();
    }
}

class EvenNumberThread extends Thread {
    @Override
    public void run() {
        for (int i = 2; i <= 10; i += 2) {
            System.out.println("Четное: " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Поток четных чисел прерван");
            }
        }
    }
}

class OddNumberRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 9; i += 2) {
            System.out.println("Нечетное: " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Поток нечетных чисел прерван");
            }
        }
    }
}