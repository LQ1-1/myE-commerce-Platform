package com.lqh.ebuyplt_jsp.Controller.ResultPack;

import java.time.OffsetDateTime;

public class ApiResult<T>
{
    private int code;                           //状态码
    private String message;                     //状态解释
    private T data;                             //泛型字段，传输结果数据
    private OffsetDateTime timestamp;           //相应时间


    //           泛型标记         ApiResult<T>：返回值类型，表示这个方法返回的是一个ApiResult对象，并且这个对象里的data字段类型是T（和参数data的类型一致）
    public static <T> ApiResult<T> success(T data)
    {
        ApiResult<T> r = new ApiResult<T>();
        r.code=200;
        r.message="success";
        r.data=data;
        r.timestamp=OffsetDateTime.now();
        return r;
    }

    public static <T>ApiResult<T> error(int CODE,String MESSAGE)
    {
        ApiResult<T>r=new ApiResult<T>();
        r.code=CODE;
        r.message=MESSAGE;
        r.timestamp=OffsetDateTime.now();
        return r;
    }
    public int getCode()
    {
        return code;
    }
    public String getMessage()
    {
        return message;
    }
    public T getData()
    {
        return data;
    }
}
