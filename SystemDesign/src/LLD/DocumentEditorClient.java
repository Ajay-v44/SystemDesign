package LLD;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Interface for document elements
interface DocumentElement {
    String render();
}

class TextElement implements DocumentElement {
    private String text;

    public TextElement(String text) {
        this.text = text;
    }

    public String render() {
        return this.text;
    }
}

class ImageElement implements DocumentElement {
    private String imagePath;

    public ImageElement(String imagePath) {
        this.imagePath = imagePath;
    }

    public String render() {
        return this.imagePath;
    }
}

class NewLineElement implements DocumentElement {
    @Override
    public String render() {
        return "\n";
    }
}

class Document {
    private List<DocumentElement> documentElements = new ArrayList<>();

    public void addElements(DocumentElement element) {
        System.out.println(documentElements);
        documentElements.add(element);
    }

    public String render() {
        StringBuilder result = new StringBuilder();
        for (DocumentElement documentElement : documentElements)
            result.append(documentElement.render());
        return result.toString();
    }
}

interface Persistance {
    void save(String data);
}

class FileStorage implements Persistance {
    public void save(String data) {
        try {
            FileWriter outFile = new FileWriter("document.txt");
            outFile.write(data);
            outFile.close();
            System.out.println("Document saved to document.txt");
        } catch (IOException e) {
            System.out.println("Error: Unable to open file for writing.");
        }
    }
}


class DBStorage implements Persistance {
    public void save(String data) {
    }
}


//Document Editor Managing Client Interactions
class DocumentEditor {
    private Document document;
    private Persistance persistance;
    private String renderDocument = "";

    public DocumentEditor(Document document, Persistance persistance) {
        this.document = document;
        this.persistance = persistance;
    }

    public void addText(String text) {
        document.addElements(new TextElement(text));
    }

    public void addImage(String imagePath) {
        document.addElements((new ImageElement(imagePath)));
    }

    public void addNewLine() {
        document.addElements(new NewLineElement());
    }

    public String renderDocuments() {
        if (renderDocument.isEmpty())
            renderDocument = document.render();
        return renderDocument;
    }

    public void saveDocument() {
        persistance.save(renderDocuments());
    }
}


class DocumentEditorClient {
    static void main() {
        Document document = new Document();
        Persistance persistence = new FileStorage();

        DocumentEditor editor = new DocumentEditor(document, persistence);

        // Simulate a client using the editor with common text formatting features.
        editor.addText("Hello, world!");
        editor.addNewLine();
        editor.addText("This is a real-world document editor example.");
        editor.addNewLine();
        editor.addText("Indented text after a tab space.");
        editor.addNewLine();
        editor.addImage("picture.jpg");

        // Render and display the final document.
        System.out.println(editor.renderDocuments());

        editor.saveDocument();
    }
}