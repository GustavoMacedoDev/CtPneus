/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.macedo.ctpneus.controller;

import br.com.macedo.ctpneus.entity.Veiculo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Obras
 */
@Stateless
public class VeiculoFacade extends AbstractFacade<Veiculo> {

    @PersistenceContext(unitName = "br.com.macedo_CtPneus_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VeiculoFacade() {
        super(Veiculo.class);
    }
    
}
