import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

//record Order(int orderId, String shoeType, int quantity) {
//    @Override
//    public String toString() {
//        return String.format("Order{id=%d, type='%s', quantity=%d}", orderId, shoeType, quantity);
//    }
//}

class ShoeWarehouseExecutor {
    public static final List<String> PRODUCT_TYPES = List.of(
            "Беговые кроссовки", "Баскетбольные кроссовки", "Футбольные бутсы",
            "Теннисные туфли", "Треккинговые ботинки", "Кеды", "Сандалии", "Сапоги"
    );

    private final List<Order> orders = new ArrayList<>();
    private final int MAX_CAPACITY = 10;
    private final AtomicInteger orderCounter = new AtomicInteger(1);
    private final ExecutorService fulfillmentExecutor;

    public ShoeWarehouseExecutor(ExecutorService fulfillmentExecutor) {
        this.fulfillmentExecutor = fulfillmentExecutor;
    }

    public synchronized Future<String> receiveOrder(String shoeType, int quantity) {
        while (orders.size() >= MAX_CAPACITY) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return CompletableFuture.completedFuture("Прервано");
            }
        }

        int orderId = orderCounter.getAndIncrement();
        Order order = new Order(orderId, shoeType, quantity);
        orders.add(order);
        System.out.println("Получен заказ: " + order);
        notifyAll();

        return fulfillmentExecutor.submit(() -> fulfillOrderTask());
    }

    private synchronized String fulfillOrderTask() {
        while (orders.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Прервано";
            }
        }

        Order order = orders.remove(0);
        System.out.println("Выполняется заказ: " + order);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Прервано";
        }

        notifyAll();
        return "Заказ выполнен: " + order;
    }

    public synchronized int getOrderCount() {
        return orders.size();
    }

    public void shutdown() {
        fulfillmentExecutor.shutdown();
    }
}

class ProducerCallable implements Callable<String> {
    private final ShoeWarehouseExecutor warehouse;
    private final int orderCount;
    private final ExecutorService producerExecutor;

    public ProducerCallable(ShoeWarehouseExecutor warehouse, int orderCount, ExecutorService producerExecutor) {
        this.warehouse = warehouse;
        this.orderCount = orderCount;
        this.producerExecutor = producerExecutor;
    }

    @Override
    public String call() {
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < orderCount; i++) {
            String shoeType = ShoeWarehouseExecutor.PRODUCT_TYPES.get(i % ShoeWarehouseExecutor.PRODUCT_TYPES.size());
            int quantity = (int) (Math.random() * 5) + 1;

            Future<String> future = warehouse.receiveOrder(shoeType, quantity);
            futures.add(future);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Producer прерван";
            }
        }

        for (Future<String> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Ошибка при выполнении заказа: " + e.getMessage());
            }
        }

        return "Producer завершил работу. Обработано заказов: " + orderCount;
    }
}

public class ShoeWarehouseExecutorService {
    public static void main(String[] args) {
        int totalOrders = 20;

        ExecutorService fulfillmentExecutor = Executors.newFixedThreadPool(3);
        ShoeWarehouseExecutor warehouse = new ShoeWarehouseExecutor(fulfillmentExecutor);

        ExecutorService producerExecutor = Executors.newSingleThreadExecutor();

        try {
            Future<String> producerFuture = producerExecutor.submit(
                    new ProducerCallable(warehouse, totalOrders, producerExecutor)
            );

            String producerResult = producerFuture.get();
            System.out.println(producerResult);

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            producerExecutor.shutdown();
            warehouse.shutdown();

            try {
                if (!producerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    producerExecutor.shutdownNow();
                }
                if (!fulfillmentExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    fulfillmentExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                producerExecutor.shutdownNow();
                fulfillmentExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Осталось заказов: " + warehouse.getOrderCount());
    }
}