/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador_ad;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author giandoso
 */
public class Loja {

//    public Caixa[] caixas;
    Queue<Double> caixas = new PriorityQueue<>();
    public int qtd_caixas;


    public Loja(double qtd_caixas) {
        this.qtd_caixas = (int) qtd_caixas;
        for (int i = 0; i < this.qtd_caixas; i++) {
            this.caixas.add(0.0);
        }
    }

    /* Essa função deve retornar verdade se existe algum
     * caixa com saida_atendimento == 0.0
     * Complexidade: O[1]
     */
    public boolean hasFree() {
        if (this.caixas.peek() == 0.0) {
            return true;
        }
        return false;
    }

    /* Essa função deve retornar algum caixa 'livre'
     * ou seja, qualquer caixa com saida_atendimento == 0.0
     * Complexidade: O[1]
     */
    Double getHead() {
        return this.caixas.peek();
    }

    /* Essa função deve atualizar a saida_atendimento
     * do caixa na cabeça da estruturas
     * Complexidade: O[2*log(n)]
     */
    void updateHead(double tempo) {
//        this.head.saida_atendimento = tempo;
//        this.getNextHead();
        Double atual = this.caixas.poll();
        atual = tempo;
        this.caixas.offer(atual);
    }
}
