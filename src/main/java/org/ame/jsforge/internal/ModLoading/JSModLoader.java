package org.ame.jsforge.internal.ModLoading;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.ame.jsforge.internal.ForgeScript;
import org.ame.jsforge.internal.JSMod;
import org.apache.commons.compress.utils.IOUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @author Amelorate
 * Loads mods from the classpath.
 */
public class JSModLoader {
	public static JSModLoader instance;

	public ScriptEngine engine;

	public ArrayList<JSMod> jsMods = new ArrayList<>();
	private boolean alreadyLoadedMods = false;

	public static JSModLoader getInstance() {
		if (instance != null) {
			return instance;
		}
		else {
			instance = new JSModLoader();
			return instance;
		}
	}

	public void loadAllJSMods() throws ScriptException {
		if (alreadyLoadedMods) {
			throw new IllegalArgumentException("All the mods have already been loaded.");
		}
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL[] classPath = ((URLClassLoader)classLoader).getURLs();
		for(URL url: classPath) {
			if (url.getPath().contains("mods")) {
				if (verifyModFile(url)) {
					loadMod(url);
				}
			}
		}
		alreadyLoadedMods = true;
	}

	public boolean verifyModFile(URL path) {
		try {
			File file = new File(path.toURI());
			ZipFile modZipFile = new ZipFile(file);
			ZipEntry modInfo = modZipFile.getEntry("modinfo.json");
			if (modInfo == null) {
				return false;
			}
			InputStream modInfoStream = modZipFile.getInputStream(modInfo);
			JsonParser jsonParser = new JsonParser();
			JsonObject modInfoJson = (JsonObject) jsonParser.parse(new InputStreamReader(modInfoStream));

			if (modInfoJson.get("API_Version").getAsInt() < ForgeScript.API_VERSION) {
				throw new InvalidModException("The mod `" + path.getFile() + "` is built for a version greater than The API version " + ForgeScript.API_VERSION + ". Please update Forgescript.");
			}
		}
		catch (ZipException e) {
			throw new InvalidModException("The mod `" + path.getFile() + "` is made up of a malformed zip file. Please redownload the file or inform the mod author.", e);
		}
		catch (ClassCastException | NullPointerException e) {
			throw new InvalidModException("The mod `" + path.getFile() + "'s modinfo.json is malformed. Please inform the mod author.", e); // This NullPointerException could come from elsewhere, but it is probably fine.
		}
		catch (URISyntaxException | IOException e) {
			throw new AssertionError(e);
		}
		return true;
	}

	public void loadMod(URL path) throws ScriptException {
		System.out.println(path);
		engine = new ScriptEngineManager(null).getEngineByName("nashorn");	// I probably need a better way to reset the context.
		Reader api = JSDependencyResolver.getInstance().resolveToReader("main");
		engine.eval(api);
		engine.eval("var apiVersion = " + ForgeScript.API_VERSION + ";");

		ZipFile zipFile;
		try {
			zipFile = new ZipFile(new File(path.toURI()));
		}
		catch (IOException | URISyntaxException e) {
			throw new AssertionError(e);
		}
		JsonObject modInfo = null;
		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		HashSet<ZipEntry> jsFiles = new HashSet<>();
		while (zipEntries.hasMoreElements()) {
			ZipEntry entry = zipEntries.nextElement();
			System.out.println(entry.getName());
			if (entry.getName().startsWith("src") && entry.getName().endsWith(".js") && !entry.isDirectory()) {
				jsFiles.add(entry);
			}
			else if (entry.getName().endsWith(".json") && !entry.isDirectory()) {
				try {
					modInfo = (JsonObject) new JsonParser().parse(new InputStreamReader(zipFile.getInputStream(entry)));
				}
				catch (IOException e) {
					throw new AssertionError(e);
				}
			}
		}
		assert modInfo != null;	// Silences idea warning.
		JSMod mod;
		try {
			mod = new JSMod(engine.getContext(), 0, path, modInfo.get("API_Version").getAsInt(), modInfo.get("Mod_Name").getAsString(), modInfo.get("Mod_Version").getAsString());
		}
		catch (NullPointerException e) {
			throw new InvalidModException("The mod " + modInfo.get("Mod_Name") + "has a invalid modinfo.json file: " + modInfo, e);
		}
		jsMods.add(mod);
		engine.eval("var modID = " + jsMods.indexOf(mod) + ";");
		mod.modID = jsMods.indexOf(mod);
		JSDependencyResolver.getInstance().resolveAll(mod, modInfo);

		for (ZipEntry jsFile : jsFiles) {
			try {
				engine.eval(new InputStreamReader(zipFile.getInputStream(jsFile)));
			}
			catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		resolveResources(zipFile, mod);
	}

	private void resolveResources(ZipFile file, JSMod mod) {
		Enumeration files = file.entries();
		while (files.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) files.nextElement();
			if (entry.getName().equals("modinfo.json") && !entry.isDirectory()) {
				try {
					loadResource(file.getInputStream(entry), entry.getName(),  mod);
				}
				catch (IOException e) {
					throw new AssertionError(e);
				}
			}
			else if (entry.getName().startsWith("resources") && !entry.isDirectory()) {
				try {
					loadResource(file.getInputStream(entry), entry.getName(),  mod);
				}
				catch (IOException e) {
					throw new AssertionError(e);
				}
			}
		}
	}

	private void loadResource(InputStream resource, String name, JSMod mod) {
		File resourceAsFile;
		try {
			resourceAsFile = File.createTempFile(name, null);
		}
		catch (IOException e) {
			throw new AssertionError(e);
		}
		resourceAsFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(resourceAsFile)) {
			IOUtils.copy(resource, out);
		}
		catch (IOException e) {
			throw new AssertionError(e);
		}
		URL url;
		try {
			url = resourceAsFile.toURI().toURL();
		}
		catch (MalformedURLException e) {
			throw new AssertionError(e);
		}
		((LaunchClassLoader) this.getClass().getClassLoader()).addURL(url);
		mod.resources.put(name, url);
	}
}
