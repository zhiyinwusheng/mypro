package me.gacl.examples.quickstart.service.account;

import java.io.Serializable;

import me.gacl.examples.quickstart.entity.User;
import me.gacl.modules.uitls.Encodes;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.google.common.base.Objects;

public class ShiroDbRealm extends AuthorizingRealm{

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//		User user = accountService.findUserByLoginName(shiroUser.loginName);
		User user = new User();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		info.addRoles(user.getRoleList());
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
		System.out.println(token.getUsername());
		User user = new User();
		user.setLoginName("aaa");
		user.setPassword("123");
		byte[] salt = Encodes.decodeHex(user.getSalt());
		return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getName()),
				user.getPassword(), ByteSource.Util.bytes(salt), getName());
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public Long id;
		public String loginName;
		public String name;

		public ShiroUser(Long id, String loginName, String name) {
			this.id = id;
			this.loginName = loginName;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(loginName);
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}
	}
}
