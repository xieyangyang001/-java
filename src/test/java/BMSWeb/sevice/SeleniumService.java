package BMSWeb.sevice;

import BMSWeb.bean.Case;
import api.DB.DBCommon;
import api.canstants.PathFileCanstant;
import api.cookie.CookieManager;
import api.frame.SeleniumApi;
import api.util.PictVerification;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.File;
import java.util.Set;

/**
 * Created by XieYangYang on 2018/12/3.
 */
public class SeleniumService {

    /**
     * @param elementJsonArray 元素和值的操作数据
     * @param webDriver        selenium的webDriver
     * @param cases            case对象
     * @param seleniumApi      selenium操作类
     */
    public static void seleniumExecuteJsonArray(JSONArray elementJsonArray, WebDriver webDriver, Case cases, SeleniumApi seleniumApi) {
        for (Object elementJsonObject : elementJsonArray) {
            JSONObject elementJson = JSONObject.parseObject(String.valueOf(elementJsonObject));
            String executeType = elementJson.getString("executeType");
            switch (executeType) {
                case "button":
                    WebElement clickElement = seleniumApi.findElement(elementJson.getString("element"), elementJson.getString("elementType"), cases.desc, webDriver);
                    clickElement.click();
                    break;
                case "input":
                    WebElement inputElement = seleniumApi.findElement(elementJson.getString("element"), elementJson.getString("elementType"), cases.desc, webDriver);
                    inputElement.clear();
                    inputElement.sendKeys(elementJson.getString("elementValue"));
                    break;
                case "imgVerificationCode":
                    WebElement imgVerificationCodeElement = seleniumApi.findElement(elementJson.getString("element"), elementJson.getString("elementType"), cases.desc, webDriver);
                    File file = seleniumApi.savePic(imgVerificationCodeElement, webDriver, new File(PathFileCanstant.picVerificationOldPath).getAbsoluteFile());
                    PictVerification pictVerification = new PictVerification();
                    String code = pictVerification.picToCharacter();
                    WebElement verificationCodeElement = seleniumApi.findElement(elementJson.getString("elementCode"), elementJson.getString("elementCodeType"), cases.desc, webDriver);
                    verificationCodeElement.clear();
                    verificationCodeElement.sendKeys(code);
                    //未完待续。。。。//ToDo
            }
        }
    }

    /**
     * 断言判断 只判断前端
     *
     * @param webDriver        webDriver
     * @param cases            cases
     * @param seleniumApi      seleniumApi
     */
    public static void assertElement( WebDriver webDriver, Case cases, SeleniumApi seleniumApi) {
        JSONArray elementJsonArray=cases.getAssertElementJson();
        for (Object elementJsonObject : elementJsonArray) {
            JSONObject elementJson = JSONObject.parseObject(String.valueOf(elementJsonObject));
            WebElement webElement = seleniumApi.findElement(elementJson.getString("element"), elementJson.getString("elementType"), cases.desc, webDriver);
            if(!webElement.getText().equals(elementJson.getString("value"))){
                seleniumApi.screenShot(webDriver,cases.desc);
            }
            Assert.assertEquals(webElement.getText(), elementJson.getString("value"));
        }
    }

    /**
     * 元素sql校验（页面元素和数据库校验）本期只支持一个一个的校验
     * sqlElementJsonArray [{"id":"1","element":"元素","elementType":""}]
     * sqlJsonArray[{"id":"1","sql":"sql语句"}]
     * sqlElementJsonArray和sqlJsonArray必须一一对应
     *
     * @param webDriver   webDriver
     * @param cases       cases
     * @param seleniumApi seleniumApi
     */
    public static void assertSqlElement(WebDriver webDriver, Case cases, SeleniumApi seleniumApi) {
        JSONArray sqlElementJsonArray = cases.getAssertSqlElementJson();
        JSONArray sqlJsonArray = cases.getAssertSqlJson();
        for (int i = 0; i < sqlElementJsonArray.size(); i++) {
            JSONObject sqlElementJson = (JSONObject) sqlElementJsonArray.get(i);
            JSONObject sqlJson = (JSONObject) sqlJsonArray.get(i);
            WebElement webElement = seleniumApi.findElement(sqlElementJson.getString("element"), sqlElementJson.getString("elementType"), cases.desc, webDriver);
            JSONObject jsonObject = (JSONObject) DBCommon.querySqltoList(sqlJson.getString("sql")).get(0);
            for (String key : jsonObject.keySet()) {
                Assert.assertEquals(webElement, jsonObject.getString(key));
            }
        }
    }


    /**
     * cookieManager类赋值到selenium的cookie
     * @param webDriver
     * @param cases
     */
    public static void cookieManagertoselenium(WebDriver webDriver, Case cases) throws Exception {
        CookieManager cookieManager=CookieManager.getCookieManager();
        if(cases.getCookieManager().equals("2")){
            Set<Cookie>  cookies=cookieManager.getCookieValues();
            if(cookies!=null){
                for(Cookie cookie:cookies){
                    webDriver.manage().addCookie(cookie);
                }
            }else{
                webDriver.quit();
                throw new Exception("当需要加入cookie的时候cookie不能为空");
            }
        }
    }


    /**
     * selenium的cookie赋值到cookieManager类
     * @param webDriver
     * @param cases
     */
    public static void seleniumToCookieManager(WebDriver webDriver, Case cases){
        CookieManager cookieManager=CookieManager.getCookieManager();
        if(cases.getCookieManager().equals("1")){
            cookieManager.setCookieValues(webDriver.manage().getCookies());
        }
    }
}
