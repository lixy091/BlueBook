package com.lixy.bluebook.Service;

import com.lixy.bluebook.Entity.Shop;
import com.lixy.bluebook.Utils.ResponseData;

/**
 * @author lixy
 */
public interface ShopService {

    ResponseData getShopById(Long id);

    ResponseData updateShop(Shop shop);

    ResponseData addHotShop(Long id);
}
