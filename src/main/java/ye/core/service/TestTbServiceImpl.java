package ye.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ye.core.bean.TestTb;
import ye.core.dao.TestTbDao;

/**
 * 
 * @author lx
 *
 */
@Service
@Transactional
public class TestTbServiceImpl implements TestTbService {

	@Autowired
	private TestTbDao testTbDao;

	/*@Transactional(readOnly = true)*/ //只读一般用于查询 */
	public void addTestTb(TestTb testTb) {
		testTbDao.addTestTb(testTb);
		 /*throw new RuntimeException();*/
	}

}
