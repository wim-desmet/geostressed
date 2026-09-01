<template>
  <div class="flex flex-col gap-5 w-1/2 m-auto h-full">
    <h1 class="m-auto">GeoStressed 😧</h1>

    <div class="flex flex-row gap-2 w-full">
      <InputText fluid type="text" v-model="geoserverUrl"/>

      <Button label="Load" @click="load"/>
    </div>

    <table>
      <tbody>
      <tr>
        <th>Title</th>
        <th>Name</th>
        <th>Default CRS</th>
        <th>WFS</th>
        <th>WMS</th>
        <th>WMTS</th>
      </tr>

      <tr v-for="featureType in featureTypeList" :key="featureType.name">
        <td>{{ featureType.title }}</td>
        <td>{{ featureType.name }}</td>
        <td>{{ featureType.defaultCRS.replace(/.*?(\d+)/, '$1') }}</td>
        <td>
          <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.wfs" name="layer"
                    :value="featureType.name"/>
        </td>
        <td>
          <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.wms" name="layer"
                    :value="featureType.name"/>
        </td>
        <td>
          <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.wmts" name="layer"
                    :value="featureType.name"/>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="flex flex-row gap-2 w-full items-center">
      <label for="users" class="font-bold w-1/2">Users</label>
      <InputNumber type="number" v-model="testConfigurationStore.users" :min="1" buttonLayout="vertical" showButtons/>

      <label for="loops" class="font-bold w-1/2">Loops</label>
      <InputNumber type="number" v-model="testConfigurationStore.loops" :min="1" buttonLayout="vertical" showButtons/>
    </div>

    <Button @click="run" :disabled="disableRun">Run</Button>

    <div>
      <p>
        <span>File:</span>
        <a target="_blank" :href="output?.jmeterResultFile">Result file</a>
      </p>
      <p>
        <span>Folder:</span>
        <a target="_blank" :href="`${output?.jmeterResultFolder}/index.html`">Result folder</a>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useCapabilitiesStore} from './stores/Capabilities'
import {useTestConfigurationStore} from './stores/TestConfiguration'
import {computed, type Ref, ref, watch} from 'vue'
import {default as Checkbox} from 'primevue/checkbox'
import {default as InputText} from 'primevue/inputtext'
import {default as InputNumber} from 'primevue/inputnumber'
import {default as Button} from 'primevue/button'
import {useAPI} from "@/processes/API.ts";

const geoserverUrl = ref("");
const output: Ref<{
  jmeterResultFile: string,
  jmeterResultFolder: string
}> = ref({
  jmeterResultFile: "",
  jmeterResultFolder: ""
});

const capabilitiesStore = useCapabilitiesStore();
const testConfigurationStore = useTestConfigurationStore();
const {runTest} = useAPI();

capabilitiesStore.setUrl(geoserverUrl.value);
watch(geoserverUrl, (newValue) =>
    capabilitiesStore.setUrl(newValue)
);

const featureTypeList = computed(() => capabilitiesStore.capabilities?.featureTypeList);

const disableRun = computed(() =>
    (testConfigurationStore.wfs.length === 0) &&
    (testConfigurationStore.wms.length === 0) &&
    (testConfigurationStore.wmts.length === 0)
);

function load() {
  capabilitiesStore.fetch();
}

async function run() {
  let result = await runTest({
    url: {
      protocol: capabilitiesStore?.geoserverUrl?.protocol || "http",
      host: capabilitiesStore?.geoserverUrl?.hostname || "",
      path: capabilitiesStore?.geoserverUrl?.pathname || "geoserver/ows",
    },
    users: testConfigurationStore.users,
    loops: testConfigurationStore.loops,
    wfs: testConfigurationStore.wfs.map(layer => ({name: layer})),
    wms: testConfigurationStore.wms.map(layer => ({name: layer})),
    wmts: testConfigurationStore.wmts.map(layer => ({name: layer})),
  });
  console.log(result.jmeterResultFile);
  output.value.jmeterResultFile = result.jmeterResultFile;
  output.value.jmeterResultFolder = result.jmeterResultFolder;
}


</script>

