package org.springframework.cglib.proxy;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.NoOp.1;

public interface NoOp extends Callback {
	NoOp INSTANCE = new 1();
}