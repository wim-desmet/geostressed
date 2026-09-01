import {defineStore} from 'pinia'
import {useAPI} from '../processes/API'
import {ref, type Ref} from 'vue'

let api = useAPI();

export const useTestConfigurationStore = defineStore('testConfiguration', () => {
    const wfs: Ref<Array<string>> = ref([]);
    const wms: Ref<Array<string>> = ref([]);
    const wmts: Ref<Array<string>> = ref([]);
    const users: Ref<number> = ref(2);
    const loops: Ref<number> = ref(10);

    return {wfs, wms, wmts, users, loops}
})