class Tortoise extends Thread {
    private final Race race;

    public Tortoise(Race race) {
        this.race = race;
    }

    @Override
    public void run() {
        race.tortoiseRun();
    }
}

class Hare implements Runnable {
    private final Race race;

    public Hare(Race race) {
        this.race = race;
    }

    @Override
    public void run() {
        race.hareRun();
    }
}

class Race {
    private final Object lock = new Object();
    private boolean isTortoiseWinner = false;
    private boolean isHareWinner = true;

    public void tortoiseRun() {
        synchronized (lock) {
            for (int lap = 1; lap <= 10; lap++) {
                System.out.println("Tortoise running lap " + lap);
                checkForWinner();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void hareRun() {
        synchronized (lock) {
            for (int lap = 1; lap <= 10; lap++) {
                System.out.println("Hare running lap " + lap);
                checkForWinner();
                lock.notify();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkForWinner() {
        if (isTortoiseWinner || isHareWinner) {
            lock.notifyAll();

            if (isTortoiseWinner) {
                System.out.println("Tortoise wins!");
            } else {
                System.out.println("Hare wins!");
            }
            System.exit(0);
        }
    }

    public void setTortoiseWinner() {
        isTortoiseWinner = true;
    }

    public void setHareWinner() {
        isHareWinner = false;
    }
}

public class Main {
    public static void main(String[] args) {
        Race race = new Race();
        Tortoise tortoise = new Tortoise(race);
        Hare hare = new Hare(race);

        Thread tortoiseThread = new Thread(tortoise);
        Thread hareThread = new Thread(hare);

        tortoiseThread.start();
        hareThread.start();

        try {
            tortoiseThread.join();
            hareThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}