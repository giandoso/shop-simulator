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
    private Caixa[] caixas;
    private int qtd_caixas;

    public Loja(double qtd_caixas) {
        this.qtd_caixas = (int)this.qtd_caixas;
        this.caixas = new Caixa[this.qtd_caixas];
    }
    
    
    public boolean hasFree(){
        for (int i = 0; i < this.qtd_caixas; i++) {
            if(this.caixas[i].getSaida_atendimento() == 0.0){
                return true;
            }
        }
        return false;       
    }

    double getMin() {
        double localMin = Double.MAX_VALUE;
        for (int i = 0; i < this.qtd_caixas; i++) {
            if(localMin > this.caixas[i].getSaida_atendimento()){
               localMin = this.caixas[i].getSaida_atendimento();
            }
        }
        return localMin;
    }

    Caixa getFree() {
        for (int i = 0; i < this.qtd_caixas; i++) {
            if(this.caixas[i].getSaida_atendimento() == 0.0){
                return this.caixas[i];
            }
        }
        return null; // nunca chega aqui
    }
}
