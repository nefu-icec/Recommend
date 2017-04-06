package com.xwkj.recommend.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.recommend.dao.WorkerDao;
import com.xwkj.recommend.domain.Worker;

public class WorkerDaoHibernate extends BaseHibernateDaoSupport<Worker> implements WorkerDao {

    public WorkerDaoHibernate() {
        super();
        setClass(Worker.class);
    }

}