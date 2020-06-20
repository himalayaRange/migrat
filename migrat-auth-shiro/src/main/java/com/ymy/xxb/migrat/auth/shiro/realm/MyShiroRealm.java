package com.ymy.xxb.migrat.auth.shiro.realm;

import java.util.Set;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import com.ymy.xxb.migrat.auth.shiro.entity.BsUserDO;
import com.ymy.xxb.migrat.auth.shiro.service.BsUserService;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.auth.shiro.vo.ProfileResultVo;
import com.ymy.xxb.migrat.common.exception.CommonException;
public class MyShiroRealm extends AuthorizingRealm {
 
	@Autowired
	private BsUserService bsUserService;
	
	// 授权
	@Override
	@SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取最新的权限信息
        ProfileResultVo profileResult = (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
        String currentMobile = ShiroContextUtil.currentMobile();
         try {
			BsUserDO bsUserDO = bsUserService.findByUsernameOrMobile(currentMobile);
			if (bsUserDO == null) {
				SecurityUtils.getSubject().logout();
				throw new CommonException("您的手机号已经修改或者当前用户已经被禁用，请重新登录");
			}
			// 1.用户信息
			BsUserDO info = new BsUserDO();
			info.setId(bsUserDO.getId());
			info.setTenantId(bsUserDO.getTenantId());
			info.setUsername(bsUserDO.getUsername());
			info.setMobile(bsUserDO.getMobile());
			info.setCompanyId(bsUserDO.getCompanyId());
			info.setBsRoles(bsUserDO.getBsRoles());
			// 2.用户Permissions
			profileResult = new ProfileResultVo(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Set<String> roles = (Set<String>)profileResult.getRoles().get(ShiroConst.PROFILE_ROLES);
        Set<String> perms = (Set<String>)profileResult.getRoles().get(ShiroConst.PROFILE_PERMS);
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(perms);
        return authorizationInfo;
    }
 
    
    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = (String) token.getPrincipal();
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        BsUserDO userInfo;
		try {
			userInfo = bsUserService.findByUsernameOrMobile(username);
		        if (userInfo == null) {
		            return null;
		        }
		        if (userInfo.getState() == 0) { //账户冻结
		            throw new LockedAccountException("您的账户已经被冻结，请联系系统管理员");
		        }
		        // 构造安全返回数据并返回(安全数据：用户基本数据、权限信息profileResultVo)
				ProfileResultVo profileResult = null;
				// 1.用户信息
				BsUserDO info = new BsUserDO();
				info.setId(userInfo.getId());
				info.setTenantId(userInfo.getTenantId());
				info.setUsername(userInfo.getUsername());
				info.setMobile(userInfo.getMobile());
				info.setCompanyId(userInfo.getCompanyId());
				info.setBsRoles(userInfo.getBsRoles());
				// 2.用户Permissions
				profileResult = new ProfileResultVo(info);
		        String salt =  "(" + userInfo.getUsername() + ")";
		        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(profileResult, userInfo.getPassword(), ByteSource.Util.bytes(salt), getName());
		        return authenticationInfo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
    }
    
    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
