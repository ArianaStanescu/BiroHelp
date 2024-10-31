//package org.example;
//import java.util.*;
//
//
//class Client implements Runnable {
//    private final String name;
//    private final Document requestedDocument;
//    private final List<Document> ownedDocuments = new ArrayList<>();
//    private final Set<Document> necessaryDocuments = new LinkedHashSet<>();
//    private final List<Office> offices;
//
//    public Client(String name, Document requestedDocument, List<Office> offices) {
//        this.name = name;
//        this.requestedDocument = requestedDocument;
//        this.offices = offices;
//    }
//
//    protected String getName() {
//        return name;
//    }
//
//    //metoda e synchronized pt a evita conflictele
//    //in cazul in acre mai multe fire de executie incearca sa acceseze ownedDocuments
//    //de exemplu alte metode pot adauga un document la lista, modificand-o
//    protected synchronized boolean ownsDocument(Document doc) {
//        return ownedDocuments.contains(doc);
//    }
//
//    protected void updateNecessaryDocuments(Document document) {
//        Set<Document> visited = new HashSet<>(); //hashset -> pt a evita dependentele circulare (e o multime de documented => nu sunt duplicate)
//        List<Document> orderedDocuments = new ArrayList<>(); //lista ordonata de documente necesare
//        Queue<Document> toCheck = new LinkedList<>(); //coada ca sa verificam documentele necesare
//        toCheck.add(document); //adaiga documentul initial la coada
//
//        //parcurgem documentele necesare pana coada devine goala
//        while (!toCheck.isEmpty()) {
//            Document neededDoc = toCheck.poll(); //scoatem documentul din coada
//            if (!visited.contains(neededDoc)) { //daca documentele vizitate deja nu il contin pe cel scos din lista
//                visited.add(neededDoc); //adaugam documentul in lista celor vizitate
//                if (!ownsDocument(neededDoc)) { //daca clientul nu detine documentul, il adaugam la documentele necesare
//                    orderedDocuments.add(neededDoc);
//                    toCheck.addAll(neededDoc.getNecessaryDocuments()); //adaugam documenetele necesare ale documentului curent la coada
//                }
//            }
//        }
//        necessaryDocuments.addAll(orderedDocuments); //adaugam docuentele necesare ordonate
//        //necessaryDocuments.forEach(doc -> System.out.println(doc.getName()));
//    }
//
//    protected void requestDocument(Document document, Set<Document> visited) throws InterruptedException {
//        if (visited.contains(document)) {
//            return; // ne asiguram ca documentele vizitate nu mai contin documentul dorit pentru a preveni recursivitatea infinita
//        }
//
//        visited.add(document); //adaugam documentul curent la lista documentelor vizitate
//        updateNecessaryDocuments(document); //actualizeaza lista de documente necesare pentru documentul curent
//
//        //parcurgem lista de documente necesare
//        for (Document dep : new ArrayList<>(necessaryDocuments)) {
//            if (!ownsDocument(dep)) { //daca clientul nu are documentul necesar
//                requestDocument(dep, visited); //solicita documentul (recursiv)
//                ownedDocuments.add(dep); //l-a obitnut si il adaugam in lista de documente obtinute
//                necessaryDocuments.remove(dep); //il eliminam din lista documentelor necesare
//            }
//        }
//
//        for (Office office : offices) {
//            if (office.canIssueDocument(document)) { //verificam daca biroul poate elibera documentul
//                System.out.println(name + " cere documentul " + document.getName() + " la biroul " + office.getName());
//                office.addClient(this); //adaugam clientul la coada
//                return;
//            }
//        }
//
//        //daca se ajunge aici inseamna ca documentul nu a putut fi solicitat (se ajunge aici daca nu se intra pe cazul de "baza" care face return)
//        System.out.println(name + " nu a putut cere documentul " + document.getName());
//    }
//
//    @Override
//    public void run() {
//        BureaucraticServer server = BureaucraticServer.getInstance();
//        Set<Document> visited = new HashSet<>(); //lista in care tinem documentele vizitate
//        try {
//            requestDocument(requestedDocument, visited); //obtinem documentele necesare pentru documentul dorit
//            server.processDocumentRequest(this, requestedDocument);
//            System.out.println(name + " a obtinut toate documentele necesare, inclusiv " + requestedDocument.getName());
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//}
//



package org.example;
import java.util.*;


class Client implements Runnable {
    private final String name;
    private final Document requestedDocument;
    private final List<Document> ownedDocuments = new ArrayList<>();
    //private final Set<Document> necessaryDocuments = new LinkedHashSet<>();
    private final List<Office> offices;

    public Client(String name, Document requestedDocument, List<Office> offices) {
        this.name = name;
        this.requestedDocument = requestedDocument;
        this.offices = offices;
    }

    protected String getName() {
        return name;
    }

    //metoda e synchronized pt a evita conflictele
    //in cazul in acre mai multe fire de executie incearca sa acceseze ownedDocuments
    //de exemplu alte metode pot adauga un document la lista, modificand-o
    protected synchronized boolean ownsDocument(Document doc) {
        return ownedDocuments.contains(doc);
    }



    protected void requestDocument(Document document) throws InterruptedException {
        Deque<Document> reverseOrder = new ArrayDeque<>(); // Coada inversata de documente - ne asiguram ca documentele necesare sunt obtinute in ordinea corecta
        buildReverseDocumentList(document, reverseOrder, new HashSet<>()); // Construim lista inversata

        while (!reverseOrder.isEmpty()) {
            Document currentDoc = reverseOrder.poll(); // Scoatem primul document de la coada
            if (ownsDocument(currentDoc)) {
                continue; // Daca clientul deține deja documentul trece la urmatorul
            }

            // Verificam daca vreun birou poate elibera documentul curent
            for (Office office : offices) {
                if (office.canIssueDocument(currentDoc)) { // Daca biroul poate elibera documentul
                    System.out.println(name + " cere documentul " + currentDoc.getName() + " la biroul " + office.getName());
                    office.addClient(this); // Adauga clientul in coada biroului
                    ownedDocuments.add(currentDoc); // Adauga documentul la lista de documente deținute
                    break;
                }
            }
        }
    }

    // Metoda recursiva care construiește lista de documente in ordine inversata
    private void buildReverseDocumentList(Document document, Deque<Document> reverseOrder, Set<Document> visited) {
        if (visited.contains(document)) {
            return; // Evitam recursivitatea infinita
        }
        visited.add(document);

        for (Document dep : document.getNecessaryDocuments()) {
            buildReverseDocumentList(dep, reverseOrder, visited); // Apel recursiv pentru fiecare document necesar
        }

        reverseOrder.add(document); // Se adauga doc curent la finalul listei si se incepe cu documentul cel mai de baza
    }



    public void run() {
        BureaucraticServer server = BureaucraticServer.getInstance();
        // Apelul la server se face acum în processDocumentRequest
        server.processDocumentRequest(this, requestedDocument);
        System.out.println(name + " a obtinut toate documentele necesare, inclusiv " + requestedDocument.getName());
    }


}
