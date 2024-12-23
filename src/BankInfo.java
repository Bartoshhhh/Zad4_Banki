
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BankInfo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Podaj trzy pierwsze cyfry numeru konta:");
        String prefix = scanner.nextLine();


        if (prefix.length() != 3 || !prefix.matches("\\d{3}")) {
            System.out.println("Błąd: Podaj dokładnie trzy cyfry.");
            scanner.close();
            return;
        }


        String fileUrl = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";


        try {

            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Błąd: Nie udało się pobrać pliku. Kod odpowiedzi: " + responseCode);
                scanner.close();
                return;
            }


            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                boolean found = false;


                while ((line = br.readLine()) != null) {

                    String[] parts = line.split("\\s{2,}");
                    if (parts.length > 1) {
                        String bankNumber = parts[0].trim();


                        if (bankNumber.equals(prefix)) {
                            String bankName = parts[1].trim();


                            System.out.println("Numer banku: " + bankNumber);
                            System.out.println("Nazwa banku: " + bankName);
                            found = true;
                            break;
                        }
                    }
                }

                //
                if (!found) {
                    System.out.println("Nie znaleziono banku dla podanego prefiksu.");
                }
            }
        } catch (Exception e) {

            System.out.println("Błąd: Nie udało się pobrać lub przetworzyć danych. Szczegóły: " + e.getMessage());
        }


        scanner.close();
    }
}
