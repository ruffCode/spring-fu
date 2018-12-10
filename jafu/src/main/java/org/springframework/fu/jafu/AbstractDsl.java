package org.springframework.fu.jafu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Base class for Jafu DSL.
 *
 * @author Sebastien Deleuze
 */
public abstract class AbstractDsl implements ApplicationContextInitializer<GenericApplicationContext> {

	private final List<ApplicationContextInitializer<GenericApplicationContext>> initializers = new ArrayList<>();

	protected GenericApplicationContext context;

	/**
	 * Get a reference to the bean by type or type + name with the syntax
	 * @param beanClass type the bean must match, can be an interface or superclass
	 */
	public <T> T ref(Class<T> beanClass) {
		return this.context.getBean(beanClass);
	}

	public <T> T ref(Class<T> beanClass, String name) {
		return this.context.getBean(name, beanClass);
	}

	@Override
	public void initialize(GenericApplicationContext context) {
		this.context = context;
		register(context);
		this.initializers.forEach(initializer -> initializer.initialize(context));
	}

	protected void addInitializer(ApplicationContextInitializer<GenericApplicationContext> initializer) {
		initializers.add(initializer);
	}

	public Environment env() {
		return context.getEnvironment();
	}

	public List<String> profiles() {
		return Arrays.asList(context.getEnvironment().getActiveProfiles());
	}

	public <T extends AbstractDsl> AbstractDsl enable(T dsl) {
		addInitializer(dsl);
		return this;
	}

	abstract public void register(GenericApplicationContext context);
}