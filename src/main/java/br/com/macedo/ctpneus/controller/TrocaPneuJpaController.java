/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.macedo.ctpneus.controller;

import br.com.macedo.ctpneus.controller.exceptions.NonexistentEntityException;
import br.com.macedo.ctpneus.controller.exceptions.RollbackFailureException;
import br.com.macedo.ctpneus.entity.TrocaPneu;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Obras
 */
public class TrocaPneuJpaController implements Serializable {

    public TrocaPneuJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TrocaPneu trocaPneu) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(trocaPneu);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TrocaPneu trocaPneu) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            trocaPneu = em.merge(trocaPneu);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = trocaPneu.getId();
                if (findTrocaPneu(id) == null) {
                    throw new NonexistentEntityException("The trocaPneu with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TrocaPneu trocaPneu;
            try {
                trocaPneu = em.getReference(TrocaPneu.class, id);
                trocaPneu.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trocaPneu with id " + id + " no longer exists.", enfe);
            }
            em.remove(trocaPneu);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TrocaPneu> findTrocaPneuEntities() {
        return findTrocaPneuEntities(true, -1, -1);
    }

    public List<TrocaPneu> findTrocaPneuEntities(int maxResults, int firstResult) {
        return findTrocaPneuEntities(false, maxResults, firstResult);
    }

    private List<TrocaPneu> findTrocaPneuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TrocaPneu.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TrocaPneu findTrocaPneu(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TrocaPneu.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrocaPneuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TrocaPneu> rt = cq.from(TrocaPneu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
