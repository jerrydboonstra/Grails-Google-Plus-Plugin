package org.grails.plugins.googlePlus.Person

/**
 * Result from v3/userinfo call using token with 'email' scope/permissions.
 */
class TokenInfo {

    String id
    String email
    Boolean email_verified
    String issuedTo
    String audience
    String scope
    Integer expiresIn
    String accessType

    static TokenInfo parseJSON(def json) {
        TokenInfo userinfo = new TokenInfo()
        userinfo.id = json?.user_id
        userinfo.email = json?.email
        userinfo.email_verified = json?.verified_email
        userinfo.issuedTo = json?.issued_to
        userinfo.audience = json?.audience
        userinfo.expiresIn = json?.expires_in
        userinfo.accessType = json?.access_type
        return userinfo
    }
}
