package Design_Patterns.Structural.Proxy;

interface IImage {
    void display();
}

class RealImage implements IImage {
    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
    }

    public void display() {
        System.out.println(fileName);
    }
}

class ImageProxy implements IImage {
    private RealImage realImage;
    private String filename;

    public ImageProxy(String file) {
        this.filename = file;
        this.realImage = null;
    }

    public void display() {
        if (realImage == null)
            realImage = new RealImage(filename);
        realImage.display();
    }

}

public class VirtualProxy {
    static void main() {
        IImage image1 = new ImageProxy("sample.jpg");
        image1.display();
    }
}
