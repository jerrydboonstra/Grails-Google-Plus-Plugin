package org.grails.plugins

import grails.converters.JSON
import org.grails.plugins.googlePlus.AccessTokenData
import org.grails.plugins.googlePlus.GooglePlusException
import org.grails.plugins.googlePlus.GoogleUserInfoException
import org.grails.plugins.googlePlus.Person.GooglePlusUtil
import org.grails.plugins.googlePlus.Person.Person
import org.grails.plugins.googlePlus.Person.ValidatedEmail

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

    /**
     * Get Person from GooglePlus.
     * @param accessToken required to have 'https://www.googleapis.com/auth/plus.me email' permissions granted.
     * @return Person
     * @throws GooglePlusException if Google sent a 200 status and an error JSON result
     * @throws IOException if Google sent a non-200 status, or other network reasons
     */
    public Person getUserProfile(String accessToken) throws GooglePlusException, IOException {
        if (!accessToken) return null
        URL url = new URL("${BASE_URL}/v1/people/me?access_token=${accessToken}")
        def json = JSON.parse(url?.text)
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GooglePlusException(json)
        }
        return Person.parseJSONForPerson(json)
    }

    /**
     * Get UserInfo (id, email) from UserInfo endpoint.
     * @param accessToken required to have 'https://www.googleapis.com/auth/plus.me email' permissions granted.
     * @return ValidatedEmail
     * @throws GoogleUserInfoException if Google sent a 200 status and an error JSON result
     * @throws IOException if Google sent a non-200 status, or other network reasons
     */
    public ValidatedEmail getUserEmail(String accessToken) throws GoogleUserInfoException, IOException {
        if (!accessToken) return null
        URL url = new URL("${googleProfileUrl}?access_token=${accessToken}")
        def json= JSON.parse(url?.text) as Map
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GoogleUserInfoException(json)
        }
        return ValidatedEmail.parseJSONForValidatedEmail(json)
    }

    boolean detectErrorResult(json) {
        json.containsKey('error')
    }
}
