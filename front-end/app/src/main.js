import Vue from 'vue'
import App from './App.vue'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueSwal from 'vue-swal'

Vue.config.productionTip = false;

Vue.use(VueSwal);

new Vue({

    render: h => h(App)
}).$mount('#app');
