package net.alkalus.asm;


import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM5;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.alkalus.asm.NonHcDarkness_CORE.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class NonHcDarkness_Transformer implements IClassTransformer {

	static {
		TimerTask repeatedTask = new TimerTask() {
			private final NonHCDarkness_EventHandler mManager = new NonHCDarkness_EventHandler();
			public void run() {
				mManager.clientTick(null);
			}			
		};		
		Timer timer = new Timer(""+NonHcDarkness_CORE.MODID);
		long period = 100l;
		timer.scheduleAtFixedRate(repeatedTask, 1000, period);	
	}

	private static Boolean mObf = checkObfuscated();

	public static boolean checkObfuscated() {
		if (mObf != null) {
			return mObf;
		}		
		boolean obfuscated = false;
		try {
			obfuscated = !(boolean) ReflectionHelper.findField(CoreModManager.class, "deobfuscatedEnvironment").get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			byte[] bs;
			try {
				bs = Launch.classLoader.getClassBytes("net.minecraft.world.World");
				if (bs != null) {
					obfuscated = false;
				} else {
					obfuscated = true;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				obfuscated = false;
			}
		}
		return obfuscated;
	}

	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		// Fix
		if (transformedName.equals("lumien.hardcoredarkness.asm.ClassTransformer")) {
			FMLRelaunchLog.log(""+NonHcDarkness_CORE.NAME+"", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer(basicClass, mObf).getWriter().toByteArray();
		}		
		return basicClass;
	}


	private static class ClassTransformer {

		private final ClassReader reader;
		private final ClassWriter writer;

		private ClassTransformer(byte[] basicClass, boolean obf) {
			ClassReader aTempReader = null;
			ClassWriter aTempWriter = null;
			aTempReader = new ClassReader(basicClass);
			aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
			aTempReader.accept(new localClassVisitor(aTempWriter), 0);
			reader = aTempReader;
			writer = aTempWriter;
			if (reader != null && writer != null) {
				injectMethod("transform");
			}
		}

		private ClassWriter getWriter() {
			return writer;
		}

		private void injectMethod(String aMethodName) {
			MethodVisitor mv;
			if (aMethodName.equals("transform")) {
				mv = getWriter().visitMethod(ACC_PUBLIC, "transform", "(Ljava/lang/String;Ljava/lang/String;[B)[B", null,
						null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(26, l0);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitInsn(ARETURN);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLocalVariable("this", "Llumien/hardcoredarkness/asm/ClassTransformer;", null, l0, l1, 0);
				mv.visitLocalVariable("name", "Ljava/lang/String;", null, l0, l1, 1);
				mv.visitLocalVariable("transformedName", "Ljava/lang/String;", null, l0, l1, 2);
				mv.visitLocalVariable("basicClass", "[B", null, l0, l1, 3);
				mv.visitMaxs(1, 4);
				mv.visitEnd();
			}
			FMLRelaunchLog.log("" + NonHcDarkness_CORE.NAME + "", Level.INFO, "Method injection complete.");

		}

		private static final class localClassVisitor extends ClassVisitor {

			private localClassVisitor(ClassVisitor cv) {
				super(ASM5, cv);
			}

			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				if (name.equals("transform")) {
					FMLRelaunchLog.log("" + NonHcDarkness_CORE.NAME + "", Level.INFO,
							"Removing method " + name);
					return null;
				}
				MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
				return methodVisitor;
			}
		}

	}
	
	public static class NonHCDarkness_EventHandler {

		private Field mEnabled;
		private Object mInstanceObject;

		public NonHCDarkness_EventHandler() {
			Logger.INFO("Creating State Manager.");
		}
		
		private boolean setVars() {
			Field b;
			Field c;
			Object o;
			try {
				@SuppressWarnings("rawtypes")
				Class a = Class.forName("lumien.hardcoredarkness.HardcoreDarkness");
				b = a.getDeclaredField("INSTANCE");
				b.setAccessible(true);
				try {
					c = a.getDeclaredField("enabled");
					c.setAccessible(true);
					try {
						o = b.get(null);
					} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
						o = null;
						return false;
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
					c = null;
					o = null;
					return false;
				}

			} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException e) {
				b = null;
				c = null;
				o = null;
				return false;
			}
			mEnabled = c;
			mInstanceObject = o;
			return true;
		}

		private final void setFields() {
			try {
				mEnabled.set(mInstanceObject, false);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		}

		public void clientTick(Object o) {
			try {
				if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().theWorld != null && setVars()) {
					setFields();
				}
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}

	}


}
