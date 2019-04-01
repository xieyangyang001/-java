package BMSWeb.bean;


import com.alibaba.fastjson.JSONArray;

/**
 * Created by XieYangYang on 2018/11/23.
 */
public class Case {
    //用例id
    public String caseId;
    //案例描述
    public String desc;
    //请求URl
    public String requestUrl;
    //验证码的值
    public String imgVerificationCode;
    //需要输入的元素的JsonArray（[{"element":"元素","elementType":"用什么方法取出element","executeType":"操作类型","elementValue":"可写可不写"}]）
    public JSONArray elementJson;
    //判断页面是否存在的元素
    public JSONArray assertElementJson;
    //页面元素对应数据库的值；
    public JSONArray assertSqlElementJson;
    //sql映射的json
    public JSONArray assertSqlJson;
    //循环次数  ToDo
    public String retryCount;
    //浏览器
    public String browsers;

    public String getBrowsers() {
        return browsers;
    }

    public void setBrowsers(String browsers) {
        this.browsers = browsers;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public JSONArray getElementJson() {
        return elementJson;
    }

    public String getImgVerificationCode() {
        return imgVerificationCode;
    }

    public void setImgVerificationCode(String imgVerificationCode) {
        this.imgVerificationCode = imgVerificationCode;
    }

    public void setElementJson(String elementJson) {
        if (!elementJson.equals("") && elementJson != null) {
            this.elementJson = JSONArray.parseArray(elementJson);
        } else {
            this.elementJson = null;
        }
    }

    public JSONArray getAssertElementJson() {
        return assertElementJson;
    }

    public void setAssertElementJson(String assertElementJson) {
        if (!assertElementJson.equals("") && assertElementJson != null) {
            this.assertElementJson = JSONArray.parseArray(assertElementJson);
        }else{
            this.assertElementJson = null;
        }

    }

    public JSONArray getAssertSqlElementJson() {
        return assertSqlElementJson;
    }

    public void setAssertSqlElementJson(String assertSqlElementJson) {
        if (!assertSqlElementJson.equals("") && assertSqlElementJson != null) {
            this.assertSqlElementJson = JSONArray.parseArray(assertSqlElementJson);
        }else{
            this.assertSqlElementJson = null;
        }

    }

    public JSONArray getAssertSqlJson() {
        return assertSqlJson;
    }

    public void setAssertSqlJson(String assertSqlJson) {
        if (!assertSqlJson.equals("") && assertSqlJson != null) {
            this.assertSqlJson = JSONArray.parseArray(assertSqlJson);
        }else{
            this.assertSqlJson = null;
        }

    }

    public String getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }
}
