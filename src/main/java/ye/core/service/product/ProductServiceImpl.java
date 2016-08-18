package ye.core.service.product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ye.common.page.Pagination;
import ye.core.bean.product.Img;
import ye.core.bean.product.Product;
import ye.core.bean.product.Sku;
import ye.core.dao.product.ProductDao;
import ye.core.query.product.ImgQuery;
import ye.core.query.product.ProductQuery;
import ye.core.web.Constants;

/**
 * 商品事务层
 * 
 * @author lixu
 * @Date [2014-3-27 下午03:31:57]
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Resource
	ProductDao productDao;
	@Resource
	ImgService imgService;
	@Resource
	SkuService skuService;

	/**
	 * 插入数据库
	 * 
	 * @return
	 */
	public Integer addProduct(Product product) {

		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String no = df.format(new Date());
		// 后台生成商品编号
		product.setNo(no);
		// 后台生成添加时间
		product.setCreateTime(new Date());
		Integer i = productDao.addProduct(product);
		// 2
		product.getImg().setIsDef(Constants.YES);
		product.getImg().setProductId(product.getId());
		// 保存图片
		imgService.addImg(product.getImg());
		// 保存Sku
		Sku sku = new Sku();
		// 商品ID
		sku.setProductId(product.getId());
		// 运费
		sku.setDeliveFee(10.00);
		// 售价
		sku.setSkuPrice(0.00);
		// 市场价
		sku.setMarketPrice(0.00);
		// 库存
		sku.setStockInventory(0);
		// 购买限制
		sku.setSkuUpperLimit(0);
		// 添加时间
		sku.setCreateTime(new Date());
		// 是否最新
		sku.setLastStatus(1);
		// 商品
		sku.setSkuType(1);
		// 销量
		sku.setSales(0);
		for (String colorId : product.getColor().split(",")) {

			sku.setColorId(Integer.parseInt(colorId));

			for (String size : product.getSize().split(",")) {

				sku.setSize(size);

				skuService.addSku(sku);
			}
		}
		return i;

	}

	/**
	 * 根据主键查找
	 */
	@Transactional(readOnly = true)
	public Product getProductByKey(Integer id) {
		Product product = productDao.getProductByKey(id);
		ImgQuery imgQuery = new ImgQuery();
		imgQuery.setProductId(product.getId());
		imgQuery.setIsDef(1);
		List<Img> imgs = imgService.getImgList(imgQuery);
		product.setImg(imgs.get(0));
		
		return product;
	}

	@Transactional(readOnly = true)
	public List<Product> getProductsByKeys(List<Integer> idList) {
		return productDao.getProductsByKeys(idList);
	}

	/**
	 * 根据主键删除
	 * 
	 * @return
	 */
	public Integer deleteByKey(Integer id) {
		return productDao.deleteByKey(id);
	}

	public Integer deleteByKeys(List<Integer> idList) {
		return productDao.deleteByKeys(idList);
	}

	/**
	 * 根据主键更新
	 * 
	 * @return
	 */
	public Integer updateProductByKey(Product product) {
		return productDao.updateProductByKey(product);
	}

	@Transactional(readOnly = true)
	public Pagination getProductListWithPage(ProductQuery productQuery) {
		Pagination p = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(),
				productDao.getProductListCount(productQuery));
		List<Product> products = productDao.getProductListWithPage(productQuery);

		// 遍历商品 添加图片
		for (Product product : products) {
			ImgQuery imgQuery = new ImgQuery();
			imgQuery.setProductId(product.getId());
			imgQuery.setIsDef(1);
			List<Img> imgs = imgService.getImgList(imgQuery);
			product.setImg(imgs.get(0));
		}

		p.setList(products);
		return p;
	}

	@Transactional(readOnly = true)
	public List<Product> getProductList(ProductQuery productQuery) {
		return productDao.getProductList(productQuery);
	}
}
