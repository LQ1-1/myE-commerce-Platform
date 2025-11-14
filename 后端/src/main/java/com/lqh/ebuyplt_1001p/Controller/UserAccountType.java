package com.lqh.ebuyplt_1001p.Controller;

import java.util.HashMap;
import java.util.Map;

public class UserAccountType
{
    public String TypeErro="NoSuchType";

    public String Type1="RegularUser";
    public String Type2="Merchant";
    public String Type3="Administrator";

    public enum UserAccountTypeEnum
    {
        NoSuchType,
        RegularUser,
        Merchant,
        Administrator,
    };

    //字符串到枚举的映射
    HashMap<String, UserAccountType.UserAccountTypeEnum> StringToEnum=new HashMap<String, UserAccountType.UserAccountTypeEnum>(
            Map.ofEntries(
                    Map.entry(TypeErro, UserAccountType.UserAccountTypeEnum.NoSuchType),
                    Map.entry(Type1, UserAccountType.UserAccountTypeEnum.RegularUser),
                    Map.entry(Type2, UserAccountType.UserAccountTypeEnum.Merchant),
                    Map.entry(Type3, UserAccountType.UserAccountTypeEnum.Administrator)
            )
    );
    //枚举到字符串的映射
    HashMap<UserAccountType.UserAccountTypeEnum,String> EnumToString=new HashMap<UserAccountType.UserAccountTypeEnum,String>(
            Map.ofEntries(
                    Map.entry(UserAccountType.UserAccountTypeEnum.NoSuchType,TypeErro),
                    Map.entry(UserAccountType.UserAccountTypeEnum.RegularUser,Type1),
                    Map.entry(UserAccountType.UserAccountTypeEnum.Merchant,Type2),
                    Map.entry(UserAccountType.UserAccountTypeEnum.Administrator,Type3)
            )
    );

}


/*

public String Type1="普通用户";
    public String Type2="商家";
    public String Type3="管理员";

 */