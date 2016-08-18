package ye.common.page;

public abstract interface Paginable {
	/**
	 * 获取总记录数
	 * @return
	 */
	public abstract int getTotalCount();

	/**
	 * 获取总页面数
	 * @return
	 */
	public abstract int getTotalPage();

	/**
	 * 获取页面大小
	 * @return
	 */
	public abstract int getPageSize();

	/**
	 * 获取页面个数
	 * @return
	 */
	public abstract int getPageNo();

	/**
	 * 是否第一页
	 * @return
	 */
	public abstract boolean isFirstPage();

	/**
	 * 是否最后一页
	 * @return
	 */
	public abstract boolean isLastPage();

	/**
	 * 获取下一页
	 * @return
	 */
	public abstract int getNextPage();

	/**
	 * 获取上一页
	 * @return
	 */
	public abstract int getPrePage();
}