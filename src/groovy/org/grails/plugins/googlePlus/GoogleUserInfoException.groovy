package org.grails.plugins.googlePlus

class GoogleUserInfoException extends RuntimeException {

    private static final long serialVersionUID = -888433433711801L;

    def result

    /**
     * eg. '{ "error": "invalid_token", "error_description": "Invalid Credentials" }'
     * @param result
     */
    public GoogleUserInfoException(Map result) {
        super(parseData(result))
        this.result = result
    }

    public GoogleUserInfoException(Map data, Throwable ex) {
        super(parseData(data), ex)
    }

    public GoogleUserInfoException(String data, Throwable ex) {
        super(data, ex)
    }

    String parseData(Map result) {
        def msg = result['error_description'] ? "${result['error']}: ${result['error_description']}" as String : result['error'] as String
        this.result = msg
        return this.result
    }
}


