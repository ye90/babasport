package ye;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.danga.MemCached.MemCachedClient;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import ye.common.junit.SpringJunitTest;
import ye.common.utils.MemCachedUtil;
import ye.core.bean.user.Buyer;

/**
 * 测试
 * @author lx
 *
 */

public class TestMemcached extends SpringJunitTest{

	@Autowired
	private MemCachedClient memCachedClient;
	@Autowired
	private GeneralCacheAdministrator xx;
	
	@Test
	public void testAdd() throws Exception {
		Buyer buyer = new Buyer();
		buyer.setUsername("范冰冰");
		
		//memCachedClient.set("fbb2",buyer);
		
		List<String> allKeys = MemCachedUtil.getAllKeys(memCachedClient);
		for (String string : allKeys) {
			System.out.println(string);
		}
		
/*		Object object = memCachedClient.get("fbb");
		System.out.println(object);*/
	}
}
