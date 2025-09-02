import java.util.Scanner;

class Carro {
    private String placa;
    private String horaEntrada;
    private String horaSaida;
    private double valor;

    public Carro(String placa, String horaEntrada) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
        this.horaSaida = "";
        this.valor = 0.0;
    }

    public String getPlaca() {
        return placa;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public double getValor() {
        return valor;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public void calcularValor() {
        try {
            int horaE = Integer.parseInt(horaEntrada.split(":")[0]);
            int minE = Integer.parseInt(horaEntrada.split(":")[1]);
            int horaS = Integer.parseInt(horaSaida.split(":")[0]);
            int minS = Integer.parseInt(horaSaida.split(":")[1]);

            int minutosEntrada = horaE * 60 + minE;
            int minutosSaida = horaS * 60 + minS;

            int tempo = minutosSaida - minutosEntrada;
            if (tempo <= 0) tempo = 1; // garante valor mínimo

            // Exemplo: R$ 5 por hora, arredondando pra cima
            this.valor = Math.ceil(tempo / 60.0) * 5.0;
        } catch (Exception e) {
            this.valor = 0;
        }
    }

    @Override
    public String toString() {
        return "Placa: " + placa +
               " | Entrada: " + horaEntrada +
               (horaSaida.isEmpty() ? "" : " | Saida: " + horaSaida) +
               (valor > 0 ? " | Valor: R$" + valor : "");
    }
}

public class Garagem {
    private static final int TAM = 5;
    private static Carro[] vagas = new Carro[TAM];
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao = 0;

        while (opcao != 6) {
            System.out.println("\n=== MENU GARAGEM ===");
            System.out.println("1 - Entrada de carro");
            System.out.println("2 - Saida de carro");
            System.out.println("3 - Listar carros");
            System.out.println("4 - Procurar carro por placa");
            System.out.println("5 - Mostrar vagas livres/ocupadas");
            System.out.println("6 - Sair");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer

            switch (opcao) {
                case 1: entradaCarro(); break;
                case 2: saidaCarro(); break;
                case 3: listarCarros(); break;
                case 4: procurarCarro(); break;
                case 5: mostrarVagas(); break;
                case 6: System.out.println("Encerrando o sistema..."); break;
                default: System.out.println("Opcao invalida!");
            }
        }
    }

    private static void entradaCarro() {
        for (int i = 0; i < TAM; i++) {
            if (vagas[i] == null) {
                System.out.print("Placa: ");
                String placa = sc.nextLine();

                // verifica duplicidade
                if (buscarCarro(placa) != -1) {
                    System.out.println("Carro ja esta na garagem!");
                    return;
                }

                System.out.print("Hora de entrada (HH:MM): ");
                String hora = sc.nextLine();
                vagas[i] = new Carro(placa, hora);
                System.out.println("Carro estacionado na vaga " + (i + 1));
                return;
            }
        }
        System.out.println("Nao ha vagas disponiveis!");
    }

    private static void saidaCarro() {
        System.out.print("Placa do carro para sair: ");
        String placa = sc.nextLine();

        int pos = buscarCarro(placa);
        if (pos == -1) {
            System.out.println("Carro nao encontrado!");
            return;
        }

        System.out.print("Hora de saida (HH:MM): ");
        String hora = sc.nextLine();

        vagas[pos].setHoraSaida(hora);
        vagas[pos].calcularValor();

        System.out.println("=== Ticket de Saida ===");
        System.out.println(vagas[pos]);

        vagas[pos] = null; // libera vaga
    }

    private static void listarCarros() {
        boolean vazio = true;
        for (int i = 0; i < TAM; i++) {
            if (vagas[i] != null) {
                System.out.println("Vaga " + (i + 1) + ": " + vagas[i]);
                vazio = false;
            }
        }
        if (vazio) System.out.println("Garagem vazia.");
    }

    private static void procurarCarro() {
        System.out.print("Digite a placa: ");
        String placa = sc.nextLine();
        int pos = buscarCarro(placa);
        if (pos == -1) {
            System.out.println("Carro nao encontrado.");
        } else {
            System.out.println("Carro encontrado na vaga " + (pos + 1) + ": " + vagas[pos]);
        }
    }

    private static void mostrarVagas() {
        int livres = 0, ocupadas = 0;
        for (int i = 0; i < TAM; i++) {
            if (vagas[i] == null) livres++; else ocupadas++;
        }
        System.out.println("Vagas livres: " + livres + " | Ocupadas: " + ocupadas);
    }

    private static int buscarCarro(String placa) {
        for (int i = 0; i < TAM; i++) {
            if (vagas[i] != null && vagas[i].getPlaca().equalsIgnoreCase(placa)) {
                return i;
            }
        }
        return -1;
    }
}
