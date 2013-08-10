package org.grails.plugins.googlePlus

class GoogleUserInfoException extends RuntimeException {

    private static final long serialVersionUID = -888433433711801L;

    def result

    public GoogleUserInfoException() {
        result = [:]
    }

    /**
     * eg. { "error": "invalid_token", "error_description": "Invalid Credentials" }
     * @param result
     */
    public GoogleUserInfoException(result) {
        super(result['error_description'] ? "${result['error']}: ${result['error_descxription']}" as String : result['error'] as String)
        this.result = result
    }

}


