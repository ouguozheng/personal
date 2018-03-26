package com;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.*;

/**
 * @author 欧国正
 * Created on  2018/3/22 22:33
 */
public class DeliverySessionCreationType implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ActionType {
        Start, Stop
    }

    private Map<String, List<String>> heads = new HashMap<String, List<String>>();

    private String TMGIPool = "";
    private String TMGI = "";

    public String choise[] = new String[]{
            TMGIPool, TMGI
    };

    private String Version;
    private String DeliverySessionId;
    private ActionType Action;
    private Long StartTime;
    private Long StopTime;

    public Map<String, List<String>> getHeads() {
        return heads;
    }

    public void setHeads(Map<String, List<String>> heads) {
        this.heads = heads;
    }

    public String getTMGIPool() {
        return TMGIPool;
    }

    public void setTMGIPool(String TMGIPool) {
        this.TMGIPool = TMGIPool;
    }

    public String getTMGI() {
        return TMGI;
    }

    public void setTMGI(String TMGI) {
        this.TMGI = TMGI;
    }

    public String[] getChoise() {
        return choise;
    }

    public void setChoise(String[] choise) {
        this.choise = choise;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getDeliverySessionId() {
        return DeliverySessionId;
    }

    public void setDeliverySessionId(String deliverySessionId) {
        DeliverySessionId = deliverySessionId;
    }

    public ActionType getAction() {
        return Action;
    }

    public void setAction(ActionType action) {
        Action = action;
    }

    public Long getStartTime() {
        return StartTime;
    }

    public void setStartTime(Long startTime) {
        StartTime = startTime;
    }

    public Long getStopTime() {
        return StopTime;
    }

    public void setStopTime(Long stopTime) {
        StopTime = stopTime;
    }

    @Override
    public String toString() {
        return "DeliverySessionCreationType{" +
                "heads=" + heads +
                ", choise=" + Arrays.toString(choise) +
                ", Version='" + Version + '\'' +
                ", DeliverySessionId=" + DeliverySessionId +
                ", Action=" + Action +
                ", StartTime=" + StartTime +
                ", StopTime=" + StopTime +
                '}';
    }
}
