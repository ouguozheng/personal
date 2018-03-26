package com;

import com.sun.net.httpserver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * @author 欧国正
 * Created on  2018/3/22 20:22
 */
public class MyServer {
    private static Logger logger = LoggerFactory.getLogger(MyServer.class);
    @Autowired
    private static SessionManagerService sessionManagerService;

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/test/", new myHandler());
        server.start();
        logger.info("start my server:{}", server.getAddress());
    }

    private static class myHandler implements HttpHandler {
        public void handle(final HttpExchange exchange) throws IOException {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        OutputStream os = exchange.getResponseBody();
                        InputStream is = exchange.getRequestBody();
                        Map<String, Object> session = new HashMap<String, Object>();
                        if (exchange.getRequestMethod().equals("GET")) {
                            String prams[] = exchange.getRequestURI().getQuery().split("&");
                            for (String p : prams) {
                                String parm[] = p.split("=");
                                if (parm.length != 2) {
                                    continue;
                                }
                                session.put(parm[0], parm[1]);
                            }
                            if (sessionManagerService.getSession(session.get("id").toString()) == null) {
                                String key = sessionManagerService.genId();
                                session.put("id", key);
                            }
                            session.put("heads", exchange.getRequestHeaders());
                            session.put("requestBody", exchange.getRequestBody());
                            sessionManagerService.setSession(session.get("id").toString(), session);
                            DeliverySessionCreationType deliverySessionCreationType = new DeliverySessionCreationType();
                            deliverySessionCreationType.setStopTime(new Date().getTime());
                            deliverySessionCreationType.setAction(DeliverySessionCreationType.ActionType.Start);
                            deliverySessionCreationType.setStartTime(new Date().getTime());
                            deliverySessionCreationType.setDeliverySessionId(session.get("id").toString());
                            String response = "200 ok response";
                            deliverySessionCreationType.setTMGI(response);
                            exchange.sendResponseHeaders(200, 0);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            os.write(deliverySessionCreationType.toString().getBytes());
                            os.close();
                        } else if (exchange.getRequestMethod().equals("POST")) {
                            DeliverySessionCreationType DeliverySession;
                            try {
                                DeliverySession = (DeliverySessionCreationType) exchange.getAttribute("DeliverySession");
                                if (DeliverySession.getDeliverySessionId() == null || DeliverySession.getDeliverySessionId().equals("")) {
                                    String key = sessionManagerService.genId();
                                    session.put("id", key);
                                    DeliverySession.setDeliverySessionId(key);
                                } else if (sessionManagerService.getSession(DeliverySession.getDeliverySessionId()) == null) {
                                    String key = sessionManagerService.genId();
                                    session.put("id", key);
                                    DeliverySession.setDeliverySessionId(key);
                                }
                                session.put("heads", exchange.getRequestHeaders());
                                session.put("requestBody", exchange.getRequestBody());
                                sessionManagerService.setSession(session.get("id").toString(), session);
                                if (DeliverySession.getAction().equals(DeliverySessionCreationType.ActionType.Stop)) {
                                    sessionManagerService.removeSession(session.get("id").toString());
                                    sessionManagerService.uninit();
                                    DeliverySession.setStopTime(new Date().getTime());
                                    DeliverySession.setAction(DeliverySessionCreationType.ActionType.Stop);
                                    String response = "200 ok response";
                                    DeliverySession.setTMGI(response);
                                    exchange.sendResponseHeaders(200, 0);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    os.write(DeliverySession.toString().getBytes());
                                } else {
                                    DeliverySession.setAction(DeliverySessionCreationType.ActionType.Start);
                                    DeliverySession.setStartTime(new Date().getTime());
                                    String response = "200 ok response";
                                    DeliverySession.setTMGI(response);
                                    exchange.sendResponseHeaders(200, 0);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    os.write(DeliverySession.toString().getBytes());
                                }
                                os.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                os.write("worng request body".getBytes());
                                os.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });

            thread.start();
        }
    }
}
