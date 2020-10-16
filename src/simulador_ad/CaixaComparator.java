/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador_ad;

import java.util.Comparator;

/**
 *
 * @author fael
 */
public class CaixaComparator implements Comparator<Caixa> {

    @Override
    public int compare(Caixa c1, Caixa c2) {
        return c1.saida_atendimento > c2.saida_atendimento ? 1 : -1;
    }
}
