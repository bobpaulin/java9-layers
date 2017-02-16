package com.bobpaulin.test;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Layer;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

import com.bobpaulin.service.ModService;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Path path = Paths.get("/Users/bpaulin/workspace-java9/mlib");
		ModuleFinder finder = ModuleFinder.of(path);
		
		Optional<ModuleReference> omrefMod1 = finder.find("mod1");
		Optional<ModuleReference> omrefMod2 = finder.find("mod2");
		Optional<ModuleReference> omrefMod3 = finder.find("mod3");
		
		
		System.out.println("Mod1 is present: " + omrefMod1.isPresent());
		System.out.println("Mod2 is present: " + omrefMod2.isPresent());
		System.out.println("Mod3 is present: " + omrefMod3.isPresent());
		
		Layer parent = Layer.boot();
		
		System.out.println(Main.class.getModule().getName());
		
		System.out.println("Layer: " + parent);
		
		Configuration cf = parent.configuration()
		         .resolveAndBind(finder, ModuleFinder.of(), Set.of("mod2", "mod3"));
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		
		Layer layer = parent.defineModulesWithOneLoader(cf, cl);
		 
		Class<?> mod2Main = layer.findLoader("mod2").loadClass("com.bobpaulin.service.impl.Main");
		
		Class<?> mod3Main = layer.findLoader("mod3").loadClass("com.bobpaulin.service.impl.Main");
		 
		Object mod2MainObj = mod2Main.getDeclaredConstructor(null).newInstance(null);
		 
		Method doSomethingMod2 = mod2Main.getMethod("doSomething", null);
		 
		doSomethingMod2.invoke(mod2MainObj, null);
		 
		Object mod3MainObj = mod3Main.newInstance();
		 
		Method doSomethingMod3 = mod3Main.getMethod("doSomething", null);
		 
		doSomethingMod3.invoke(mod3MainObj, null);
		 
		 
	}
}

