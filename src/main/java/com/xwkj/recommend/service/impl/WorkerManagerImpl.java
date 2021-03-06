package com.xwkj.recommend.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.recommend.bean.WorkerBean;
import com.xwkj.recommend.domain.Worker;
import com.xwkj.recommend.service.WorkerManager;
import com.xwkj.recommend.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "WorkerManager")
public class WorkerManagerImpl extends ManagerTemplate implements WorkerManager {

    @RemoteMethod
    @Transactional
    public boolean addWorker(String number, String name, String password, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Worker worker = workerDao.getByNumber(number);
        if (worker != null) {
            Debug.error("Cannot find the worker by this worker number.");
            return false;
        }
        worker = new Worker();
        worker.setNumber(number);
        worker.setName(name);
        worker.setPassword(password);
        worker.setState(true);
        workerDao.save(worker);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean removeWorker(String wid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Worker worker = workerDao.get(wid);
        if (worker == null) {
            Debug.error("Cannot find the worker by this wid.");
            return false;
        }
        if (orderDao.getOrderCountForWorker(worker) > 0) {
            Debug.error("Cannot delete this worker because this worker has orders.");
            return false;
        }
        workerDao.delete(worker);
        return true;
    }

    @RemoteMethod
    public List<WorkerBean> getWorkers(boolean onlyEnable, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        List<WorkerBean> workerBeans = new ArrayList<WorkerBean>();
        List<Worker> workers = onlyEnable? workerDao.findEnable() : workerDao.findAll("number", false);
        for (Worker worker : workers) {
            workerBeans.add(new WorkerBean(worker));
        }
        return workerBeans;
    }

    @RemoteMethod
    @Transactional
    public boolean changeState(String wid, boolean state, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Worker worker = workerDao.get(wid);
        if (worker == null) {
            Debug.error("Cannot find this worker by this wid.");
            return false;
        }
        worker.setState(state);
        workerDao.update(worker);
        return true;
    }

    @RemoteMethod
    public boolean login(String number, String password, HttpSession session) {
        Worker worker = workerDao.getByNumber(number);
        if (worker == null) {
            Debug.error("Cannot find the worker by this worker number.");
            return false;
        }
        if (!worker.getPassword().equals(password)) {
            Debug.error("Worker's password is wrong!");
            return false;
        }
        session.setAttribute(WorkerFlag, worker.getWid());
        return true;
    }

    @RemoteMethod
    public WorkerBean checkSession(HttpSession session) {
        Worker worker = getWorkerFromSession(session);
        if (worker == null) {
            return null;
        }
        return new WorkerBean(worker);
    }

}
