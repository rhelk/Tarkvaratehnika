import Vue from 'vue'
import App from './App.vue'
import VueResource from 'vue-resource'
import Routes from './routes'
import VueRouter from 'vue-router'
import axios from 'axios';
import VueAxios from 'vue-axios'


Vue.use(VueResource);
Vue.use(VueRouter);
//Vue.use(VueAxios, axios);
//Vue.prototype.$http = axios;

const router = new VueRouter({
  routes: Routes,
  mode: 'history'
});

new Vue({
  el: '#app',
  render: h => h(App),
  router: router
})
