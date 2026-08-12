package Design_Patterns.Factory.SF;

// --- Burger Interface ---
interface Burger {
    void prepare();
}

// --- Concrete Burger Implementations ---
class BasicBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Basic Burger with bun, patty, and ketchup!");
    }
}

class StandardBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Standard Burger with bun, patty, cheese, and lettuce!");
    }
}

class PremiumBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Premium Burger with gourmet bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}


//Burger Factory
class BurgerFactory {
    public Burger createBurger(String type) {
        if (type.equalsIgnoreCase("basic"))
            return new BasicBurger();
        else if (type.equalsIgnoreCase("standard")) {
            return new StandardBurger();
        } else if (type.equalsIgnoreCase("premium")) {
            return new PremiumBurger();
        } else {
            System.out.println("Invalid burger type!");
            return null;
        }
    }
}

public class SimpleFactoryDesignPattern {
    static void main() {
        String type = "standard";
        BurgerFactory myBurgerFactory = new BurgerFactory();
        Burger burger = myBurgerFactory.createBurger(type);
        if (burger != null) {
            burger.prepare();
        }
    }
}
