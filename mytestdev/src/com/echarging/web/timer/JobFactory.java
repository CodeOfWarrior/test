package com.echarging.web.timer;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class JobFactory extends AdaptableJobFactory {
	
	@Autowired
    private AutowireCapableBeanFactory capableBeanFactory;
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object obj = super.createJobInstance(bundle);
		capableBeanFactory.autowireBean(obj);
		return obj;
	}

	public AutowireCapableBeanFactory getCapableBeanFactory() {
		return capableBeanFactory;
	}

	public void setCapableBeanFactory(AutowireCapableBeanFactory capableBeanFactory) {
		this.capableBeanFactory = capableBeanFactory;
	}
	
	
	
	

}
