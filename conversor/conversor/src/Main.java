import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        Scanner escolha = new Scanner(System.in);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        boolean continuar = true;

        while (continuar) {
            System.out.println("""
                    
                    **************************************************************
                    Seja bem-vindo/a ao Conversor de Moeda
                    
                    1) Dólar Americano -> Euro
                    2) Euro -> Dólar Americano
                    3) Dólar Americano -> Real
                    4) Real -> Dólar Americano
                    5) Dólar Americano -> Peso Colombiano
                    6) Peso Colombiano -> Dólar Americano
                    7) Sair
                    Escolha uma opção válida:
                    **************************************************************
                    """);
            int entrada = escolha.nextInt();
            String base = "";
            String alvo = "";

            switch (entrada) {
                case 1 -> {
                    base = "USD";
                    alvo = "EUR";
                }
                case 2 -> {
                    base = "EUR";
                    alvo = "USD";
                }
                case 3 -> {
                    base = "USD";
                    alvo = "BRL";
                }
                case 4 -> {
                    base = "BRL";
                    alvo = "USD";
                }
                case 5 -> {
                    base = "USD";
                    alvo = "COP";
                }
                case 6 -> {
                    base = "COP";
                    alvo = "USD";
                }
                case 7 -> {
                    continuar = false;
                    System.out.println("Saindo");
                    continue;
                }
                default -> {
                    System.out.println("Escolha Invalida");
                    continue;
                }
            }

            System.out.println("Diga o valor que será convertido");
            double valor = escolha.nextDouble();
            var url = "https://v6.exchangerate-api.com/v6/b8d5f5738c11fad754cdcec3/latest/" + base;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String json = response.body();
                JsonObject jsonResponse = gson.fromJson(json, JsonObject.class);
                JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");
                if (rates != null && rates.has(alvo)) {
                    double exchangeRate = rates.get(alvo).getAsDouble();
                    double valorConvertido = valor * exchangeRate;
                    System.out.println("O valor convertido é: " + valorConvertido + " " + alvo);
                } else {
                    System.out.println("Não foi possível encontrar a taxa de câmbio para essa conversão.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("**************************************************************");
            System.out.println("Pressione Enter para continuar...");
            escolha.nextLine();
            escolha.nextLine();
        }
        escolha.close();
    }
}