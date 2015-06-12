Resource = {};

Resource.getURL = function(modResource) {
    return org.ame.jsforge.internal.ModLoading.JSModLoader.getInstance().jsMods.get(modID).resources.get(modResource);
}

Resource.getFile = function(modResource) {
    return new java.io.File(Resource.getURL(modResource).toURI());  // I know I'm converting a File to a URL to a File, but I don't feel like implementing special functionality for such a small optimisation.
}