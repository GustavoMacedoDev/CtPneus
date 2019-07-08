/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.macedo.ctpneus.controller;

import br.com.macedo.ctpneus.entity.TrocaPneu;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Obras
 */
@Stateless
public class TrocaPneuFacade extends AbstractFacade<TrocaPneu> {

    @PersistenceContext(unitName = "br.com.macedo_CtPneus_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrocaPneuFacade() {
        super(TrocaPneu.class);
    }
    
}
