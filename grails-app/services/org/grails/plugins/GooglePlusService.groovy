package org.grails.plugins

import grails.converters.JSON
import org.grails.plugins.googlePlus.GooglePlusException
import org.grails.plugins.googlePlus.GoogleUserInfoException
import org.grails.plugins.googlePlus.Person.GooglePlusUtil
import org.grails.plugins.googlePlus.Person.Person
import org.grails.plugins.googlePlus.Person.TokenInfo
import org.grails.plugins.googlePlus.Person.ValidatedEmail

class GooglePlusService {

    static transactional = false

    private static final String googleProfileUrl = 'https://www.googleapis.com/oauth2/v3/userinfo'
    private static final String googleRevokeUrl = 'https://accounts.google.com/o/oauth2/revoke'
    private static final String googlePlusMeUrl = 'https://www.googleapis.com/plus/v1/people/me'
    private static final String googleTokenInfoUrl = 'https://www.googleapis.com/oauth2/v1/tokeninfo'


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
        URL url = new URL("${googlePlusMeUrl}?access_token=${accessToken}")
        def json = JSON.parse(url?.text)
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GooglePlusException(json)
        }
        return Person.parseJSON(json)
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
        def json = JSON.parse(url?.text) as Map
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GoogleUserInfoException(json)
        }
        return ValidatedEmail.parseJSON(json)
    }

    /**
     * Get TokenInfo (scope, etc..) from TokenInfo endpoint.
     * @param accessToken
     * @return TokenInfo
     * @throws GoogleUserInfoException if Google sent a 200 status and an error JSON result
     * @throws IOException if Google sent a non-200 status, or other network reasons
     */
    public TokenInfo getTokenInfo(String accessToken) throws GoogleUserInfoException, IOException {
        if (!accessToken) return null
        URL url = new URL("${googleTokenInfoUrl}?access_token=${accessToken}")
        def json = JSON.parse(url?.text) as Map
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GoogleUserInfoException(json)
        }
        return TokenInfo.parseJSON(json)
    }

    /**
     * Revoke our accessToken, and transitively, any refreshToken.
     * If you are logged into Google, view your granted permissions here: https://accounts.google.com/b/0/IssuedAuthSubTokens
     * @param accessToken to revoke; refreshToken is also revoked as a side-effect.
     * @return true if revocation worked, or if no accessToken was passed; else false
     * @throws GoogleUserInfoException if Google sent a 200 status and an error JSON result
     * @throws IOException if Google sent a non-200 status, or other network reasons
     */
    public boolean revokePermissions(String accessToken) throws GoogleUserInfoException, IOException {
        if (!accessToken) return true
        URL url = new URL("${googleRevokeUrl}?token=${accessToken}")
        def json = JSON.parse(url?.text) as Map
        log.info "URL : " + url.toString()
        log.info "JSON : " + json

        if (detectErrorResult(json)) {
            throw new GoogleUserInfoException(json)
        }
        return true
    }

    boolean detectErrorResult(json) {
        json.containsKey('error')
    }
}
