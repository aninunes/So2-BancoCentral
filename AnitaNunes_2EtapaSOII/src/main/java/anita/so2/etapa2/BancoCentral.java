/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anita.so2.etapa2;

/**
 *
 * @author anita
 */
import java.util.ArrayList;
import java.util.Scanner;
import anita.so2.etapa2.Pessoa;
import anita.so2.etapa2.Conta;

public class BancoCentral {

    static Scanner input = new Scanner(System.in);
    static ArrayList<Conta> contasBancarias;

    public static void main(String[] args) {
        contasBancarias = new ArrayList<Conta>();
        operacoes();
    }

    public static void operacoes() {

        int user = input.nextInt();;
        System.out.println("-------------Bem vindos ao Banco Central--------------");
        System.out.println("***** Digite /n1 para ADM  /n2 para cliente *****");

        switch (user) {
            case 1:

                System.out.println("-------------Bem vindos ao Banco Central---------------");

                System.out.println("|   Opção 1 - Criar conta   |");
                System.out.println("|   Opção 2 - Listar        |");
                System.out.println("|   Opção 3 - Sair          |");

                int operacao = input.nextInt();
                ;

                switch (operacao) {
                    case 1:
                        criarConta();
                        break;

                    case 2:
                        listarContas();
                        break;

                    case 3:
                        System.out.println("Volte Sempre!");
                        System.exit(0);

                    default:
                        System.out.println("Opção inválida!");
                        operacoes();
                        break;
                }
                break;

            case 2:
                System.out.println("-------------Bem vindos ao Banco Central---------------");

                System.out.println("|   Opção 1 - Saque          |");
                System.out.println("|   Opção 2 - Deposito       |");
                System.out.println("|   Opção 3 - Transferir     |");

                int operacao2 = input.nextInt();
                ;

                switch (operacao2) {
                    case 1:
                        sacar();
                        break;

                    case 2:
                        depositar();
                        break;
                    case 3:
                        transferir();
                    default:
                        System.out.println("Opção inválida!");
                        operacoes();
                        break;

                }
                break;

            default:
                System.out.println("Opção inválida!");
                operacoes();
                break;

        }

    }

    public static void criarConta() {
        //System.out.println("Você está criando uma conta\n");

        System.out.println("\nNome: ");
        String nome = input.next();

        System.out.println("\nCPF: ");
        String cpf = input.next();
        System.out.println("\nAgencia: ");
        String agencia = input.next();
        System.out.println("Email: ");
        String email = input.next();

        Pessoa cliente = new Pessoa(nome, cpf, agencia, email);

        Conta conta = new Conta(cliente);

        contasBancarias.add(conta);
        System.out.println("--- Sua conta foi criada com sucesso! ---");

        operacoes();

    }

    private static Conta encontrarConta(int numeroConta) {
        Conta conta = null;
        if (contasBancarias.size() > 0) {
            for (Conta contaa : contasBancarias) {
                if (contaa.getNumeroConta() == numeroConta) {
                    conta = contaa;
                }
            }
        }
        return conta;
    }

    public static void depositar() {
        System.out.println("Número da conta: ");
        int numeroConta = input.nextInt();
        Conta conta = encontrarConta(numeroConta);

        if (conta != null) {
            System.out.println("Qual valor deseja depositar? ");
            Double valorDeposito = input.nextDouble();

            conta.depositar(valorDeposito);
        } else {
            System.out.println("--- Conta não encontrada ---");
        }

        operacoes();

    }

    public static void sacar() {
        System.out.println("Número da conta: ");
        int numeroConta = input.nextInt();

        Conta conta = encontrarConta(numeroConta);

        if (conta != null) {
            System.out.println("Qual valor deseja sacar? ");
            Double valorSaque = input.nextDouble();

            conta.sacar(valorSaque);
            System.out.println("--- Saque realizado com sucesso! ---");
        } else {
            System.out.println("--- Conta não encontrada ---");
        }

        operacoes();

    }

    public static void transferir() {
        System.out.println("Número da conta que vai enviar a transferência: ");
        int numeroContaRemetente = input.nextInt();

        Conta contaRemetente = encontrarConta(numeroContaRemetente);

        if (contaRemetente != null) {
            System.out.println("Número da conta do destinatário: ");
            int numeroContaDestinatario = input.nextInt();

            Conta contaDestinatario = encontrarConta(numeroContaDestinatario);

            if (contaDestinatario != null) {
                System.out.println("Valor da transferência: ");
                Double valor = input.nextDouble();

                contaRemetente.transferencia(contaDestinatario, valor);

            } else {
                System.out.println("--- A conta para depósito não foi encontrada ---");
            }

        } else {
            System.out.println("--- Conta para transferência não encontrada ---");
        }
        operacoes();
    }

    public static void listarContas() {
        if (contasBancarias.size() > 0) {
            for (Conta conta : contasBancarias) {
                System.out.println(conta);
            }
        } else {
            System.out.println("--- Não há contas cadastradas ---");
        }

        operacoes();
    }
}
