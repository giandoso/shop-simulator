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
    static long seed = 123456789;
//    static Random generator = new Random(seed);
    static Random generator = new Random(System.currentTimeMillis());

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
        double tempo = 0.0;
        double tempo_simulacao = 1000000;
        double tempo_medio_clientes = 1.0 / 0.5; // o tempo medio entre a chegada de clientes (segundos)
        
        /**
         * O tempo medio gasto para atender cada pessoa (segundos)
         * Ocupacao = TMA / TMC
         * TMA = Ocupacao * TMC
         */
        
//        double tempo_medio_atendimento = 1.0 / 0.2;   //  Ocupação == 40%
//        double tempo_medio_atendimento = 1.0 / 0.4;   //  Ocupação == 80%
//        double tempo_medio_atendimento = 1.0 / 0.45;  //  Ocupação == 90%
        double tempo_medio_atendimento = 1.0 / 0.499;  //  Ocupação == 99%

        double chegada_cliente = (double) ((-1.0/tempo_medio_clientes) * Math.log(aleatorio()));
        double tempo_inicial = chegada_cliente;
        
        double tempo_plot_en = chegada_cliente;
        double tempo_plot_ewE = chegada_cliente;
        double tempo_plot_ewS = chegada_cliente;

        // armazena o tempo em que o cliente que estiver em atendimento saira do comercio
        // saida_atendimento == 0.0 indica caixa ocioso
        double saida_atendimento = 0.0;

        double fila = 0.0;

        // somar os tempos de atendimento, para no final calcularmos a ocupacao.        
        double soma_atendimentos = 0.0;

        // variaveis para o calculo de E[N] e E[W]
        Pacote en = new Pacote();
        Pacote ewEntrada = new Pacote();
        Pacote ewSaida = new Pacote();

        
        File f_o = new File("o.txt");
        File f_en = new File("en.txt");
        File f_ewE = new File("ewE.txt");
        File f_ewS = new File("ewS.txt");
        BufferedWriter bw_o = new BufferedWriter(new FileWriter(f_en));
        BufferedWriter bw_en = new BufferedWriter(new FileWriter(f_en));
        BufferedWriter bw_ewE = new BufferedWriter(new FileWriter(f_ewE));
        BufferedWriter bw_ewS = new BufferedWriter(new FileWriter(f_ewS));

        // logica da simulacao
        while (tempo <= tempo_simulacao) {
            // nao existe cliente sendo atendido no momento atual,
            // de modo que a simulacao pode avancar no tempo para
            // a chegada do proximo cliente
            if (saida_atendimento == 0.0) {
                tempo = chegada_cliente;
            } else {
                tempo = minimo(chegada_cliente, saida_atendimento);
            }

            if (tempo == chegada_cliente) {
                // evento de chegada de cliente
                fila++;

                // indica que o caixa esta ocioso
                // logo, pode-se comecar a atender
                // o cliente que acaba de chegar
                if (saida_atendimento == 0.0) {
                    saida_atendimento = tempo;
                }

                chegada_cliente = tempo + (-1.0/tempo_medio_clientes) * Math.log(aleatorio());

                // calculo do E[N]
                en.somaAreas += en.numeroEventos * (tempo - en.tempoAnterior);
                en.tempoAnterior = tempo;
                en.numeroEventos++;
                // plot do E[N]
                if (tempo >= tempo_plot_en + 100.0 || tempo == tempo_inicial) {
                    bw_en.append(en.tempoAnterior + "\t" + en.somaAreas + "\t" + en.numeroEventos + "\n");
                    tempo_plot_en = tempo; // ou tempo_plot += 100 ?
                }
                
                // calculo do E[W]
                ewEntrada.somaAreas += ewEntrada.numeroEventos * (tempo - ewEntrada.tempoAnterior);
                ewEntrada.tempoAnterior = tempo;
                ewEntrada.numeroEventos++;
                // plot do E[W]
                if (tempo >= tempo_plot_ewE + 100.0 || tempo == tempo_inicial) {
                    bw_ewE.append(ewEntrada.tempoAnterior + "\t" + ewEntrada.somaAreas + "\t" + ewEntrada.numeroEventos + "\n");
                    tempo_plot_ewE = tempo; // ou tempo_plot += 100 ?
                }

            } else {
                // evento executado se houver saida de cliente
                // ou ainda se houver chegada de cliente, mas
                // o caixa estiver ocioso.
                // a cabeca da fila nao consiste no cliente em atendimento.
                // o cliente que comeca a ser atendido portanto, sai da fila,
                // e passa a estar ainda no comercio, mas em atendimento no caixa.

                // verifica se ha cliente na fila
                if (fila > 0.0) {
                    fila--;

                    double tempo_atendimento = (-1.0 / tempo_medio_atendimento) * Math.log(aleatorio());
                    saida_atendimento = tempo + tempo_atendimento;
                    soma_atendimentos += tempo_atendimento;
                } else {
                    saida_atendimento = 0.0;
                }

                if (en.tempoAnterior < tempo) {
                    // calculo do E[N]
                    en.somaAreas += en.numeroEventos * (tempo - en.tempoAnterior);
                    en.tempoAnterior = tempo;
                    en.numeroEventos--;
                    // plot do E[N]
                    if (tempo >= tempo_plot_en + 100.0 || tempo == tempo_inicial) {
                        bw_en.append(en.tempoAnterior + "\t" + en.somaAreas + "\t" + en.numeroEventos + "\n");
                        tempo_plot_en = tempo; // ou tempo_plot += 100 ?
                    }

                    // calculo do E[W]
                    ewSaida.somaAreas += ewSaida.numeroEventos * (tempo - ewSaida.tempoAnterior);
                    ewSaida.tempoAnterior = tempo;
                    ewSaida.numeroEventos++;
                    // plot do E[W]
                    if (tempo >= tempo_plot_ewS + 100.0 || tempo == tempo_inicial) {
                        bw_ewS.append(ewSaida.tempoAnterior + "\t" + ewSaida.somaAreas + "\t" +  ewSaida.numeroEventos + "\n");
                        tempo_plot_ewS = tempo; // ou tempo_plot += 100 ?
                    }
                }
            }
        }

        if (saida_atendimento > tempo) {
            soma_atendimentos -= (saida_atendimento - tempo);
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
        System.out.printf("Validação de Little: %.20f\n", (Math.abs(enF - lambda * ew)));

        
        bw_o.close();
        bw_en.close();
        bw_ewE.close();
        bw_ewS.close();
    }
}
