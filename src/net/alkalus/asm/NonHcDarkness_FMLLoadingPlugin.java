package net.alkalus.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

@SortingIndex(1) 
@MCVersion(value = "1.7.10")
public class NonHcDarkness_FMLLoadingPlugin implements IFMLLoadingPlugin  {
	
	//-Dfml.coreMods.load=net.alkalus.asm.NonHcDarkness_FMLLoadingPlugin
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"net.alkalus.asm.NonHcDarkness_Transformer"};
	}

	@Override
	public String getModContainerClass() {
		return "net.alkalus.asm.NonHcDarkness_CORE";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

}