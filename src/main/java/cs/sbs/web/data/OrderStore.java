package cs.sbs.web.data;

import cs.sbs.web.model.MenuItem;
import cs.sbs.web.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 内存模拟的菜单与订单存储（作业不要求数据库）。
 */
public final class OrderStore {

    private static final List<MenuItem> MENU = List.of(
            new MenuItem("Fried Rice", 8),
            new MenuItem("Fried Noodles", 9),
            new MenuItem("Burger", 10)
    );

    private static final List<Order> ORDERS = new ArrayList<>();
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1000);

    private OrderStore() {
    }

    public static List<MenuItem> allMenuItems() {
        return MENU;
    }

    public static List<MenuItem> searchByName(String query) {
        if (query == null || query.isBlank()) {
            return MENU;
        }
        String q = query.trim().toLowerCase();
        return MENU.stream()
                .filter(m -> m.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public static synchronized Order createOrder(String customer, String food, int quantity) {
        int id = NEXT_ID.incrementAndGet();
        Order order = new Order(id, customer, food, quantity);
        ORDERS.add(order);
        return order;
    }

    public static synchronized Optional<Order> findOrderById(int id) {
        return ORDERS.stream().filter(o -> o.getId() == id).findFirst();
    }
}
