import {createApp} from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import 'virtual:uno.css'
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura'


const pinia = createPinia();
createApp(App)
    .use(pinia)
    .use(PrimeVue, {
        theme: {
            preset: Aura
        }
    })
    .mount('#app')
