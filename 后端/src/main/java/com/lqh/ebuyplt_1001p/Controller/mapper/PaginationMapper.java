package com.lqh.ebuyplt_1001p.Controller.mapper;

import com.lqh.ebuyplt_1001p.Controller.AdminPack.*;

import java.util.ArrayList;

public interface PaginationMapper
{
    public ArrayList<UserAccountTableItem> UserAccountTablePagination(PageItem para);

    public ArrayList<UserDeliveryInfoTableItem> UserDeliveryInfoTablePagination(PageItem para);

    public ArrayList<UserFavoritesTableItem> UserFavoritesTablePagination(PageItem para);

    public ArrayList<UserShoppingCartTableItem> UserShoppingCartTablePagination(PageItem para);

    public ArrayList<MerchantsProductTableItem> MerchantsProductTablePagination(PageItem para);

    public ArrayList<ProductTableItem> ProductTablePagination(PageItem para);

    public ArrayList<ProductImagesTableItem> ProductImagesTablePagination(PageItem para);

    public ArrayList<ProductClicksInfoTableItem> ProductClicksInfoTablePagination(PageItem para);

    public ArrayList<OrderFullInfoTableItem> OrderFullInfoTablePagination(PageItem para);

    public ArrayList<OrderProductInfoTableItem> OrderProductInfoTablePagination(PageItem para);

}
