package com.psc.vote.client.dao;

import com.psc.vote.client.domain.ClientRegret;
import com.psc.vote.common.dao.ConnectionDao;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class ClientRegretDao extends ConnectionDao {

    public void insertRegret(ClientRegret clientRegret) throws Exception {
        String insertStmt = "INSERT INTO client_regrets(device_id, regret_details, creation_date) " +
                " VALUES (:deviceId, :regretDetail, sysdate());";
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(clientRegret);
        try {
            this.getNamedParameterJdbcTemplate().update(insertStmt, sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to insert for client regret: " + clientRegret + ex.getMessage());
            throw ex;
        }
    }
}