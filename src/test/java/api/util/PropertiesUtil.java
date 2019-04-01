package api.util;

import api.canstants.PathFileCanstant;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Created by xieYangYang on 2018/11/24.
 */
public class PropertiesUtil {
    Logger logger = Logger.getLogger(PropertiesUtil.class);
    private static InputStream inputStream;

    /**
     * 设置读取文件路径
     * @param path
     */
    public PropertiesUtil(String path) {
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key读取值
     * @param key
     * @return
     */
    public String getPropertyValue(String key) {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return String.valueOf(properties.get(key));
        } catch (FileNotFoundException e) {
            logger.error("文件路径未找到，文件路径");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("加载文件properties失败");
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        PropertiesUtil propertiesUtil = new PropertiesUtil(PathFileCanstant.dbPropertiesPath);
        System.out.println(propertiesUtil.getPropertyValue("jdbc.mysql.driver"));
    }
}
