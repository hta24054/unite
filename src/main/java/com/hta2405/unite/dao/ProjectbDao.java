package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ProjectbDao {
    private DataSource ds;

    public ProjectbDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }

    


} 