package ye.core.web;
/**
 * 业务常量
 * @author lx
 *
 */
public interface Constants {

	/**
	 * 图片服务器
	 */
	public static final String IMAGE_URL = "http://localhost:8088/image-web/";
	/**
	 * Integer=1
	 */
	public static final Integer YES = 1;
	
	/**
	 * Integer=0
	 */
	public static final Integer NO = 0;
	
	/**  前台每页数  */
	public static final int FRONT_PRODUCT_PAGE_SIZE = 8;
	/** 用户 session 的 cookie 名称*/
	public static final String SESSION_ID = "JSESSIONID";
	
	/**
	 * 用户Session
	 */
	public static final String BUYER_SESSION = "buyer_session";
	/**
	 * 购物车Cookie
	 */
	public static final String BUYCART_COOKIE = "buyCart_cookie";
}
