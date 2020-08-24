/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladorJPA;

import ControladorJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Factura;
import Entidades.Producto;
import Entidades.ProductosDeFactura;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Andre
 */
public class ProductosDeFacturaJpaController implements Serializable {

    public ProductosDeFacturaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("my_persistence_unit"); 
        em = emf.createEntityManager();
    }
    private EntityManager em = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean create(ProductosDeFactura productosDeFactura) {
        
        try {
            
            em.getTransaction().begin();
            Factura facturaIdfactura = productosDeFactura.getFacturaIdfactura();
            if (facturaIdfactura != null) {
                facturaIdfactura = em.getReference(facturaIdfactura.getClass(), facturaIdfactura.getIdfactura());
                productosDeFactura.setFacturaIdfactura(facturaIdfactura);
            }
            Producto productoIdproducto = productosDeFactura.getProductoIdproducto();
            if (productoIdproducto != null) {
                productoIdproducto = em.getReference(productoIdproducto.getClass(), productoIdproducto.getIdproducto());
                productosDeFactura.setProductoIdproducto(productoIdproducto);
            }
            em.persist(productosDeFactura);
            if (facturaIdfactura != null) {
                facturaIdfactura.getProductosDeFacturaCollection().add(productosDeFactura);
                facturaIdfactura = em.merge(facturaIdfactura);
            }
            if (productoIdproducto != null) {
                productoIdproducto.getProductosDeFacturaCollection().add(productosDeFactura);
                productoIdproducto = em.merge(productoIdproducto);
            }
            em.getTransaction().commit();
            return true;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public boolean edit(ProductosDeFactura productosDeFactura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductosDeFactura persistentProductosDeFactura = em.find(ProductosDeFactura.class, productosDeFactura.getIdproductosDeFactura());
            Factura facturaIdfacturaOld = persistentProductosDeFactura.getFacturaIdfactura();
            Factura facturaIdfacturaNew = productosDeFactura.getFacturaIdfactura();
            Producto productoIdproductoOld = persistentProductosDeFactura.getProductoIdproducto();
            Producto productoIdproductoNew = productosDeFactura.getProductoIdproducto();
            if (facturaIdfacturaNew != null) {
                facturaIdfacturaNew = em.getReference(facturaIdfacturaNew.getClass(), facturaIdfacturaNew.getIdfactura());
                productosDeFactura.setFacturaIdfactura(facturaIdfacturaNew);
            }
            if (productoIdproductoNew != null) {
                productoIdproductoNew = em.getReference(productoIdproductoNew.getClass(), productoIdproductoNew.getIdproducto());
                productosDeFactura.setProductoIdproducto(productoIdproductoNew);
            }
            productosDeFactura = em.merge(productosDeFactura);
            if (facturaIdfacturaOld != null && !facturaIdfacturaOld.equals(facturaIdfacturaNew)) {
                facturaIdfacturaOld.getProductosDeFacturaCollection().remove(productosDeFactura);
                facturaIdfacturaOld = em.merge(facturaIdfacturaOld);
            }
            if (facturaIdfacturaNew != null && !facturaIdfacturaNew.equals(facturaIdfacturaOld)) {
                facturaIdfacturaNew.getProductosDeFacturaCollection().add(productosDeFactura);
                facturaIdfacturaNew = em.merge(facturaIdfacturaNew);
            }
            if (productoIdproductoOld != null && !productoIdproductoOld.equals(productoIdproductoNew)) {
                productoIdproductoOld.getProductosDeFacturaCollection().remove(productosDeFactura);
                productoIdproductoOld = em.merge(productoIdproductoOld);
            }
            if (productoIdproductoNew != null && !productoIdproductoNew.equals(productoIdproductoOld)) {
                productoIdproductoNew.getProductosDeFacturaCollection().add(productosDeFactura);
                productoIdproductoNew = em.merge(productoIdproductoNew);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productosDeFactura.getIdproductosDeFactura();
                if (findProductosDeFactura(id) == null) {
                    throw new NonexistentEntityException("The productosDeFactura with id " + id + " no longer exists.");
                }
            }
            throw ex;
            
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductosDeFactura productosDeFactura;
            try {
                productosDeFactura = em.getReference(ProductosDeFactura.class, id);
                productosDeFactura.getIdproductosDeFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productosDeFactura with id " + id + " no longer exists.", enfe);
            }
            Factura facturaIdfactura = productosDeFactura.getFacturaIdfactura();
            if (facturaIdfactura != null) {
                facturaIdfactura.getProductosDeFacturaCollection().remove(productosDeFactura);
                facturaIdfactura = em.merge(facturaIdfactura);
            }
            Producto productoIdproducto = productosDeFactura.getProductoIdproducto();
            if (productoIdproducto != null) {
                productoIdproducto.getProductosDeFacturaCollection().remove(productosDeFactura);
                productoIdproducto = em.merge(productoIdproducto);
            }
            em.remove(productosDeFactura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductosDeFactura> findProductosDeFacturaEntities() {
        return findProductosDeFacturaEntities(true, -1, -1);
    }

    public List<ProductosDeFactura> findProductosDeFacturaEntities(int maxResults, int firstResult) {
        return findProductosDeFacturaEntities(false, maxResults, firstResult);
    }

    private List<ProductosDeFactura> findProductosDeFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductosDeFactura.class));
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

    public ProductosDeFactura findProductosDeFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductosDeFactura.class, id);
        } finally {
            em.close();
        }
    }
    public List<ProductosDeFactura> getProductosdeFactura(Integer idfactura){
        EntityManager em = getEntityManager();
        Query sql = em.createQuery("SELECT u FROM ProductosDeFactura u WHERE u.facturaIdfactura.idfactura = :idfac ",ProductosDeFactura.class);
        
        List<ProductosDeFactura> productosDeFactura = sql.setParameter("idfac",idfactura).getResultList();
        System.out.println("________HAY TANTOS " + idfactura);
        return productosDeFactura;
    }  

    public int getProductosDeFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductosDeFactura> rt = cq.from(ProductosDeFactura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}





