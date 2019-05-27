package com.whyxs.common.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedisSessionDAO extends AbstractSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    private RedisManager redisManager;  
      
    private String keyPrefix = "shiro_redis_session:";  
      
    @Override  
    public void update(Session session) throws UnknownSessionException {  
        this.saveSession(session);  
    }  
      
    private void saveSession(Session session) throws UnknownSessionException{  
        if(session == null || session.getId() == null){  
            logger.error("session or session id is null");  
            return;  
        }  
        byte[] key = getByteKey(session.getId());  
        byte[] value = Serialize.serialize(session);
        session.setTimeout(redisManager.getExpire()*1000);        
        this.redisManager.set(key, value, redisManager.getExpire());  
    }  
  
    @Override  
    public void delete(Session session) {  
        if(session == null || session.getId() == null){  
            logger.error("session or session id is null");  
            return;  
        }  
        redisManager.del(this.getByteKey(session.getId()));  
  
    }  
  
    //用来统计当前活动的session  
    @Override  
    public Collection<Session> getActiveSessions() {  
        Set<Session> sessions = new HashSet<Session>();  
        Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");  
        if(keys != null && keys.size()>0){  
            for(byte[] key:keys){  
                /*Session s = (Session)SerializeUtils.deserialize(redisManager.get(key));*/
                Session s = (Session)Serialize.deserialize(redisManager.get(key));  
                sessions.add(s);  
            }  
        }  
        return sessions;  
    }  
  
    @Override  
    protected Serializable doCreate(Session session) {  
        Serializable sessionId = this.generateSessionId(session);    
        this.assignSessionId(session, sessionId);  
        this.saveSession(session);  
        return sessionId;  
    }  
  
    @Override  
    protected Session doReadSession(Serializable sessionId) {  
        if(sessionId == null){  
            logger.error("session id is null");  
            return null;  
        }  
        Session s = (Session)Serialize.deserialize(redisManager.get(this.getByteKey(sessionId)));  
        return s;  
    }  
      
    private byte[] getByteKey(Serializable sessionId){  
        String preKey = this.keyPrefix + sessionId;  
        return preKey.getBytes();  
    }  
  
    public RedisManager getRedisManager() {  
        return redisManager;  
    }  
  
    public void setRedisManager(RedisManager redisManager) {  
        this.redisManager = redisManager;  
        this.redisManager.init();  
    }  
  
    public String getKeyPrefix() {  
        return keyPrefix;  
    }  
  
    public void setKeyPrefix(String keyPrefix) {  
        this.keyPrefix = keyPrefix;  
    }  
}
