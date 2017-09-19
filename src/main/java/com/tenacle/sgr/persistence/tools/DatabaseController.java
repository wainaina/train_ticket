/*
 * Copyright 2014 samuel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tenacle.sgr.persistence.tools;

import com.tenacle.sgr.entities.PersistenceManager;
import com.tenacle.sgr.entities.TenacleEntity;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Controls all the crud operations to a database.
 *
 * @author samuel
 */
public class DatabaseController<T> {

    private Class<T> entityClass;

    private InitialContext ic = null;

    public DatabaseController(Class<T> entityClass) {
        try {
            this.entityClass = entityClass;
            ic = new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return PersistenceManager.getInstance().getEntityManagementFactory();
    }

    public synchronized T create(T entity) {
        try {
            hasViolatedConstraints(entity);
            System.out.println("Creating new entity: " + entity.toString());
            EntityManager em = getEntityManager();
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(entity);
            em.flush();
            et.commit();
            em.getEntityManagerFactory().getCache().evict(entityClass);

            //this piece of the code is used to set the id of the entity here.
            TenacleEntity t_entity = (TenacleEntity) entity;
            t_entity.setId((Integer) getEntityManagerFactory()
                    .getPersistenceUnitUtil()
                    .getIdentifier(entity));

            System.out.println("Created entity with ID: " + entity.toString() + " ID: " + t_entity.getId());

            return entity;
        } catch (SecurityException | IllegalStateException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public synchronized T edit(T entity, final T oldEntity) {
        try {

            hasViolatedConstraints(entity);
            System.out.println("Editing entity: " + entity.toString());
            EntityManager em = getEntityManager();
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.merge(entity);
            et.commit();
            em.getEntityManagerFactory().getCache().evict(entityClass);
            return entity;
        } catch (SecurityException | IllegalStateException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Edit components recursively
     */
    public T recursiveEdit(T obj) {

        if (obj == null) {
            return null;
        }
        Field[] fields = obj
                .getClass()
                .getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            try {
                //change field security
                fields[i].setAccessible(true);

                T o = (T) fields[i].get(obj);

                //check if it is an instance of the Tenacle Entities
                if ((o instanceof TenacleEntity) && (o != null) && (o.getClass() != obj.getClass())) {
                    recursiveEdit(o);
                }

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //run edit operation here.        
        return edit(obj, obj);
    }

    public synchronized void remove(T entity, Object id) {
        try {

            hasViolatedConstraints(entity);
            System.out.println("Deleting entity: " + entity);
            EntityManager em = getEntityManager();
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.remove((entity = em.find(entityClass, id)));
            em.flush();
            et.commit();
            em.getEntityManagerFactory().getCache().evict(entityClass);

        } catch (SecurityException | IllegalStateException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public T find(Object id) {

        T t = getEntityManager().find(entityClass, id);
        return t;
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Checks whether an entity has violated constraints.
     *
     * @param entity
     * @return
     */
    private boolean hasViolatedConstraints(T entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                System.err.println("Constraint violation: " + cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        }
        return constraintViolations.size() > 0;
    }

}
