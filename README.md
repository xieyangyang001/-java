# 关于selenium自动化的小项目
----
### 一、项目说明
selenium前端自动测测试（包括无界面和有界面两种方式）

### 二、目录介绍

| 目录 | 介绍   |
| -------- |-----:|
| src\test\java\api| 封装selenium、db操作、常用的工具类 |
| src\test\java\api\canstants| 一些成员变量  |
| src\test\java\api\DB|   一些数据库操作   |
| src\test\java\api\frame|    selenium框架的二次封装    |
| src\test\java\api\util|    一些工具类（excel解析、简单的ocr识别）    |
| src\test\java\BMSWeb| 具体到的测试封装（如果有需要可以自己改） |
| src\test\java\BMSWeb\bean| excel对应的javabean |
| src\test\java\BMSWeb\db| 这个暂时还有用到 |
| src\test\java\BMSWeb\sevice| 测试步骤封装 |
| src\test\java\BMSWeb\test| 写测试类的目录 |
| src\test\resources| 一些配置和驱动 |
| src\test\resources\browserDriver| 浏览器驱动（如果不适合你的浏览器版本，请重新下载） |
| src\test\java\BMSWeb\db|  mysql的连接地址|
| src\test\resources\excel| excel目录 |
| src\test\java\BMSWeb\pic| ocr识别时候的图片 |
| src\test\java\BMSWeb\tessdata| 需要ocr支持的驱动 |

###三、使用方式说明

####1、例子
+ 例子：
    + 在excel目录新建一个Test.xls文件，下面介绍一下excle
    + 在写测试类的包下写一个叫Test.java的测试类
    + 写一个测试方法名字对应excel的sheet

####2、excel介绍
| case_id| desc|requestUrl |elementJson|assertElementJSON|assertSqlElementJson|assertSqlJson|retryCount|browsers|
| --------| -----:|:----:|:----:|:----:|:----:|:----:|:----:|:----:|
|测试用例的id| 测试用例描述| 请求地址|页面需要操作的元素| 断言需要的元素|断言需要的sql对应的元素 |断言需要的sql|重复次数（暂不支持）|浏览器名称（例如chrome只支持小写） |


####3、测试类的测试方法介绍
```java
//必须继承parseExcel
public class Test extends ParseExcel {
	//若是集成测试写excelToMap，若是单测写excelToMapSingle
    @Test(dataProvider = "excelToMap")
	//测试方法对应excel的sheet
    public void test(Map maps){
        ProcessService processService=new ProcessService();
        processService.executeStep(maps); //测试封装方法
    }
}
```

###联系方式
如有帮助请联系邮箱地址：1396925801@qq.com , this all , 三克油 :smile:

----