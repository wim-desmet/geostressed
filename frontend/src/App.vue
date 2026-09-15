<template>
  <div class="flex flex-col gap-5 w-2/3 m-auto h-full">
    <h1 class="m-auto">GeoStressed 😧</h1>

    <div class="flex flex-row gap-2 w-full">
      <InputText fluid type="text" v-model="geoserverUrl"/>

      <Button label="Load" @click="load"/>
    </div>

    <div class="overflow-y-auto h-full flex-grow">
      <table>
        <thead class="sticky top-0 bg-white z-10">
        <tr>
<!--          <th>Title</th>-->
          <th>Name</th>
<!--          <th>Style</th>-->
          <th class="w-20">CRS</th>
          <th>WFS</th>
          <th>WMS</th>
          <th>WMTS</th>
          <th>Features</th>
          <th>Maps</th>

          <th>
            <MultiSelect :options="CRS" v-model="testConfigurationStore.crs"/>
          </th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="featureType in featureTypeList" :key="featureType.name">
<!--          <td>{{ featureType.title }}</td>-->
          <td>{{ featureType.name }}</td>
<!--          <td>{{ featureType.defaultStyle }}</td>-->
          <td class="mx-auto">{{ featureType.defaultCRS.replace(/.*?(\d+)/, '$1') }}</td>
          <td>
            <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.wfs" name="layer"
                      :value="{name: featureType.name}"/>
          </td>
          <td>
            <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.wms" name="layer"
                      :value="{name: featureType.name}"/>
          </td>
          <td>
            <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.wmts" name="layer"
                      :value="{name: featureType.name}"/>
          </td>
          <td>
            <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.features" name="layer"
                      :value="{name: featureType.name}"/>
          </td>
          <td>
            <Checkbox :inputId="featureType.name" v-model="testConfigurationStore.maps" name="layer"
                      :value="{name: featureType.name, defaultStyle: featureType.defaultStyle}"/>
          </td>
          <td>
            <i>zie bovenaan</i>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="flex flex-row gap-2 w-full items-center">
      <label for="users" class="font-bold w-1/2">Users</label>
      <InputNumber type="number" v-model="testConfigurationStore.users" :min="1" buttonLayout="vertical" showButtons/>

      <label for="loops" class="font-bold w-1/2">Loops</label>
      <InputNumber type="number" v-model="testConfigurationStore.loops" :min="1" buttonLayout="vertical" showButtons/>
      <div class="flex-grow w-full"></div>
    </div>

    <Button class="w-30 h-20 self-center p-4" @click="run" :disabled="disableRun">
      <div class="h-4 relative">
        <ProgressSpinner unstyled class="relative inline-flex items-center justify-center h-6 w-6"/>
      </div>
      Run
    </Button>

    <div>
      <p>
        <span>Test File:</span>
        <a target="_blank" :href="output?.jmeterTestFile">{{output?.jmeterTestFile}}</a>
      </p>
      <p>
        <span>Result File:</span>
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
import {default as Button} from 'primevue/button'
import {default as Checkbox} from 'primevue/checkbox'
import {default as InputNumber} from 'primevue/inputnumber'
import {default as InputText} from 'primevue/inputtext'
import {default as MultiSelect} from "primevue/multiselect"
import {default as ProgressSpinner} from "primevue/progressspinner"
import {useAPI} from "@/processes/API.ts";

const geoserverUrl = ref("https://geoserver-api-2.apps.sd-k3s.int.ngi.be/geoserver/");
const output: Ref<{
  jmeterTestFile: string,
  jmeterResultFile: string,
  jmeterResultFolder: string
}> = ref({
  jmeterTestFile: "",
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
const CRS = computed(() => capabilitiesStore.capabilities.CRS);

const disableRun = computed(() =>
    (testConfigurationStore.wfs.length === 0) &&
    (testConfigurationStore.wms.length === 0) &&
    (testConfigurationStore.wmts.length === 0) &&
    (testConfigurationStore.features.length === 0) &&
    (testConfigurationStore.maps.length === 0) &&
    (testConfigurationStore.crs.length === 0)
);

function load() {
  capabilitiesStore.fetch();
}

const running = ref(false);

async function run() {
  running.value = true;
  output.value = {
    jmeterTestFile: "",
    jmeterResultFile: "",
    jmeterResultFolder: ""
  }

  let result = await runTest({
    url: {
      protocol: capabilitiesStore?.geoserverUrl?.protocol || "http",
      host: capabilitiesStore?.geoserverUrl?.hostname || "",
      path: capabilitiesStore?.geoserverUrl?.pathname || "geoserver/ows",
    },
    users: testConfigurationStore.users,
    loops: testConfigurationStore.loops,
    wfs: testConfigurationStore.wfs,
    wms: testConfigurationStore.wms,
    wmts: testConfigurationStore.wmts,
    features: testConfigurationStore.features,
    maps: testConfigurationStore.maps,
    crs: testConfigurationStore.crs
  });
  running.value = false;
  console.log(result.jmeterResultFile);

  output.value.jmeterTestFile = result.jmeterTestFile;
  output.value.jmeterResultFile = result.jmeterResultFile;
  output.value.jmeterResultFolder = result.jmeterResultFolder;
}
</script>

<style lang="scss">
.layertable {
  overflow-y: auto;
  height: 15em;
}

.layertable thead tr {
  position: sticky;
  top: 0;
  @apply bg-white;
}


</style>


Request URL
https://geoserver-api-2.apps.sd-k3s.int.ngi.be/geoserver/ogc/maps/v1/collections/gdoc:three_d_coord/styles/point/map?transparent=true&layers=gdoc%3Athree_d_coord&styles=point&f=image%2Fpng&crs=EPSG%3A3812&styles=&width=768&height=632&bbox=680367.8682523458%2C645408.4430838973%2C695027.3487488613%2C657452.8860439252
https://geoserver-api-2.apps.sd-k3s.int.ngi.be/geoserver/ogc/maps/v1/collections/gdoc:three_d_coord/styles/point/map?layers=gdoc:three_d_coord&styles=point&crs=EPSG:3812&bbox=701695.0867433712,578514.3037469533,714215.5777504357,591034.7947540177&transparent=true&f=image/png&width=256&height=256