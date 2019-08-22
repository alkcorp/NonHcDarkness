package net.alkalus.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.7.10")
public class NonHcDarkness_CORE extends DummyModContainer {

	public static final String NAME = "Non-HCDarkness";
	public static final String MODID = "NotDark";
	public static final String VERSION = "0.1";
	public static List<?> DEPENDENCIES = new ArrayList<>(
			Arrays.asList(new String[] { "required-before:HardcoreDarkness;" }));

	@SuppressWarnings("unchecked")
	public NonHcDarkness_CORE() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = NonHcDarkness_CORE.MODID;
		meta.name = NonHcDarkness_CORE.NAME;
		meta.version = NonHcDarkness_CORE.VERSION;
		meta.credits = "Roll Credits...";
		meta.authorList = Arrays.asList("Alkalus");
		meta.description = "Disables HC-Darkness mod";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
		meta.dependencies = (List<ArtifactVersion>) NonHcDarkness_CORE.DEPENDENCIES;
		instance = this;
	}

	//Mod Instance
	@Mod.Instance(MODID)
	public static NonHcDarkness_CORE instance;

	public static class Logger {

		// Logging Functions
		public static final org.apache.logging.log4j.Logger modLogger = Logger.makeLogger();

		// Generate GT++ Logger
		public static org.apache.logging.log4j.Logger makeLogger() {
			final org.apache.logging.log4j.Logger gtPlusPlusLogger = LogManager.getLogger("Non-HCDarkness");
			return gtPlusPlusLogger;
		}

		public static final org.apache.logging.log4j.Logger getLogger() {
			return modLogger;
		}

		// Non-Dev Comments
		public static void INFO(final String s) {
			modLogger.info(s);
		}

	}

}
