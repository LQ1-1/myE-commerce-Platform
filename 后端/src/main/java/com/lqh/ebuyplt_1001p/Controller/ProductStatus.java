package com.lqh.ebuyplt_1001p.Controller;

import java.util.HashMap;
import java.util.Map;

public class ProductStatus
{
    public String StatusErro="NoSuchStatus";

    public String Status1="OnSale";
    public String Status2="OffSale";

    public enum ProductStatusEnum
    {
        NoSuchStatus,
        OnSale,
        OffSale,
    };

    //字符串到枚举的映射
    HashMap<String, ProductStatus.ProductStatusEnum> StringToEnum=new HashMap<String, ProductStatus.ProductStatusEnum>(
            Map.ofEntries(
                    Map.entry(StatusErro, ProductStatus.ProductStatusEnum.NoSuchStatus),
                    Map.entry(Status1, ProductStatus.ProductStatusEnum.OnSale),
                    Map.entry(Status2, ProductStatus.ProductStatusEnum.OffSale)
            )
    );
    //枚举到字符串的映射
    HashMap<ProductStatus.ProductStatusEnum,String> EnumToString=new HashMap<ProductStatus.ProductStatusEnum,String>(
            Map.ofEntries(
                    Map.entry(ProductStatus.ProductStatusEnum.NoSuchStatus,StatusErro),
                    Map.entry(ProductStatus.ProductStatusEnum.OnSale,Status1),
                    Map.entry(ProductStatus.ProductStatusEnum.OffSale,Status2)
            )
    );

}

/*

    public String Status1="上架";
    public String Status2="下架";

 */
