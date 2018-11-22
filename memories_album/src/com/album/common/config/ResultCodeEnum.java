package com.album.common.config;


public enum ResultCodeEnum
{
	SUCCESS("1000", "Success"),
	NetERROR("1001","网络错误,请稍后再试"),
	NOT_FIND("1002","访问资源不存在"),
	DATABASE_ERROR("1003","数据库异常，请稍后重试"),
	UPDATE_ERROR("1004","修改失败"),
	ADD_ERROR("1005","添加失败"),
	DELETE_ERROR("1006","删除失败"),
	FIND_ERROR("1007","查询失败"),
	NOT_COMPLETE("1008","参数为空"),
	DATA_ERROR("1009","参数格式错误"),
	INNERERROR("1010","内部错误请重试"),
	DATA_EMPTY("1011","数据为空"),
	DECODE_ERROR("1012","解析错误"),
	UPLOAD_ERROR("1013","上传图片失败");
	

	
	
	private String code;
    private String desc;

    ResultCodeEnum(String code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    public String getCode()
    {
        return code;
    }

    public String getDesc()
    {
        return desc;
    }


}
