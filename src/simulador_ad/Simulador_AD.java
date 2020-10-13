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
    static Random generator = new Random(seed);
//    static Random generator = new Random(System.currentTimeMillis());
    static boolean DEBUG = true;

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
        double tempo_simulacao = 200;
        double fila = 0.0;
        double qtd_caixas = 2.0;
        Loja l = new Loja(qtd_caixas);
        double tempo_medio_atendimento = qtd_caixas / 60.0;
        double relacao_chegada_capacidade = 0.4;
        double tempo_medio_clientes = relacao_chegada_capacidade * tempo_medio_atendimento;
        double chegada_cliente = (double) ((-1.0 / tempo_medio_clientes) * Math.log(aleatorio()));

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
                double saida_atendimento = l.getMin();
                tempo = minimo(chegada_cliente, saida_atendimento);
            }

            if (tempo == chegada_cliente) {
                if (DEBUG)
                    System.out.printf("Chegada de cliente: %f\n", chegada_cliente);
                // evento de chegada de cliente 
                fila++;
                if (DEBUG)
                    System.out.printf("fila: %f\n", fila);

                // indica que o caixa esta ocioso
                // logo, pode-se comecar a atender
                // o cliente que acaba de chegar
                if (l.hasFree()) {
                    l.updateHead(tempo);
                }

                chegada_cliente = tempo + (-1.0 / tempo_medio_clientes) * Math.log(aleatorio());
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
                    if (DEBUG)
                        System.out.printf("fila: %f\n", fila);

                    l.head.saida_atendimento += (-1.0 / tempo_medio_atendimento) * Math.log(aleatorio());
                    
                    if (DEBUG)
                        System.out.printf("saida de cliente: %f\n", l.head.saida_atendimento);
                } else {
                    l.head.saida_atendimento = 0.0;
                    if (DEBUG)
                        System.out.println("Caixa "+ l.head.label +" ocioso até "+chegada_cliente);
                }
            }
            
            if (DEBUG)
                System.out.printf("==================\n");
        }
    }
}
