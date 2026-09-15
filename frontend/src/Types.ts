export type Body = {
    crs: Array<string>;
    wfs: Array<{ name: string }>;
    wms: Array<{ name: string }>;
    wmts: Array<{ name: string }>;
    features: Array<{ name: string }>;
    maps: Array<{ name: string, defaultStyle: string }>;

    loops: number;
    users: number;
    url: {
        protocol: string;
        host: string;
        path: string;
    }
}

export type Layer = {
    name: string,
    wfs?: boolean,
    wms?: boolean,
    wmts?: boolean,
    features?: boolean,
    maps?: boolean,
    defaultStyle?: string

};