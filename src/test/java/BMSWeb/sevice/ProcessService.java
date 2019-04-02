package BMSWeb.sevice;

import BMSWeb.bean.Case;
import api.frame.SeleniumApi;
import com.alibaba.fastjson.JSONArray;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

/**
 * Created by XieYangYang on 2018/12/3.
 */
public class ProcessService {
    //selenium操作
    SeleniumApi seleniumApi = new SeleniumApi(ProcessService.class);

    /**
     * 测试步骤
     *
     * @param testData 测试数据
     */
    public void executeStep(Map<String, String> testData) {
        //第一步转换excel转javaBean
        Case cases = testDataToCase(testData);
        //第二步执行case
        caseStep(cases);
    }

    /**
     * testData转换Case javabean
     *
     * @param testData 测试数据
     * @return
     */
    public Case testDataToCase(Map<String, String> testData) {
        Case cases = new Case();
        cases.setCaseId(testData.get("case_id"));
        cases.setDesc(testData.get("desc"));
        cases.setRequestUrl(testData.get("requestUrl"));
        cases.setElementJson(testData.get("elementJson"));
        cases.setAssertElementJson(testData.get("assertElementJSON"));
        cases.setAssertSqlElementJson(testData.get("assertSqlElementJson"));
        cases.setAssertSqlJson(testData.get("assertSqlJson"));
        cases.setRetryCount(testData.get("retryCount"));
        cases.setBrowsers(testData.get("browsers"));
        cases.setCookieManager(testData.get("cookieManager"));
        return cases;
    }

    /**
     * @param cases case的执行步骤
     */
    public void caseStep(Case cases) {
        //打印本次测试的描述
        System.out.println("本条测试case测试：" + cases.getDesc() + "测试id：" + cases.getCaseId());
        //无请求地址不执行
        if (cases.getRequestUrl().equals("") || cases.getRequestUrl() == null) {
            System.out.println("本条测试case作废");
            Assert.assertEquals(true,false);
            return;
        }
        //无断言案例不执行
        if (cases.getAssertElementJson() == null ) {
            System.out.println("本条测试case作废,该案无断言");
            return;
        }
        String[] browsers = cases.getBrowsers().split(",");
        //打开请求网址,并进行后续操作（只支持chrom，firefox，ie）
        for (String browser : browsers) {
            WebDriver webDriver = seleniumApi.initLocalSelenium(browser, null);
            try {
                executeSelenium(cases,webDriver);
                executeAssert(cases,webDriver);

            }catch (Exception e){
                System.out.println("案例执行失败");
            }finally {
                //關閉瀏覽器
                webDriver.quit();
            }


        }
    }

    /**
     * 执行selenium操作
     *
     * @param cases     case对象（javabean）
     * @param webDriver selenium的webDriver
     */
    public void executeSelenium(Case cases, WebDriver webDriver) {
        //打开网址
        webDriver.get(cases.getRequestUrl());
        JSONArray elementArray=cases.getElementJson();
        //操作网页
        SeleniumService.seleniumExecuteJsonArray(elementArray,webDriver,cases,seleniumApi);
    }

    /**
     * 断言
     * @param cases  cases
     * @param webDriver webDriver
     */
    public void executeAssert(Case cases, WebDriver webDriver){

        SeleniumService.assertElement(webDriver,cases,seleniumApi);
        if(cases.getAssertSqlJson()!=null){
            SeleniumService.assertSqlElement(webDriver,cases,seleniumApi);
        }
    }

}
