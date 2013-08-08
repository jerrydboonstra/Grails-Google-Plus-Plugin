package org.grails.plugin.googlePlus

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class GooglePlusController {

    def accessTokenService
    def googlePlusService

    def callback = { AuthenticationCallbackCommand cmd ->
        def (accessToken, refreshToken, expiresIn) = accessTokenService.generateAccessToken(cmd.code)
        googlePlusService.accessToken = accessToken
        String callbackAction = ConfigurationHolder.config.grails.plugins.googlePlus.callbackAction
        String callbackController = ConfigurationHolder.config.grails.plugins.googlePlus.callbackController
        redirect(controller: callbackController , action: callbackAction,
                params: [accessToken: accessToken, refreshToken: refreshToken, expiresIn: expiresIn, state: cmd.state, error: cmd.error])
    }
}

class AuthenticationCallbackCommand {
    String code
    String state
    String error

    static constraints = {
        code(nullable:false)
        state(nullable:true)
        error(nullable:true)
    }
}
