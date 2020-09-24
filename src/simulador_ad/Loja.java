/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador_ad;

/**
 *
 * @author giandoso
 */
public class Loja {

    public Caixa[] caixas;
    public int qtd_caixas;
    
    /* A head deve ser o caixa com menor tempo de saida_atendimento
     * ps: 0.0 significa caixa livre
    */
    Caixa head;

    public Loja(double qtd_caixas) {
        this.qtd_caixas = (int) this.qtd_caixas;
        this.caixas = new Caixa[this.qtd_caixas];
    }

    /* Essa função deve retornar verdade se existe algum
     * caixa com saida_atendimento == 0.0
     * TODO: melhor complexidade 
     */
    public boolean hasFree() {
        for (int i = 0; i < this.qtd_caixas; i++) {
            if (this.caixas[i].saida_atendimento == 0.0) {
                return true;
            }
        }
        return false;
    }

    /* Essa função retorna o valor de saida_atendimento do 
     * caixa com o menor valor
     * TODO: melhor complexidade 
     */
    double getMin() {
        double localMin = Double.MAX_VALUE;
        for (int i = 0; i < this.qtd_caixas; i++) {
            if (localMin > this.caixas[i].saida_atendimento) {
                localMin = this.caixas[i].saida_atendimento;
            }
        }
        return localMin;
    }

    /* Essa função deve retornar algum caixa 'livre'
     * ou seja, qualquer caixa com saida_atendimento == 0.0
     * TODO melhorar complexidade
     */
    Caixa getFree() {
        for (int i = 0; i < this.qtd_caixas; i++) {
            if (this.caixas[i].saida_atendimento == 0.0) {
                return this.caixas[i];
            }
        }
        return null; // nunca chega aqui
    }
    
    /* Essa função seta a head como quem tem o menor tempo 
     * de saida_atendimento diferente de 0. 
     * TODO: melhor complexidade 
     */
    void update_head() {
        double localMin = Double.MAX_VALUE;
        for (int i = 0; i < this.qtd_caixas; i++) {
            Caixa atual = this.caixas[i];
            if (localMin > atual.saida_atendimento && atual.saida_atendimento != 0.0) {
                localMin = atual.saida_atendimento;
                this.head = atual;  
            }                                                
        }
    }
}
