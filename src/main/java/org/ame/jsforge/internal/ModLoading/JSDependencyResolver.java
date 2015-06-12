package org.ame.jsforge.internal.ModLoading;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ame.jsforge.internal.JSMod;

import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Amelorate
 * Resolves mod dependencys.
 */
public class JSDependencyResolver {
	private static JSDependencyResolver instance;

	public static JSDependencyResolver getInstance() {
		if (instance == null) {
			instance = new JSDependencyResolver();
		}
		return instance;
	}

	public Reader resolveToReader(String dependency) {
		return new InputStreamReader(this.getClass().getResourceAsStream("/api/" + dependency + ".js"));
	}

	public void resolveLoad(String dependency, JSMod mod) throws ScriptException{
		JSModLoader.getInstance().engine.setContext(mod.context);
		JSModLoader.getInstance().engine.eval(resolveToReader(dependency));
	}

	public Collection<String> getAllDependencies(JsonObject modJSON) {
		JsonArray array = (JsonArray) modJSON.get("Depends");
		HashSet<String> set = new HashSet<>();
		if (array == null) {
			return set;
		}
		array.forEach((e) -> set.add(e.getAsString()));
		return set;
	}

	public void resolveAll(JSMod mod, JsonObject modJSON) throws ScriptException {
		for (String dependency : getAllDependencies(modJSON)) {
			resolveLoad(dependency, mod);
		}
	}
}
