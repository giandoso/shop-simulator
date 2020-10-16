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
    Queue<Caixa> caixasOciosos = new PriorityQueue<>(new CaixaComparator());
    Queue<Caixa> caixasOcupados = new PriorityQueue<>(new CaixaComparator());
    public int qtd_caixas;

    public Loja(double qtd_caixas) {
        this.qtd_caixas = (int) qtd_caixas;
        for (int i = 0; i < this.qtd_caixas; i++) {
            this.caixasOciosos.add(new Caixa(i));
        }
    }

    /* Essa função deve retornar verdade se existe algum
     * caixa com saida_atendimento == 0.0
     * Complexidade: O[1]
     */
    public boolean hasFree() {
        if (!this.caixasOciosos.isEmpty()) {
            return true;
        }
        return false;
    }

    /* Essa função deve retornar o caixa ocupado que ficará livre primeiro
     * Complexidade: O[1]
     */
    Caixa getHeadOcupado() {
        return this.caixasOcupados.peek();
    }
    
    /* Essa função deve retornar algum caixa ocioso
     * Complexidade: O[1]
     */
    Caixa getHeadOcioso() {
        return this.caixasOciosos.peek();
    }
    
    /* Essa função deve retornar algum caixa ocioso
     * Complexidade: O[1]
     */
    Caixa getHead() {
        if (!this.caixasOciosos.isEmpty()) {
            return this.caixasOciosos.peek();
        }
        return this.caixasOcupados.peek();
    }

    /* Essa função deve atualizar a saida_atendimento
     * do caixa na cabeça da estruturas
     * Complexidade: O[2*log(n)]
     */
    void updateHead(double tempo) {
//        this.head.saida_atendimento = tempo;
//        this.getNextHead();
        if (tempo == 0.0) {
            Caixa atual = this.caixasOcupados.poll();
            atual.saida_atendimento = tempo;
            this.caixasOciosos.offer(atual);
        } else {
            if (!this.caixasOciosos.isEmpty()) {
                Caixa atual = this.caixasOciosos.poll();
                atual.saida_atendimento = tempo;
                this.caixasOcupados.offer(atual);
            } else {
                Caixa atual = this.caixasOcupados.poll();
                atual.saida_atendimento = tempo;
                this.caixasOcupados.offer(atual);
            }
        }
    }
}
