package Design_Patterns.Singleton;

public class SingletonDesignPattern {
    private static SingletonDesignPattern instance = null;

    private SingletonDesignPattern() {
        System.out.println("Singleton Constructor Called!");
    }

    // Double check locking..
    public static SingletonDesignPattern getInstance() {
        if (instance == null) { // First check (no locking)
            synchronized (SingletonDesignPattern.class) { // Lock only if needed
                if (instance == null) { // Second check (after acquiring lock)
                    instance = new SingletonDesignPattern();
                }
            }
        }
        return instance;
    }

    static void main() {
        SingletonDesignPattern s1 = SingletonDesignPattern.getInstance();
        SingletonDesignPattern s2 = SingletonDesignPattern.getInstance();
        System.out.println(s2==s1);

    }
}
