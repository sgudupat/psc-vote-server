package com.psc.vote.client.dao;

import com.psc.vote.client.domain.Client;
import com.psc.vote.common.dao.ConnectionDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDao extends ConnectionDao {

    public Client getClient(Client client) {
        String selectStmt = "SELECT client_id, client_name, password, email_address, creation_date, website_address, about, operating_country " +
                " FROM clients WHERE client_id = ?";
        return this.getJdbcTemplate().queryForObject(selectStmt, new Object[]{client.getClientId()}, new ClientRowMapper());
    }

    public void registerClient(Client client) throws Exception {
        String insertStmt = "INSERT INTO clients(client_id, password, email_address, creation_date) " +
                " VALUES (:clientId, :password, :emailAddress, sysdate());";
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(client);
        try {
            this.getNamedParameterJdbcTemplate().update(insertStmt, sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to insert for client: " + client + ex.getMessage());
            throw ex;
        }
    }

    public void updatePassword(Client client) throws Exception {
        StringBuilder updateStmt = new StringBuilder();
        updateStmt.append("UPDATE clients SET password = :password ");
        updateStmt.append(" WHERE client_id = :clientId ");
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(client);
        try {
            this.getNamedParameterJdbcTemplate().update(updateStmt.toString(), sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to update password for client: " + client + ex.getMessage());
            throw ex;
        }
    }

    public void updatePasswordCode(Client client) throws Exception {
        StringBuilder updateStmt = new StringBuilder();
        updateStmt.append("UPDATE clients SET password_code = :passwordCode ");
        updateStmt.append(" WHERE client_id = :clientId ");
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(client);
        try {
            this.getNamedParameterJdbcTemplate().update(updateStmt.toString(), sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to update password for client: " + client + ex.getMessage());
            throw ex;
        }
    }

    public void updateClient(Client client) throws Exception {
        StringBuilder updateStmt = new StringBuilder();
        updateStmt.append("UPDATE clients ");
        updateStmt.append("   SET client_name = :clientName ");
        updateStmt.append("      ,website_address = :websiteURL ");
        updateStmt.append("      ,about = :about ");
        updateStmt.append("      ,operating_country = :operatingCountry ");
        updateStmt.append(" WHERE client_id = :clientId ");
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(client);
        try {
            this.getNamedParameterJdbcTemplate().update(updateStmt.toString(), sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to update for client: " + client + ex.getMessage());
            throw ex;
        }
    }

    private static final class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setClientId(rs.getString("CLIENT_ID"));
            client.setClientName(rs.getString("CLIENT_NAME"));
            client.setPassword(rs.getString("PASSWORD"));
            client.setEmailAddress(rs.getString("EMAIL_ADDRESS"));
            client.setCreationDate(rs.getDate("CREATION_DATE"));
            client.setWebsiteURL(rs.getString("WEBSITE_ADDRESS"));
            client.setAbout(rs.getString("ABOUT"));
            client.setOperatingCountry(rs.getString("OPERATING_COUNTRY"));
            return client;
        }
    }
}