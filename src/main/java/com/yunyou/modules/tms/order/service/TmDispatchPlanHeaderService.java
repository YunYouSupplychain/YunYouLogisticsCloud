package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanHeader;
import com.yunyou.modules.tms.order.mapper.TmDispatchPlanHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调度计划单头Service
 * @author WMJ
 * @version 2020-07-07
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchPlanHeaderService extends CrudService<TmDispatchPlanHeaderMapper, TmDispatchPlanHeader> {

}