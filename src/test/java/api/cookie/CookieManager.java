package api.cookie;


import org.openqa.selenium.Cookie;

import java.util.Set;

/**
 * Created by XieYangYang on 2019/4/2.
 */
public class CookieManager {

    private static volatile CookieManager cookieManager;

    private CookieManager(){}

    public static CookieManager getCookieManager(){
        if(cookieManager==null){
            synchronized(CookieManager.class){
                if(cookieManager==null){
                    cookieManager=new CookieManager();
                }
            }
        }
        return cookieManager;
    }

    public Set<Cookie> cookieValues;

    public Set<Cookie> getCookieValues() {
        return cookieValues;
    }

    public void setCookieValues(Set<Cookie> cookieValues) {
        this.cookieValues = cookieValues;
    }
}
