export type Body = {
    wfs: Array<{ name: string }>;
    wms: Array<{ name: string }>;
    wmts: Array<{ name: string }>;
    loops: number;
    users: number;
    url: {
        protocol: string;
        host: string;
        path: string;
    }
}

export type Layer = { name: string, wfs?: boolean, wms?: boolean, wmts?: boolean };