package com.point.web.dao;

import java.util.List;
import java.util.Map;
import com.point.web.entity.User;

/**
 * @Title: UserDao接口
 * @Description:
 * @Since: 2015年7月04日
 * @author yanbin_a
 *
 */
public interface UserDao {
	
	/**
	 * @Title: 查找用户数量接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public int getUserCountByName(String username);
	

	/**
	 * @Title: 查找全部的方法接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public List<User> findAll(Map map);
	

	/**
	 * @Title: 查找指定用户的方法接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public User findById(String id);

	/**
	 * @Title: 插入用户的方法接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public boolean insertUser(User user);

	/**
	 * @Title: 更新用户的方法接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public void update(User user);

	/**
	 * @Title: 删除批量用户的方法接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public void deleteByParams(String[] params);

	/**
	 * @Title: 删除指定用户的方法接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public void delete(User user);

	/**
	 * @Title: 按条件用户的方法接口
	 * @Description:
	 * @Since: 2015年7月04日
	 * @param yanbin_a
	 */
	public List<User> findAllByCondition(Map paramMap);
}
