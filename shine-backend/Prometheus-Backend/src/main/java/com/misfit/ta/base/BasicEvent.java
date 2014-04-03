package com.misfit.ta.base;

public abstract class BasicEvent<V> {
	
	abstract public V call(Object sender, Object arguments);
}
