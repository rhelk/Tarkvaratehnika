import axios from 'axios'


const apiUrl = "http://localhost:8080/api/";

function doGet(addon) {
  axios({url: apiUrl, method: 'GET'})
}
