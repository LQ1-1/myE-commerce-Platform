package com.lqh.ebuyplt_1001p.Controller;

import java.util.HashMap;
import java.util.Map;

public class UserAccountStatus
{
    public String StatusErro="NoSuchStatus";

    public String Status1="Normal";
    public String Status2="Banned";
    public String Status3="Deleted";

    public enum UserAccountStatusEnum
    {
        NoSuchStatus,
        Normal,
        Banned,
        Deleted,
    };

    //字符串到枚举的映射
    HashMap<String, UserAccountStatus.UserAccountStatusEnum> StringToEnum=new HashMap<String, UserAccountStatus.UserAccountStatusEnum>(
            Map.ofEntries(
                    Map.entry(StatusErro, UserAccountStatus.UserAccountStatusEnum.NoSuchStatus),
                    Map.entry(Status1, UserAccountStatus.UserAccountStatusEnum.Normal),
                    Map.entry(Status2, UserAccountStatus.UserAccountStatusEnum.Banned),
                    Map.entry(Status3, UserAccountStatus.UserAccountStatusEnum.Deleted)
            )
    );
    //枚举到字符串的映射
    HashMap<UserAccountStatus.UserAccountStatusEnum,String> EnumToString=new HashMap<UserAccountStatus.UserAccountStatusEnum,String>(
            Map.ofEntries(
                    Map.entry(UserAccountStatus.UserAccountStatusEnum.NoSuchStatus,StatusErro),
                    Map.entry(UserAccountStatus.UserAccountStatusEnum.Normal,Status1),
                    Map.entry(UserAccountStatus.UserAccountStatusEnum.Banned,Status2),
                    Map.entry(UserAccountStatus.UserAccountStatusEnum.Deleted,Status3)
            )
    );


}

/*

    public String Status1="正常";
    public String Status2="封禁";
    public String Status3="注销";

 */
