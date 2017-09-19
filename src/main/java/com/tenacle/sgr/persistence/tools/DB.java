/*
 * Copyright 2015 samuel.
 *
 * Licensed under the Qubit Software License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.qubit.co.ke/licenses/LICENSE-1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tenacle.sgr.persistence.tools;

import com.tenacle.sgr.entities.PersistenceManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import javax.persistence.metamodel.EntityType;

/**
 *
 * @author samuel
 */
public class DB extends HashMap<Class<?>, DatabaseController<?>> {

    Statement statement;

    private DB() {
        loadDatabaseDefinitions();
        getStatement();
    }

    private void loadDatabaseDefinitions() {
        //get the fields present in the enities
        for (EntityType<?> entityType : PersistenceManager.getInstance().getEntityManager().getMetamodel().getEntities()) {
            Class<?> javaType = entityType.getJavaType();
            put(javaType, new DatabaseController(javaType));
        }
    }

    /**
     * Returns the database controller definition for this object this makes it
     * easier to reuse objects.
     *
     * @param clazz - object class reference.
     *
     * @return
     */
    public DatabaseController get(Class clazz) {
        return super.get(clazz);
    }

    public static DB getInstance() {
        return DBHolder.INSTANCE;

    }

    private static class DBHolder {

        private static final DB INSTANCE = new DB();
    }

    private Statement getStatement() {

        if (statement == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/sgr?zeroDateTimeBehavior=convertToNull", "root", "root");
                statement = con.createStatement();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return statement;
    }
}
