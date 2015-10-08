package com.psc.vote.vote.dao;

import com.psc.vote.common.dao.ConnectionDao;
import com.psc.vote.vote.domain.Anchor;
import com.psc.vote.vote.domain.Vote;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotesDao extends ConnectionDao {

    public void submitVote(String userName, String optionId) throws Exception {
        String insertStmt = "INSERT INTO votes(user_name, campaign_option_id, vote_date) VALUES (?, ?, SYSDATE());";
        try {
            this.getJdbcTemplate().update(insertStmt, new Object[]{userName, optionId});
        } catch (Exception ex) {
            System.out.println("failed to insert Agent for user_name: " + ex.getMessage());
            throw ex;
        }
    }

    public void resubmitVote(String userName, String optionId, String oldOptionId) throws Exception {
        String insertStmt = "UPDATE votes SET campaign_option_id = ? , vote_date = SYSDATE() WHERE user_name = ? AND campaign_option_id = ? ";
        try {
            this.getJdbcTemplate().update(insertStmt, new Object[]{optionId, userName, oldOptionId});
        } catch (Exception ex) {
            System.out.println("failed to update Agent for user_name: " + ex.getMessage());
            throw ex;
        }
    }

    public Vote getVote(String userName, String campaignId) throws Exception {
        String selectStmt = "select v.user_name, v.campaign_option_id " +
                " from votes v, campaign_options co " +
                " where v.campaign_option_id = co.campaign_option_id " +
                "   and v.user_name = ? " +
                "   and co.campaign_id = ? ";
        try {
            return this.getJdbcTemplate().queryForObject(selectStmt, new Object[]{userName, campaignId}, new VoteRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            System.out.println("Not yet voted hence return NULL");
            return null;
        } catch (Exception ex) {
            System.out.println("failed to insert Agent for user_name: " + ex.getMessage());
            throw ex;
        }
    }

    public List<Map<String, String>> displayStats(String anchorName) {
        StringBuilder selectStmt = new StringBuilder();

        selectStmt.append(" SELECT cl.client_name ");
        selectStmt.append("       ,a.anchor_id ");
        selectStmt.append("       ,c.campaign_id ");
        selectStmt.append("       ,c.question ");
        selectStmt.append("       ,u.age ");
        selectStmt.append("       ,count(1) cnt_value ");
        selectStmt.append("  FROM campaigns c ");
        selectStmt.append("      ,campaign_options co ");
        selectStmt.append("      ,votes v ");
        selectStmt.append("      ,anchors a ");
        selectStmt.append("      ,clients cl ");
        selectStmt.append("      ,users u ");
        selectStmt.append(" where c.campaign_id = ? ");
        selectStmt.append(" and c.campaign_id = co.campaign_id ");
        selectStmt.append(" and co.campaign_option_id = v.campaign_option_id ");
        selectStmt.append(" and v.user_name = u.user_name ");
        selectStmt.append(" and c.anchor_id = a.anchor_id ");
        selectStmt.append(" and a.client_id = cl.client_id ");
        selectStmt.append(" group by cl.client_name ");
        selectStmt.append("         ,a.anchor_id ");
        selectStmt.append("         ,c.campaign_id ");
        selectStmt.append("         ,c.question ");
        selectStmt.append("         ,u.age ");

        return this.getJdbcTemplate().query(selectStmt.toString(), new Object[]{anchorName}, new StatsRowMapper());
    }

    public List<Anchor> recentUserViewedAnchors(String userName) {
        StringBuilder selectStmt = new StringBuilder();

        selectStmt.append("SELECT a.anchor_id ");
        selectStmt.append("      ,a.client_id ");
        selectStmt.append("      ,cl.client_name ");
        selectStmt.append("      ,a.price ");
        selectStmt.append("      ,a.creation_date ");
        selectStmt.append("      ,cl.website_address ");
        selectStmt.append("      ,cl.about ");
        selectStmt.append("      ,a.active ");
        selectStmt.append("  FROM campaigns c ");
        selectStmt.append("      ,campaign_options co ");
        selectStmt.append("      ,votes v ");
        selectStmt.append("      ,anchors a ");
        selectStmt.append("      ,clients cl ");
        selectStmt.append(" where 1 = 1 ");
        selectStmt.append(" and c.campaign_id = co.campaign_id ");
        selectStmt.append(" and co.campaign_option_id = v.campaign_option_id ");
        selectStmt.append(" and v.user_name = ? ");
        selectStmt.append(" and c.anchor_id = a.anchor_id ");
        selectStmt.append(" and a.client_id = cl.client_id ");
        selectStmt.append(" order by v.vote_date desc ");

        return this.getJdbcTemplate().query(selectStmt.toString(), new Object[]{userName}, new Anchor2RowMapper());
    }

    private static final class VoteRowMapper implements RowMapper<Vote> {
        @Override
        public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
            Vote vote = new Vote();
            vote.setUserName(rs.getString("USER_NAME"));
            vote.setOptionValueId(rs.getString("CAMPAIGN_OPTION_ID"));
            return vote;
        }
    }

    private static final class StatsRowMapper implements RowMapper<Map<String, String>> {
        @Override
        public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, String> map = new HashMap<String, String>();
            map.put("client_name", rs.getString("CLIENT_NAME"));
            map.put("anchor_id", rs.getString("ANCHOR_ID"));
            map.put("campaign_id", rs.getString("CAMPAIGN_ID"));
            map.put("question", rs.getString("QUESTION"));
            map.put("age", rs.getString("AGE"));
            map.put("cnt_value", rs.getString("CNT_VALUE"));
            return map;
        }
    }

    private static final class Anchor2RowMapper implements RowMapper<Anchor> {
        @Override
        public Anchor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Anchor anchor = new Anchor();
            anchor.setAnchorName(rs.getString("ANCHOR_ID"));
            anchor.setClientId(rs.getString("CLIENT_ID"));
            anchor.setClientName(rs.getString("CLIENT_NAME"));
            anchor.setAnchorPrice(rs.getBigDecimal("PRICE"));
            anchor.setCreationDate(rs.getDate("CREATION_DATE"));
            anchor.setClientWebsiteAddress(rs.getString("WEBSITE_ADDRESS"));
            anchor.setClientInfo(rs.getString("ABOUT"));
            anchor.setStatus(rs.getString("ACTIVE"));
            return anchor;
        }
    }
}