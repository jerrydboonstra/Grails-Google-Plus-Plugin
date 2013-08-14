package org.grails.plugins.googlePlus.Person

/**
 * Result from v3/userinfo call using token with 'email' scope/permissions.
 */
class ValidatedEmail {

    String id
    String email
    Boolean email_verified
    String domain

    static ValidatedEmail parseJSON(def json) {
        ValidatedEmail userinfo = new ValidatedEmail()
        userinfo.id = json?.sub
        userinfo.email = json?.email
        userinfo.email_verified = json?.email_verified
        userinfo.domain = json?.hd
        return userinfo
    }
}
