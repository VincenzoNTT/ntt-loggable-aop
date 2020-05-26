package com.nttdata.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.nttdata.aop.LoggableConfig;
import com.nttdata.aop.service.PocService;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AppRunAOP {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
	       ctx.register(LoggableConfig.class);
	       ctx.refresh();
	       
	       PocService service = ctx.getBean(PocService.class);
	       service.multiply(2, 3);
	       
	      String accnumber = "12345";
	      service.displayBalance(accnumber);
			
	       service.getCall();
	       	
	}
}

//https://progressive-code.com/post/6/Spring-AOP-in-conjunction-with-custom-annotations