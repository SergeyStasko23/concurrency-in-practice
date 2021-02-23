package examples;

import java.util.concurrent.CountDownLatch;

/*
    Если бы мы просто создали и запустили потоки, то потоки, начатые ранее,
    имели бы "фору" перед потоками, запущенными позднее,
    и степень конкуренции изменялась бы с течением времени по мере увеличения или уменьшения числа активных потоков.

    Использование startGate позволяет главному потоку сразу отпустить все рабочие потоки,
    а endGate позволяет главному потоку ждать завершения только последнего потока,
    а не ждать последовательного завершения каждого потока.
 */
public class TestHarness {
    public static void main(String[] args) throws InterruptedException {
        long result = timeTasks(5, () -> System.out.println("TASK RUNNING"));
        System.out.println(result);
    }

    private static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    // Все созданные потоки ожидают, когда startGate примет значение 0, прежде чем выполнять задачу
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        // После выполнения задачи из общего количества потоков в endGate вычитается уже отработанный
                        endGate.countDown();
                    }
                } catch(InterruptedException ignored) {}
            });
            thread.start();
        }

        long start = System.nanoTime();
        /*
           Когда все потоки созданы, происходит уменьшение startGate на единицу,
           что приведёт к одновременной работе всех предварительно созданных потоков
         */
        startGate.countDown();

        /*
           Все уже отработанные потоки ждут, когда количество в endGate станет равно нулю,
           чтобы зафиксировать время окончания работы всех потоков
         */
        endGate.await();
        long end = System.nanoTime();
        return end-start;
    }
}
