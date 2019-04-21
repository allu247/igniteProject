import axios from 'axios';
const apiUrl = 'http://localhost:8090';


async function getRequest(url) {
    let response = await axios.get(apiUrl + url);

    return response
}



async function postRequest(url, data) {
    let response = await axios.post(apiUrl + url,
        data);

    return response
}



export default {
    apiUrl,

    getRequestToApi(url) {
        return getRequest(url);
    },

    postRequestToApi(url, data) {
        return postRequest(url, data);
    },


}
