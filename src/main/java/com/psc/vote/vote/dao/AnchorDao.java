package com.psc.vote.vote.dao;

import com.psc.vote.common.dao.ConnectionDao;
import com.psc.vote.vote.domain.Anchor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AnchorDao extends ConnectionDao {

    public List<Anchor> getAnchorsByName(String anchorName) {
        StringBuilder selectStmt = new StringBuilder();
        selectStmt.append("SELECT a.anchor_id ");
        selectStmt.append("      ,c.client_name ");
        selectStmt.append("      ,a.price ");
        selectStmt.append("      ,a.creation_date ");
        selectStmt.append("      ,c.website_address ");
        selectStmt.append("      ,c.about ");
        selectStmt.append("  FROM anchors a ");
        selectStmt.append("      ,clients c ");
        selectStmt.append("  WHERE a.client_id = c.client_id ");
        selectStmt.append("    AND a.anchor_id like '%").append(anchorName).append("%'");
        selectStmt.append("  ORDER by creation_date DESC ");
        return this.getJdbcTemplate().query(selectStmt.toString(), new AnchorRowMapper());
    }

    public List<Anchor> getAnchorsByClientId(String clientId) {
        StringBuilder selectStmt = new StringBuilder();
        selectStmt.append("SELECT a.anchor_id ");
        selectStmt.append("      ,a.client_id ");
        selectStmt.append("      ,a.price ");
        selectStmt.append("      ,a.creation_date ");
        selectStmt.append("      ,a.active ");
        selectStmt.append("  FROM anchors a ");
        selectStmt.append("  WHERE a.client_id = ? ");
        selectStmt.append("    AND a.active = 'Y' ");
        selectStmt.append("  ORDER by creation_date DESC ");
        return this.getJdbcTemplate().query(selectStmt.toString(), new Object[]{clientId}, new OnlyAnchorRowMapper());
    }

    public void createAnchor(Anchor anchor) throws Exception {
        String insertStmt = "INSERT INTO anchors(anchor_id, client_id, creation_date, active) " +
                " VALUES (:anchorName, :clientId, SYSDATE(), :status);";
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(anchor);
        try {
            this.getNamedParameterJdbcTemplate().update(insertStmt, sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to create anchor : " + anchor + ex.getMessage());
            throw ex;
        }
    }


    public void deleteAnchor(String anchorId) throws Exception {
        String insertStmt = "UPDATE anchors SET active = 'N' WHERE anchor_id = ? ";
        try {
            this.getJdbcTemplate().update(insertStmt, new Object[]{anchorId});
        } catch (Exception ex) {
            System.out.println("failed to delete anchor : " + anchorId + ex.getMessage());
            throw ex;
        }
    }

    public Anchor checkAnchorAvailability(String anchorId) {
        StringBuilder selectStmt = new StringBuilder();
        selectStmt.append("SELECT a.anchor_id ");
        selectStmt.append("      ,a.client_id ");
        selectStmt.append("      ,a.price ");
        selectStmt.append("      ,a.creation_date ");
        selectStmt.append("      ,a.active ");
        selectStmt.append("  FROM anchors a ");
        selectStmt.append("  WHERE a.anchor_id = ? ");
        try {
            return this.getJdbcTemplate().queryForObject(selectStmt.toString(), new Object[]{anchorId}, new OnlyAnchorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static final class AnchorRowMapper implements RowMapper<Anchor> {
        @Override
        public Anchor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Anchor anchor = new Anchor();
            anchor.setAnchorName(rs.getString("ANCHOR_ID"));
            anchor.setClientName(rs.getString("CLIENT_NAME"));
            anchor.setAnchorPrice(rs.getBigDecimal("PRICE"));
            anchor.setCreationDate(rs.getDate("CREATION_DATE"));
            anchor.setClientWebsiteAddress(rs.getString("WEBSITE_ADDRESS"));
            anchor.setClientInfo(rs.getString("ABOUT"));
            return anchor;
        }
    }

    private static final class OnlyAnchorRowMapper implements RowMapper<Anchor> {
        @Override
        public Anchor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Anchor anchor = new Anchor();
            anchor.setAnchorName(rs.getString("ANCHOR_ID"));
            anchor.setClientId(rs.getString("CLIENT_ID"));
            anchor.setAnchorPrice(rs.getBigDecimal("PRICE"));
            anchor.setCreationDate(rs.getDate("CREATION_DATE"));
            anchor.setStatus(rs.getString("ACTIVE"));
            return anchor;
        }
    }
}