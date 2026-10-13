package Projects.QuickBytes;

import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. DECORATOR PATTERN
// ==========================================
interface IProducts {
    String getDescription();

    double getCost();
}

class Burger implements IProducts {
    @Override
    public double getCost() {
        return 5.00;
    }

    @Override
    public String getDescription() {
        return "Classic Burger";
    }
}

abstract class ProductDecorator implements IProducts {
    protected IProducts decoratedProduct;

    public ProductDecorator(IProducts decoratedProduct) {
        this.decoratedProduct = decoratedProduct;
    }

    @Override
    public double getCost() {
        return decoratedProduct.getCost();
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription();
    }
}

class ExtraCheese extends ProductDecorator {
    public ExtraCheese(IProducts decoratedProduct) {
        super(decoratedProduct);
    }

    @Override
    public double getCost() {
        return super.getCost() + 1.50;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Extra Cheese";
    }
}

class Bacon extends ProductDecorator {
    public Bacon(IProducts decoratedProduct) {
        super(decoratedProduct);
    }

    @Override
    public double getCost() {
        return super.getCost() + 2.00;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Bacon";
    }
}

// ==========================================
// 2. STRATEGY PATTERN
// ==========================================
interface IStrategy {
    double calculateTotal(double total);
}

class StandardStrategy implements IStrategy {
    @Override
    public double calculateTotal(double total) {
        return total;
    }
}

class ProMemberStrategy implements IStrategy {
    private final double discountPercentage = 15.0;

    @Override
    public double calculateTotal(double total) {
        return total - (total * (discountPercentage / 100));
    }
}

// ==========================================
// 3. FACTORY PATTERN
// ==========================================
enum NotificationType {EMAIL, WHATSAPP, SMS}

interface IAlert {
    void send(String recipient, String message);
}

class SMSAlert implements IAlert {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[SMS to " + recipient + "] " + message);
    }
}

class EmailAlert implements IAlert {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[Email to " + recipient + "] " + message);
    }
}

class NotificationFactory {
    public static IAlert createAlert(NotificationType type) {
        if (type == NotificationType.EMAIL) return new EmailAlert();
        if (type == NotificationType.SMS) return new SMSAlert();
        return new SMSAlert();
    }
}

// ==========================================
// DOMAIN MODELS
// ==========================================
class User {
    String name;
    String contactInfo;
    List<NotificationType> notificationTypes;

    public User(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.notificationTypes = new ArrayList<>();
    }

    public void addPreference(NotificationType type) {
        notificationTypes.add(type);
    }
}

enum OrderStatus {PLACED, PREPARING, OUT_FOR_DELIVERY}

class Order {
    int orderId;
    User user;
    List<IProducts> items;
    double total;
    OrderStatus status;

    public Order(int orderId, User user, List<IProducts> items, double total) {
        this.orderId = orderId;
        this.user = user;
        this.items = items;
        this.total = total;
        this.status = OrderStatus.PLACED;
    }
}

class Cart {
    User user;
    List<IProducts> products = new ArrayList<>();
    IStrategy pricingStrategy;

    public Cart(User user, IStrategy strategy) {
        this.user = user;
        this.pricingStrategy = strategy;
    }

    public void addProduct(IProducts product) {
        products.add(product);
    }

    public double calculateTotal() {
        double sum = 0;
        for (IProducts p : products) sum += p.getCost();
        return pricingStrategy.calculateTotal(sum);
    }

    public Order checkout() {
        Order newOrder = new Order((int) (Math.random() * 1000), user, products, calculateTotal());
        OrderProcessingHub.getInstance().placeOrder(newOrder);
        return newOrder;
    }
}

// ==========================================
// 4 & 5. SINGLETON + OBSERVER PATTERNS
// ==========================================
interface IObserver {
    void update(Order order);
}

class KitchenDashboard implements IObserver {
    @Override
    public void update(Order order) {
        System.out.println("--> [KITCHEN DASHBOARD] Order #" + order.orderId + " is " + order.status);
        for (IProducts p : order.items) {
            System.out.println("    Prepare: " + p.getDescription());
        }
    }
}

class UserNotificationsApp implements IObserver {
    @Override
    public void update(Order order) {
        for (NotificationType type : order.user.notificationTypes) {
            IAlert alert = NotificationFactory.createAlert(type);
            alert.send(order.user.contactInfo, "Your order #" + order.orderId + " is now " + order.status);
        }
    }
}

class OrderProcessingHub {
    private static OrderProcessingHub instance;
    private List<IObserver> observers = new ArrayList<>();

    private OrderProcessingHub() {
    }

    public static synchronized OrderProcessingHub getInstance() {
        if (instance == null) {
            instance = new OrderProcessingHub();
        }
        return instance;
    }

    public void attach(IObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(Order order) {
        for (IObserver obs : observers) {
            obs.update(order);
        }
    }

    public void placeOrder(Order order) {
        System.out.println("\n=== NEW ORDER RECEIVED ===");
        notifyObservers(order);
    }

    public void updateOrderStatus(Order order, OrderStatus newStatus) {
        System.out.println("\n=== ORDER STATUS UPDATED ===");
        order.status = newStatus;
        notifyObservers(order);
    }
}

public class QuickBytes {
    static void main(String[] args) {
        OrderProcessingHub hub = OrderProcessingHub.getInstance();
        hub.attach(new KitchenDashboard());
        hub.attach(new UserNotificationsApp());

        User ajay = new User("Ajay", "+91 9876543210");
        ajay.addPreference(NotificationType.SMS);
        ajay.addPreference(NotificationType.EMAIL);

        Cart cart = new Cart(ajay, new ProMemberStrategy());
        IProducts customBurger = new Bacon(new ExtraCheese(new Burger()));
        cart.addProduct(customBurger);

        Order placedOrder = cart.checkout();

        hub.updateOrderStatus(placedOrder, OrderStatus.PREPARING);
    }
}