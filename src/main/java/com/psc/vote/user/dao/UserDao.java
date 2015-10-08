package com.psc.vote.user.dao;

import com.psc.vote.common.dao.ConnectionDao;
import com.psc.vote.user.domain.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao extends ConnectionDao {

    public User getUser(User user) {
        String selectStmt = "SELECT user_name, password, mobile, gender, age, push_notification FROM users WHERE user_name = ?";
        return this.getJdbcTemplate().queryForObject(selectStmt, new Object[]{user.getUsername()}, new UserRowMapper());
    }

    public void registerUser(User user) throws Exception {
        String insertStmt = "INSERT INTO users(user_name, password, mobile, gender, age, device_id, push_notification, registration_id) " +
                " VALUES (:username, :password, :mobile, :gender, :age, :deviceId, :pushNotification, :registrationId);";
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(user);
        try {
            this.getNamedParameterJdbcTemplate().update(insertStmt, sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to insert for user_name: " + user + ex.getMessage());
            throw ex;
        }
    }

    public void updateUser(User user) throws Exception {
        StringBuilder updateStmt = new StringBuilder();
        updateStmt.append("UPDATE users SET gender = :gender ");
        updateStmt.append("      ,age = :age ");
        updateStmt.append("      ,push_notification = :pushNotification ");
        updateStmt.append(" WHERE user_name = :username ");
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(user);
        try {
            this.getNamedParameterJdbcTemplate().update(updateStmt.toString(), sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to update user_name: " + user + ex.getMessage());
            throw ex;
        }
    }

    public void updatePassword(User user) throws Exception {
        StringBuilder updateStmt = new StringBuilder();
        updateStmt.append("UPDATE users SET password = :password ");
        updateStmt.append(" WHERE user_name = :username ");
        SqlParameterSource sqlParams = new BeanPropertySqlParameterSource(user);
        try {
            this.getNamedParameterJdbcTemplate().update(updateStmt.toString(), sqlParams);
        } catch (Exception ex) {
            System.out.println("failed to update password for user_name: " + user + ex.getMessage());
            throw ex;
        }
    }

    public List<User> getUsersForRewardPush(String clientCountry, boolean onlyRespondents, String limitValue) {
        StringBuilder selectStmt = new StringBuilder();
        selectStmt.append("SELECT user_name, password, mobile, gender, age, push_notification, registration_id  FROM users u ");
        selectStmt.append("  WHERE 1 = 1 ");
        if (!StringUtils.endsWithIgnoreCase("Global", clientCountry)) {
            selectStmt.append("    AND u.country = '" + clientCountry + "'");
        }
        if (onlyRespondents) {
            selectStmt.append("    AND EXISTS (SELECT 1 FROM votes v where v.user_name = u.user_name)");
        }
        if (!StringUtils.isEmpty(limitValue)) {
            selectStmt.append("    LIMIT " + limitValue);
        }
        System.out.println("selectStmt:" + selectStmt);
        return this.getJdbcTemplate().query(selectStmt.toString(), new UserRowMapper());
    }

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUsername(rs.getString("USER_NAME"));
            user.setPassword(rs.getString("PASSWORD"));
            user.setMobile(rs.getString("MOBILE"));
            user.setGender(rs.getString("GENDER"));
            user.setAge(rs.getString("AGE"));
            user.setPushNotification(rs.getString("PUSH_NOTIFICATION"));
            user.setRegistrationId(rs.getString("REGISTRATION_ID"));
            return user;
        }
    }

}