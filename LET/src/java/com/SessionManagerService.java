package com;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * @author 欧国正
 * Created on  2018/3/26 11:05
 */
@Service
public class SessionManagerService {

    private int expired = 3600;// 秒
    private int checkInterval = 1800;// 秒
    private Timer timer = null;

    public int getExpired() {
        return expired;
    }

    static class DeleteJob extends TimerTask {

        Map<String, Map<String, Object>> sessions;
        Map<String, Date> sessionExpired;

        public DeleteJob(Map<String, Map<String, Object>> sessions, Map<String, Date> sessionExpired) {
            this.sessionExpired = sessionExpired;
            this.sessions = sessions;
        }

        @Override
        public void run() {
            Date now = new Date();
            for (String key : sessionExpired.keySet()) {
                if (now.after(sessionExpired.get(key))) {
                    sessions.remove(key);
                    sessionExpired.remove(key);
                }
            }

        }
    }

    @PostConstruct
    public void init() {
        // 删除过期的session
        DeleteJob job = new DeleteJob(sessions, sessionExpired);
        timer = new Timer(true);
        timer.schedule(job, 0, checkInterval);
    }

    @PreDestroy
    public void uninit() {
        if (timer != null) {
            try {
                DeleteJob job = new DeleteJob(sessions, sessionExpired);
                job.run();
                timer.cancel();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    // 设置session过期时间
    public void setExpired(int expired) {
        this.expired = expired;
    }

    public final static Map<String, Map<String, Object>> sessions = Collections
            .synchronizedMap(new HashMap<String, Map<String, Object>>());

    public final static Map<String, Date> sessionExpired = Collections.synchronizedMap(new HashMap<String, Date>());

    // 保存session
    public synchronized void setSession(String key, Map<String, Object> session) {
        this.sessions.put(key, session);
        Date now = new Date();
        this.sessionExpired.put(key, new Date(now.getTime() + expired * 1000));
    }

    // 删除session
    public void removeSession(String key) {
        this.sessions.remove(key);
    }

    // 获取session
    public Map<String, Object> getSession(String key) {
        return this.sessions.get(key);
    }

    // 生成sessionid
    public String genId() {
        return UUID.randomUUID().toString();
    }

}