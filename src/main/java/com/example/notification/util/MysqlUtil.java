package com.example.notification.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lwg
 */
public class MysqlUtil {

    /**
     * 查询数据库的数据
     * @return
     */
    public static List<String> getNameList() {

        ArrayList<String> list = new ArrayList<>();
        PreparedStatement psts = null;
        Connection connection = null;
        ResultSet rs = null;
        String sql = "SELECT username FROM user;";

        try {
            connection = JdbcUtil.getConnection();
            psts = connection.prepareStatement(sql);
            rs = psts.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                list.add(username);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, psts, connection);
        }

        return list;
    }

}
