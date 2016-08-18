package ye.common.web.aop;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;

import com.danga.MemCached.MemCachedClient;

import ye.common.encode.Md5Utils;
import ye.common.utils.MemCachedUtil;

/**
 * TODO 缓存Memcached中数据的切面对象
 * @author ye
 * 2016年7月21日
 */
public class CacheInterceptor {

	@Autowired
	private MemCachedClient memCachedClient;
	
	private static final Logger logger = Logger.getLogger(CacheInterceptor.class);

	private int expiry ;
	
	//配置环绕方法
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
		//去Memcached中看看有没有我们的数据  包名+ 类名 + 方法名 + 参数(多个)
		String cacheKey = getCacheKey(pjp);
		logger.info("生成的cacheKey："+cacheKey);
		
		if(memCachedClient.stats().isEmpty()){
			logger.warn("Memcached服务器可能不存在或是连接不上");
			return pjp.proceed();
		}
		
		//返回值
		if(null == memCachedClient.get(cacheKey)){
			//回Service
			Object proceed = pjp.proceed();
			memCachedClient.set(cacheKey, proceed,expiry);
		}
		return memCachedClient.get(cacheKey);
	}
	//后置由于数据库数据变更  清理get*
	public void doAfter(JoinPoint jp){
		//包名+ 类名 
		String packageName = jp.getTarget().getClass().getName();
		
		//包名+ 类名  开始的 都清理
		List<String> allKeys = MemCachedUtil.getAllKeys(memCachedClient);
		for (String key : allKeys) {
			if(key.startsWith(packageName)){
				memCachedClient.delete(key);
			}
		}
	}
	
	// 包名+ 类名 + 方法名 + 参数(多个) 生成Key
	public String getCacheKey(ProceedingJoinPoint pjp){
		StringBuilder key = new StringBuilder();
		//包名+ 类名   cn.itcast.core.serice.product.ProductServiceImpl.productList
		String packageName = pjp.getTarget().getClass().getName();
		key.append(packageName);
		// 方法名
		String methodName = pjp.getSignature().getName();
		key.append(".").append(methodName);
		
		//参数
		Object[] args = pjp.getArgs();
		ObjectMapper  mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		for(Object arg : args){
			try {
				key.append(".").append(Md5Utils.encoder(mapper.writeValueAsString(arg)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return key.toString();
	}
	
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}
}
