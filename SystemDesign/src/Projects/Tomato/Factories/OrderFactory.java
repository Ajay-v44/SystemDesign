package Projects.Tomato.Factories;

import Projects.Tomato.Models.*;
import Projects.Tomato.Strategies.PaymentStrategy;

import java.util.List;

public interface OrderFactory {
    Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
                      PaymentStrategy paymentStrategy, double totalCost, String orderType);
}
