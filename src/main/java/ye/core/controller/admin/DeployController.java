package ye.core.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * 系统部署控制层
 * @author AceLau
 * 2016年7月22日
 */
@Controller
public class DeployController {
	
	@Autowired
	private GeneralCacheAdministrator osCache;

	@RequestMapping("/deploy/OSCache.do")
	public String OSCache(){
		System.out.println("");
		Cache cache = osCache.getCache();
		osCache.destroy();
		osCache.flushAll();
		return "deploy/OSCache";
	}
}
