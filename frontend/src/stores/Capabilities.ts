import {defineStore} from 'pinia'
import {useAPI} from '../processes/API'
import {type Ref, ref} from 'vue'

let api = useAPI();

export const useCapabilitiesStore = defineStore('capabilities', () => {
    const capabilities = ref({
        featureTypeList: [] as Array<{name: string, title: string, defaultCRS: string, defaultStyle: string}>,
        CRS: [] as Array<string>
    });
    const geoserverUrl: Ref<URL | null> = ref(null);

    function fetch() {
        if (geoserverUrl.value !== null) {
            api.getCapabilities(geoserverUrl.value.toString())
                .then(response => capabilities.value = response);
        }
    }

    function setUrl(url: string) {
        geoserverUrl.value = new URL(url);
    }


    return {capabilities, geoserverUrl, fetch, setUrl}
})