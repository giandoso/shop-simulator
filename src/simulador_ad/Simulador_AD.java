/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador_ad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author João Pedro Giandoso Machado & Rafael Felipe dos Santos Machado
 */
public class Simulador_AD {

    // seed e funções auxiliares
    static long seed = 1605713891;
    static Random generator = new Random(seed);
//    static Random generator = new Random(System.currentTimeMillis());
    static boolean DEBUG = false;

    public static double aleatorio() {
        return 1.00 - generator.nextDouble();
    }

    public static double minimo(double a, double b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }
    
    public static void simulador(double qtd_caixas, double relacao_chegada_capacidade) throws IOException {
        double tempo = 0.0;
        double tempo_simulacao = 1200000;
        double fila = 0.0;
        
        Loja l = new Loja(qtd_caixas);
        double tempo_medio_atendimento = qtd_caixas / 60.0;
        
        
        double tempo_medio_clientes = relacao_chegada_capacidade * tempo_medio_atendimento;
        double chegada_cliente = (double) ((-1.0 / tempo_medio_clientes) * Math.log(aleatorio()));
        double tempo_inicial = chegada_cliente;
        double intervalo_plot = 600.0;

        double tempo_plot_o = chegada_cliente;
        double tempo_plot_en = chegada_cliente;
        double tempo_plot_ewE = chegada_cliente;
        double tempo_plot_ewS = chegada_cliente;

        // somar os tempos de atendimento, para no final calcularmos a ocupacao.        
        double soma_atendimentos = 0.0;

        // variaveis para o calculo de E[N] e E[W]
        Contador en = new Contador();
        Contador ewEntrada = new Contador();
        Contador ewSaida = new Contador();

        File f_o = new File("o" + qtd_caixas + "_" + relacao_chegada_capacidade + ".txt");
        File f_en = new File("en" + qtd_caixas + "_" + relacao_chegada_capacidade + ".txt");
        File f_ewE = new File("ewE" + qtd_caixas + "_" + relacao_chegada_capacidade + ".txt");
        File f_ewS = new File("ewS" + qtd_caixas + "_" + relacao_chegada_capacidade + ".txt");
        BufferedWriter bw_o = new BufferedWriter(new FileWriter(f_o));
        BufferedWriter bw_en = new BufferedWriter(new FileWriter(f_en));
        BufferedWriter bw_ewE = new BufferedWriter(new FileWriter(f_ewE));
        BufferedWriter bw_ewS = new BufferedWriter(new FileWriter(f_ewS));

        // logica da simulacao
        while (tempo <= tempo_simulacao) {
            // nao existe cliente sendo atendido no momento atual,
            // de modo que a simulacao pode avancar no tempo para
            // a chegada do proximo cliente
            if (l.hasFree()) {
                tempo = chegada_cliente;
            } else {
                // checa se chegada cliente é menor do que saida_atendimento 
                // de qualquer caixa da loja 
                double saida_atendimento = l.getHead();
                tempo = minimo(chegada_cliente, saida_atendimento);
            }

            if (tempo == chegada_cliente) {
                if (DEBUG) {
                    System.out.printf("Chegada de cliente: %f\n", chegada_cliente);
                }
                // evento de chegada de cliente 
                fila++;
                if (DEBUG) {
                    System.out.printf("fila: %f\n", fila);
                }

                // indica que o caixa esta ocioso
                // logo, pode-se comecar a atender
                // o cliente que acaba de chegar
                if (l.hasFree()) {
                    l.updateHead(tempo);
                }

                chegada_cliente = tempo + (-1.0 / tempo_medio_clientes) * Math.log(aleatorio());

                // calculo do E[N]
                en.somaAreas += en.numeroEventos * (tempo - en.tempoAnterior);
                en.tempoAnterior = tempo;
                en.numeroEventos++;
                // plot do E[N]
                if (tempo >= tempo_plot_en + intervalo_plot || tempo == tempo_inicial) {
                    bw_en.append(en.tempoAnterior + "\t" + en.somaAreas + "\t" + en.numeroEventos + "\n");
                    tempo_plot_en = tempo; // ou tempo_plot += 100 ?
                }

                // calculo do E[W]
                ewEntrada.somaAreas += ewEntrada.numeroEventos * (tempo - ewEntrada.tempoAnterior);
                ewEntrada.tempoAnterior = tempo;
                ewEntrada.numeroEventos++;
                // plot do E[W]
                if (tempo >= tempo_plot_ewE + intervalo_plot || tempo == tempo_inicial) {
                    bw_ewE.append(ewEntrada.tempoAnterior + "\t" + ewEntrada.somaAreas + "\t" + ewEntrada.numeroEventos + "\n");
                    tempo_plot_ewE = tempo; // ou tempo_plot += 100 ?
                }
            } else {
                // evento executado se houver saida de clien
                // ou ainda se houver chegada de cliente, maste
                // ou ainda se houver chegada de cliente, mas
                // o caixa estiver ocioso.
                // a cabeca da fila nao consiste no cliente em atendimento.
                // o cliente que comeca a ser atendido portanto, sai da fila,
                // e passa a estar ainda no comercio, mas em atendimento no caixa.

                // verifica se ha cliente na fila
                if (fila > 0.0) {
                    fila--;
                    if (DEBUG) {
                        System.out.printf("fila: %f\n", fila);
                    }

                    double tempo_atendimento = (-1.0 / tempo_medio_atendimento) * Math.log(aleatorio());
                    l.updateHead(tempo + tempo_atendimento);
                    soma_atendimentos += tempo_atendimento;
                    if (tempo >= tempo_plot_o + intervalo_plot || tempo == tempo_inicial) {
                        bw_o.append(tempo + "\t" + (soma_atendimentos / tempo) + "\n");
                        tempo_plot_o = tempo;
                    }

                    if (DEBUG) {
                        System.out.printf("saida de cliente: %f\n", l.getHead());
                    }
                } else {
                    l.updateHead(0.0);
                    if (DEBUG) {
                        System.out.println("Caixa ocioso até " + chegada_cliente);
                    }
                }
                
                if (en.tempoAnterior < tempo) {
                    // calculo do E[N]
                    en.somaAreas += en.numeroEventos * (tempo - en.tempoAnterior);
                    en.tempoAnterior = tempo;
                    en.numeroEventos--;
                    // plot do E[N]
                    if (tempo >= tempo_plot_en + intervalo_plot || tempo == tempo_inicial) {
                        bw_en.append(en.tempoAnterior + "\t" + en.somaAreas + "\t" + en.numeroEventos + "\n");
                        tempo_plot_en = tempo; // ou tempo_plot += 100 ?
                    }

                    // calculo do E[W]
                    ewSaida.somaAreas += ewSaida.numeroEventos * (tempo - ewSaida.tempoAnterior);
                    ewSaida.tempoAnterior = tempo;
                    ewSaida.numeroEventos++;
                    // plot do E[W]
                    if (tempo >= tempo_plot_ewS + intervalo_plot || tempo == tempo_inicial) {
                        bw_ewS.append(ewSaida.tempoAnterior + "\t" + ewSaida.somaAreas + "\t" +  ewSaida.numeroEventos + "\n");
                        tempo_plot_ewS = tempo; // ou tempo_plot += 100 ?
                    }
                }
            }

            if (DEBUG) {
                System.out.printf("==================\n");
            }
        }
        
        if (l.getHead() > tempo) {
            soma_atendimentos -= (l.getHead() - tempo);
        }

        //fazendo o calculo da ultima area dos graficos antes do termino da simulacao
        ewSaida.somaAreas += ewSaida.numeroEventos * (tempo - ewSaida.tempoAnterior);
        ewEntrada.somaAreas += ewEntrada.numeroEventos * (tempo - ewEntrada.tempoAnterior);

        double enF = en.somaAreas / tempo;
        double ew = (ewEntrada.somaAreas - ewSaida.somaAreas) / ewEntrada.numeroEventos;
        double lambda = ewEntrada.numeroEventos / tempo;
        double ocupacao = soma_atendimentos / tempo;

        System.out.printf("Ocupacao: %.2f porcento\n", ocupacao * 100.0);
        System.out.println("E[N]: " + enF);
        System.out.println("E[W]: " + ew);
        System.out.println("Lambda: " + lambda);
        //Little --> en = lambda * ew
        //Little --> en - lambda * ew ~ 0.0
        System.out.printf("Little: |%.13f - (%.13f * %.13f)| = %.13f\n", enF , lambda , ew, (Math.abs(enF - lambda * ew)));
        System.out.println("--------------------------------------------------------------");
        System.out.println("");
//        System.out.printf("Validação de Little: %.20f\n", (Math.abs(enF - lambda * ew)));

        
        bw_o.close();
        bw_en.close();
        bw_ewE.close();
        bw_ewS.close();
    }

    /**
     * Simulador de um caixa onde clientes chegam e sao atendidos no modelo
     * FIFO. O tempo entre a chegada de clientes, bem como o tempo de
     * atendimento devem ser gerados de maneira pseudoaleatoria atraves da v.a.
     * exponencial.
     *
     * Utilizacao ou Ocupacao = fracao de tempo que o caixa permanecera ocupado.
     * Little: E[N] - lambda * E[w]
     */
    public static void main(String[] args) throws IOException {
        System.out.println("SIMULAÇÃO DE 5 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.4: ");
        simulador(5.0, 0.4);
        System.out.println("SIMULAÇÃO DE 5 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.8: ");
        simulador(5.0, 0.8);
        System.out.println("SIMULAÇÃO DE 5 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.9: ");
        simulador(5.0, 0.9);
        System.out.println("SIMULAÇÃO DE 5 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.99: ");
        simulador(5.0, 0.99);
        
        System.out.println("SIMULAÇÃO DE 10 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.4: ");
        simulador(10.0, 0.4);
        System.out.println("SIMULAÇÃO DE 10 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.8: ");
        simulador(10.0, 0.8);
        System.out.println("SIMULAÇÃO DE 10 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.9: ");
        simulador(10.0, 0.9);
        System.out.println("SIMULAÇÃO DE 10 CAIXAS COM RELAÇÃO ENTRE A CHEGADA E CAPACIDADE TOTAL DE ATENDIMENTO IGUAL A 0.99: ");
        simulador(10.0, 0.99);
        
        
    }
}
