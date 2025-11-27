import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

record Order(int orderId, String shoeType, int quantity) {
    @Override
    public String toString() {
        return String.format("Order{id=%d, type='%s', quantity=%d}", orderId, shoeType, quantity);
    }
}

class ShoeWarehouse {
    public static final List<String> PRODUCT_TYPES = List.of(
            "Беговые кроссовки", "Баскетбольные кроссовки", "Футбольные бутсы",
            "Теннисные туфли", "Треккинговые ботинки", "Кеды", "Сандалии", "Сапоги"
    );

    private final List<Order> orders = new ArrayList<>();
    private final int MAX_CAPACITY = 10;
    private final AtomicInteger orderCounter = new AtomicInteger(1);

    public synchronized void receiveOrder(String shoeType, int quantity) {
        while (orders.size() >= MAX_CAPACITY) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        int orderId = orderCounter.getAndIncrement();
        Order order = new Order(orderId, shoeType, quantity);
        orders.add(order);
        System.out.println("Получен заказ: " + order);
        notifyAll();
    }

    public synchronized Order fulfillOrder() {
        while (orders.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        Order order = orders.remove(0);
        System.out.println("Выполняется заказ: " + order);
        notifyAll();
        return order;
    }

    public synchronized int getOrderCount() {
        return orders.size();
    }
}

class Producer implements Runnable {
    private final ShoeWarehouse warehouse;
    private final int orderCount;

    public Producer(ShoeWarehouse warehouse, int orderCount) {
        this.warehouse = warehouse;
        this.orderCount = orderCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < orderCount; i++) {
            String shoeType = ShoeWarehouse.PRODUCT_TYPES.get(i % ShoeWarehouse.PRODUCT_TYPES.size());
            int quantity = (int) (Math.random() * 5) + 1;
            warehouse.receiveOrder(shoeType, quantity);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

class Consumer implements Runnable {
    private final ShoeWarehouse warehouse;
    private final int ordersToProcess;
    private final String consumerName;

    public Consumer(ShoeWarehouse warehouse, int ordersToProcess, String consumerName) {
        this.warehouse = warehouse;
        this.ordersToProcess = ordersToProcess;
        this.consumerName = consumerName;
    }

    @Override
    public void run() {
        for (int i = 0; i < ordersToProcess; i++) {
            Order order = warehouse.fulfillOrder();
            if (order == null) break;

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

public class ShoeWarehouseSystem {
    public static void main(String[] args) {
        ShoeWarehouse warehouse = new ShoeWarehouse();
        int totalOrders = 20;

        Thread producerThread = new Thread(new Producer(warehouse, totalOrders));
        producerThread.start();

        int consumerCount = totalOrders / 5;
        Thread[] consumerThreads = new Thread[consumerCount];

        for (int i = 0; i < consumerCount; i++) {
            String consumerName = "Consumer-" + (i + 1);
            consumerThreads[i] = new Thread(new Consumer(warehouse, 5, consumerName));
            consumerThreads[i].start();
        }

        try {
            producerThread.join();
            for (Thread consumerThread : consumerThreads) {
                consumerThread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }

        System.out.println("Осталось на складе: " + warehouse.getOrderCount());
    }
}