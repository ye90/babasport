package ye.core.service.product;

import java.util.List;

import ye.common.page.Pagination;
import ye.core.bean.product.Brand;
import ye.core.query.product.BrandQuery;

/**
 * 品牌
 * 
 * @author lx
 *
 */
public interface BrandService {

	public Pagination getBrandListWithPage(Brand brand);
	
	//查询集合
	public List<Brand> getBrandList(BrandQuery brandQuery);

	// 添加品牌
	public void addBrand(Brand brand);

	// 删除
	public void deleteBrandByKey(Integer id);

	// 删除 批量
	public void deleteBrandByKeys(Integer[] ids);// List<Integer> ids
	// 修改

	public void updateBrandByKey(Brand brand);
	
	public Brand getBrandByKey(Integer id);
}
