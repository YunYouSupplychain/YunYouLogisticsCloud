package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsContractStorageSteppedPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractSteppedPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：运输阶梯价格Mapper
 *
 * @author liujianhua created on 2019-11-14
 */
@MyBatisMapper
public interface BmsContractStorageSteppedPriceMapper extends BaseMapper<BmsContractStorageSteppedPrice> {

    void deleteByFkId(String id);

    List<BmsContractStorageSteppedPrice> findByFkId(@Param("fkId") String fkId);

    List<BmsContractSteppedPrice> findSteppedPriceByFkId(@Param("fkId") String fkId);
}