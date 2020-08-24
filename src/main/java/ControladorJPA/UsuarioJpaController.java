/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladorJPA;

import ControladorJPA.exceptions.IllegalOrphanException;
import ControladorJPA.exceptions.NonexistentEntityException;
import ControladorJPA.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Factura;
import Entidades.Usuarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Andre
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController() {
        
        this.emf = Persistence.createEntityManagerFactory("my_persistence_unit"); 
        em = emf.createEntityManager();
    }   
    
    private EntityManager em = null;
    
    private EntityManagerFactory emf = null;

  

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean create(Usuarios usuario) throws RollbackFailureException, Exception {
        if (usuario.getFacturaCollection() == null) {
            usuario.setFacturaCollection(new ArrayList<Factura>());
        }
        if (usuario.getFacturaCollection1() == null) {
            usuario.setFacturaCollection1(new ArrayList<Factura>());
        }
        
        
        try {
            em.getTransaction().begin();
            
            
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : usuario.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdfactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            usuario.setFacturaCollection(attachedFacturaCollection);
            Collection<Factura> attachedFacturaCollection1 = new ArrayList<Factura>();
            for (Factura facturaCollection1FacturaToAttach : usuario.getFacturaCollection1()) {
                facturaCollection1FacturaToAttach = em.getReference(facturaCollection1FacturaToAttach.getClass(), facturaCollection1FacturaToAttach.getIdfactura());
                attachedFacturaCollection1.add(facturaCollection1FacturaToAttach);
            }
            usuario.setFacturaCollection1(attachedFacturaCollection1);
            em.persist(usuario);
            for (Factura facturaCollectionFactura : usuario.getFacturaCollection()) {
                Usuarios oldIdUsuariovendedorOfFacturaCollectionFactura = facturaCollectionFactura.getIdUsuariovendedor();
                facturaCollectionFactura.setIdUsuariovendedor(usuario);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldIdUsuariovendedorOfFacturaCollectionFactura != null) {
                    oldIdUsuariovendedorOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldIdUsuariovendedorOfFacturaCollectionFactura = em.merge(oldIdUsuariovendedorOfFacturaCollectionFactura);
                }
            }
            for (Factura facturaCollection1Factura : usuario.getFacturaCollection1()) {
                Usuarios oldIdUsuariocompradorOfFacturaCollection1Factura = facturaCollection1Factura.getIdUsuariocomprador();
                facturaCollection1Factura.setIdUsuariocomprador(usuario);
                facturaCollection1Factura = em.merge(facturaCollection1Factura);
                if (oldIdUsuariocompradorOfFacturaCollection1Factura != null) {
                    oldIdUsuariocompradorOfFacturaCollection1Factura.getFacturaCollection1().remove(facturaCollection1Factura);
                    oldIdUsuariocompradorOfFacturaCollection1Factura = em.merge(oldIdUsuariocompradorOfFacturaCollection1Factura);
                }
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
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

    public void edit(Usuarios usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em.getTransaction().begin();
            
            Usuarios persistentUsuario = em.find(Usuarios.class, usuario.getIdusuario());
            Collection<Factura> facturaCollectionOld = persistentUsuario.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = usuario.getFacturaCollection();
            Collection<Factura> facturaCollection1Old = persistentUsuario.getFacturaCollection1();
            Collection<Factura> facturaCollection1New = usuario.getFacturaCollection1();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its idUsuariovendedor field is not nullable.");
                }
            }
            for (Factura facturaCollection1OldFactura : facturaCollection1Old) {
                if (!facturaCollection1New.contains(facturaCollection1OldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollection1OldFactura + " since its idUsuariocomprador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdfactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            usuario.setFacturaCollection(facturaCollectionNew);
            Collection<Factura> attachedFacturaCollection1New = new ArrayList<Factura>();
            for (Factura facturaCollection1NewFacturaToAttach : facturaCollection1New) {
                facturaCollection1NewFacturaToAttach = em.getReference(facturaCollection1NewFacturaToAttach.getClass(), facturaCollection1NewFacturaToAttach.getIdfactura());
                attachedFacturaCollection1New.add(facturaCollection1NewFacturaToAttach);
            }
            facturaCollection1New = attachedFacturaCollection1New;
            usuario.setFacturaCollection1(facturaCollection1New);
            usuario = em.merge(usuario);
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Usuarios oldIdUsuariovendedorOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getIdUsuariovendedor();
                    facturaCollectionNewFactura.setIdUsuariovendedor(usuario);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldIdUsuariovendedorOfFacturaCollectionNewFactura != null && !oldIdUsuariovendedorOfFacturaCollectionNewFactura.equals(usuario)) {
                        oldIdUsuariovendedorOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldIdUsuariovendedorOfFacturaCollectionNewFactura = em.merge(oldIdUsuariovendedorOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (Factura facturaCollection1NewFactura : facturaCollection1New) {
                if (!facturaCollection1Old.contains(facturaCollection1NewFactura)) {
                    Usuarios oldIdUsuariocompradorOfFacturaCollection1NewFactura = facturaCollection1NewFactura.getIdUsuariocomprador();
                    facturaCollection1NewFactura.setIdUsuariocomprador(usuario);
                    facturaCollection1NewFactura = em.merge(facturaCollection1NewFactura);
                    if (oldIdUsuariocompradorOfFacturaCollection1NewFactura != null && !oldIdUsuariocompradorOfFacturaCollection1NewFactura.equals(usuario)) {
                        oldIdUsuariocompradorOfFacturaCollection1NewFactura.getFacturaCollection1().remove(facturaCollection1NewFactura);
                        oldIdUsuariocompradorOfFacturaCollection1NewFactura = em.merge(oldIdUsuariocompradorOfFacturaCollection1NewFactura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdusuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em.getTransaction().begin();
            
            Usuarios usuario;
            try {
                usuario = em.getReference(Usuarios.class, id);
                usuario.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Factura> facturaCollectionOrphanCheck = usuario.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable idUsuariovendedor field.");
            }
            Collection<Factura> facturaCollection1OrphanCheck = usuario.getFacturaCollection1();
            for (Factura facturaCollection1OrphanCheckFactura : facturaCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Factura " + facturaCollection1OrphanCheckFactura + " in its facturaCollection1 field has a non-nullable idUsuariocomprador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
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

    public List<Usuarios> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

   
    public Usuarios findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }
    public boolean getLogin(String email , String pass){
        EntityManager em = getEntityManager();
        Query sql = em.createQuery("SELECT u FROM Usuarios u WHERE u.userEmail = :email AND u.userPassword=:pass",Usuarios.class);
        Usuarios user = (Usuarios)sql.setParameter("email",email).setParameter("pass",pass).getSingleResult();
        System.out.println(user + " ____USUARIO____");
        if(user != null){
            return true;
        }else{
            return false;
        }
    }   

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
