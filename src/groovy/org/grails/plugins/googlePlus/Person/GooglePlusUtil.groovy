package org.grails.plugins.googlePlus.Person

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class GooglePlusUtil {

    static String getAuthorizationUrl() {
        String clientId = ConfigurationHolder.config.grails.plugins.googlePlus.clientId
        String callbackUrl = ConfigurationHolder.config.grails.plugins.googlePlus.callBackUrl
        String scope = ConfigurationHolder.config.grails.plugins.googlePlus.scope ?: 'https://www.googleapis.com/auth/plus.me email'
        String accessType = ConfigurationHolder.config.grails.plugins.googlePlus.accessType ?: 'offline'
        def requiredParams = [:]
        requiredParams.put 'scope', scope
        requiredParams.put 'redirect_uri', callbackUrl
        requiredParams.put 'response_type', 'code'
        requiredParams.put 'client_id', clientId
        requiredParams.put 'access_type', accessType

        String state = ConfigurationHolder.config.grails.plugins.googlePlus.state ?: ''
        String loginHint = ConfigurationHolder.config.grails.plugins.googlePlus.loginHint ?: ''
        def optionalParams = [:]
        if (state) optionalParams.put 'state', state
        if (loginHint) optionalParams.put 'login_hint', loginHint

        def queryParams = (requiredParams + optionalParams).collect { k,v -> "${k}=${v}" }.join('&')
        return "https://accounts.google.com/o/oauth2/auth?${queryParams}"
    }
}
