package com.reto4;

import java.util.*;
import java.util.stream.Collectors;

interface Conversor {
    double convertir(double cantidad);
}

class EuroToUsd implements Conversor { public double convertir(double c) { return c * 1.10; } }
class EuroToJpy implements Conversor { public double convertir(double c) { return c * 165.0; } }
class CopToUsd  implements Conversor { public double convertir(double c) { return c * 0.00025; } }
class CopToEur  implements Conversor { public double convertir(double c) { return c * 0.00023; } }
class CopToJpy implements Conversor { public double convertir(double c) { return c * 0.0375; } }

class Resultado {
    String monedaDestino;
    double montoConvertido;

    public Resultado(String monedaDestino, double montoConvertido) {
        this.monedaDestino = monedaDestino;
        this.montoConvertido = montoConvertido;
    }
    public String getMonedaDestino() { return monedaDestino; }
    public double getMontoConvertido() { return montoConvertido; }
}

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        Map<String, Conversor> estrategias = new HashMap<>();
        estrategias.put("EUR-USD", new EuroToUsd());
        estrategias.put("EUR-JPY", new EuroToJpy());
        estrategias.put("COP-USD", new CopToUsd());
        estrategias.put("COP-EUR", new CopToEur());
        estrategias.put("COP-JPY", new CopToJpy());

        List<Resultado> todosLosResultados = new ArrayList<>();

        System.out.print("Ingrese número de transacciones: ");
        int n = sc.nextInt();

        for (int i = 1; i <= n; i++) {
            System.out.println("\n--- Transacción " + i + " ---");
            System.out.print("Ingrese monto: ");
            double monto = sc.nextDouble();
            System.out.print("Ingrese moneda de origen (EUR, COP, etc): ");
            String origen = sc.next().toUpperCase();
            System.out.print("Ingrese monedas destino (separadas por coma, ej: USD,JPY): ");
            String[] destinos = sc.next().toUpperCase().split(",");

            System.out.println("\nTransacción " + i + ": " + monto + " " + origen);
            
            for (String dest : destinos) {
                String llave = origen + "-" + dest;
                if (estrategias.containsKey(llave)) {
                    double resultado = estrategias.get(llave).convertir(monto);
                    System.out.printf("   Convertido a %s: %.2f %s\n", dest, resultado, dest);
                    todosLosResultados.add(new Resultado(dest, resultado));
                } else {
                    System.out.println("   [Error] No hay tasa para " + llave);
                }
            }
        }

        System.out.println("\n--- Totales por moneda ---");
        Map<String, Double> totales = todosLosResultados.stream()
            .collect(Collectors.groupingBy(
                Resultado::getMonedaDestino,
                Collectors.summingDouble(Resultado::getMontoConvertido)
            ));

        totales.forEach((moneda, total) -> 
            System.out.printf("%s: %.2f %s\n", moneda, total, moneda)
        );
        
        sc.close();
    }
}