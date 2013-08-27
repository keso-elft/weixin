package com.weixin.server.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;

/**
 * 服务管理类
 */
public class ServiceManager {

	/**
	 * 由于反射生成,需自己完成单例
	 */
	private static ServiceManager serviceManager;

	/**
	 * 长期Service
	 */
	public Map<String, Service> commonServiceMap;

	/**
	 * 生效Service
	 */
	public Map<String, Service> activeServiceMap;

	/**
	 * 获得实例
	 * @return
	 */
	public static ServiceManager getInstance() {
		if (serviceManager == null) {
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return serviceManager;
	}

	/**
	 * 根据配置文件初始化
	 */
	public static void init() throws Exception {

		InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.xml");

		Digester digester = new Digester();

		digester.setValidating(false);

		// 建立Server对象
		digester.addObjectCreate("server", ServiceManager.class);

		// 建立Service对象
		digester.addObjectCreate("server/service", Service.class);

		// 设置Service对象的参数
		digester.addBeanPropertySetter("server/service/service-name", "serviceName");
		digester.addBeanPropertySetter("server/service/service-send-message", "serviceSendMessage");

		// 设置Server对象的serviceMap
		digester.addSetNext("server/service", "addCommonService");

		// 建立ServiceStep对象
		digester.addObjectCreate("server/service/step", ServiceStep.class);

		// 设置ServiceStep对象的参数
		digester.addSetProperties("server/service/step");

		// 建立ServiceChoise对象
		digester.addObjectCreate("server/service/step/choise", ServiceChoise.class);

		// 设置ServiceStep对象的参数
		digester.addSetProperties("server/service/step/choise");

		// 建立FilterChain对象
		digester.addObjectCreate("server/service/step/choise/filter-chain", com.weixin.server.filter.FilterChain.class);

		// 设置Service对象的stepMap
		digester.addSetNext("server/service/step", "addStep");

		// 设置ServiceStep对象的choiceMap
		digester.addSetNext("server/service/step/choise", "addChoise");

		// 设置ServiceChoice的FilterChain
		digester.addSetNext("server/service/step/choise/filter-chain", "setChain");

		// 设置ServiceChoice的Processor
		digester.addCallMethod("server/service/step/choise/processor", "setProcessor", 1);
		digester.addCallParam("server/service/step/choise/processor", 0);

		// 设置FilterChain的filterList
		digester.addCallMethod("server/service/step/choise/filter-chain/filter", "addFilter", 2);
		digester.addCallParam("server/service/step/choise/filter-chain/filter/filter-type", 0);
		digester.addCallParam("server/service/step/choise/filter-chain/filter/filter-para", 1);

		// 建立active-Service对象
		digester.addObjectCreate("server/active-service", Service.class);

		// 设置active-Service对象的参数
		digester.addBeanPropertySetter("server/active-service/service-name", "serviceName");
		digester.addBeanPropertySetter("server/active-service/service-send-message", "serviceSendMessage");

		// 设置active-Server对象的activeServiceMap
		digester.addSetNext("server/active-service", "addActiveService");

		// 建立active-ServiceStep对象
		digester.addObjectCreate("server/active-service/step", ServiceStep.class);

		// 设置active-ServiceStep对象的参数
		digester.addSetProperties("server/active-service/step");

		// 建立active-ServiceChoise对象
		digester.addObjectCreate("server/active-service/step/choise", ServiceChoise.class);

		// 设置active-ServiceChoise对象的参数
		digester.addSetProperties("server/active-service/step/choise");

		// 建立active-FilterChain对象
		digester.addObjectCreate("server/active-service/step/choise/filter-chain",
				com.weixin.server.filter.FilterChain.class);

		// 设置active-Service对象的stepMap
		digester.addSetNext("server/active-service/step", "addStep");

		// 设置active-ServiceStep对象的choiceMap
		digester.addSetNext("server/active-service/step/choise", "addChoise");

		// 设置active-ServiceChoice的FilterChain
		digester.addSetNext("server/active-service/step/choise/filter-chain", "setChain");

		// 设置active-ServiceChoice的Processor
		digester.addCallMethod("server/active-service/step/choise/processor", "setProcessor", 1);
		digester.addCallParam("server/active-service/step/choise/processor", 0);

		// 设置active-FilterChain的filterList
		digester.addCallMethod("server/active-service/step/choise/filter-chain/filter", "addFilter", 2);
		digester.addCallParam("server/active-service/step/choise/filter-chain/filter/filter-type", 0);
		digester.addCallParam("server/active-service/step/choise/filter-chain/filter/filter-para", 1);

		serviceManager = (ServiceManager) digester.parse(file);
	}

	/**
	 * 服务器构建时调用
	 * @param service
	 * @throws Exception
	 */
	public void addCommonService(Service service) throws Exception {
		if (commonServiceMap == null)
			commonServiceMap = new HashMap<String, Service>();
		commonServiceMap.put(service.getServiceName(), service);
	}

	/**
	 * 服务器构建时调用
	 * @param service
	 */
	public void addActiveService(Service service) {
		if (activeServiceMap == null)
			activeServiceMap = new HashMap<String, Service>();
		activeServiceMap.put(service.getServiceName(), service);
	}

	/**
	 * 获取所有Service
	 * @return
	 */
	public Collection<Service> getCommonServices() {
		return commonServiceMap.values();
	}

	/**
	 * 获取所有Service
	 * @return
	 */
	public Collection<Service> getServices() {
		Map<String, Service> result = new HashMap<String, Service>();
		result.putAll(commonServiceMap);
		result.putAll(activeServiceMap);
		return result.values();
	}

	/**
	 * 获取指定service
	 * @param serviceType
	 * @return
	 */
	public Service getService(String serviceType) {
		Service service = commonServiceMap.get(serviceType);
		if (service == null)
			service = activeServiceMap.get(serviceType);
		return service;
	}

	/**
	 * 删除主动Service
	 * @param serviceType
	 */
	public void removeActiveService(String service) {
		activeServiceMap.remove(service);
	}

	public static void main(String[] args) {
		ServiceManager.getInstance();
	}
}
