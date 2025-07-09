import java.util.Properties
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.net.http.HTTPBuilder
import static groovy.net.http.Method.* // <--- ADD THIS LINE

class Utils {
    private static Properties settings;

    static {
        // Load settings.properties when the class is first accessed
        settings = new Properties();
        def configFile = new File(System.getProperty("user.dir") + "/jmeter_project/config/settings.properties");
        if (configFile.exists()) {
            configFile.withInputStream { is ->
                settings.load(is);
            }
        } else {
            log.warn("settings.properties not found at " + configFile.getAbsolutePath());
        }
    }

    /**
     * Retrieves a setting, prioritizing environment variables over settings.properties.
     *
     * @param key The key for the setting.
     * @return The value of the setting.
     */
    static String getSetting(String key) {
        def envValue = System.getenv(key.toUpperCase()); // Check environment variable (e.g., BASE_URL)
        if (envValue != null && !envValue.isEmpty()) {
            logInfo("Using environment variable for setting: ${key} = ${envValue}");
            return envValue;
        }
        def propValue = settings.getProperty(key); // Fallback to settings.properties
        if (propValue != null && !propValue.isEmpty()) {
            logInfo("Using settings.properties for setting: ${key} = ${propValue}");
            return propValue;
        }
        logError("Setting not found: ${key}");
        return null; // Or throw an exception, depending on desired strictness
    }

    static def parseJson(String jsonString) {
        def slurper = new JsonSlurper();
        return slurper.parseText(jsonString);
    }

    static void logInfo(String message) {
        log.info("[Utils] " + message);
    }

    static void logError(String message) {
        log.error("[Utils] " + message);
    }

    /**
     * Makes a generic HTTP request and returns the response.
     * This method should be called from a JSR223 Sampler.
     *
     * @param method HTTP method (e.g., GET, POST, PUT, DELETE)
     * @param path The API endpoint path (e.g., "/todos")
     * @param requestBody Optional: The request body for POST/PUT requests (Map or String)
     * @param headers Optional: Map of request headers
     * @return The response object (can be parsed JSON, String, etc.)
     */
    static def makeHttpRequest(Method method, String path, def requestBody = null, Map headers = [:]) {
        def baseUrl = getSetting("base_url");
        def protocol = getSetting("protocol");
        def fullUrl = "${protocol}://${baseUrl}${path}";

        logInfo("Making ${method.name()} request to: ${fullUrl}");
        if (requestBody) {
            logInfo("Request Body: ${requestBody instanceof String ? requestBody : JsonOutput.toJson(requestBody)}");
        }
        if (!headers.isEmpty()) {
            logInfo("Request Headers: ${headers}");
        }

        def http = new HTTPBuilder(fullUrl);
        http.request(method) { req ->
            headers.each { k, v ->
                req.headers."${k}" = v; 
            }

            if (requestBody) {
                if (requestBody instanceof Map) {
                    req.body = JsonOutput.toJson(requestBody);
                    req.headers.'Content-Type' = 'application/json';
                } else { // Assume String
                    req.body = requestBody;
                }
            }

            response.success = { resp, json ->
                logInfo("Response Status: ${resp.status}");
                // Store response data in JMeter variables for assertions/further processing
                sampler.setResponseData(resp.getData().getText());
                sampler.setResponseCode(resp.status.toString());
                sampler.setResponseMessage(resp.statusLine.reasonPhrase);
                sampler.setSuccessful(true as boolean); // <--- CASTED TO BOOLEAN
                return json; // Return parsed JSON if available
            }
            response.failure = { resp ->
                logError("Request Failed: ${resp.status} - ${resp.statusLine.reasonPhrase}");
                logError("Response Data on Failure: ${resp.getData().getText()}");
                sampler.setResponseData(resp.getData().getText());
                sampler.setResponseCode(resp.status.toString());
                sampler.setResponseMessage(resp.statusLine.reasonPhrase);
                sampler.setSuccessful(false as boolean); // <--- CASTED TO BOOLEAN
                return null;
            }
        }
    }
}