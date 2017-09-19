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
package com.tenacle.sgr.entities;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Build the necessary entity management classes and factories here.
 *
 * @author samuel
 */
public class PersistenceManager {

    private EntityManagerFactory emf = null;

    private PersistenceManager() {
        //creates the entity manager factory.
        emf = Persistence.createEntityManagerFactory("com.tenacle.sgr_sgr_war_1.0-SNAPSHOTPU");
    }

    public static PersistenceManager getInstance() {
        return PersistenceManagementHolder.INSTANCE;
    }

    private static class PersistenceManagementHolder {

        private static final PersistenceManager INSTANCE = new PersistenceManager();
    }

    /**
     *
     * Creates and returns the entity manager.
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /***
     * Get the Entity Management Factory.
     * 
     * @return 
     */
    public EntityManagerFactory getEntityManagementFactory() {
        return emf;
    }

    public void destroyEntityManager(EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}
