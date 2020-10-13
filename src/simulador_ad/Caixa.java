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
public class Caixa {
    double saida_atendimento;
    int label;

    public Caixa(int label) {
        this.saida_atendimento = 0.0;
        this.label = label + 1;
    }
}
