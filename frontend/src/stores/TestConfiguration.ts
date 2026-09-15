import {defineStore} from 'pinia'
import {useAPI} from '../processes/API'
import {ref, type Ref} from 'vue'

let api = useAPI();

export const useTestConfigurationStore = defineStore('testConfiguration', () => {
    const crs: Ref<Array<string>> = ref([]);
    const wfs: Ref<Array<{name: string}>> = ref([]);
    const wms: Ref<Array<{name: string}>> = ref([]);
    const wmts: Ref<Array<{name: string}>> = ref([]);
    const features: Ref<Array<{name: string}>> = ref([]);
    const maps: Ref<Array<{name: string, defaultStyle: string}>> = ref([]);
    const users: Ref<number> = ref(2);
    const loops: Ref<number> = ref(10);

    return {crs, wfs, wms, wmts, features, maps, users, loops}
})