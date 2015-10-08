package com.psc.vote.vote.dao;

import com.psc.vote.common.dao.ConnectionDao;
import com.psc.vote.vote.domain.Campaign;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CampaignDao extends ConnectionDao {

    public List<Campaign> getCampaignsByAnchor(String anchorName) {
        System.out.println("AnchorName:" + anchorName);
        StringBuilder selectStmt = new StringBuilder();

        selectStmt.append("select c.campaign_id ");
        selectStmt.append("        ,c.question ");
        selectStmt.append("        ,c.start_date ");
        selectStmt.append("        ,c.end_date ");
        selectStmt.append("        ,c.region_country ");
        selectStmt.append("        ,c.reward_info ");
        selectStmt.append("        ,c.price ");
        selectStmt.append("        ,c.status ");
        selectStmt.append(" from campaigns c ");
        selectStmt.append(" where c.anchor_id = ? ");
        selectStmt.append("   and (c.status <> 'DELETED' OR c.status IS NULL) ");
        selectStmt.append("   order by c.creation_date desc ");

        System.out.println("selectStmt:" + selectStmt.toString());
        return this.getJdbcTemplate().query(selectStmt.toString(), new Object[]{anchorName}, new CampaignRowMapper());
    }

    public Campaign getCampaign(String campaignId) {
        StringBuilder selectStmt = new StringBuilder();

        selectStmt.append("select c.campaign_id ");
        selectStmt.append("        ,c.question ");
        selectStmt.append("        ,c.start_date ");
        selectStmt.append("        ,c.end_date ");
        selectStmt.append("        ,c.region_country ");
        selectStmt.append("        ,c.reward_info ");
        selectStmt.append("        ,c.price ");
        selectStmt.append("        ,c.status ");
        selectStmt.append(" from campaigns c ");
        selectStmt.append(" where c.campaign_id = ? ");

        return this.getJdbcTemplate().queryForObject(selectStmt.toString(), new Object[]{campaignId}, new CampaignRowMapper());
    }

    public void createCampaign(Campaign campaign) throws Exception {
        String insertStmt = "INSERT INTO campaigns(campaign_id, anchor_id, question, start_date, end_date, region_country, reward_info, creation_date) " +
                " VALUES (:campaignId, :anchorId, :question, :startDate, :endDate, :regionCountry, :rewardInfo, sysdate());";
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(campaign);
        try {
            this.getNamedParameterJdbcTemplate().update(insertStmt, sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to create campaign : " + campaign + ex.getMessage());
        }
    }

    public void modifyCampaign(Campaign campaign) throws Exception {
        String updateStmt = "UPDATE campaigns " +
                "               SET start_date = :startDate " +
                "                  ,end_date = :endDate " +
                "                  ,region_country = :regionCountry " +
                "                  ,reward_info = :rewardInfo " +
                "             WHERE campaign_id = :campaignId ";
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(campaign);
        try {
            this.getNamedParameterJdbcTemplate().update(updateStmt, sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to update campaign : " + campaign + ex.getMessage());
        }
    }

    public void updateCampaignStatus(String campaignId, String status) throws Exception {
        String updateStmt = "UPDATE campaigns SET status = ? WHERE campaign_id = ? ";
        try {
            this.getJdbcTemplate().update(updateStmt, new Object[]{status, campaignId});
        } catch (Exception ex) {
            System.out.println("failed to update status of campaign : " + campaignId + ex.getMessage());
            throw ex;
        }
    }

    private static final class CampaignRowMapper implements RowMapper<Campaign> {
        @Override
        public Campaign mapRow(ResultSet rs, int rowNum) throws SQLException {
            Campaign campaign = new Campaign();
            campaign.setCampaignId(rs.getString("CAMPAIGN_ID"));
            campaign.setQuestion(rs.getString("QUESTION"));
            campaign.setStartDate(rs.getDate("START_DATE"));
            campaign.setEndDate(rs.getDate("END_DATE"));
            campaign.setRegionCountry(rs.getString("REGION_COUNTRY"));
            campaign.setRewardInfo(rs.getString("REWARD_INFO"));
            campaign.setCampaignPrice(rs.getBigDecimal("PRICE"));
            campaign.setStatus(rs.getString("STATUS"));
            return campaign;
        }
    }
}