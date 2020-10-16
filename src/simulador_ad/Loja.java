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
    Queue<Caixa> caixas = new PriorityQueue<>(new CaixaComparator());
    public int qtd_caixas;


    public Loja(double qtd_caixas) {
        this.qtd_caixas = (int) qtd_caixas;
        for (int i = 0; i < this.qtd_caixas; i++) {
            this.caixas.add(new Caixa(i));
        }
    }

    /* Essa função deve retornar verdade se existe algum
     * caixa com saida_atendimento == 0.0
     * Complexidade: O[1]
     */
    public boolean hasFree() {
        if (this.caixas.peek().saida_atendimento == 0.0) {
            return true;
        }
        return false;
    }

    /* Essa função deve retornar algum caixa 'livre'
     * ou seja, qualquer caixa com saida_atendimento == 0.0
     * Complexidade: O[1]
     */
    Caixa getHead() {
        return this.caixas.peek();
    }

    /* Essa função deve atualizar a saida_atendimento
     * do caixa na cabeça da estruturas
     * Complexidade: O[2*log(n)]
     */
    void updateHead(double tempo) {
//        this.head.saida_atendimento = tempo;
//        this.getNextHead();
        Caixa atual = this.caixas.poll();
        atual.saida_atendimento = tempo;
        this.caixas.offer(atual);
    }
}
