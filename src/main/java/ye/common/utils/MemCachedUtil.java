package ye.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.danga.MemCached.MemCachedClient;

public class MemCachedUtil {
	
	private static final Logger logger = Logger.getLogger(MemCachedUtil.class);
	/**
	 * 获取服务器上面所有的key
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAllKeys(MemCachedClient memCachedClient) {
		logger.info("开始获取没有挂掉服务器中所有的key.......");
		List<String> list = new ArrayList<String>();
		Map<String, Map<String, String>> items = memCachedClient.statsItems();
		for (Iterator<String> itemIt = items.keySet().iterator(); itemIt.hasNext();) {
			String itemKey = itemIt.next();
			Map<String, String> maps = items.get(itemKey);
			for (Iterator<String> mapsIt = maps.keySet().iterator(); mapsIt.hasNext();) {
				String mapsKey = mapsIt.next();
				String mapsValue = maps.get(mapsKey);
				if (mapsKey.endsWith("number")) { // memcached key 类型
													// item_str:integer:number_str
					String[] arr = mapsKey.split(":");
					int slabNumber = Integer.valueOf(arr[1].trim());
					int limit = Integer.valueOf(mapsValue.trim());
					Map<String, Map<String, String>> dumpMaps = memCachedClient.statsCacheDump(slabNumber, limit);
					for (Iterator<String> dumpIt = dumpMaps.keySet().iterator(); dumpIt.hasNext();) {
						String dumpKey = dumpIt.next();
						Map<String, String> allMap = dumpMaps.get(dumpKey);
						for (Iterator<String> allIt = allMap.keySet().iterator(); allIt.hasNext();) {
							try {
								list.add(URLDecoder.decode(allIt.next(), "UTF-8"));//获取key值
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}

						}
					}
				}
			}
		}
		logger.info("获取没有挂掉服务器中所有的key完成.......");
		return list;
	}

}
