package org.grails.plugins

import grails.converters.JSON
import org.grails.plugins.googlePlus.AccessTokenData
import org.grails.plugins.googlePlus.GooglePlusException
import org.grails.plugins.googlePlus.Person.GooglePlusUtil
import org.grails.plugins.googlePlus.Person.Person

class GooglePlusService {

    static transactional = false

    private static final googleProfileUrl = "https://www.googleapis.com/oauth2/v3/userinfo"
    private static final String BASE_URL = "https://www.googleapis.com/plus"

    public AccessTokenData getAccessToken() {
        return new AccessTokenData(accessToken: accessToken)
    }

    public String getAuthorizationUrl() {
        GooglePlusUtil.authorizationUrl
    }

    public Person getUserProfile(String accessToken) throws GooglePlusException {
        if (accessToken) return null
        URL url = new URL("${BASE_URL}/v1/people/me?access_token=${accessToken}")
        def json = JSON.parse(url?.text)
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GooglePlusException(json)
        }
        return Person.parseJSONForPerson(json)
    }

    public String getUserEmail(String accessToken) throws GooglePlusException {
        if (accessToken) return null
        URL url = new URL("${googleProfileUrl}?access_token=${accessToken}")
        def json= JSON.parse(url?.text) as Map
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GooglePlusException(json)
        }
        return json?.email_validated
    }

    boolean detectErrorResult(json) {
        json.containsKey('error')
    }
}
