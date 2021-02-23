package examples;

import examples.entity.ProductInfo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static examples.util.ExceptionUtils.launderThrowable;

/*
    Класс Preloader создает экземпляр FutureTask,
    который описывает задачу загрузки информации о продуктах из базы данных
    и поток, в котором будет выполняться вычисление.

    Он предоставляет метод start для запуска потока,
    так как нецелесообразно запускать поток из конструктора или статического инициализатора.

    Когда программе позже понадобится экземпляр ProductInfo, она может вызвать метод get,
    который вернёт загруженные данные, если они готовы,
    или будет ожидать завершения загрузки, если они ещё не готовы.
 */
public class Preloader {
    private final FutureTask<List<ProductInfo>> future = new FutureTask<>(this::loadProductInfo);
    private final Thread thread = new Thread(future);

    public List<ProductInfo> get() throws InterruptedException {
        try {
            return future.get();
        } catch(ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }

    private void start() {
        thread.start();
    }

    private List<ProductInfo> loadProductInfo() {
        return Arrays.asList(new ProductInfo(), new ProductInfo(), new ProductInfo());
    }
}
