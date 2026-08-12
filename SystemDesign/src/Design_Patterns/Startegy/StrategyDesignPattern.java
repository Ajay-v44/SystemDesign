package Design_Patterns.Startegy;

//strategy Interface For Walk
interface WalkableRobot {
    void walk();
}

// --- Concrete Strategies for walk ---
class NormalWalk implements WalkableRobot {
    public void walk() {
        System.out.println("Normal Walk");
    }
}

class NoWalk implements WalkableRobot {
    public void walk() {
        System.out.println("Cant Walk");
    }
}

interface TalkableRobot {
    void talk();
}

// --- Concrete Strategies for Talk ---
class NormalTalk implements TalkableRobot {
    public void talk() {
        System.out.println("Talking normally...");
    }
}

class NoTalk implements TalkableRobot {
    public void talk() {
        System.out.println("Cannot talk.");
    }
}

// --- Strategy Interface for Fly ---
interface FlyableRobot {
    void fly();
}

class NormalFly implements FlyableRobot {
    public void fly() {
        System.out.println("Flying normally...");
    }
}

class NoFly implements FlyableRobot {
    public void fly() {
        System.out.println("Cannot fly.");
    }
}

//----Robot Base Class ----
abstract class Robot {
    protected WalkableRobot walkableRobot;
    protected TalkableRobot talkableRobot;
    protected FlyableRobot flyableRobot;

    public Robot(WalkableRobot w, TalkableRobot t, FlyableRobot f) {
        this.walkableRobot = w;
        this.talkableRobot = t;
        this.flyableRobot = f;
    }

    public void walk() {
        walkableRobot.walk();
    }

    public void talk() {
        talkableRobot.talk();
    }

    public void fly() {
        flyableRobot.fly();
    }

    // Abstract method for subclasses
    public abstract void projection();
}

// --- Concrete Robot Types ---
class CompanionRobot extends Robot {
    public CompanionRobot(WalkableRobot w, TalkableRobot t, FlyableRobot f) {
        super(w, t, f);
    }

    public void projection() {
        System.out.println("Displaying friendly companion features...");
    }
}

class WorkerRobot extends Robot {
    public WorkerRobot(WalkableRobot w, TalkableRobot t, FlyableRobot f) {
        super(w, t, f);
    }

    public void projection() {
        System.out.println("Displaying worker efficiency stats...");
    }
}

public class StrategyDesignPattern {
    static void main() {
        Robot robot1 = new CompanionRobot(new NormalWalk(), new NormalTalk(), new NoFly());
        robot1.walk();
        robot1.talk();
        robot1.fly();
        robot1.projection();

        System.out.println("--------------------");

        Robot robot2 = new WorkerRobot(new NoWalk(), new NoTalk(), new NoFly());
        robot2.walk();
        robot2.talk();
        robot2.fly();
        robot2.projection();
    }
}
