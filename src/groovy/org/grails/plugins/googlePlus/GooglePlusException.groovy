package org.grails.plugins.googlePlus

class GooglePlusException extends RuntimeException {

    private static final long serialVersionUID = -553433433711801L;

    def result

    public GooglePlusException() {
        result = [:]
    }

    /**
     * eg. { "error": { "errors": [
     * { "domain": "global", "reason": "authError", "message": "Invalid Credentials", "locationType": "header",
     * "location": "Authorization" } ], "code": 401, "message": "Invalid Credentials" } }
     * @param result
     */
    public GooglePlusException(result) {
        super(result['error'] ? result['error']['message'] as String : result['error_msg'] as String)
        this.result = result
    }


    public def getType() {
        return (result['error'] && result['error']['type']) ? result['error']['type'] : 'Exception'
    }

    public Integer getCode() {
        return result['code'] as Integer ?: null
    }


    public String toString() {
        return "${type}: ${message}; status=${code}"
    }
}


