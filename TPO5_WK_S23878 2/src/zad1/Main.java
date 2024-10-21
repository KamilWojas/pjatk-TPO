package zad1;
//Zakladam, ze serwer OpenJMS jest uruchomiony na tcp://localhost:3035
public class Main {
    public static void main(String[] args) {
        String[] users = args.length > 0 ? args : new String[]{"Jacek", "Wacek"};

        for (String name : users) {
            new Thread(new Chat(name)).start();
        }
    }
}
