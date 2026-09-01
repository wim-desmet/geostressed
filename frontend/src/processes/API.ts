import type {Body, Layer} from "@/Types.ts";

export function useAPI() {

    async function getCapabilities(url: string) {
        return await fetch("/api/geoserver/wfs_capabilities",
            {
                method: "post",
                body: url
            }
        ).then(body => body.json())
    }


    async function runTest(body: Body): Promise<{
        jmeterResultFile: string,
        jmeterResultFolder: string
    }> {
        let layerNames: Array<string> = [...new Set([
            ...body.wfs.map(layer => layer.name),
            ...body.wms.map(layer => layer.name),
            ...body.wmts.map(layer => layer.name)])];

        let allLayers: Array<Layer> = layerNames.map(layerName => ({
            name: layerName,
            wfs : body.wfs.some(wfs => wfs.name === layerName),
            wms : body.wms.some(wms => wms.name === layerName),
            wmts : body.wmts.some(wmts => wmts.name === layerName)
        }));


        const testRequest = {
            url: {
                ...body.url,
                protocol: body.url.protocol.replace(":", ""),
            },
            users: body.users,
            loops: body.loops,
            layers: allLayers
        }

        const testResponse = await fetch("/api/jmeter/test", {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(testRequest)
        });
        return await testResponse.json();
    }

    return {getCapabilities, runTest}

}