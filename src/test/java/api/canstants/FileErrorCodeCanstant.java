package api.canstants;

/**
 * Created by xieyangyang on 2018/11/8.
 */
public enum FileErrorCodeCanstant {
    FILE_ERROR_FORMAT("FILE_ERROR_FORMAT","文件格式出错"),
    FILE_ERROR_NOTFOUND("FILE_NOTFOUND","文件未找到");

    private String code;
    private String desc;
    FileErrorCodeCanstant(String code,String desc){
    this.code=code;
    this.desc=desc;
    }

    public String getCode(){
        return this.code=code;
    }

    public String getDesc(){
        return this.desc=desc;
    }
}
