package api.DB;

import api.canstants.PathFileCanstant;
import api.util.PropertiesUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by xieYangYang on 2018/11/24.
 */
public class C3P0DB {
    private static DataSource ds=null;
    private static PropertiesUtil propertiesUtil=new PropertiesUtil(PathFileCanstant.dbPropertiesPath);
    /**
     * 获取数据源
     * @return 连接池
     */
    public static DataSource getDataSource(){
        return ds ;
    }

    /**
     * 获取datasource 从properties文件里
     * @return
     */
    public static DataSource getDataSourceFromUrl(){

        ds=new DriverManagerDataSource(propertiesUtil.getPropertyValue("jdbc.mysql.url"));
       return ds;
    }

    /**
     * 释放资源
     * @param conn
     * @param st
     * @param rs
     */
    public static void CloseResource(Connection conn,Statement st , ResultSet rs){
        closeResultSet(rs);
        closeStaement(st);
        closeConn(conn);
    }

    /**
     * 获取连接
     * @return 连接
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }

    /**
     *释放连接
     * @param conn
     *     连接
     */
    public static void closeConn(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conn = null ;
            }
        }
    }

    /**
     * 释放语句执行者
     * @param st
     * 语句执行者
     */
    public static void closeStaement(Statement st){
        if(st!=null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                st = null ;
            }
        }
    }

    /**
     * 释放结果集
     * @param rs
     * 结果集
     */
    public static void closeResultSet(ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                rs = null ;
            }
        }
    }
}
