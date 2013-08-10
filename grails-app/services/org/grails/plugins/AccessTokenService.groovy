package org.grails.plugins

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AccessTokenService {
    private final String URL_TO_REQUEST_TOKEN = 'https://accounts.google.com/o/oauth2/token'
    static transactional = false

    def generateAccessToken(String code) {
        String queryString = generateAccessTokenQueryString(code)
        String accessToken = null
        String refreshToken = null
        String expiresIn = null
        try {
            HttpURLConnection connection = loadConnectionSettings(queryString.size())
            postAccessTokenRequest(connection, queryString)
            (accessToken, refreshToken, expiresIn) = extractData(connection)
        }
        catch (Exception e) {
            e.printStackTrace()
        }
        return [accessToken, refreshToken, expiresIn]
    }

    def refreshAccessToken(String refreshToken) {
        String queryString = generateRefreshAccessTokenQueryString(refreshToken)
        String accessToken = null
        String expiresIn = null
        try {
            HttpURLConnection connection = loadConnectionSettings(queryString.size())
            postAccessTokenRequest(connection, queryString)
            (accessToken, expiresIn) = extractData(connection)
        }
        catch (Exception e) {
            e.printStackTrace()
        }
        return [accessToken, expiresIn]
    }
    private def extractData(HttpURLConnection connection) {
        String resultData = connection.content.text
        def responseJson = JSON.parse(resultData) as Map
        String accessToken = responseJson?.access_token
        String refreshToken = responseJson?.refresh_token
        Integer expiresIn = responseJson?.expires_in
        return [accessToken, refreshToken, expiresIn]
    }

    private postAccessTokenRequest(HttpURLConnection connection, String queryString) {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
        outputStreamWriter.write(queryString);
        outputStreamWriter.flush()
        log.debug("Response code ${connection.responseCode} , Message : ${connection.responseMessage}")
    }

    private HttpURLConnection loadConnectionSettings(Long querySize) {
        URL url = new URL(URL_TO_REQUEST_TOKEN)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", "" + querySize);
        connection.setRequestProperty("Host", "accounts.google.com")
        return connection
    }

    private String generateAccessTokenQueryString(String code) {
        String clientId = ConfigurationHolder.config.grails.plugins.googlePlus.clientId
        String clientSecret = ConfigurationHolder.config.grails.plugins.googlePlus.clientSecret
        String callBackUrl = ConfigurationHolder.config.grails.plugins.googlePlus.callBackUrl

        StringBuilder queryString = new StringBuilder("code=")
        queryString.with{
            append(code);
            append("&client_id=");
            append(encodeInUTF8(clientId));
            append("&client_secret=");
            append(encodeInUTF8(clientSecret));
            append("&redirect_uri=");
            append(encodeInUTF8(callBackUrl));
            append("&grant_type=");
            append(encodeInUTF8('authorization_code'))
        }
        return queryString.toString()
    }

    private String generateRefreshAccessTokenQueryString(String code) {
        String clientId = ConfigurationHolder.config.grails.plugins.googlePlus.clientId
        String clientSecret = ConfigurationHolder.config.grails.plugins.googlePlus.clientSecret
        String callBackUrl = ConfigurationHolder.config.grails.plugins.googlePlus.callBackUrl

        StringBuilder queryString = new StringBuilder("code=")
        queryString.with{
            append(code);
            append("&client_id=");
            append(encodeInUTF8(clientId));
            append("&client_secret=");
            append(encodeInUTF8(clientSecret));
            append("&redirect_uri=");
            append(encodeInUTF8(callBackUrl));
            append("&grant_type=");
            append(encodeInUTF8('authorization_code'))
        }
        return queryString.toString()
    }

    private String encodeInUTF8(String code) {
        return URLEncoder.encode(code, "UTF-8")
    }
}
