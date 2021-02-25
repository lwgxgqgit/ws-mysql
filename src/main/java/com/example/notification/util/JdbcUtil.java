package com.example.notification.util;


import java.sql.*;

/**
 * @author lwg
 */
public class JdbcUtil {

    private static String driverClass = "com.mysql.jdbc.Driver";

    private static String url = "jdbc:mysql://192.168.4.62:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&failOverReadOnly=false";

    private static String user = "root";

    private static String password = "root@hiekn";

    static {

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    /**
     * 关闭资源
     */
    public static void close(ResultSet rs, Statement st,Connection conn){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(st != null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

















