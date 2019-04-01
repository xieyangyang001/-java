package api.DB;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by XieYangYang on 2018/11/24.
 */
public class DBCommon {
    public static Logger logger = Logger.getLogger(DBCommon.class);
    public static QueryRunner queryRunner = new QueryRunner(C3P0DB.getDataSourceFromUrl());

    /**
     * 查询数据返回list
     *
     * @param sql
     * @return JSONObject
     */
    public static JSONArray querySqltoList(String sql) {
        List list = new ArrayList();
        try {
            list = (List) queryRunner.query(sql, new MapListHandler());
        } catch (SQLException e) {
            logger.error("查询sql失败，sql语句：" + sql);
            e.printStackTrace();
        }
        return (JSONArray) JSONArray.toJSON(list);
    }

    /**
     * 插入更新sql
     * @param sql sql语句
     * @return 0修改、插入失败，1修改、插入成功
     */
    public static int insertOrUpdateSql(String sql) {
        int isSuccess = 0;
        List<String> sqlList = new ArrayList<String>();
        if (sql.contains(";")) {
            sqlList = Arrays.asList(sql.split(";"));
        }
        for (String s : sqlList) {
            try {
                isSuccess = queryRunner.execute(s);
            } catch (SQLException e) {
                logger.error("执行sql失败，sql语句：" + sql);
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static void main(String[] args) {
        System.out.println(insertOrUpdateSql("UPDATE `ce_shi`.`ce` SET `name` = '测试2' WHERE `id` = '2';UPDATE `ce_shi`.`ce` SET `name` = '测试9' WHERE `id` = '1'"));
    }
}
