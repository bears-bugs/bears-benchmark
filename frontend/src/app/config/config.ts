/**
 * Config file to switch between different types of applications. Ex. Hand Hygiene, Blue Gloves, or Blue Masks.
 */
export class Config {
    servicesReturnFakeData = false;

    appName: string;
    frontendHostname: string;
    frontendPort: number;
    bannerUrl: string;
    logoUrl: string;
    backendHostname: string;
    backendPort: number;

    constructor(appName: string,
                frontendHostname: string,
                frontendPort: number,
                bannerUrl: string,
                logoUrl: string,
                backendHostname: string,
                backendPort: number) {
        this.appName = appName;
        this.frontendHostname = frontendHostname;
        this.frontendPort = frontendPort;
        this.bannerUrl = bannerUrl;
        this.logoUrl = logoUrl;
        this.backendHostname = backendHostname;
        this.backendPort = backendPort;
    }

    public setConfig(config: Config) {
        this.appName = config.appName;
        this.frontendHostname = config.frontendHostname;
        this.frontendPort = config.frontendPort;
        this.bannerUrl = config.bannerUrl;
        this.logoUrl = config.logoUrl;
        this.backendHostname = config.backendHostname;
        this.backendPort = config.backendPort;
    }

    public getFrontendUrl() {
        return this.frontendHostname + ':' + this.frontendPort;
    }

    public getBackendUrl() {
        return this.backendHostname + ':' + this.backendPort + '/api';
    }
}
