import axios from "axios";
import { LOGIN_URL } from "../constants";

/**
 * Analyze error reponse from API request
 * @param {} err 
 */
export function checkApiError(err) {
    let errMsg;
    if (axios.isAxiosError(err)) {
        if (err.response) {
            errMsg = "HTTP ERROR #" + err.response.status;

            if (err.response?.status == 401) {
                // redirect to login
                console.log("Redirecting to login on 401 error.");
                window.location.href  = LOGIN_URL;
            }
        } else if (err.request) {
            errMsg = "No Response Received";
        } else {
            errMsg = 'Request Error: ' + err.message;
        }
    } else {
        errMsg = 'An unexpected error occurred: ' + err;
    }

    return errMsg;
}