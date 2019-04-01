package BMSWeb;
import BMSWeb.sevice.ProcessService;
import api.util.ParseExcel;

import java.util.Map;

/**
 * Created by Administrator on 2018/11/8.
 */

public class Test extends ParseExcel {
    @org.testng.annotations.Test(dataProvider = "excelToMap")
    public void test(Map maps){
        ProcessService processService=new ProcessService();
        processService.executeStep(maps);
    }
}
