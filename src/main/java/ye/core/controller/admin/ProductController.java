package ye.core.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import ye.common.page.Pagination;
import ye.core.bean.product.Brand;
import ye.core.bean.product.Color;
import ye.core.bean.product.Feature;
import ye.core.bean.product.Img;
import ye.core.bean.product.Product;
import ye.core.bean.product.Sku;
import ye.core.bean.product.Type;
import ye.core.query.product.BrandQuery;
import ye.core.query.product.ColorQuery;
import ye.core.query.product.FeatureQuery;
import ye.core.query.product.ProductQuery;
import ye.core.query.product.TypeQuery;
import ye.core.service.product.BrandService;
import ye.core.service.product.ColorService;
import ye.core.service.product.FeatureService;
import ye.core.service.product.ProductService;
import ye.core.service.product.SkuService;
import ye.core.service.product.TypeService;
import ye.core.service.staticpage.StaticPageService;
import ye.core.web.Constants;

/**
 * 后台商品管理 商品列表 商品添加' 商品上架
 * 
 * @author lx
 *
 */
@Controller
public class ProductController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private FeatureService featureService;
	@Autowired
	private ColorService colorService;
	@Autowired
	private SkuService	skuService;
	@Autowired
	private StaticPageService staticPageService;

	/**
	 * 商品管理--列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/list.do")
	public String list(Integer pageNo, String name, Integer brandId, Integer isShow, ModelMap model) {
		// 默认加载品牌
		BrandQuery brandQuery = new BrandQuery();
		// 设置 可见不可见
		brandQuery.setIsDisplay(Constants.YES);
		// 设置只要Id Name
		brandQuery.setFields("id,name");
		List<Brand> brands = brandService.getBrandList(brandQuery);
		model.addAttribute("brands", brands);

		StringBuilder params = new StringBuilder();
		// 设置条件的对象
		ProductQuery productQuery = new ProductQuery();
		// 默认加载
		// 1:设置未删除
		productQuery.setIsDel(Constants.YES);
		// 2:设置下架状态
		productQuery.setIsShow(Constants.NO);
		productQuery.orderbyId(false);

		// 拼接查询条件
		if (StringUtils.isNotBlank(name)) {
			productQuery.setName(name);
			productQuery.setNameLike(true);
			params.append("&name=" + name);
			// 回显查询条件
			model.addAttribute("name", name);
		}
		// 品牌ID
		if (null != brandId) {
			productQuery.setBrandId(brandId);
			params.append("&brandId=" + brandId);
			// 回显品牌
			model.addAttribute("brandId", brandId);
		}
		// 上下架
		if (null != isShow) {
			productQuery.setIsShow(isShow);
			params.append("&isShow=" + isShow);
			model.addAttribute("isShow", isShow);
		} else {
			model.addAttribute("isShow", Constants.NO);
		}
		productQuery.setPageNo(Pagination.cpn(pageNo));// 1 10

		productQuery.setPageSize(5);

		// 分页对象的使用方法
		Pagination pagination = productService.getProductListWithPage(productQuery);

		String url = "/product/list.do";
		// String params = "brandId=1&name=2014瑜伽服套装新款&pageNo=1";
		pagination.pageView(url, params.toString());

		// <a href="javascript:void(0)"
		// onclick="javascript:window.location.href='
		// /product/list.do?&=1&name=2014瑜伽服套装新款&pageNo=1'" />

		model.addAttribute("pagination", pagination);

		return "product/list";
	}

	// 去添加页面
	@RequestMapping(value = "/product/toAdd.do")
	public String toAdd(ModelMap model) {
		// 加载商品类型
		TypeQuery typeQuery = new TypeQuery();
		// 指定查询哪些字段
		typeQuery.setFields("id,name");
		typeQuery.setIsDisplay(Constants.YES);
		List<Type> types = typeService.getTypeList(typeQuery);
		// 显示在页面
		model.addAttribute("types", types);
		// 加载商品品牌
		// 品牌条件对象
		BrandQuery brandQuery = new BrandQuery();
		// 设置指定字段
		brandQuery.setFields("id,name");
		// 设置可见
		brandQuery.setIsDisplay(Constants.YES);
		// 加载品牌
		List<Brand> brands = brandService.getBrandList(brandQuery);
		// 显示在页面
		model.addAttribute("brands", brands);
		// 加载商品属性
		FeatureQuery featureQuery = new FeatureQuery();

		List<Feature> features = featureService.getFeatureList(featureQuery);
		// 显示在页面
		model.addAttribute("features", features);
		// 加载颜色
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.setParentId(0);// 什么什么系列
		List<Color> colors = colorService.getColorList(colorQuery);
		// 显示在页面
		model.addAttribute("colors", colors);

		return "product/add";
	}

	// 商品添加
	@RequestMapping(value = "/product/add.do")
	public String add(Product product, Img img) {
		// 1:商品 表 图片表 SKu表
		product.setImg(img);
		// 传商品对象到Servcie
		productService.addProduct(product);

		return "redirect:/product/list.do";
	}

	// 上架
	@RequestMapping(value = "/product/isShow.do")
	public String isShow(Integer[] ids, Integer pageNo, String name, Integer brandId, Integer isShow, ModelMap model) {
		// 实例化商品
		Product product = new Product();
		product.setIsShow(1);
		// 上架
		if (null != ids && ids.length > 0) {
			for (Integer id : ids) {
				product.setId(id);
				// 修改上架状态
				productService.updateProductByKey(product);
				
				/*静态化 */
				Map<String,Object> root = new HashMap<String,Object>();
				//设置值
				//商品加载
				Product p = productService.getProductByKey(id);
				
				root.put("product", p);
				
				//skus
				List<Sku> skus = skuService.getStock(id);
				root.put("skus", skus);
				//去重复
				List<Color>  colors = new ArrayList<Color>();
				//遍历SKu
				for(Sku sku : skus){
					//判断集合中是否已经有此颜色对象了
					if(!colors.contains(sku.getColor())){
						colors.add(sku.getColor());
					}
				}
				root.put("colors", colors);
				staticPageService.productIndex(root, id);
			}
		}

	
		// 回显判断
		if (null != pageNo) {
			model.addAttribute("pageNo", pageNo);
		}
		if (StringUtils.isNotBlank(name)) {
			model.addAttribute("name", name);
		}
		if (null != brandId) {
			model.addAttribute("brandId", brandId);
		}
		if (null != isShow) {
			model.addAttribute("isShow", isShow);
		}

		return "redirect:/product/list.do";
	}

}
