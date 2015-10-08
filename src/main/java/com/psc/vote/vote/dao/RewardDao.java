package com.psc.vote.vote.dao;

import com.psc.vote.common.dao.ConnectionDao;
import com.psc.vote.vote.domain.Reward;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class RewardDao extends ConnectionDao {

    public void createReward(Reward reward) throws Exception {
        String insertStmt = "INSERT INTO rewards(reward_desc, reward_image, start_date " +
                "  , end_date, creation_date, campaign_id, push_limit, push_region, push_filter) " +
                " VALUES (:description, :imageURL, :startDate, :endDate, sysdate(), :campaignId, :pushLimit, :pushRegion, :pushFilter);";
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(reward);

        String selectStmt = "SELECT reward_id FROM rewards WHERE reward_desc = :description AND campaign_id = :campaignId ";
        try {
            this.getNamedParameterJdbcTemplate().update(insertStmt, sqlParams);
            String rewardId = this.getNamedParameterJdbcTemplate().queryForObject(selectStmt, sqlParams, String.class);
            System.out.println("rewardId::" + rewardId);
            reward.setRewardId(rewardId);
        } catch (Exception ex) {
            System.out.println("failed to create reward : " + reward + ex.getMessage());
        }
    }
}