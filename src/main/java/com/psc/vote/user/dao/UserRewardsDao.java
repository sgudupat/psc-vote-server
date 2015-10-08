package com.psc.vote.user.dao;

import com.psc.vote.common.dao.ConnectionDao;
import com.psc.vote.user.domain.UserReward;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserRewardsDao extends ConnectionDao {

    public List<UserReward> getUserRewards(String username) {
        StringBuilder selectStmt = new StringBuilder();
        selectStmt.append("SELECT r.reward_id ");
        selectStmt.append("      ,r.reward_desc ");
        selectStmt.append("      ,r.reward_image ");
        selectStmt.append("      ,r.start_date ");
        selectStmt.append("      ,r.end_date ");
        selectStmt.append("      ,r.creation_date ");
        selectStmt.append("      ,ur.user_name ");
        selectStmt.append("      ,ur.user_viewed ");
        selectStmt.append("      ,ur.user_availed ");
        selectStmt.append("      ,ur.avail_date ");
        selectStmt.append("      ,ur.reward_push_date ");
        selectStmt.append("  FROM rewards r ");
        selectStmt.append("      ,user_rewards ur ");
        selectStmt.append(" WHERE ur.user_name = ? ");
        selectStmt.append("   AND ur.reward_id = r.reward_id ");
        return this.getJdbcTemplate().query(selectStmt.toString(), new Object[]{username}, new UserRewardRowMapper());
    }

    private static final class UserRewardRowMapper implements RowMapper<UserReward> {
        @Override
        public UserReward mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserReward userReward = new UserReward();
            userReward.setRewardId(rs.getString("REWARD_ID"));
            userReward.setDescription(rs.getString("REWARD_DESC"));
            userReward.setImageURL(rs.getString("REWARD_IMAGE"));
            userReward.setStartDate(rs.getDate("START_DATE"));
            userReward.setEndDate(rs.getDate("END_DATE"));
            userReward.setCreationDate(rs.getDate("CREATION_DATE"));
            userReward.setUsername(rs.getString("USER_NAME"));
            userReward.setUserViewed(rs.getString("USER_VIEWED"));
            userReward.setUserAvailed(rs.getString("USER_AVAILED"));
            userReward.setAvailDate(rs.getDate("AVAIL_DATE"));
            userReward.setRewardPushDate(rs.getDate("REWARD_PUSH_DATE"));
            return userReward;
        }
    }

    public void pushUserReward(String userName, String rewardId) throws Exception {
        String insertStmt = "INSERT INTO user_rewards(reward_id, user_name, user_viewed, user_availed, avail_date, reward_push_date) " +
                " VALUES (?, ?, 'N', 'N', null, sysdate());";
        try {
            this.getJdbcTemplate().update(insertStmt, new Object[]{rewardId, userName});
        } catch (Exception ex) {
            System.out.println("failed to create user rewards : " + rewardId + ":" + userName + ex.getMessage());
        }
    }
}