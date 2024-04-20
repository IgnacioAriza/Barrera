public class Barrera {
    private int numHilos;
    private int count = 0;
    private Runnable action;

    public Barrera(int numHilos, Runnable action) {
        this.numHilos = numHilos;
        this.action = action;
    }

    public synchronized void await() throws InterruptedException {
        count++;
        if (count == numHilos) {
            action.run(); // Se ejecuta la acción cuando todos los hilos llegan a la barrera
            notifyAll(); // Se despiertan todos los hilos esperando en la barrera
        } else {
            while (count < numHilos) {
                wait(); // Se espera hasta que todos los hilos lleguen
            }
        }
    }

    public static void main(String[] args) {
        final int NUM_HILS = 3;
        final Barrera barrera = new Barrera(NUM_HILS, () -> {
            System.out.println("Todos los hilos han llegado a la barrera.");
        });

        for (int i = 0; i < NUM_HILS; i++) {
            Thread hilo = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " esperando en la barrera...");
                try {
                    barrera.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " continuando después de la barrera.");
            });
            hilo.start();
        }
    }
}
