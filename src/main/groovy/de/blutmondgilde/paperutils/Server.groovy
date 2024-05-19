package de.blutmondgilde.paperutils

class Server {
    ArrayList<RuntimePlugin> plugins = []

    def plugin(String url, String fileName) {
        plugins << new RuntimePlugin(url, fileName)
    }
}
