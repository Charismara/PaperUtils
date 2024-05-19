package de.blutmondgilde.paperutils

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

class PaperUtilsPluginExtension {
    String minecraftVersion
    File testServerDir
    final Server server

    @Inject
    PaperUtilsPluginExtension(ObjectFactory objectFactory) {
        server = objectFactory.newInstance(Server)
    }

    void server(Action<? super Server> action) {
        action.execute(server)
    }
}