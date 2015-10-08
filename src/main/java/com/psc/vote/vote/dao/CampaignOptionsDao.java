package com.psc.vote.vote.dao;

import com.psc.vote.common.dao.ConnectionDao;
import com.psc.vote.vote.domain.CampaignOption;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CampaignOptionsDao extends ConnectionDao {

    public List<CampaignOption> getOptionsByCampaignId(String anchorName) {
        StringBuilder selectStmt = new StringBuilder();

        selectStmt.append("select c.campaign_option_id ");
        selectStmt.append("        ,c.campaign_id ");
        selectStmt.append("        ,c.option_value ");
        selectStmt.append(" from campaign_options c ");
        selectStmt.append(" where c.campaign_id = ? ");

        return this.getJdbcTemplate().query(selectStmt.toString(), new Object[]{anchorName}, new CampaignOptionRowMapper());
    }

    public void createCampaignOption(String campaignId, String optionValue) throws Exception {
        String insertStmt = "INSERT INTO campaign_options(campaign_id, option_value) VALUES (?, ?);";
        try {
            this.getJdbcTemplate().update(insertStmt, new Object[]{campaignId, optionValue});
        } catch (Exception ex) {
            System.out.println("failed to create campaign :" + campaignId + ": option :" + optionValue + ex.getMessage());
        }
    }

    private static final class CampaignOptionRowMapper implements RowMapper<CampaignOption> {
        @Override
        public CampaignOption mapRow(ResultSet rs, int rowNum) throws SQLException {
            CampaignOption option = new CampaignOption();
            option.setOptionId(rs.getString("CAMPAIGN_OPTION_ID"));
            option.setOptionValue(rs.getString("OPTION_VALUE"));
            return option;
        }
    }
}