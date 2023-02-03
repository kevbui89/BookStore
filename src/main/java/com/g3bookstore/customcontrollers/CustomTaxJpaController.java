package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.TaxJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Province;
import com.g3bookstore.entities.Province_;
import com.g3bookstore.entities.Tax;
import com.g3bookstore.entities.Tax_;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Custom controller for Taxes.
 * 
 * @author Alessandro Ciotola, Denis Lebedev
 */
@Named("theTax")
@RequestScoped
public class CustomTaxJpaController implements Serializable 
{
    @Inject
    private TaxJpaController taxJpaController;
    
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    
    /**
     * Allow to create a Tax entity.
     * 
     * @param tax
     * @throws Exception 
     */
    public void create(Tax tax) throws Exception {
        taxJpaController.create(tax);
    }
    
    /**
     * Allow to update a Tax entity.
     * 
     * @param tax
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void update(Tax tax) throws RollbackFailureException, Exception {
        taxJpaController.edit(tax);
    }
    
    /**
     * Allow to delete a Tax entity.
     * 
     * @param id
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void delete(Integer id) throws RollbackFailureException, Exception {
        taxJpaController.destroy(id);
    }
    
    /**
     * Method which will return all taxes in the database
     * 
     * @return 
     */
    public List<Tax> getTaxes() 
    {
        return taxJpaController.findTaxEntities();
    }

    /**
     * Method which will find a specific tax by ID
     * 
     * @param id
     * @return 
     */
    public Tax getTaxByID(int id) 
    {
        return taxJpaController.findTax(id);
    }
    
    /**
     * Method which will search through the Tax table for specific tax by province.
     * 
     * @param provinceName
     * @return The Tax object
     */
    public Tax getTaxByProvince(String provinceName)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Province> cq = cb.createQuery(Province.class);
        Root<Province> province = cq.from(Province.class);
        cq.select(province);
        cq.where(cb.equal(province.get(Province_.province), provinceName));
        
        Query q = em.createQuery(cq);
        Province newProvince = (Province)q.getSingleResult();
        
        CriteriaQuery<Tax> cq2 = cb.createQuery(Tax.class);
        Root<Tax> tax = cq2.from(Tax.class);
        cq2.select(tax);
        cq2.where(cb.equal(tax.get(Tax_.provinceId), newProvince));
        
        Query q2 = em.createQuery(cq2);
        return ((Tax) q2.getSingleResult());     
    }
}
