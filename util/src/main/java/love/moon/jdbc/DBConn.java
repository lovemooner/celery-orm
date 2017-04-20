package love.moon.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * User: lovemooner
 * Date: 17-4-18
 * Time: 下午12:46
 */
public class DBConn {
      private static final Logger LOG = LoggerFactory.getLogger(DBConn.class);
    //三属性、四方法

    //三大核心接口
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    //四个方法
    //method1: 创建数据库的连接
    public Connection getConnection(){
        try {
            //1: 加载连接驱动，Java反射原理
            Class.forName(Config.CLASS_NAME);
            //2:创建Connection接口对象，用于获取MySQL数据库的连接对象。三个参数：url连接字符串    账号  密码
            String url = Config.DATABASE_URL+"://"+Config.SERVER_IP+":"+Config.SERVER_PORT+"/"+Config.DATABASE_SID;
            conn = DriverManager.getConnection(url,Config.USERNAME,Config.PASSWORD);
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(),e);
        } catch (SQLException e) {
             LOG.error(e.getMessage(),e);
        }
        return conn;
    }


    //method2：关闭数据库的方法
    public void closeConn(){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(),e);
            }
        }
        if(pstmt!=null){
            try {
                pstmt.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(),e);
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(),e);
            }
        }
    }


    //method3: 专门用于发送增删改语句的方法
    public int execOther(PreparedStatement pstmt){
        try {
            //1、使用Statement对象发送SQL语句
            int affectedRows = pstmt.executeUpdate();
            //2、返回结果
            return affectedRows;
        } catch (SQLException e) {
            LOG.error(e.getMessage(),e);
            return -1;
        }
    }


    //method4: 专门用于发送查询语句
    public ResultSet execQuery(PreparedStatement pstmt){
        try {
            //1、使用Statement对象发送SQL语句
            rs = pstmt.executeQuery();
            //2、返回结果
            return rs;
        } catch (SQLException e) {
             LOG.error(e.getMessage(),e);
            return null;
        }
    }



}
